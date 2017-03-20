package com.team334.frcplugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(
        name = "Settings",
        storages = {
                @Storage(StoragePathMacros.WORKSPACE_FILE),
                @Storage("settings.xml")
        }
)
public class Settings implements PersistentStateComponent<Settings> {
    private static String teamNumber = String.valueOf(0);
    private static String version = "current";
    private static String projectPackage = "org.usfirst.frc.team" + teamNumber + ".robot";

    public static boolean installed = false;

    public static final Settings INSTANCE = new Settings();

    private Settings() {}

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
    public void loadState(Settings Settings) {
        XmlSerializerUtil.copyBean(Settings, this);
    }
}
