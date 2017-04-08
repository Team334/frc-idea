package com.team334.frcplugin.project;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.team334.frcplugin.Settings;
import com.team334.frcplugin.panels.RobotProject;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RobotModuleBuilder extends JavaModuleBuilder {

    @Override
    public ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep) {
        return new RobotStep(settingsStep);
    }

    RobotModuleBuilder() {
        String[] modules = new String[]{
                "WPILib", "NetworkTables", "opencv", "cscore"
        };

        for (String module : modules) {
            addModuleLibrary(Settings.WPI_LIBS + File.separator + module + ".jar", null);
        }
    }

    private class RobotStep extends ModuleWizardStep {
        private ModuleWizardStep javaStep;
        private RobotProject robotProject;

        private Settings settings = Settings.getInstance();
        private String pkg = settings.getPackage();
        private String folderStructure = pkg.replaceAll("\\.", File.separator);

        RobotStep(SettingsStep settingsStep) {
            javaStep = JavaModuleType.getModuleType().modifyProjectTypeStep(settingsStep, RobotModuleBuilder.this);
            robotProject = new RobotProject();

            settingsStep.addSettingsComponent(robotProject.getPanel());
        }

        private File getFileFromTemplates(String resourcePath, String fileName) {
            InputStream in = getClass().getResourceAsStream("/templates/" + resourcePath);

            File f = new File(fileName);
            try {
                Files.copy(in, f.toPath());
            } catch (IOException e) {
                Messages.showErrorDialog("Failed to copy build files.", "Project Creation Error");
            }

            return f;
        }

        private void getBuildFiles() {
            String fileBase = getContentEntryPath() + File.separator + "build.";
            File properties = getFileFromTemplates("build.properties", fileBase + "properties");
            File xml = getFileFromTemplates("build.xml", fileBase + "xml");

            replaceStrings(xml, "\\$package", pkg);
            replaceStrings(properties, "\\$package", pkg);
        }

        private void copyRobotFile(JRadioButton button) {
            String end = File.separator + "Robot.java";

            switch (button.getText()) {
                case "Iterative Robot":
                    end = "iterative" + end;
                    break;
                case "Command Based Robot":
                    end = "command-based" + end;
                    break;
                case "Sample Robot":
                    end = "sample" + end;
                    break;
            }

            String path = Pair.getFirst(getSourcePaths().get(0)) + File.separator + folderStructure;
            File robot = getFileFromTemplates(end, path + File.separator + "Robot.java");

            replaceStrings(robot, "\\$package", pkg);
        }

        private void replaceStrings(File file, String search, String replace) {
            try {
                Path path = Paths.get(file.toURI());

                String content = new String(Files.readAllBytes(path), "UTF-8");
                content = content.replaceAll(search, replace);

                Files.write(path, content.getBytes("UTF-8"));
            } catch (IOException e) {
                String err = String.format("Failed to replace %s with %s in file %s.", search, replace, file.getName());
                Messages.showErrorDialog(err, "Project Creation");

            }
        }

        @Override
        public JComponent getComponent() {
            return javaStep.getComponent();
        }

        @Override
        public void updateDataModel() {
            javaStep.updateDataModel();

            getBuildFiles();
            if (new File(Pair.getFirst(getSourcePaths().get(0)), folderStructure).mkdirs()) {
                copyRobotFile(robotProject.getSelectedRadioButton());
            }
        }

        @Override
        public void disposeUIResources() {
            super.disposeUIResources();
            javaStep.disposeUIResources();
        }
    }
}
