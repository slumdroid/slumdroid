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

package it.slumdroid.utilities.module.guianalyzer;

import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.utilities.module.Tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.*;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import net.iharder.dnd.FileDrop;

public class GuiAnalyzer extends JFrame {

	private static final long serialVersionUID = 1L;
	private ProcessGuiTree pI;

	private String currentDirectory = new String();
	private String firstPath = new String();
	private String screenshotsDirectory = "\\..\\screenshots\\";
	private String preferencesPath = "\\..\\data\\preferences.xml";
	private String[] comboValues = {
			"",
			"Generic",
			"Number",
			"Url",
			"EMail",
			"Zip Code",
			"ISBN",
			"Credit Card",
			"Exclude"
	};

	private File theFile;
	private DefaultTableModel tabelModel;

	private JComboBox<?> comboBox;
	private JMenu jMenuFile;
	private JMenuBar jMenuBar;
	private JMenuItem jMenuOpen;
	private JMenuItem jMenuSave;
	private JMenuItem jMenuExit;
	private JPanel jPanelWidgets;
	private JPanel jPanelImage;
	private JScrollPane jScrollPane;
	private JTabbedPane jTabbedWidget;
	private JTable jTableInfo;

	private Object[] colId;
	private Object[] colWidgets;
	private Object[] colName;
	private Object[] colValue;
	private Object[] colType;
	private Object[] colScreen;
	private Object[] colInteraction;
	private Object[] colSimpleType;

	public GuiAnalyzer () {

		setResizable(false);
		initComponents();

		new FileDrop (null, jPanelWidgets, new FileDrop.Listener() {
			public void filesDropped(File[] files ) {
				if (files.length==0) return;
				theFile = files[0];
				currentDirectory = files[0].getPath().replace(files[0].getName(), "");
				createLayout();
			}
		});

	}

	public GuiAnalyzer (String expPath) {

		setFirstPath(expPath);
		setResizable(false);
		initComponents();

		new FileDrop (null, jPanelWidgets, new FileDrop.Listener() {
			public void filesDropped(File[] files ) {
				if (files.length==0) return;
				theFile = files[0];
				currentDirectory = files[0].getPath().replace(files[0].getName(), "");
				createLayout();
			}
		});

	}

	private void initComponents() {

		jTabbedWidget = new JTabbedPane();
		jPanelWidgets = new JPanel();
		jPanelWidgets.setLayout(new BorderLayout());
		jTableInfo = new JTable();
		jMenuBar = new JMenuBar();
		jMenuFile = new JMenu();
		jMenuFile.setText("File");

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("GUI Analyzer");
		setPreferredSize(new Dimension(960, 513));
		getContentPane().setLayout(new GridLayout(1, 0));

		jScrollPane = new JScrollPane();
		jScrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		jScrollPane.setViewportView(jTableInfo);
		jPanelWidgets.add(jScrollPane, BorderLayout.CENTER);
		jPanelImage = new ImagePanel("");
		jPanelWidgets.add(jPanelImage, BorderLayout.LINE_END);
		jTabbedWidget.addTab("Widgets", jPanelWidgets);

		getContentPane().add(jTabbedWidget);

		jMenuOpen = new javax.swing.JMenuItem();
		jMenuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		jMenuOpen.setIcon(new ImageIcon(getClass().getResource("/open.png"))); 
		jMenuOpen.setText("Open");
		jMenuOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				open();
			}
		});
		jMenuFile.add(jMenuOpen);

		jMenuSave = new JMenuItem();
		jMenuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		jMenuSave.setIcon(new ImageIcon(getClass().getResource("/save.png")));
		jMenuSave.setText("Save");
		jMenuSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!save()) JOptionPane.showMessageDialog(null, "Error\nPreferences.xml was not created");
				else System.exit(NORMAL);
			}
		});
		jMenuFile.add(jMenuSave);

		jMenuExit = new JMenuItem();
		jMenuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		jMenuExit.setIcon(new ImageIcon(getClass().getResource("/exit.png")));
		jMenuExit.setText("Exit");
		jMenuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});
		jMenuFile.add(jMenuExit);

		jMenuBar.add(jMenuFile);
		setJMenuBar(jMenuBar);
		pack();
	}

	private void createLayout() {

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		resetAll();

		try {
			pI = new ProcessGuiTree(theFile.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		colId = new Object[pI.numWidgets];
		colWidgets = new Object[pI.numWidgets];
		colName = new Object[pI.numWidgets];
		colValue = new Object[pI.numWidgets];
		colType = new Object[pI.numWidgets];
		colScreen = new Object[pI.numWidgets];
		colInteraction = new Object[pI.numWidgets];
		colSimpleType = new Object[pI.numWidgets];

		int i = 0;
		Collection<WidgetState> WidgetsColl = pI.getWidgets().values();
		for (WidgetState widget: WidgetsColl) {
			colId[i] = widget.getId();
			colWidgets[i] = widget.getType();
			colName[i] = widget.getName();
			colValue[i] = widget.getValue();
			colType[i] = widget.getTextType();
			colInteraction[i] = pI.getInteractions().get(widget.getId());
			colSimpleType[i] = widget.getSimpleType();
			colScreen[i] = new JButton(pI.getScreens().get(widget.getId()));
			i++;
		}

		tabelModel.addColumn("Id", colId);	
		tabelModel.addColumn("Widgets Type", colWidgets);
		tabelModel.addColumn("Simple Type", colSimpleType);
		tabelModel.addColumn("Name", colName);
		tabelModel.addColumn("Valid Input", colValue);
		tabelModel.addColumn("Text Type", colType);
		tabelModel.addColumn("Interactions", colInteraction);
		tabelModel.addColumn("Perturbation", new String[]{""});
		tabelModel.addColumn("Screenshot", colScreen);

		// ComboBox
		comboBox = new JComboBox<Object>(comboValues);
		jTableInfo.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(comboBox));
		jTableInfo.getColumnModel().getColumn(7).setCellRenderer(new ComboBoxRenderer(comboValues));

		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged (ItemEvent e) {
				changeWidgetInfo();
			}

		});

		jTableInfo.setRowHeight(20);
		jTableInfo.getColumnModel().getColumn(8).setMinWidth(0);
		jTableInfo.getColumnModel().getColumn(8).setMaxWidth(0);
		jTableInfo.getColumnModel().getColumn(8).setWidth(0);
		jTableInfo.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked (MouseEvent e) {
				changeWidgetInfo();
			}

		});	

	}

	private void resetAll () {

		tabelModel = (DefaultTableModel) jTableInfo.getModel();
		tabelModel.setRowCount(0);
		tabelModel.setColumnCount(0);

	}

	private void changeWidgetInfo () {

		int row = jTableInfo.getSelectedRow();
		Object value = ((JButton) jTableInfo.getValueAt(row, 8)).getText();
		addImage(currentDirectory + screenshotsDirectory + value);

	}

	private void addImage (String image) {

		try{
			jPanelWidgets.remove(jPanelImage);
		}catch (Exception e){
			e.printStackTrace();
		}

		jPanelImage = new ImagePanel(image);
		jPanelImage.setBackground(new Color(255, 255, 255));
		jPanelImage.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		jPanelWidgets.add(jPanelImage, BorderLayout.LINE_END);
		jPanelWidgets.revalidate();

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

	private boolean save(){

		try {
			String place = System.getProperty("user.dir");
			createXml(place + preferencesPath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	private void createXml(String preferencesFile) {

		StringBuilder builder =  new StringBuilder();
		String line = new String();
		String data = "<entry key=\"EXTRA_INPUTS[INDEX]\" value=\"writeText,ID,PERTUBATIONS\"/>";

		try{
			BufferedReader inputStream1 = new BufferedReader (new FileReader (preferencesFile));
			while ((line = inputStream1.readLine()) != null ) {
				builder.append("<"+ line.split("<")[1]);
				if (line.contains("<node name=\"interactions\">")){
					line = inputStream1.readLine();
					builder.append("<"+ line.split("<")[1]);
					int countInput = 0;
					int countEvent = 0;
					for (int i=0; i < pI.numWidgets; i++){ 
						String control = (String) jTableInfo.getValueAt(i, 7);
						if (control!=null){
							try{
								if (!control.equals("exclude")){
									String pertubedInput = new Perturbations(colType[i], (String) jTableInfo.getValueAt(i, 4)).perturb(control);
									if (!pertubedInput.equals("")){
										if ((boolean) jTableInfo.getValueAt(i, 6).equals("Input")){
											builder.append(data
													.replace("INDEX", String.valueOf(countInput))
													.replace("ID",  String.valueOf(colId[i]))
													.replace("PERTUBATIONS", pertubedInput));
											countInput++;
										} else if ((boolean) jTableInfo.getValueAt(i, 6).equals("Event")){
											if ((boolean) ((String) jTableInfo.getValueAt(i, 5)).endsWith("SearchAutoComplete")){
												builder.append(data
														.replace("EXTRA_INPUTS", "EXTRA_EVENTS")
														.replace("INDEX", String.valueOf(countEvent))
														.replace("ID",  String.valueOf(colId[i]))
														.replace("writeText", "enterText")
														.replace("PERTUBATIONS", pertubedInput));
											} else{
												builder.append(data
														.replace("EXTRA_INPUTS", "EXTRA_EVENTS")
														.replace("INDEX", String.valueOf(countEvent))
														.replace("ID",  String.valueOf(colId[i]))
														.replace("PERTUBATIONS", pertubedInput));	
											}

											countEvent++;
										} else if ((boolean) jTableInfo.getValueAt(i, 6).equals("Input & Event")){
											builder.append(data
													.replace("INDEX", String.valueOf(countInput))
													.replace("ID",  String.valueOf(colId[i]))
													.replace("PERTUBATIONS", pertubedInput));
											countInput++;
											builder.append(data
													.replace("EXTRA_INPUTS", "EXTRA_EVENTS")
													.replace("INDEX", String.valueOf(countEvent))
													.replace("ID",  String.valueOf(colId[i]))
													.replace("PERTUBATIONS", pertubedInput));
											countEvent++;
										}
									}
								}	
							}catch (Exception e){
								e.printStackTrace();
							}
						}
					}
				}
			}
			inputStream1.close();
			new Tools().xmlWriter(preferencesFile, builder);


			if (!firstPath.equals("")){
				if (!new File(firstPath).exists()) new File(firstPath).mkdir();
				PrintWriter outputStream1 = new PrintWriter (firstPath.concat("/firstboot.txt"));
				outputStream1.write("firstboot");
				outputStream1.close();	
			}	

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setFirstPath(String s) {
		firstPath = s;
	}

}