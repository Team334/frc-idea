package com.team334.frcplugin.panels;

import com.team334.frcplugin.wizard.Properties;

import javax.swing.*;

public class RobotProject {
    private JPanel robotProject;

    private JPanel projectType;

    private JTextField packageField;

    private JLabel packageLabel;

    private JRadioButton iterativeBased;
    private JRadioButton commandBased;
    private JRadioButton sampleBased;

    private String teamProject;
    private String teamPackage = "org.usfirst.frc.team" + new Properties().getTeamNumber() + ".robot";

    public JPanel getPanel() {
        setPackageField();
        return robotProject;
    }

    public String getTeamPackage() {
        return teamPackage;
    }

    private void setPackageField() {
        packageField.setText(teamPackage);
    }
}
