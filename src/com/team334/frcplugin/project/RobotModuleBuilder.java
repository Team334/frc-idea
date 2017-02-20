package com.team334.frcplugin.project;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.util.Pair;
import com.team334.frcplugin.Settings;
import com.team334.frcplugin.panels.RobotProject;

import javax.swing.*;
import java.io.File;

public class RobotModuleBuilder extends JavaModuleBuilder {
    private Settings settings = Settings.getInstance();

    private void addSupplementaryPackages() {
        String folderStructure = settings.getPackage().replaceAll("\\.", File.separator);
        String basePath = Pair.getFirst(getSourcePaths().get(0));
        File buildXML = new File("resources", "build.xml");

        System.out.println(basePath + File.separator + buildXML.getPath());
        addSourcePath(Pair.create(basePath + File.separator + buildXML.getPath(), ""));

        new File(basePath,folderStructure).mkdirs();
        Pair.getFirst(getSourcePaths().get(0)).concat(folderStructure);

    }

    @Override
    public ModuleWizardStep modifySettingsStep(SettingsStep settingsStep) {
        addSupplementaryPackages();
        return new RobotStep(settingsStep);
    }

    private class RobotStep extends ModuleWizardStep {
        private ModuleWizardStep javaStep;
        private RobotProject robotProject;

        RobotStep(SettingsStep settingsStep) {
            javaStep = JavaModuleType.getModuleType().modifyProjectTypeStep(settingsStep, RobotModuleBuilder.this);
            robotProject = new RobotProject();

            settingsStep.addSettingsComponent(robotProject.getPanel());
        }

        @Override
        public JComponent getComponent() {
            return javaStep.getComponent();
        }

        @Override
        public void updateDataModel() {
            javaStep.updateDataModel();
        }

        @Override
        public void disposeUIResources() {
            super.disposeUIResources();
            javaStep.disposeUIResources();
        }
    }
}
