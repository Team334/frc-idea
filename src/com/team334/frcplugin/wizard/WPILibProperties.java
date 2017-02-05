package com.team334.frcplugin.wizard;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WPILibProperties extends AnAction {
    private String teamNumber = String.valueOf(0);
    private String version = "current";

    @Override
    public void actionPerformed(AnActionEvent e) {
        new WPILibSetProperties();
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

    public void serialize(String wpiDir) throws IOException {
        FileWriter fw = new FileWriter(new File(wpiDir, "wpilib.properties"));

        fw.write("# new properties entered inside will be removed on plugin update\n");
        fw.write(String.format("version=%s\n", getVersion()));
        fw.write(String.format("team-number=%s\n", getTeamNumber()));

        fw.close();
    }
}
