package com.team334.frcplugin.panels;

import com.intellij.openapi.ui.DialogWrapper;
import com.team334.frcplugin.wizard.Properties;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.text.NumberFormat;

import static com.team334.frcplugin.wizard.Properties.WPI_DIR;
import static com.team334.frcplugin.wizard.Properties.WPI_PATH;

public class PropertiesControl extends DialogWrapper {
    private Properties prop = new Properties();
    private JPanel mainPanel;

    private JFormattedTextField teamNumberField;
    private NumberFormat numberFormat;

    private JComboBox versionField;

    public PropertiesControl() {
        super(false);

        setTitle("Set Properties");
        init();

        versionField.setSelectedItem(prop.getVersion());
    }

    public JFormattedTextField getTeamNumberField() {
        return teamNumberField;
    }

    public JComboBox getVersionField() {
        return versionField;
    }

    @Override
    protected void doOKAction() {
        prop.setTeamNumber(getTeamNumberField().getText());
        prop.setVersion(getVersionField().getSelectedItem().toString());

        if (WPI_DIR.exists()) {
            try {
                prop.serialize(WPI_PATH);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        close(OK_EXIT_CODE);
    }

    private void createUIComponents() {
        numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setParseIntegerOnly(true);

        teamNumberField = new JFormattedTextField(numberFormat);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }
}
