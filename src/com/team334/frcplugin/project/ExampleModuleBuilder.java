package com.team334.frcplugin.project;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;

public class ExampleModuleBuilder extends JavaModuleBuilder {

    @Override
    public ModuleWizardStep modifySettingsStep(SettingsStep settingsStep) {
        return new ExampleStep(settingsStep);
    }

    private class ExampleStep extends ModuleWizardStep {
        private ModuleWizardStep javaStep;

        public ExampleStep(SettingsStep settingsStep) {
            javaStep = JavaModuleType.getModuleType().modifyProjectTypeStep(settingsStep, ExampleModuleBuilder.this);
            settingsStep.addSettingsComponent(createUIComponents());
        }

        private JPanel createUIComponents() {
            JBPanel panel = new JBPanel(new BorderLayout());
            JBScrollPane scrollPane = new JBScrollPane(new JBList<>());

            panel.add(scrollPane, BorderLayout.CENTER);

            return panel;
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
