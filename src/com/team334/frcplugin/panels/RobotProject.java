package com.team334.frcplugin.panels;

import com.team334.frcplugin.Settings;

import javax.swing.*;

public class RobotProject {
    private Settings settings = Settings.getInstance();

    private JPanel robotProject;

    private JPanel projectType;

    private JTextField packageField;

    private JLabel packageLabel;

    private JRadioButton iterativeBased;
    private JRadioButton commandBased;
    private JRadioButton sampleBased;

    public JPanel getPanel() {
        setPackageField();
        return robotProject;
    }

    private void setPackageField() {
        packageField.setText(settings.getPackage());
    }
}
