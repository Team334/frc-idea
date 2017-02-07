package com.team334.frcplugin.wizard;

import javax.swing.*;
import java.text.NumberFormat;

public class SetProperties extends JFrame {
    private static Properties prop;
    private JPanel mainPanel;

    private JFormattedTextField teamNumberField;
    private NumberFormat numberFormat;

    private JComboBox versionField;

    private JButton cancelButton;
    private JButton submitButton;

    public SetProperties() {
        super("Properties");
        prop = new Properties();

        versionField.setSelectedItem(prop.getVersion());

        setContentPane(mainPanel);
        setLocationRelativeTo(null);

        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pack();

        setVisible(true);
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public JFormattedTextField getTeamNumberField() {
        return teamNumberField;
    }

    public JComboBox getVersionField() {
        return versionField;
    }

    private void createUIComponents() {
        numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setParseIntegerOnly(true);

        teamNumberField = new JFormattedTextField(numberFormat);
    }
}
