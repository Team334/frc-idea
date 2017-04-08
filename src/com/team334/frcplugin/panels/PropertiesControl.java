package com.team334.frcplugin.panels;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.team334.frcplugin.Settings;
import com.team334.frcplugin.wizard.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.text.NumberFormat;

import static com.team334.frcplugin.Settings.WPI_PATH;

public class PropertiesControl extends DialogWrapper {
    private Settings settings;

    private JPanel mainPanel;

    private JFormattedTextField teamNumberField;

    private JComboBox versionField;

    public PropertiesControl(Settings settings) {
        super(false);
        setTitle("Set Properties");

        this.settings = settings;
        versionField.setSelectedItem(settings.getVersion());

        init();
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{getOKAction(), getCancelAction()};
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
                Properties.serialize(WPI_PATH);
            } catch (IOException io) {
                Messages.showErrorDialog("Cannot create wpilib.properties.", "Settings Serialization");
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
