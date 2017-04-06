package com.team334.frcplugin.actions;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.RunnerRegistry;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.team334.frcplugin.Settings;
import com.team334.frcplugin.project.RunConfiguration;

public class Deploy extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project p = e.getProject();

        RunnerAndConfigurationSettings configuration = RunConfiguration.getDeployConfiguration();

        Executor executor = DefaultRunExecutor.getRunExecutorInstance();
        ProgramRunner runner = RunnerRegistry.getInstance().getRunner(executor.getId(), configuration.getConfiguration());
        ExecutionEnvironment environment = new ExecutionEnvironment(executor, runner, configuration, p);

        try {
            runner.execute(environment);
        } catch (ExecutionException e1) {
            Messages.showErrorDialog("Failed to run deploy target, target may not exist.", "Deploy Execution");
        }
    }

    @Override
    public void update(AnActionEvent e) {
        Presentation p = e.getPresentation();
        p.setVisible(Settings.installed);
        p.setEnabled(RunConfiguration.getDeployConfiguration() != null);
    }
}
