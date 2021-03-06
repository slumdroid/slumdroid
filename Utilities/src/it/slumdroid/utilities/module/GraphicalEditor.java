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

import static it.slumdroid.droidmodels.model.InteractionType.CLICK;
import static it.slumdroid.droidmodels.model.InteractionType.WRITE_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.AUTOC_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.CHECKBOX;
import static it.slumdroid.droidmodels.model.SimpleType.EDIT_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.IMAGE_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.LINEAR_LAYOUT;
import static it.slumdroid.droidmodels.model.SimpleType.MENU_ITEM;
import static it.slumdroid.droidmodels.model.SimpleType.NUMBER_PICKER_BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.TAB_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.TOGGLE_BUTTON;
import static it.slumdroid.utilities.Resources.TOOL;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphicalEditor.
 */
@SuppressWarnings("rawtypes")
public class GraphicalEditor extends JFrame {

	/** The content pane. */
	private JPanel contentPane;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The path. */
	private static String path = System.getProperty("user.dir") + "/../data/preferences.xml";

	/** The app package. */
	private static String appPackage = new String();

	/** The app package class. */
	private static String appPackageClass = new String();

	/** The screenshot box. */
	private static JComboBox screenshotBox;

	/** The scheduler box. */
	private static JComboBox schedulerBox;

	/** The max event selector box. */
	private static JComboBox maxEventSelectorBox;

	/** The tab events box. */
	private static JComboBox tabEventsBox;

	/** The waiting event field. */
	private static JFormattedTextField waitingEventField;

	/** The waiting restart field. */
	private static JFormattedTextField waitingRestartField;

	/** The waiting task field. */
	private static JFormattedTextField waitingTaskField;

	/** The waiting throbber field. */
	private static JFormattedTextField waitingThrobberField;

	/** The chckbx input pertubation. */
	private JCheckBox chckbxInputPertubation;

	/** The available comparator box. */
	private static JComboBox availableComparatorBox;

	/** The check comparator box. */
	private static JComboBox checkComparatorBox;

	/** The list comparator box. */
	private static JComboBox listComparatorBox;

	/** The Title comparator box. */
	private JComboBox titleComparatorBox;

	/** The toast comparator box. */
	private static JComboBox toastComparatorBox;

	/** The edit text box. */
	private static JComboBox editTextBox;

	/** The auto complete box. */
	private static JComboBox autoCompleteBox;

	/** The check box. */
	private static JComboBox checkBox;

	/** The toggle box. */
	private static JComboBox toggleBox;

	/** The input text box. */
	private static JComboBox inputTextBox;

	/** The btn default values. */
	private static JButton btnDefaultValues;

	/** The btn save. */
	private static JButton btnSave;

	/** The algorithm. */
	private static String[] algorithm = {"Breadth (BFS)", "Depth (DFS)", "Random (RFS)"};

	/** The bool. */
	private static String[] bool = {"true", "false"};

	/** The inputs. */
	private static String[] inputs = {"hash values", "random values"};

	/** The interactions. */
	private static String[] interactions = {"event", "input", "both", "none"};

	/** The max event selector. */
	private static String[] maxEventSelector = {"no limit", "1", "2", "3", "4", "5"};

	/** The first path. */
	private static String firstPath = new String();

	/** The builder. */
	private static StringBuilder builder = new StringBuilder();

	/**
	 * Instantiates a new graphical editor.
	 *
	 * @param expPath the exp path
	 * @param appPack the app pack
	 * @param appClass the app class
	 */
	@SuppressWarnings("unchecked")
	public GraphicalEditor(String expPath, String appPack, String appClass) {
		setResizable(false);
		setTitle("Preference Editor");

		setAppPackage(appPack);
		setAppPackageClass(appClass);
		setFirstPath(expPath);

		setBounds(100, 100, 485, 406);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Labels
		JLabel lblGeneralParameters = new JLabel("General Parameters");
		lblGeneralParameters.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblGeneralParameters.setBounds(10, 5, 225, 27);
		contentPane.add(lblGeneralParameters);

		JLabel lblScreenshotEnabled = new JLabel("Screenshot Enabled");
		lblScreenshotEnabled.setBounds(10, 36, 151, 14);
		contentPane.add(lblScreenshotEnabled);

		JLabel lblScheduler = new JLabel("Scheduler Algorithm");
		lblScheduler.setBounds(10, 61, 123, 14);
		contentPane.add(lblScheduler);

		JLabel lblAutomationParameters = new JLabel("Automation Parameters");
		lblAutomationParameters.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAutomationParameters.setBounds(10, 229, 225, 29);
		contentPane.add(lblAutomationParameters);

		JLabel lblAfterEvent = new JLabel("Waiting after Event");
		lblAfterEvent.setBounds(10, 269, 123, 14);
		contentPane.add(lblAfterEvent);

		JLabel lblAfterRestart = new JLabel("Waiting after Restart");
		lblAfterRestart.setBounds(10, 294, 123, 14);
		contentPane.add(lblAfterRestart);

		JLabel lblAfterTask = new JLabel("Waiting after Task");
		lblAfterTask.setBounds(10, 319, 123, 14);
		contentPane.add(lblAfterTask);

		JLabel lblOnThrobber = new JLabel("Waiting on Throbber");
		lblOnThrobber.setBounds(10, 343, 123, 14);
		contentPane.add(lblOnThrobber);

		JLabel lblMs0 = new JLabel("ms");
		lblMs0.setBounds(214, 269, 21, 14);
		contentPane.add(lblMs0);

		JLabel lblMs1 = new JLabel("ms");
		lblMs1.setBounds(214, 294, 21, 14);
		contentPane.add(lblMs1);

		JLabel lblMs2 = new JLabel("ms");
		lblMs2.setBounds(214, 319, 21, 14);
		contentPane.add(lblMs2);

		JLabel lblMs3 = new JLabel("ms");
		lblMs3.setBounds(214, 344, 21, 14);
		contentPane.add(lblMs3);

		JLabel lblComparatorParameters = new JLabel("Comparator Parameters");
		lblComparatorParameters.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblComparatorParameters.setBounds(10, 80, 225, 29);
		contentPane.add(lblComparatorParameters);

		JLabel lblCompareAvailable = new JLabel("Compare Available");
		lblCompareAvailable.setBounds(10, 111, 151, 14);
		contentPane.add(lblCompareAvailable);

		JLabel lblCompareCheckbox = new JLabel("Compare CheckBox");
		lblCompareCheckbox.setBounds(10, 136, 151, 14);
		contentPane.add(lblCompareCheckbox);

		JLabel lblCompareListCount = new JLabel("Compare List Count");
		lblCompareListCount.setBounds(10, 161, 151, 14);
		contentPane.add(lblCompareListCount);

		JLabel lblCompareTitle = new JLabel("Compare Title");
		lblCompareTitle.setBounds(10, 186, 151, 14);
		contentPane.add(lblCompareTitle);

		JLabel lblCompareToast = new JLabel("Compare Toast");
		lblCompareToast.setBounds(10, 211, 151, 14);
		contentPane.add(lblCompareToast);

		JLabel lblInteractionParameters = new JLabel("Interaction Parameters");
		lblInteractionParameters.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblInteractionParameters.setBounds(245, 6, 216, 25);
		contentPane.add(lblInteractionParameters);

		JLabel lblEditText = new JLabel("EditText as");
		lblEditText.setBounds(245, 111, 132, 14);
		contentPane.add(lblEditText);

		JLabel lblAutoComplete = new JLabel("AutoComplete as");
		lblAutoComplete.setBounds(245, 136, 132, 14);
		contentPane.add(lblAutoComplete);

		JLabel lblCheckBox = new JLabel("CheckBox as");
		lblCheckBox.setBounds(245, 186, 132, 14);
		contentPane.add(lblCheckBox);

		JLabel lblToggle = new JLabel("Toggle as");
		lblToggle.setBounds(245, 210, 132, 14);
		contentPane.add(lblToggle);

		JLabel lblTextInputs = new JLabel("Text Inputs use");
		lblTextInputs.setBounds(245, 161, 99, 14);
		contentPane.add(lblTextInputs);

		JLabel lblMaxEventsSelector = new JLabel("Max Events 4 Selector");
		lblMaxEventsSelector.setBounds(245, 61, 132, 14);
		contentPane.add(lblMaxEventsSelector);

		JLabel lblSwapTab = new JLabel("SwapTab only initially");
		lblSwapTab.setBounds(244, 36, 133, 14);
		contentPane.add(lblSwapTab);

		// FormattedTextField
		waitingEventField = new JFormattedTextField();
		waitingEventField.setHorizontalAlignment(SwingConstants.RIGHT);
		waitingEventField.setText("1000");
		waitingEventField.setBounds(140, 266, 64, 20);
		contentPane.add(waitingEventField);

		waitingRestartField = new JFormattedTextField();
		waitingRestartField.setHorizontalAlignment(SwingConstants.RIGHT);
		waitingRestartField.setText("0");
		waitingRestartField.setBounds(140, 291, 64, 20);
		contentPane.add(waitingRestartField);

		waitingTaskField = new JFormattedTextField();
		waitingTaskField.setText("0");
		waitingTaskField.setHorizontalAlignment(SwingConstants.RIGHT);
		waitingTaskField.setBounds(140, 316, 64, 20);
		contentPane.add(waitingTaskField);

		waitingThrobberField = new JFormattedTextField();
		waitingThrobberField.setText("1000");
		waitingThrobberField.setHorizontalAlignment(SwingConstants.RIGHT);
		waitingThrobberField.setBounds(140, 340, 64, 20);
		contentPane.add(waitingThrobberField);

		screenshotBox = new JComboBox(bool);
		screenshotBox.setBounds(171, 33, 64, 20);
		screenshotBox.setSelectedIndex(0);
		contentPane.add(screenshotBox);
		
		editTextBox = new JComboBox(interactions);
		editTextBox.setBounds(387, 108, 74, 20);
		editTextBox.setSelectedIndex(1);
		contentPane.add(editTextBox);

		autoCompleteBox = new JComboBox(interactions);
		autoCompleteBox.setBounds(387, 133, 74, 20);
		autoCompleteBox.setSelectedIndex(1);
		contentPane.add(autoCompleteBox);

		checkBox = new JComboBox(interactions);
		checkBox.setBounds(387, 183, 74, 20);
		checkBox.setSelectedIndex(1);
		contentPane.add(checkBox);

		toggleBox = new JComboBox(interactions);
		toggleBox.setBounds(387, 207, 74, 20);
		toggleBox.setSelectedIndex(1);
		contentPane.add(toggleBox);

		inputTextBox = new JComboBox(inputs);
		inputTextBox.setBounds(354, 158, 107, 20);
		contentPane.add(inputTextBox);

		schedulerBox = new JComboBox(algorithm);
		schedulerBox.setSelectedIndex(1);
		schedulerBox.setBounds(143, 58, 92, 20);
		contentPane.add(schedulerBox);

		tabEventsBox = new JComboBox(bool);
		tabEventsBox.setSelectedIndex(0);
		tabEventsBox.setBounds(387, 33, 74, 20);
		contentPane.add(tabEventsBox);
	
		maxEventSelectorBox = new JComboBox(maxEventSelector);
		maxEventSelectorBox.setSelectedIndex(3);
		maxEventSelectorBox.setBounds(387, 58, 74, 20);
		contentPane.add(maxEventSelectorBox);

		availableComparatorBox = new JComboBox(bool);
		availableComparatorBox.setSelectedIndex(1);
		availableComparatorBox.setBounds(171, 108, 64, 20);
		availableComparatorBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (availableComparatorBox.getSelectedIndex() == 0) {
					maxEventSelectorBox.setSelectedIndex(1);
					maxEventSelectorBox.setEnabled(false);
				} else {
					if (listComparatorBox.getSelectedIndex() != 0) {
						maxEventSelectorBox.setSelectedIndex(3);
						maxEventSelectorBox.setEnabled(true);	
					}
				}
			}

		});
		contentPane.add(availableComparatorBox);
		
		checkComparatorBox = new JComboBox(bool);
		checkComparatorBox.setSelectedIndex(1);
		checkComparatorBox.setBounds(171, 133, 64, 20);
		contentPane.add(checkComparatorBox);

		listComparatorBox = new JComboBox(bool);
		listComparatorBox.setSelectedIndex(1);
		listComparatorBox.setBounds(171, 158, 64, 20);
		listComparatorBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (listComparatorBox.getSelectedIndex() == 0) {
					maxEventSelectorBox.setSelectedIndex(1);
					maxEventSelectorBox.setEnabled(false);
				} else {
					if (availableComparatorBox.getSelectedIndex() != 0) {
						maxEventSelectorBox.setSelectedIndex(3);
						maxEventSelectorBox.setEnabled(true);	
					}
				}
			}

		});
		contentPane.add(listComparatorBox);

		titleComparatorBox = new JComboBox(bool);
		titleComparatorBox.setSelectedIndex(0);
		titleComparatorBox.setBounds(171, 183, 64, 20);
		contentPane.add(titleComparatorBox);

		toastComparatorBox = new JComboBox(bool);
		toastComparatorBox.setSelectedIndex(1);
		toastComparatorBox.setBounds(171, 207, 64, 20);
		contentPane.add(toastComparatorBox);

		// CheckBox
		chckbxInputPertubation = new JCheckBox("Input Pertubation ");
		chckbxInputPertubation.setBounds(241, 316, 132, 18);
		contentPane.add(chckbxInputPertubation);

		// Button
		btnDefaultValues = new JButton("Default Values");
		btnDefaultValues.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnDefaultValues.setBounds(245, 337, 121, 27);
		btnDefaultValues.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				resetDefaultValues();
			}
			
		});
		contentPane.add(btnDefaultValues);

		btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSave.setBounds(369, 337, 92, 27);
		btnSave.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				saveXML(); 
			}
			
		});
		contentPane.add(btnSave);

		resetDefaultValues();
	}

	/**
	 * Save xml.
	 */
	public void saveXML() {
		if(!validateField()) {
			JOptionPane.showMessageDialog(null, "Automation Parameters don't valid", "Information", JOptionPane.INFORMATION_MESSAGE);
		} else {
			createGeneralParameters();
			createAutomationParameters();
			createComparatorParameters();
			createInteractionsParameters();
			if (!finalizeXml()) {
				JOptionPane.showMessageDialog(null, "Error\nPreferences.xml was not created");
			}
		}
	}

	/**
	 * Creates the general parameters.
	 */
	private void createGeneralParameters() {
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		builder.append("<!DOCTYPE preferences SYSTEM \"http://java.sun.com/dtd/preferences.dtd\">");
		builder.append("<preferences EXTERNAL_XML_VERSION=\"1.0\">");
		builder.append("<root type=\"user\">");
		builder.append("<map/>");
		builder.append("<node name=\""+ TOOL +"\">");
		builder.append("<map>");
		builder.append("<entry key=\"PACKAGE_NAME\" value=\""+ getAppPackage() +"\"/>");
		builder.append("<entry key=\"CLASS_NAME\"   value=\"" + getAppPackageClass() + "\"/>");
		if (screenshotBox.getSelectedIndex() != 0) {
			builder.append("<entry key=\"SCREENSHOT_ENABLED\" value=\"false\"/>");
		}
		String scheduler_algorithm = new String();
		switch (schedulerBox.getSelectedIndex()) {
		case 0:
			scheduler_algorithm = "BREADTH_FIRST";
			break;
		case 1: 
			scheduler_algorithm = "DEPTH_FIRST";
			break;
		case 2:
			scheduler_algorithm = "RANDOM_FIRST";
			break;
		}
		if (schedulerBox.getSelectedIndex() != 0) {
			builder.append("<entry key=\"SCHEDULER_ALGORITHM\" value=\"" + scheduler_algorithm + "\"/>");
		}
		builder.append("</map>");
	}

	/**
	 * Creates the automation parameters.
	 */
	private void createAutomationParameters() {
		int automation = 0;
		if (waitingEventField.getText().equals("1000")) {
			automation++;
		}
		if (waitingRestartField.getText().equals("0")) {
			automation++;
		}
		if (waitingTaskField.getText().equals("0")) {
			automation++;
		}
		if (waitingThrobberField.getText().equals("1000")) {
			automation++;
		}
		if (automation != 4) {
			builder.append("<node name=\"automation\">");
			builder.append("<map>");

			if (!waitingEventField.getText().equals("1000")) {
				builder.append("<entry key=\"SLEEP_AFTER_EVENT\" value=\""+ Integer.valueOf(waitingEventField.getText()) +"\"/>");
			}
			if (!waitingRestartField.getText().equals("0")) {
				builder.append("<entry key=\"SLEEP_AFTER_RESTART\" value=\""+ Integer.valueOf(waitingRestartField.getText()) +"\"/>");
			}
			if (!waitingTaskField.getText().equals("0")) {
				builder.append("<entry key=\"SLEEP_AFTER_TASK\" value=\""+ Integer.valueOf(waitingTaskField.getText()) +"\"/>");
			}
			if (!waitingThrobberField.getText().equals("1000")) {
				builder.append("<entry key=\"SLEEP_ON_THROBBER\" value=\""+ Integer.valueOf(waitingThrobberField.getText()) +"\"/>");
			}
			builder.append("</map>");
			builder.append("</node>");	
		}
	}

	/**
	 * Creates the comparator parameters.
	 */
	private void createComparatorParameters() {
		if (titleComparatorBox.getSelectedIndex() != 0 
				|| listComparatorBox.getSelectedIndex() != 1 
				|| checkComparatorBox.getSelectedIndex() != 1
				|| availableComparatorBox.getSelectedIndex() != 1
				|| toastComparatorBox.getSelectedIndex() != 1) {
			builder.append("<node name=\"comparator\">");
			builder.append("<map>");
			if (availableComparatorBox.getSelectedIndex() != 1) {
				builder.append("<entry key=\"COMPARE_AVAILABLE\" value=\"true\"/>");
			}
			if (checkComparatorBox.getSelectedIndex() != 1) {
				builder.append("<entry key=\"COMPARE_CHECKBOX\" value=\"true\"/>");
			}
			if (listComparatorBox.getSelectedIndex() != 1) {
				builder.append("<entry key=\"COMPARE_LIST_COUNT\" value=\"true\"/>");
			}
			if (titleComparatorBox.getSelectedIndex() != 0) {
				builder.append("<entry key=\"COMPARE_TITLE\" value=\"false\"/>");
			}
			if (toastComparatorBox.getSelectedIndex() != 1) {
				builder.append("<entry key=\"COMPARE_TOAST\" value=\"true\"/>");
			}
			builder.append("</map>");
			builder.append("</node>");
		}
	}

	/**
	 * Creates the interactions parameters.
	 */
	private void createInteractionsParameters() {
		String events = new String();
		String inputs = new String();
		int countEvent = 0;
		int countInput = 0;
		builder.append("<node name=\"interactions\">");
		builder.append("<map>");
		if (editTextBox.getSelectedIndex() == 0 
				|| editTextBox.getSelectedIndex() == 2) {
			events = events.concat(", " + EDIT_TEXT);
		}
		if (autoCompleteBox.getSelectedIndex() == 0 
				|| autoCompleteBox.getSelectedIndex() == 2) {
			events = events.concat(", " + AUTOC_TEXT);
		}
		if (!events.equals("")) {
			builder.append("<entry key=\"EVENTS["+ countEvent +"]\" value=\"" + WRITE_TEXT + events + "\"/>");
			countEvent++;
		}
		events = new String();
		if (checkBox.getSelectedIndex() == 0 
				|| checkBox.getSelectedIndex() == 2) {
			events = events.concat(", " + CHECKBOX);
		}
		if (toggleBox.getSelectedIndex() == 0 
				|| toggleBox.getSelectedIndex() == 2) {
			events = events.concat(", " + TOGGLE_BUTTON);
		}
		if (!events.equals("")) {
			builder.append("<entry key=\"EVENTS["+ countEvent +"]\" value=\"" + CLICK + ", " + BUTTON + ", " + MENU_ITEM + ", " + LINEAR_LAYOUT + ", " + IMAGE_VIEW + ", " + TAB_VIEW + events + "\"/>");
		}
		
		if (editTextBox.getSelectedIndex() == 1 
				|| editTextBox.getSelectedIndex() == 2) {
			inputs = inputs.concat(", " + EDIT_TEXT);
		}
		if (autoCompleteBox.getSelectedIndex() == 1 
				|| autoCompleteBox.getSelectedIndex() == 2) {
			inputs = inputs.concat(", " + AUTOC_TEXT);
		}
		if (!inputs.equals("")) {
			builder.append("<entry key=\"INPUTS["+ countInput +"]\" value=\"" + WRITE_TEXT + inputs + "\"/>");
			countInput++;
		}
		
		inputs = new String();
		int countButton = 0;		
		if (checkBox.getSelectedIndex() == 1 
				|| checkBox.getSelectedIndex() == 2) {
			inputs = inputs.concat(", " + CHECKBOX);
			countButton++;
		}
		
		if (toggleBox.getSelectedIndex() == 1 
				|| toggleBox.getSelectedIndex() == 2) {
			inputs = inputs.concat(", " + TOGGLE_BUTTON);
			countButton++;
		}
		if (!inputs.equals("")) {
			if (countButton != 2) {
				builder.append("<entry key=\"INPUTS["+ countInput +"]\" value=\"" + CLICK + ", " + NUMBER_PICKER_BUTTON +  inputs + "\"/>");
			}
		} else {
			builder.append("<entry key=\"INPUTS["+ countInput +"]\" value=\"" + CLICK + ", " + NUMBER_PICKER_BUTTON + "\"/>");
		}
		
		if (inputTextBox.getSelectedIndex() != 0) {
			builder.append("<entry key=\"HASH_VALUES\" value=\"false\"/>");
		}
		if (maxEventSelectorBox.getSelectedIndex() != 3) {
			builder.append("<entry key=\"MAX_NUM_EVENTS_PER_SELECTOR\" value=\""+ maxEventSelectorBox.getSelectedIndex() + "\"/>");
		}
		if (tabEventsBox.getSelectedIndex() != 1) {
			builder.append("<entry key=\"TAB_EVENTS_START_ONLY\" value=\"true\"/>");
		}
		builder.append("</map>");
		builder.append("</node>");
	}

	/**
	 * Finalize xml.
	 *
	 * @return true, if successful
	 */
	private boolean finalizeXml() {
		builder.append("</node>");
		builder.append("</root>");
		builder.append("</preferences>");
		try {
			String folder = System.getProperty("user.dir") + "/../data";
			if (!new File(folder).exists()) new File(folder).mkdir();
			new Tools().xmlWriter(path, builder);
			if (!chckbxInputPertubation.isSelected()) {
				if (!new File(firstPath).exists()) new File(firstPath).mkdir();
				PrintWriter outputStream1 = new PrintWriter (firstPath.concat("/firstboot.txt"));
				outputStream1.write("firstboot");
				outputStream1.close();
				System.exit(NORMAL);
			} else {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run () {
						GuiAnalyzer frame = new GuiAnalyzer(firstPath);
						frame.setVisible(true);
					}
					
				});
				setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Validate field.
	 *
	 * @return true, if successful
	 */
	private boolean validateField() {
		if (!controlValue(waitingEventField.getText())) {
			return false;
		}
		if (!controlValue(waitingRestartField.getText())) {
			return false;
		}
		if (!controlValue(waitingTaskField.getText())) {
			return false;
		}
		if (!controlValue(waitingThrobberField.getText())) {
			return false;
		}
		return true;
	}

	/**
	 * Control value.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	private boolean controlValue(String value) {
		try{
			return ((Integer.valueOf(value) >= 0) && (Integer.valueOf(value) <= 20000));
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Reset default values.
	 */
	public void resetDefaultValues() {
		chckbxInputPertubation.setSelected(false);

		screenshotBox.setSelectedIndex(0);
		schedulerBox.setSelectedIndex(0);

		availableComparatorBox.setSelectedIndex(1);
		checkComparatorBox.setSelectedIndex(1);
		listComparatorBox.setSelectedIndex(1);
		titleComparatorBox.setSelectedIndex(0);
		toastComparatorBox.setSelectedIndex(1);

		waitingEventField.setText("1000");
		waitingRestartField.setText("0");
		waitingTaskField.setText("0");
		waitingThrobberField.setText("1000");

		maxEventSelectorBox.setSelectedIndex(3);
		maxEventSelectorBox.setEnabled(true);

		tabEventsBox.setSelectedIndex(1);
		inputTextBox.setSelectedIndex(0);
		editTextBox.setSelectedIndex(1);
		autoCompleteBox.setSelectedIndex(1);
		checkBox.setSelectedIndex(1);
		toggleBox.setSelectedIndex(1);
	}

	/**
	 * Sets the first path.
	 *
	 * @param value the new first path
	 */
	public static void setFirstPath(String value) {
		firstPath = value;
	}

	/**
	 * Gets the app package.
	 *
	 * @return the app package
	 */
	public static String getAppPackage() {
		return appPackage;
	}

	/**
	 * Sets the app package.
	 *
	 * @param value the new app package
	 */
	public static void setAppPackage(String value) {
		appPackage = value;
	}

	/**
	 * Gets the app package class.
	 *
	 * @return the app package class
	 */
	public static String getAppPackageClass() {
		return appPackageClass;
	}

	/**
	 * Sets the app package class.
	 *
	 * @param value the new app package class
	 */
	public static void setAppPackageClass(String value) {
		appPackageClass = value;
	}
}
