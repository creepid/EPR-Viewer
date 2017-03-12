/*
 * EPRcommView.java
 */

package by.bsu.physics;

import by.bsu.physics.graph.GraphPanel;
import by.bsu.physics.report.ReportSettingsPanel;
import by.bsu.physics.report.WriteExcelReport;
import by.bsu.physics.serialcomm.CommPortLister;
import by.bsu.physics.serialcomm.SerialComm;
import by.bsu.physics.settings.SettingsPanel;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jxl.write.WriteException;

/**
 * The application's main frame.
 */
public class EPRcommView extends FrameView {

    public EPRcommView(SingleFrameApplication app) {
        super(app);

        initComponents();
        graphPanel = new GraphPanel();
        graphPanel.setName("graphPanel");
        
        settingsPanel = new SettingsPanel();
        settingsPanel.setName("settingsPanel");
        
        reportSettingsPanel=new ReportSettingsPanel();
        reportSettingsPanel.setName("reportPanel");
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        tabbedPane.addTab(resourceMap.getString("tabs.graph.name"), graphPanel);
        tabbedPane.addTab(resourceMap.getString("tabs.settings.name"), settingsPanel);
        tabbedPane.addTab(resourceMap.getString("tabs.report.name"), reportSettingsPanel);
        
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = EPRcomm.getApplication().getMainFrame();
            aboutBox = new EPRcommAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        EPRcomm.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        freePortsItem = new javax.swing.JMenuItem();
        saveReportMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        commandMenu = new javax.swing.JMenu();
        checkconMenuItem = new javax.swing.JMenuItem();
        startMenuItem = new javax.swing.JMenuItem();
        prpositionMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setBorder(new javax.swing.border.MatteBorder(null));
        mainPanel.setMaximumSize(new java.awt.Dimension(800, 600));
        mainPanel.setMinimumSize(new java.awt.Dimension(800, 0));
        mainPanel.setName("mainPanel"); // NOI18N

        tabbedPane.setName("tabbedPane"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(by.bsu.physics.EPRcomm.class).getContext().getResourceMap(EPRcommView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        freePortsItem.setActionCommand(resourceMap.getString("freePortsItem.actionCommand")); // NOI18N
        freePortsItem.setLabel(resourceMap.getString("freePortsItem.label")); // NOI18N
        freePortsItem.setName("freePortsItem"); // NOI18N
        freePortsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                freePortsItemActionPerformed(evt);
            }
        });
        fileMenu.add(freePortsItem);

        saveReportMenuItem.setText(resourceMap.getString("saveReportMenuItem.text")); // NOI18N
        saveReportMenuItem.setEnabled(false);
        saveReportMenuItem.setName("saveReportMenuItem"); // NOI18N
        saveReportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveReportMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveReportMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(by.bsu.physics.EPRcomm.class).getContext().getActionMap(EPRcommView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        commandMenu.setLabel(resourceMap.getString("commandMenu.label")); // NOI18N
        commandMenu.setName("commandMenu"); // NOI18N

        checkconMenuItem.setText(resourceMap.getString("checkconMenuItem.text")); // NOI18N
        checkconMenuItem.setName("checkconMenuItem"); // NOI18N
        checkconMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkconMenuItemActionPerformed(evt);
            }
        });
        commandMenu.add(checkconMenuItem);

        startMenuItem.setAction(actionMap.get("startMenuItemAction")); // NOI18N
        startMenuItem.setLabel(resourceMap.getString("startMenuItem.label")); // NOI18N
        startMenuItem.setName("startMenuItem"); // NOI18N
        startMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startMenuItemActionPerformed(evt);
            }
        });
        commandMenu.add(startMenuItem);
        startMenuItem.getAccessibleContext().setAccessibleName(resourceMap.getString("jMenuItem1.AccessibleContext.accessibleName")); // NOI18N

        prpositionMenuItem.setText(resourceMap.getString("prpositionMenuItem.text")); // NOI18N
        prpositionMenuItem.setName("prpositionMenuItem"); // NOI18N
        prpositionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prpositionMenuItemActionPerformed(evt);
            }
        });
        commandMenu.add(prpositionMenuItem);

        menuBar.add(commandMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 630, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

private void startMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startMenuItemActionPerformed
        SerialComm.setCOM_BOUD_RATE(settingsPanel.getBoudRate());
        SerialComm.setCOM_PORT_NUM(settingsPanel.getSerialNumber());
        SerialComm serialcomm=new SerialComm("start");
            serialcomm.setGraph(graphPanel);
        int conTimeMilli=settingsPanel.getConTime();
        serialcomm.setCommTimeSec(conTimeMilli);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
        reportSettingsPanel.setReportDate(dateFormat.format(date));
        reportSettingsPanel.setReportScanTime(conTimeMilli);
        saveReportMenuItem.setEnabled(true);
}//GEN-LAST:event_startMenuItemActionPerformed

private void prpositionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prpositionMenuItemActionPerformed
     String message;
     SerialComm.setCOM_BOUD_RATE(settingsPanel.getBoudRate());
     SerialComm.setCOM_PORT_NUM(settingsPanel.getSerialNumber());
     ResourceMap resourceMap = getResourceMap(); 
     message=resourceMap.getString("message.limit.right.true");
    new SerialComm("primary_position").setMessageDlg(message);
}//GEN-LAST:event_prpositionMenuItemActionPerformed

private void checkconMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkconMenuItemActionPerformed
     String message;
     SerialComm.setCOM_BOUD_RATE(settingsPanel.getBoudRate());
     SerialComm.setCOM_PORT_NUM(settingsPanel.getSerialNumber());
     SerialComm serialcomm=new SerialComm("check");
      ResourceMap resourceMap = getResourceMap();      
     if (serialcomm.isCheckAnswer()==true)
            message=resourceMap.getString("message.check.answer.true");
                    else 
                    message=resourceMap.getString("message.check.answer.false");
     JOptionPane.showMessageDialog(null, message, resourceMap.getString("message.information.title"), JOptionPane.INFORMATION_MESSAGE);          
}//GEN-LAST:event_checkconMenuItemActionPerformed

private void freePortsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_freePortsItemActionPerformed
    StringBuffer message=new StringBuffer();
    ResourceMap resourceMap = getResourceMap();  
    List<String> freePorts=new CommPortLister().getList();
    Iterator it=freePorts.iterator();
    while (it.hasNext()){
        message.append(it.next().toString()+"\n");
    }
    if (message.length()==0)
        message.append(resourceMap.getString("message.ports.free.no"));
    JOptionPane.showMessageDialog(null, message.toString(), resourceMap.getString("message.ports.free.title"), JOptionPane.INFORMATION_MESSAGE);
}//GEN-LAST:event_freePortsItemActionPerformed

private void saveReportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveReportMenuItemActionPerformed
    report.clear();
    report.put("Title", reportSettingsPanel.getTitle());
    report.put("Date", reportSettingsPanel.getDate());
    report.put("Operator", reportSettingsPanel.getOperator());
    report.put("", "");
    report.put("Scan Range, G", reportSettingsPanel.getRange());
    report.put("Field Set, G", reportSettingsPanel.getFieldSet());
    report.put("N", reportSettingsPanel.getn());
    report.put("Scan time", reportSettingsPanel.getScanTime());
    report.put("Modulation amplitude, G", reportSettingsPanel.getAmp());
    report.put("Modulation freq, Hz", reportSettingsPanel.getFreq());
    report.put("Receiver gain", reportSettingsPanel.getGain());
    report.put("Temperature, C", reportSettingsPanel.getTemp());
    report.put("Microwave power, mW", reportSettingsPanel.getPower());
    report.put("Microwave freq, Hz", reportSettingsPanel.getMicFreq());
    
    JFileChooser fc = new JFileChooser();
    fc.setSelectedFile(new File("report.xls"));
    int returnVal = fc.showSaveDialog(null);
     if (returnVal == JFileChooser.APPROVE_OPTION) {
	File file = fc.getSelectedFile();
        WriteExcelReport excelReport = new WriteExcelReport();
        excelReport.setSpectrum(graphPanel.getDataToView());
        excelReport.setReportMap(report);
        excelReport.setOutputFile(file.getAbsolutePath());
        try{
            excelReport.write();
        }
            catch (WriteException ex){}
            catch (IOException ex){}
        }
}//GEN-LAST:event_saveReportMenuItemActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem checkconMenuItem;
    private javax.swing.JMenu commandMenu;
    private javax.swing.JMenuItem freePortsItem;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem prpositionMenuItem;
    private javax.swing.JMenuItem saveReportMenuItem;
    private javax.swing.JMenuItem startMenuItem;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    private by.bsu.physics.graph.GraphPanel graphPanel;
    private by.bsu.physics.settings.SettingsPanel settingsPanel;
    private by.bsu.physics.report.ReportSettingsPanel reportSettingsPanel;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private LinkedHashMap<String, String> report=new LinkedHashMap<String, String>();
     
}

