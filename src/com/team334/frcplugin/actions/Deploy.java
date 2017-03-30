package com.team334.frcplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.team334.frcplugin.Settings;

public class Deploy extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO create deploy action
    }

    @Override
    public void update(AnActionEvent e) {
        if (!Settings.installed) {
            Presentation p = e.getPresentation();
            p.setEnabled(false);
        }
    }
}
