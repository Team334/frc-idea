package com.team334.frcplugin.wizard;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class WPILibInstall extends AnAction {
    private final WPILibProperties WPI_PROPS = new WPILibProperties();

    private final String WPI_PATH = System.getProperty("user.home") + "/wpilib";
    private final File WPI_DIR = new File(WPI_PATH);

    private final String BASE_URL = "http://first.wpi.edu/FRC/roborio/release/eclipse/";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (WPI_DIR.mkdir()) {
            try {
                WPI_PROPS.serialize(WPI_PATH);
            } catch (IOException io) {
                io.printStackTrace();
            }

            install();

            // create user directories
            new File(WPI_PATH, "user/java").mkdirs();
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Presentation p = e.getPresentation();
        if (p.isEnabled() && WPI_DIR.exists()) {
            p.setEnabled(false);
            p.setVisible(false);
        }
    }
    
    private void install() {
        try {
            final File TEMP_DIR = File.createTempFile("wpilib", null);
            TEMP_DIR.delete();

            if (TEMP_DIR.mkdir()) {
                String pluginPath = WPI_PATH + "/java/" + WPI_PROPS.getVersion();
                if (new File(pluginPath).mkdirs()) {
                    loadLibraries(TEMP_DIR.getAbsolutePath(), pluginPath);
                }
            }

            TEMP_DIR.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLibraries(@NotNull String tempDir, @NotNull String wpiDir) {
        try {
            System.out.format("Downloading site.xml from %s\n", BASE_URL + "site.xl");
            File siteXML = downloadFromURL(BASE_URL + "site.xml", "site.xml", tempDir);

            String featureURL = BASE_URL + findXMLAttribute(siteXML, "feature", "url");
            JarFile featureJar = new JarFile(downloadFromURL(featureURL, "feature.jar", tempDir));

            System.out.format("Downloading %s and extracting feature.xml\n", featureURL);
            File featureXML = extractFileFromJar(featureJar, "feature.xml", tempDir);

            final String PLUGIN = findXMLAttribute(featureXML, "plugin", "id");
            final String VERSION = findXMLAttribute(featureXML, "plugin", "version");
            final String PLUGIN_JAR = PLUGIN + "_" + VERSION;
            final String PLUGIN_URL = BASE_URL + "plugins/" + PLUGIN_JAR + ".jar";

            System.out.format("Downloading %s and extracting resources/java.zip\n", PLUGIN_URL);
            JarFile wpiLibJavaJar = new JarFile(downloadFromURL(PLUGIN_URL, "wpilib.jar", tempDir));

            JarFile javaJar;
            javaJar = new JarFile(extractFileFromJar(wpiLibJavaJar, "resources", "java.zip", tempDir));

            System.out.println("Extracting files from java.zip");
            extractFolderFromJar(javaJar, "ant/", wpiDir);
            extractFolderFromJar(javaJar, "lib/", wpiDir);
            extractFolderFromJar(javaJar, "javadoc/", wpiDir);
        } catch (Exception e) {
            e.printStackTrace();
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

    private String findXMLAttribute(File xml, String tag, String attrib) throws JDOMException, IOException {
        SAXBuilder sax = new SAXBuilder();
        Document doc = sax.build(xml);

        List<Element> elems = doc.getRootElement().getChildren(tag);
        Element e = elems.get(elems.size() - 1);

        return e.getAttributeValue(attrib);
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
            if (entry.getName().contains(directory)) {
                if (entry.isDirectory()) {
                    System.out.print("creating: ");
                    new File(toDir, entry.getName()).mkdir();
                } else {
                    System.out.print("inflating: ");
                    extractFileFromJar(jar, entry.getName(), toDir);
                }
                System.out.println(entry.getName());
            }
        }
    }
}
