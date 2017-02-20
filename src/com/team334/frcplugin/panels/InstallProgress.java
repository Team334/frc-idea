package com.team334.frcplugin.panels;

import javax.swing.*;

public class InstallProgress extends JPanel {
    private JPanel progressPanel;
    private JProgressBar progress;
    private JTextArea log;
    private JScrollPane scrollPane;
    private JLabel header;

    public JComponent getPanel() {
        return progressPanel;
    }

    public JTextArea getLog() {
        return log;
    }

    public JProgressBar getProgress() {
        return progress;
    }
}
