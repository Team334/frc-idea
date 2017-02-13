package com.team334.frcplugin.panels;

import javax.swing.*;

public class ProjectType {
    private JPanel selection;

    private ButtonGroup selectionGroup;

    private JRadioButton robotJava;
    private JRadioButton exampleRobotJava;

    public JPanel getPanel() {
        return selection;
    }

    public JRadioButton getExampleRobotJava() {
        return exampleRobotJava;
    }

    public JRadioButton getRobotJava() {
        return robotJava;
    }
}