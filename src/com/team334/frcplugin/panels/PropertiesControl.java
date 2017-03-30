package com.team334.frcplugin.panels;

import com.intellij.openapi.ui.DialogWrapper;
import com.team334.frcplugin.Settings;
import com.team334.frcplugin.wizard.Properties;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.text.NumberFormat;

import static com.team334.frcplugin.Settings.WPI_PATH;

public class PropertiesControl extends DialogWrapper {
    private Settings settings;
    private Properties prop;

    private JPanel mainPanel;

    private JFormattedTextField teamNumberField;

    private JComboBox versionField;

    public PropertiesControl() {
        super(false);
        setTitle("Set Properties");

        settings = Settings.INSTANCE;
        prop = new Properties();

        versionField.setSelectedItem(settings.getVersion());

        init();
    }

    public JFormattedTextField getTeamNumberField() {
        return teamNumberField;
    }

    public JComboBox getVersionField() {
        return versionField;
    }

    @Override
    protected void doOKAction() {
        settings.setTeamNumber(getTeamNumberField().getText());
        settings.setVersion(getVersionField().getSelectedItem().toString());

        if (Settings.installed) {
            try {
                prop.serialize(WPI_PATH);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        close(OK_EXIT_CODE);
    }

    private void createUIComponents() {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
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
