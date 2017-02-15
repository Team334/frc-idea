package com.team334.frcplugin.project;

import com.intellij.openapi.ui.DialogWrapper;
import com.team334.frcplugin.panels.ExampleProject;
import com.team334.frcplugin.panels.ProjectType;
import com.team334.frcplugin.panels.RobotProject;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class Project extends DialogWrapper {
    private ProjectType project = new ProjectType();
    private RobotProject robot = new RobotProject();
    private ExampleProject example = new ExampleProject();

    private CardLayout cl = new CardLayout();
    private JPanel rotator = new JPanel(cl);

    public Project() {
        super(false);

        setTitle("New Project");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        robot.setPackageField();
        rotator.add(robot.getPanel(), "robot");
        rotator.add(example.getPanel(), "example");

        project.getRobotJava().addActionListener(actionEvent -> cl.show(rotator, "robot"));
        project.getExampleRobotJava().addActionListener(actionEvent -> cl.show(rotator, "example"));

        panel.add(project.getPanel(), BorderLayout.NORTH);
        panel.add(rotator, BorderLayout.SOUTH);

        return panel;
    }

    @Override
    protected void doOKAction() {
        // TODO project creation
        close(OK_EXIT_CODE);
    }

}
