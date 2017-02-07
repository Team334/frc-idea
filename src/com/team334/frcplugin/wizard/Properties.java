package com.team334.frcplugin.wizard;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Properties extends AnAction {
    public static final String WPI_PATH = System.getProperty("user.home") + "/wpilib";
    public static final File WPI_DIR = new File(WPI_PATH);

    private String teamNumber = String.valueOf(0);
    private String version = "current";

    @Override
    public void actionPerformed(AnActionEvent e) {
        SetProperties props = new SetProperties();
        
        props.getTeamNumberField().setText(teamNumber);
        props.getVersionField().setSelectedItem(version);

        props.getCancelButton().addActionListener((actionEvent) -> props.dispose());

        props.getSubmitButton().addActionListener((ActionEvent actionEvent) -> {
            setTeamNumber(props.getTeamNumberField().getText());
            setVersion(props.getVersionField().getSelectedItem().toString());

            props.dispose();

            if (WPI_DIR.exists()) {
                try {
                    serialize(WPI_PATH);
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        });
    }

    public void setTeamNumber(String teamNumber) {
        this.teamNumber = teamNumber;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTeamNumber() {
        return teamNumber;
    }

    public String getVersion() {
        return version;
    }

    public void serialize(String dir) throws IOException {
        FileWriter fw = new FileWriter(new File(dir, "wpilib.properties"));

        fw.write("# new properties entered here will be removed on plugin update\n");
        fw.write(String.format("version=%s\n", getVersion()));
        fw.write(String.format("team-number=%s\n", getTeamNumber()));

        fw.close();
    }
}
