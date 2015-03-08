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
 * Copyright (C) 2013-2015 Gennaro Imparato
 */

package it.slumdroid.utilities.module;

import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.utilities.module.guianalyzer.ComboBoxRenderer;
import it.slumdroid.utilities.module.guianalyzer.ImagePanel;
import it.slumdroid.utilities.module.guianalyzer.Perturbations;
import it.slumdroid.utilities.module.guianalyzer.ProcessGuiTree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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

// TODO: Auto-generated Javadoc
/**
 * The Class GuiAnalyzer.
 */
public class GuiAnalyzer extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ProcessGuiTree object. */
	private ProcessGuiTree processGuiTree;

	/** The current directory. */
	private String currentDirectory = new String();

	/** The first path. */
	private String firstPath = new String();

	/** The screenshots directory. */
	private String screenshotsDirectory = "\\..\\screenshots\\";

	/** The preferences path. */
	private String preferencesPath = "\\..\\data\\preferences.xml";

	/** The combo values. */
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

	/** The file. */
	private File theFile;

	/** The tabel model. */
	private DefaultTableModel tabelModel;

	/** The j panel widgets. */
	private JPanel jPanelWidgets;

	/** The j panel image. */
	private JPanel jPanelImage;

	/** The j table info. */
	private JTable jTableInfo;

	/** The col type. */
	private Object[] colType;

	/** The col id. */
	private Object[] colId;

	/**
	 * Instantiates a new gui analyzer.
	 */
	public GuiAnalyzer() {
		setResizable(false);
		initComponents();
		new FileDrop (null, jPanelWidgets, new FileDrop.Listener() {
			public void filesDropped(File[] files ) {
				if (files.length == 0) {
					return;
				}
				theFile = files[0];
				currentDirectory = files[0].getPath().replace(files[0].getName(), "");
				createLayout();
			}
		});
	}

	/**
	 * Instantiates a new gui analyzer.
	 *
	 * @param expPath the exp path
	 */
	public GuiAnalyzer(String expPath) {
		this.firstPath = new String(expPath);
		setResizable(false);
		initComponents();
		new FileDrop (null, jPanelWidgets, new FileDrop.Listener() {
			public void filesDropped(File[] files ) {
				if (files.length == 0) {
					return;
				}
				theFile = files[0];
				currentDirectory = files[0].getPath().replace(files[0].getName(), "");
				createLayout();
			}
		});
	}

	/**
	 * Inits the components.
	 */
	private void initComponents() {
		JTabbedPane jTabbedWidget = new JTabbedPane();
		jPanelWidgets = new JPanel();
		jPanelWidgets.setLayout(new BorderLayout());
		jTableInfo = new JTable();
		JMenuBar jMenuBar = new JMenuBar();
		JMenu jMenuFile = new JMenu();
		jMenuFile.setText("File");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("GUI Analyzer");
		setPreferredSize(new Dimension(960, 513));
		getContentPane().setLayout(new GridLayout(1, 0));
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		jScrollPane.setViewportView(jTableInfo);
		jPanelWidgets.add(jScrollPane, BorderLayout.CENTER);
		jPanelImage = new ImagePanel("");
		jPanelWidgets.add(jPanelImage, BorderLayout.LINE_END);
		jTabbedWidget.addTab("Widgets", jPanelWidgets);
		getContentPane().add(jTabbedWidget);
		JMenuItem jMenuOpen = new javax.swing.JMenuItem();
		jMenuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		jMenuOpen.setIcon(new ImageIcon(getClass().getResource("/open.png"))); 
		jMenuOpen.setText("Open");
		jMenuOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				open();
			}
		});
		jMenuFile.add(jMenuOpen);
		JMenuItem jMenuSave = new JMenuItem();
		jMenuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		jMenuSave.setIcon(new ImageIcon(getClass().getResource("/save.png")));
		jMenuSave.setText("Save");
		jMenuSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!save()) {
					JOptionPane.showMessageDialog(null, "Error\nPreferences.xml was not created");
				} else {
					System.exit(NORMAL);
				}
			}
		});
		jMenuFile.add(jMenuSave);
		JMenuItem jMenuExit = new JMenuItem();
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

	/**
	 * Creates the layout.
	 */
	private void createLayout() {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		resetAll();

		try {
			processGuiTree = new ProcessGuiTree(theFile.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		int numWidgets =  processGuiTree.getNumWidgets();

		colId = new Object[numWidgets];
		Object[] colWidgets = new Object[numWidgets];
		Object[] colName = new Object[numWidgets];
		Object[] colValue = new Object[numWidgets];
		colType = new Object[numWidgets];
		Object[] colScreen = new Object[numWidgets];
		Object[] colInteraction = new Object[numWidgets];
		Object[] colSimpleType = new Object[numWidgets];

		int item = 0;
		Collection<WidgetState> WidgetsColl = processGuiTree.getWidgets().values();
		for (WidgetState widget: WidgetsColl) {
			colId[item] = widget.getId();
			colWidgets[item] = widget.getType();
			colName[item] = widget.getName();
			colValue[item] = widget.getValue();
			colType[item] = widget.getTextType();
			colInteraction[item] = processGuiTree.getInteractions().get(widget.getId());
			colSimpleType[item] = widget.getSimpleType();
			colScreen[item] = new JButton(processGuiTree.getScreens().get(widget.getId()));
			item++;
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
		JComboBox<String> comboBox = new JComboBox<String>(comboValues);
		jTableInfo.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(comboBox));
		jTableInfo.getColumnModel().getColumn(7).setCellRenderer(new ComboBoxRenderer(comboValues));

		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged (ItemEvent event) {
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

		firstImage();
	}

	/**
	 * Reset all.
	 */
	private void resetAll() {
		tabelModel = (DefaultTableModel) jTableInfo.getModel();
		tabelModel.setRowCount(0);
		tabelModel.setColumnCount(0);
	}

	/**
	 * Change widget info.
	 */
	private void changeWidgetInfo() {
		int row = jTableInfo.getSelectedRow();
		Object value = ((JButton) jTableInfo.getValueAt(row, 8)).getText();
		addImage(currentDirectory + screenshotsDirectory + value);
	}

	/**
	 * First image.
	 */
	private void firstImage() {
		if (processGuiTree.getNumWidgets() != 0) {
			Object value = ((JButton) jTableInfo.getValueAt(0, 8)).getText();
			addImage(currentDirectory + screenshotsDirectory + value);	
		}
	}

	/**
	 * Adds the image.
	 *
	 * @param image the image
	 */
	private void addImage(String image) {
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

	/**
	 * Open.
	 */
	private void open() {
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

	/**
	 * Save.
	 *
	 * @return true, if successful
	 */
	private boolean save() {
		try {
			String place = System.getProperty("user.dir");
			createXml(place + preferencesPath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Creates the xml.
	 *
	 * @param preferencesFile the preferences file
	 */
	private void createXml(String preferencesFile) {
		StringBuilder builder =  new StringBuilder();
		String line = new String();
		boolean interact = false;

		BufferedReader inputStream1 = null;
		PrintWriter outputStream1 = null;

		try{
			inputStream1 = new BufferedReader (new FileReader (preferencesFile));
			while ((line = inputStream1.readLine()) != null ) {
				if (line.contains("<node name=\"interactions\">")) {
					interact = true;
				}
				if (line.contains("</map>") && interact) {
					interact = false;
					addPerturbations(builder);
				}
				builder.append("<"+ line.split("<")[1]);
			}
			inputStream1.close();
			new Tools().xmlWriter(preferencesFile, builder);
			if (!firstPath.equals("")) {
				if (!new File(firstPath).exists()) {
					new File(firstPath).mkdir();
				}
				outputStream1 = new PrintWriter (firstPath.concat("/firstboot.txt"));
				outputStream1.write("firstboot");
				outputStream1.close();	
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream1.close();
				outputStream1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Adds the perturbations.
	 *
	 * @param builder the builder
	 */
	private void addPerturbations(StringBuilder builder) {
		String data = "<entry key=\"EXTRA_INPUTS[INDEX]\" value=\"writeText,ID,PERTUBATIONS\"/>";
		int countInput = 0;
		int countEvent = 0;
		for (int index = 0; index < processGuiTree.getNumWidgets(); index++) { 
			String control = (String) jTableInfo.getValueAt(index, 7);
			if (control != null) {
				try{
					if (!control.equals("exclude")) {
						String pertubedInput = new Perturbations(colType[index], (String) jTableInfo.getValueAt(index, 4)).perturbe(control);
						if (!pertubedInput.equals("")) {
							if (jTableInfo.getValueAt(index, 6).equals("Input")) {
								builder.append(data
										.replace("INDEX", String.valueOf(countInput))
										.replace("ID",  String.valueOf(colId[index]))
										.replace("PERTUBATIONS", pertubedInput));
								countInput++;
							} else if (jTableInfo.getValueAt(index, 6).equals("Event")) {
								if (((String) jTableInfo.getValueAt(index, 5)).endsWith("SearchAutoComplete")) {
									builder.append(data
											.replace("EXTRA_INPUTS", "EXTRA_EVENTS")
											.replace("INDEX", String.valueOf(countEvent))
											.replace("ID",  String.valueOf(colId[index]))
											.replace("writeText", "enterText")
											.replace("PERTUBATIONS", pertubedInput));
								} else {
									builder.append(data
											.replace("EXTRA_INPUTS", "EXTRA_EVENTS")
											.replace("INDEX", String.valueOf(countEvent))
											.replace("ID",  String.valueOf(colId[index]))
											.replace("PERTUBATIONS", pertubedInput));	
								}
								countEvent++;
							} else if (jTableInfo.getValueAt(index, 6).equals("Input & Event")) {
								builder.append(data
										.replace("INDEX", String.valueOf(countInput))
										.replace("ID",  String.valueOf(colId[index]))
										.replace("PERTUBATIONS", pertubedInput));
								countInput++;
								builder.append(data
										.replace("EXTRA_INPUTS", "EXTRA_EVENTS")
										.replace("INDEX", String.valueOf(countEvent))
										.replace("ID",  String.valueOf(colId[index]))
										.replace("PERTUBATIONS", pertubedInput));
								countEvent++;
							}
						}
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}