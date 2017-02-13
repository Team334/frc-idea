package com.team334.frcplugin.project;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;

import static com.team334.frcplugin.wizard.Properties.WPI_DIR;

public class ProjectTemplate extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project p = new Project();
        p.setModal(true);
        
        p.setResizable(false);
        p.show();
    }

    @Override
    public void update(AnActionEvent e) {
        Presentation p = e.getPresentation();
        p.setEnabled(WPI_DIR.exists());
    }
}
