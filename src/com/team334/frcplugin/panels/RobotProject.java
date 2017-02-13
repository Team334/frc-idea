package com.team334.frcplugin.panels;

import com.team334.frcplugin.wizard.Properties;

import javax.swing.*;

public class RobotProject {
    private JPanel robotProject;

    private JPanel projectType;
    private JPanel projectInfo;

    private JTextField projectField;
    private JTextField packageField;

    private JLabel nameLabel;
    private JLabel packageLabel;

    private JRadioButton iterativeBased;
    private JRadioButton commandBased;
    private JRadioButton sampleBased;

    private String teamProject;
    private String teamPackage = "org.usfirst.frc.team" + new Properties().getTeamNumber() + ".robot";

    public JPanel getPanel() {
        return robotProject;
    }

    public String getTeamPackage() {
        return teamPackage;
    }

    public void setPackageField() {
        packageField.setText(teamPackage);
    }
}
