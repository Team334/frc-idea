package com.team334.frcplugin.project;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.platform.ProjectTemplate;
import com.intellij.platform.ProjectTemplatesFactory;
import com.team334.frcplugin.Icons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ProjectFactory extends ProjectTemplatesFactory {

    @NotNull
    @Override
    public String[] getGroups() {
        return new String[] {"WPILib"};
    }

    @NotNull
    @Override
    public ProjectTemplate[] createTemplates(@Nullable String s, WizardContext wizardContext) {
        return new ProjectTemplate[] {new RobotTemplate(), new ExampleTemplate()};
    }

    @Override
    public Icon getGroupIcon(String group) {
        return Icons.WPI_ICON;
    }
}
