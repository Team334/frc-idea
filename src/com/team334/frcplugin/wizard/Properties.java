package com.team334.frcplugin.wizard;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.team334.frcplugin.Settings;
import com.team334.frcplugin.panels.PropertiesControl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Properties extends AnAction {
    private Settings settings = Settings.getInstance();

    public static final String WPI_PATH = System.getProperty("user.home") + "/wpilib";
    public static final File WPI_DIR = new File(WPI_PATH);

    @Override
    public void actionPerformed(AnActionEvent e) {
        PropertiesControl props = new PropertiesControl();

        props.getTeamNumberField().setText(settings.getTeamNumber());
        props.getVersionField().setSelectedItem(settings.getVersion());

        props.setModal(false);

        props.show();
    }

    public void serialize(String dir) throws IOException {
        FileWriter fw = new FileWriter(new File(dir, "wpilib.properties"));

        fw.write("# new properties entered here will be removed on plugin update\n");
        fw.write(String.format("version=%s\n", settings.getVersion()));
        fw.write(String.format("team-number=%s\n", settings.getTeamNumber()));

        fw.close();
    }
}
