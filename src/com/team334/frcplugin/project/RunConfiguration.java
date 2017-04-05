package com.team334.frcplugin.project;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunManagerEx;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.ide.DataManager;
import com.intellij.lang.ant.config.AntBuildFile;
import com.intellij.lang.ant.config.AntConfiguration;
import com.intellij.lang.ant.config.AntNoFileException;
import com.intellij.lang.ant.config.execution.AntRunConfiguration;
import com.intellij.lang.ant.config.execution.AntRunConfigurationType;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public final class RunConfiguration {

    public static RunnerAndConfigurationSettings getDeployConfiguration() {
        DataContext context = DataManager.getInstance().getDataContextFromFocus().getResult();
        Project p = DataKeys.PROJECT.getData(context);
        RunManager runManager = RunManager.getInstance(p);

        RunnerAndConfigurationSettings configuration = null;
        for (RunnerAndConfigurationSettings settings : runManager.getAllSettings()) {
            if (settings.getName().equals("Deploy")) {
                configuration = settings;
            }
        }

        return configuration;
    }

    public static void addConfiguration() {
        DataContext context = DataManager.getInstance().getDataContextFromFocus().getResult();
        Project p = DataKeys.PROJECT.getData(context);
        VirtualFile file = p.getBaseDir().findChild("build.xml");

        AntConfiguration antConfiguration = AntConfiguration.getInstance(p);
        AntBuildFile buildFile = null;
        try {
            buildFile = antConfiguration.addBuildFile(file);
        } catch (AntNoFileException e1) {
            e1.printStackTrace();
        }

        AntRunConfigurationType antRunConfigurationType = new AntRunConfigurationType();
        ConfigurationFactory configurationFactory = antRunConfigurationType.getFactory();

        AntRunConfiguration runConfiguration = new AntRunConfiguration(p, configurationFactory, "Deploy");
        runConfiguration.acceptSettings(buildFile.getModel().findTarget("deploy"));

        RunManager runManager = RunManager.getInstance(p);
        RunnerAndConfigurationSettings configurationSettings = runManager.createConfiguration(runConfiguration, configurationFactory);

        RunManagerEx runManagerEx = RunManagerEx.getInstanceEx(p);
        runManagerEx.addConfiguration(configurationSettings, false);
        runManagerEx.setSelectedConfiguration(configurationSettings);
    }
}
