package com.team334.frcplugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import static java.io.File.separator;

@State(name = "Settings", storages = @Storage("other.xml"))
public class Settings implements PersistentStateComponent<Settings> {
    private static String teamNumber = String.valueOf(0);
    private static String version = "current";
    private static String projectPackage = "org.usfirst.frc.team" + teamNumber + ".robot";

    public static final String WPI_PATH = System.getProperty("user.home") + separator + "wpilib";
    public static final String WPI_LIBS = WPI_PATH + separator + "java" + separator + "current" + separator + "lib";

    public static boolean installed = false;

    public static Settings getInstance() {
        return ServiceManager.getService(Settings.class);
    }

    public String getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(String teamNumber) {
        Settings.teamNumber = teamNumber;
        Settings.projectPackage = "org.usfirst.frc.team" + teamNumber + ".robot";
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        Settings.version = version;
    }

    public String getPackage() {
        return projectPackage;
    }

    @Nullable
    @Override
    public Settings getState() {
        return this;
    }

    @Override
    public void loadState(Settings settings) {
        XmlSerializerUtil.copyBean(settings, this);
    }
}
