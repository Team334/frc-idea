package com.team334.frcplugin.actions;

import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.team334.frcplugin.Settings;
import com.team334.frcplugin.project.RunConfiguration;

public class Configuration extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        RunConfiguration.addConfiguration();
    }

    @Override
    public void update(AnActionEvent e) {
        RunnerAndConfigurationSettings configuration = RunConfiguration.getDeployConfiguration();

        Presentation p = e.getPresentation();
        p.setVisible(Settings.installed && configuration == null);
    }
}
