package com.team334.frcplugin.wizard;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.team334.frcplugin.Settings;
import com.team334.frcplugin.panels.InstallProgress;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static com.team334.frcplugin.Settings.WPI_PATH;
import static com.team334.frcplugin.wizard.Properties.WPI_DIR;

public class Installer extends AnAction {
    private Properties props = new Properties();
    private Settings settings = Settings.getInstance();

    private Logger logger;
    private Thread t = new Thread(this::install);

    private class Logger extends DialogWrapper {
        private InstallProgress progress = new InstallProgress();

        Logger() {
            super(false);
            setTitle("WPILib Installer");

            init();
        }

        @NotNull
        @Override
        protected Action[] createActions() {
            return new Action[]{getOKAction(), getCancelAction()};
        }

        private void shiftProgress(int shift) {
            JProgressBar bar = progress.getProgress();
            bar.setValue(bar.getValue() + shift);
        }

        void log(String message) {
            progress.getLog().append(message);
        }

        void log(String message, int shift) {
            log(message);
            shiftProgress(shift);
        }

        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            return progress.getPanel();
        }

        @Override
        public void doCancelAction() {
            t.interrupt();
            close(OK_EXIT_CODE);
        }

        @Override
        public void doOKAction() {
            props.actionPerformed(null);
            close(OK_EXIT_CODE);
        }
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (WPI_DIR.mkdir()) {
            ApplicationManager.getApplication().invokeLater(() -> {
                logger = new Logger();

                logger.setModal(false);
                logger.setResizable(false);

                logger.show();

                SwingUtilities.invokeLater(() -> t.start());
            });

            new File(WPI_PATH, "user" + File.separator + "java" + File.separator + "lib").mkdirs();
        } else {
            Messages.showErrorDialog("WPILib folder already exists. Delete the folder, which is located in "
                    + System.getProperty("user.home") + " and rerun.", "Installation");
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        if (Settings.installed && WPI_DIR.exists()) {
            Presentation p = e.getPresentation();
            p.setEnabled(false);
            p.setVisible(false);
        }
    }

    private void install() {
        try {
            final File TEMP_DIR = File.createTempFile("wpilib", null);
            TEMP_DIR.delete();

            if (TEMP_DIR.mkdir()) {
                String pluginPath = WPI_PATH + File.separator + "java" + File.separator + settings.getVersion();
                if (new File(pluginPath).mkdirs()) {
                    loadLibraries(TEMP_DIR.getAbsolutePath(), pluginPath);
                }
            }

            TEMP_DIR.delete();
            Settings.installed = true;
        } catch (IOException e) {
            Messages.showErrorDialog("Temporary file could not be created.", "Temporary File Creation");
        }
    }

    private void loadLibraries(@NotNull String tempDir, @NotNull String wpiDir) {
        final String BASE_URL = "http://first.wpi.edu/FRC/roborio/release/eclipse/";
        try {
            logger.log(String.format("Downloading site.xml from %ssite.xml\n", BASE_URL), 5);
            File siteXML = downloadFromURL(BASE_URL + "site.xml", "site.xml", tempDir);

            String featureURL = BASE_URL + findXMLAttribute(siteXML, "feature", "url");
            JarFile featureJar = new JarFile(downloadFromURL(featureURL, "feature.jar", tempDir));

            logger.log(String.format("Downloading %s and extracting feature.xml\n", featureURL), 10);
            File featureXML = extractFileFromJar(featureJar, "feature.xml", tempDir);

            final String PLUGIN = findXMLAttribute(featureXML, "plugin", "id");
            final String VERSION = findXMLAttribute(featureXML, "plugin", "version");
            final String PLUGIN_JAR = PLUGIN + "_" + VERSION;
            final String PLUGIN_URL = BASE_URL + "plugins/" + PLUGIN_JAR + ".jar";

            logger.log(String.format("Downloading %s and extracting resources/java.zip\n", PLUGIN_URL), 25);
            JarFile wpiLibJavaJar = new JarFile(downloadFromURL(PLUGIN_URL, "wpilib.jar", tempDir));

            File javaZip = extractFileFromJar(wpiLibJavaJar, "resources", "java.zip", tempDir);
            JarFile javaJar = new JarFile(javaZip);

            logger.log("Extracting files from java.zip\n", 1);
            extractFolderFromJar(javaJar, "ant", wpiDir);
            extractFolderFromJar(javaJar, "lib", wpiDir);
            extractFolderFromJar(javaJar, "javadoc", wpiDir);
        } catch (IOException io) {
            Messages.showErrorDialog("Failed to create temporary file.", "WPILib Installation");
        } catch (JDOMException jd) {
            Messages.showErrorDialog("Cannot find xml attributes.", "WPILib Installation");
        }
    }

    private File downloadFromURL(String url, String fileName, String toDir) throws IOException {
        File file = new File(toDir, fileName);

        URL link = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(link.openStream());

        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        return file;
    }

    private String findXMLAttribute(File xml, String tag, String attribute) throws JDOMException, IOException {
        SAXBuilder sax = new SAXBuilder();
        Document doc = sax.build(xml);

        List<Element> elems = doc.getRootElement().getChildren(tag);
        Element e = elems.get(elems.size() - 1);

        return e.getAttributeValue(attribute);
    }

    private File extractFileFromJar(JarFile jar, String resource, String toDir) throws IOException {
        File file = new File(toDir, resource);

        if (!file.exists()) {
            ZipEntry entry = jar.getEntry(resource);
            InputStream stream = jar.getInputStream(entry);
            Files.copy(stream, file.getAbsoluteFile().toPath());
        }

        return file;
    }

    private File extractFileFromJar(JarFile jar, JarEntry entry, String resource, String toDir) throws IOException {
        File file = new File(toDir, resource);

        if (!file.exists()) {
            InputStream stream = jar.getInputStream(entry);
            Files.copy(stream, file.toPath());
        }

        return file;
    }

    private File extractFileFromJar(JarFile jar, String directory, String resource, String toDir) throws IOException {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (!entry.getName().contains(directory)) continue;

            if (entry.getName().contains(resource)) {
                return extractFileFromJar(jar, entry, resource, toDir);
            }
        }

        return null;
    }

    private void extractFolderFromJar(JarFile jar, String directory, String toDir) throws IOException {
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().contains(directory + File.separator)) {
                if (entry.isDirectory()) {
                    logger.log("creating: ");
                    new File(toDir, entry.getName()).mkdir();
                } else {
                    logger.log("inflating: ");
                    extractFileFromJar(jar, entry.getName(), toDir);
                }
                logger.log(entry.getName() + "\n", 1);
            }
        }
    }
}
