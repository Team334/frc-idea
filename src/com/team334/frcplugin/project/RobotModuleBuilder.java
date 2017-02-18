package com.team334.frcplugin.project;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.module.JavaModuleType;
import com.team334.frcplugin.panels.RobotProject;

import javax.swing.*;

public class RobotModuleBuilder extends JavaModuleBuilder {

    @Override
    public ModuleWizardStep modifySettingsStep(SettingsStep settingsStep) {
        return new RobotStep(settingsStep);
    }

    private class RobotStep extends ModuleWizardStep {
        private ModuleWizardStep javaStep;
        private RobotProject robotProject;

        public RobotStep(SettingsStep settingsStep) {
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
