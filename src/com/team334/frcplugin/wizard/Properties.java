package com.team334.frcplugin.wizard;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.team334.frcplugin.panels.PropertiesControl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Properties extends AnAction {
    public static final String WPI_PATH = System.getProperty("user.home") + "/wpilib";
    public static final File WPI_DIR = new File(WPI_PATH);

    private static String teamNumber = String.valueOf(0);
    private static String version = "current";

    @Override
    public void actionPerformed(AnActionEvent e) {
        PropertiesControl props = new PropertiesControl();

        props.getTeamNumberField().setText(getTeamNumber());
        props.getVersionField().setSelectedItem(getVersion());

        props.setModal(false);

        props.show();
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
