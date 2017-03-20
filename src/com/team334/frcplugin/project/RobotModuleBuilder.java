package com.team334.frcplugin.project;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.util.Pair;
import com.intellij.xml.actions.xmlbeans.FileUtils;
import com.team334.frcplugin.Settings;
import com.team334.frcplugin.panels.RobotProject;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RobotModuleBuilder extends JavaModuleBuilder {

    @Override
    public ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep) {
        return new RobotStep(settingsStep);
    }

    private class RobotStep extends ModuleWizardStep {
        private ModuleWizardStep javaStep;
        private RobotProject robotProject;

        private Settings settings = Settings.INSTANCE;
        private String pkg = settings.getPackage();
        private String folderStructure = pkg.replaceAll("\\.", File.separator);

        RobotStep(SettingsStep settingsStep) {
            javaStep = JavaModuleType.getModuleType().modifyProjectTypeStep(settingsStep, RobotModuleBuilder.this);
            robotProject = new RobotProject();

            settingsStep.addSettingsComponent(robotProject.getPanel());
        }

        private URL resourceUrl = this.getClass().getClassLoader().getResource("templates/");
        private File resourceFolder = new File(resourceUrl.getPath());

        private void getFiles() {
            for (File file : resourceFolder.listFiles()) {
                if (file.isFile()) {
                    File copy = new File(getContentEntryPath() + File.separator + file.getName());
                    FileUtils.copyFile(file, copy);

                    replaceStrings(file, "\\$package", pkg);
                }
            }
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

            File file = new File(resourceFolder, end);

            String path = Pair.getFirst(getSourcePaths().get(0)) + File.separator + folderStructure;
            File robot = new File(path + File.separator + "Robot.java");

            FileUtils.copyFile(file, robot);

            replaceStrings(robot, "\\$package", pkg);
        }

        private void replaceStrings(File file, String search, String replace) {
            try {
                Path path = Paths.get(file.toURI());

                String content = new String(Files.readAllBytes(path), "UTF-8");
                content = content.replaceAll(search, replace);

                Files.write(path, content.getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public JComponent getComponent() {
            return javaStep.getComponent();
        }

        @Override
        public void updateDataModel() {
            javaStep.updateDataModel();

            new File(Pair.getFirst(getSourcePaths().get(0)), folderStructure).mkdirs();
            getFiles();
            copyRobotFile(robotProject.getSelectedRadioButton());
        }

        @Override
        public void disposeUIResources() {
            super.disposeUIResources();
            javaStep.disposeUIResources();
        }
    }
}
