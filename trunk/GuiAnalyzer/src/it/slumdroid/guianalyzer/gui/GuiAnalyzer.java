/* This file is part of SlumDroid <https://code.google.com/p/slumdroid/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 * for more details.
 * 
 * Copyright (C) 2014 Gennaro Imparato
 */

package it.slumdroid.guianalyzer.gui;

import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.guianalyzer.perturbation.PerturbationHandler;
import it.slumdroid.guianalyzer.tools.ProcessInputs;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.*;
import java.io.*;
import java.util.Collection;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import net.iharder.dnd.FileDrop;

import java.awt.Dimension;

public class GuiAnalyzer extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private ProcessInputs pI;
	private String[] comboValues = PerturbationHandler.listPerturbations();
	private String currentDirectory;
	private File theFile;
	private DefaultTableModel model1, model2;
	private final String screenshotsDirectory = "\\..\\screenshots\\";
	private JComboBox<?> comboBox;

	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JMenuItem jMenuItem2;
	private javax.swing.JMenuItem jMenuItem3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JTable jTable1;
	private javax.swing.JTable jTable2;
	private javax.swing.JTextArea jTextArea1;
	private javax.swing.JTextArea jTextArea2;
	
	public static void main (String args[]) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run () {
				new GuiAnalyzer().setVisible(true);
			}
		});

	}

	public GuiAnalyzer () {

		setResizable(false);
		initComponents();
		
		new FileDrop (null, jPanel1, new FileDrop.Listener() {
			public void filesDropped(File[] files ) {
				if (files.length==0) return;
				theFile = files[0];
				createLayout();
			}
		});

	}
	
	private void initComponents() {

		jTabbedPane1 = new javax.swing.JTabbedPane();
		jPanel1 = new javax.swing.JPanel();
		jScrollPane4 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();
		jPanel2 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTable2 = new javax.swing.JTable();
		jScrollPane3 = new javax.swing.JScrollPane();
		jTextArea2 = new javax.swing.JTextArea();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenuItem1 = new javax.swing.JMenuItem();
		jMenuItem2 = new javax.swing.JMenuItem();
		jMenuItem3 = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("GUI Analyzer");
		setPreferredSize(new Dimension(960, 513));
		getContentPane().setLayout(new java.awt.GridLayout(1, 0));

		jPanel1.setLayout(new java.awt.BorderLayout());
		jScrollPane4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
		jScrollPane4.setViewportView(jTable1);
		jPanel1.add(jScrollPane4, java.awt.BorderLayout.CENTER);
		jTabbedPane1.addTab("Widgets", jPanel1);

		jPanel2.setLayout(new java.awt.GridLayout(1, 0));
		jScrollPane2.setViewportView(jTable2);
		jPanel2.add(jScrollPane2);

		jTextArea2.setColumns(20);
		jTextArea2.setRows(5);
		jScrollPane3.setViewportView(jTextArea2);

		jTextArea1.setColumns(20);
		jTextArea1.setEditable(false);
		jTextArea1.setRows(5);
		jScrollPane1.setViewportView(jTextArea1);

		getContentPane().add(jTabbedPane1);

		jMenu1.setText("File");

		jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open.png"))); 
		jMenuItem1.setText("Open");
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				open();
			}
		});
		jMenu1.add(jMenuItem1);

		jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png")));
		jMenuItem2.setText("Save as...");
		jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					save();
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(null, "You must first generate JUnit.java in order to save it.");
				}
			}
		});
		jMenu1.add(jMenuItem2);

		jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exit.png")));
		jMenuItem3.setText("Exit");
		jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(0);
			}
		});
		jMenu1.add(jMenuItem3);

		jMenuBar1.add(jMenu1);
		setJMenuBar(jMenuBar1);
		pack();
	}
	
	private void open () {

		JFileChooser toLoad = new JFileChooser();
		toLoad.setFileFilter(new FileNameExtensionFilter("XML GUI Tree", "xml"));
		toLoad.setCurrentDirectory(new File(System.getProperty("user.dir")));
		int code = toLoad.showOpenDialog(this);
		if (code == JFileChooser.APPROVE_OPTION) {
			theFile = toLoad.getSelectedFile();
			currentDirectory = toLoad.getCurrentDirectory().toString();
			createLayout();
		}
	}
	
	private void save () {

		JFileChooser toSave = new JFileChooser();
		toSave.setFileFilter(new FileNameExtensionFilter("Files", "java"));
		toSave.setCurrentDirectory(new File(currentDirectory));
		toSave.setSelectedFile(new File(currentDirectory + "\\JUnit.java")); 
		int code = toSave.showSaveDialog(this);
		File fileName = toSave.getSelectedFile();

		if (code == JFileChooser.APPROVE_OPTION) {
			try {
				FileWriter fileOut = new FileWriter(fileName);
				fileOut.write(jTextArea1.getText());
				fileOut.close();
			} catch (Exception e) {
				System.err.println(e);
			}
		}

	}

	private void createLayout() {
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		resetAll();

		try {
			pI = new ProcessInputs(theFile.toString(), currentDirectory + "\\JUnit.java");
		} catch (Exception e) {
			pI = null;
			currentDirectory = null;

			JOptionPane.showMessageDialog(null, e.getMessage());

			return;
		} finally {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		pI.PerturbWidgets();

		Object[] colId = new Object[pI.numWidgets];
		Object[] colWidgets = new Object[pI.numWidgets];
		Object[] colName = new Object[pI.numWidgets];
		Object[] colValue = new Object[pI.numWidgets];
		Object[] colType = new Object[pI.numWidgets];
		Object[] colScreen = new Object[pI.numWidgets];

		int i = 0;
		Collection<WidgetState> WidgetsColl = pI.getWidgets().values();
		for (WidgetState widget: WidgetsColl) {
			colId[i] = widget.getId();
			colWidgets[i] = widget.getType();
			colName[i] = widget.getName();
			colValue[i] = widget.getValue();
			colType[i] = widget.getTextType();
			colScreen[i] = new JButton(pI.getScreens().get(widget.getId()));
			i++;
		}

		model1.addColumn("Id", colId);
		model1.addColumn("Widgets Type", colWidgets);
		model1.addColumn("Description", colName);
		model1.addColumn("Value", colValue);
		model1.addColumn("Text Type", colType);
		model1.addColumn("Perturbation", new String[]{""});
		model1.addColumn("Screenshot", colScreen);

		jTable1.setRowHeight(20);
		// Resize ID column
		jTable1.getColumnModel().getColumn(0).setWidth(80);
		jTable1.getColumnModel().getColumn(0).setMinWidth(80);
		jTable1.getColumnModel().getColumn(0).setMaxWidth(80);

		// ComboBox
		comboBox = new JComboBox<Object>(comboValues);
		jTable1.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboBox));
		jTable1.getColumnModel().getColumn(5).setCellRenderer(new ComboBoxRenderer(comboValues));

		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged (ItemEvent e) {
				changeWidgetInfo();

				if (e.getStateChange() == ItemEvent.SELECTED) {
					String widgetId = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
					String value = e.getItem().toString().toLowerCase();

					if (value.equals("dictionary")) {
						JFileChooser toLoad = new JFileChooser();
						toLoad.setFileFilter(new FileNameExtensionFilter("Dictionary", "txt"));
						toLoad.setCurrentDirectory(new File(System.getProperty("user.dir"))); 
						int code = toLoad.showOpenDialog(jTable1);
						if (code == JFileChooser.APPROVE_OPTION) {
							File dicFile = toLoad.getSelectedFile();
							if (!dicFile.exists()) {
								JOptionPane.showMessageDialog(null, "Unable to find: " + dicFile);
								comboBox.setSelectedItem("");
							} else {
								pI.perturbationHandler.addManualPerturbation(widgetId, "file:" + dicFile.toString());
							}
						} else {
							comboBox.setSelectedItem("");
						}
					} else if (value.equals("manual")) {
						String input = JOptionPane.showInputDialog(null, "Enter text: ", "", 1);
						if (input != null) {
							pI.perturbationHandler.addManualPerturbation(widgetId, "manual:" + input);
						} else {
							comboBox.setSelectedItem("");
						}
					} else if (value.equals("manualregex")) {
						String input = JOptionPane.showInputDialog(null, "Enter Regular Expression: ", "", 1);
						if (input != null) {
							pI.perturbationHandler.addManualPerturbation(widgetId, "regex:" + input);
						} else {
							comboBox.setSelectedItem("");
						}
					} else {
						pI.perturbationHandler.addManualPerturbation(widgetId, value);
					}
				}
			}
		});

		jTable1.getColumnModel().getColumn(6).setMinWidth(0);
		jTable1.getColumnModel().getColumn(6).setMaxWidth(0);
		jTable1.getColumnModel().getColumn(6).setWidth(0);

		jTable1.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked (MouseEvent e) {
				changeWidgetInfo();
			}
			
		});

		jPanel2 = new JPanel();
		jPanel2.setBackground(new java.awt.Color(255, 255, 255));
		jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(
				jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 240, Short.MAX_VALUE));
		jPanel3Layout.setVerticalGroup(
				jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 489, Short.MAX_VALUE));
		jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_END);
		jPanel1.revalidate();
		
	}
	
	private void resetAll () {

		model1 = (DefaultTableModel) jTable1.getModel();
		model1.setRowCount(0);
		model1.setColumnCount(0);

		model2 = (DefaultTableModel) jTable2.getModel();
		model2.setRowCount(0);
		model2.setColumnCount(0);

		jTextArea1.setText("");
		jTextArea2.setText("");

		jPanel1.remove(jPanel2);

	}
	
	private void changeWidgetInfo () {

		int row = jTable1.getSelectedRow();
		Object value = ((JButton) jTable1.getValueAt(row, 6)).getText();
		String perturbations = pI.perturbationHandler.getWidgetsPerturbations().get(jTable1.getValueAt(row, 0).toString()).toString();
		addImage(currentDirectory + screenshotsDirectory + value);
		jTable1.setToolTipText(perturbations);

	}
	
	private void addImage (String image) {

		jPanel1.remove(jPanel2);
		jPanel2 = new ImagePanel(image);
		jPanel2.setBackground(new java.awt.Color(255, 255, 255));
		jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel3Layout);
		jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_END);
		jPanel1.revalidate();

	}

}