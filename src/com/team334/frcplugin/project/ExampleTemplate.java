package com.team334.frcplugin.project;

import com.intellij.ide.util.projectWizard.AbstractModuleBuilder;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.platform.ProjectTemplate;
import com.team334.frcplugin.Icons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ExampleTemplate implements ProjectTemplate {
    @NotNull
    @Override
    public String getName() {
        return "Example Project";
    }

    @Nullable
    @Override
    public String getDescription() {
        return "Example WPILib Robot Project";
    }

    @Override
    public Icon getIcon() {
        return Icons.WPI_ICON;
    }

    @NotNull
    @Override
    public AbstractModuleBuilder createModuleBuilder() {
        return new ExampleModuleBuilder();
    }

    @Nullable
    @Override
    public ValidationInfo validateSettings() {
        return null;
    }
}
