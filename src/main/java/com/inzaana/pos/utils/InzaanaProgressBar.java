package com.inzaana.pos.utils;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

public class InzaanaProgressBar extends JDialog {

    Container container;
    JProgressBar progressBar;
    JTextArea textArea;

    public InzaanaProgressBar(JFrame frame) {
        super(frame, "Upload Status", false);
        container = getContentPane();
        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        container.add(progressBar);

        textArea = new JTextArea();
        textArea.setAutoscrolls(true);
        textArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

//        scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
//            }
//        });

        container.add(scroll);

        setSize(500, 250);
        container.setLayout(new BoxLayout(container, 3));
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }

    public void setStatusMessage(String status) {
        
        textArea.setText(status + "\n" + textArea.getText());
    }
}
