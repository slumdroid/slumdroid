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

package it.slumdroid.utilities.module;

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

@SuppressWarnings("rawtypes")
public class GraphicalEditor extends JFrame {

	private JPanel contentPane;

	private static final long serialVersionUID = 1L;
	private static String path = System.getProperty("user.dir") + "/../data/preferences.xml";
	private static String appPackage = new String();
	private static String appPackageClass = new String();
	
	private static JComboBox screenshotBox;
	private static JComboBox schedulerBox;
	private static JComboBox maxEventSelectorBox;
	private static JComboBox tabEventsBox;

	private static JFormattedTextField waitingEventField;
	private static JFormattedTextField waitingRestartField;
	private static JFormattedTextField waitingTaskField;
	private static JFormattedTextField waitingThrobberField;

	private JCheckBox chckbxInputPertubation;

	private static JComboBox listComparatorBox;
	private static JComboBox checkComparatorBox;

	private static JComboBox editTextBox;
	private static JComboBox autoCompleteBox;
	private static JComboBox checkBox;
	private static JComboBox toggleBox;
	private static JComboBox inputTextBox;

	private static JButton btnDefaultValues;
	private static JButton btnSave;

	private static String[] algorithm = {"Breadth (BFS)","Depth (DFS)", "Random"};
	private static String[] bool = {"true", "false"};
	private static String[] inputs = {"hash values", "random values"};
	private static String[] interactions = {"event", "input", "both", "none"};
	private static String[] maxEventSelector = {"no limit", "1", "2", "3", "4", "5"};
	private static String firstPath = new String();
	private static StringBuilder builder = new StringBuilder();

	@SuppressWarnings("unchecked")
	public GraphicalEditor(final String expPath, String appPack, String appClass) {
		setResizable(false);
		setTitle("Preference Editor");

		setAppPackage(appPack);
		setAppPackageClass(appClass);
		setFirstPath(expPath);

		setBounds(100, 100, 485, 324);
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
		lblScreenshotEnabled.setBounds(10, 36, 112, 14);
		contentPane.add(lblScreenshotEnabled);

		JLabel lblScheduler = new JLabel("Scheduler Algorithm");
		lblScheduler.setBounds(10, 61, 112, 14);
		contentPane.add(lblScheduler);

		JLabel lblAutomationParameters = new JLabel("Automation Parameters");
		lblAutomationParameters.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAutomationParameters.setBounds(10, 164, 225, 14);
		contentPane.add(lblAutomationParameters);

		JLabel lblAfterEvent = new JLabel("Waiting after Event");
		lblAfterEvent.setBounds(10, 187, 112, 14);
		contentPane.add(lblAfterEvent);

		JLabel lblAfterRestart = new JLabel("Waiting after Restart");
		lblAfterRestart.setBounds(10, 212, 112, 14);
		contentPane.add(lblAfterRestart);

		JLabel lblAfterTask = new JLabel("Waiting after Task");
		lblAfterTask.setBounds(10, 237, 112, 14);
		contentPane.add(lblAfterTask);

		JLabel lblOnThrobber = new JLabel("Waiting on Throbber");
		lblOnThrobber.setBounds(10, 262, 112, 14);
		contentPane.add(lblOnThrobber);

		JLabel lblMs0 = new JLabel("ms");
		lblMs0.setBounds(214, 187, 21, 14);
		contentPane.add(lblMs0);

		JLabel lblMs1 = new JLabel("ms");
		lblMs1.setBounds(214, 212, 21, 14);
		contentPane.add(lblMs1);

		JLabel lblMs2 = new JLabel("ms");
		lblMs2.setBounds(214, 237, 21, 14);
		contentPane.add(lblMs2);

		JLabel lblMs3 = new JLabel("ms");
		lblMs3.setBounds(214, 262, 21, 14);
		contentPane.add(lblMs3);

		JLabel lblComparatorParameters = new JLabel("Comparator Parameters");
		lblComparatorParameters.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblComparatorParameters.setBounds(10, 86, 225, 29);
		contentPane.add(lblComparatorParameters);

		JLabel lblCompareListCount = new JLabel("Compare List Count");
		lblCompareListCount.setBounds(10, 114, 151, 14);
		contentPane.add(lblCompareListCount);
		
		JLabel lblCompareCheckbox = new JLabel("Compare CheckBox values");
		lblCompareCheckbox.setBounds(10, 139, 151, 14);
		contentPane.add(lblCompareCheckbox);

		JLabel lblInteractionParameters = new JLabel("Interaction Parameters");
		lblInteractionParameters.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblInteractionParameters.setBounds(245, 6, 216, 25);
		contentPane.add(lblInteractionParameters);

		JLabel lblEdittextInteractions = new JLabel("EditText Interactions");
		lblEdittextInteractions.setBounds(245, 101, 132, 14);
		contentPane.add(lblEdittextInteractions);

		JLabel lblAutocompleteInteractions = new JLabel("AutoComplete");
		lblAutocompleteInteractions.setBounds(245, 126, 132, 14);
		contentPane.add(lblAutocompleteInteractions);

		JLabel lblCheckboxInteractions = new JLabel("CheckBox Interactions");
		lblCheckboxInteractions.setBounds(245, 187, 132, 14);
		contentPane.add(lblCheckboxInteractions);

		JLabel lblToggleInteractions = new JLabel("Toggle Interactions");
		lblToggleInteractions.setBounds(245, 212, 132, 14);
		contentPane.add(lblToggleInteractions);

		JLabel lblTextInputs = new JLabel("Text Inputs use");
		lblTextInputs.setBounds(245, 151, 99, 14);
		contentPane.add(lblTextInputs);
		
		JLabel lblMaxEventsFor = new JLabel("Max Events for Selector");
		lblMaxEventsFor.setBounds(245, 61, 132, 14);
		contentPane.add(lblMaxEventsFor);
		
		JLabel lblSwapTab = new JLabel("SwapTab only initially");
		lblSwapTab.setBounds(244, 36, 133, 14);
		contentPane.add(lblSwapTab);

		// FormattedTextField
		waitingEventField = new JFormattedTextField();
		waitingEventField.setHorizontalAlignment(SwingConstants.RIGHT);
		waitingEventField.setText("1000");
		waitingEventField.setBounds(140, 184, 64, 20);
		contentPane.add(waitingEventField);

		waitingRestartField = new JFormattedTextField();
		waitingRestartField.setHorizontalAlignment(SwingConstants.RIGHT);
		waitingRestartField.setText("0");
		waitingRestartField.setBounds(140, 209, 64, 20);
		contentPane.add(waitingRestartField);

		waitingTaskField = new JFormattedTextField();
		waitingTaskField.setText("0");
		waitingTaskField.setHorizontalAlignment(SwingConstants.RIGHT);
		waitingTaskField.setBounds(140, 234, 64, 20);
		contentPane.add(waitingTaskField);

		waitingThrobberField = new JFormattedTextField();
		waitingThrobberField.setText("1000");
		waitingThrobberField.setHorizontalAlignment(SwingConstants.RIGHT);
		waitingThrobberField.setBounds(140, 259, 64, 20);
		contentPane.add(waitingThrobberField);

		screenshotBox = new JComboBox(bool);
		screenshotBox.setBounds(171, 33, 64, 20);
		screenshotBox.setSelectedIndex(0);
		contentPane.add(screenshotBox);

		listComparatorBox = new JComboBox(bool);
		listComparatorBox.setSelectedIndex(1);
		listComparatorBox.setBounds(171, 111, 64, 20);
		contentPane.add(listComparatorBox);
		
		checkComparatorBox = new JComboBox(bool);
		checkComparatorBox.setSelectedIndex(1);
		checkComparatorBox.setBounds(171, 136, 64, 20);
		contentPane.add(checkComparatorBox);

		editTextBox = new JComboBox(interactions);
		editTextBox.setBounds(387, 98, 74, 20);
		editTextBox.setSelectedIndex(1);
		contentPane.add(editTextBox);

		autoCompleteBox = new JComboBox(interactions);
		autoCompleteBox.setBounds(387, 123, 74, 20);
		autoCompleteBox.setSelectedIndex(1);
		contentPane.add(autoCompleteBox);

		checkBox = new JComboBox(interactions);
		checkBox.setBounds(387, 184, 74, 20);
		checkBox.setSelectedIndex(1);
		contentPane.add(checkBox);

		toggleBox = new JComboBox(interactions);
		toggleBox.setBounds(387, 209, 74, 20);
		toggleBox.setSelectedIndex(1);
		contentPane.add(toggleBox);

		inputTextBox = new JComboBox(inputs);
		inputTextBox.setBounds(354, 148, 107, 20);
		contentPane.add(inputTextBox);

		schedulerBox = new JComboBox(algorithm);
		schedulerBox.setSelectedIndex(1);
		schedulerBox.setBounds(140, 58, 95, 20);
		contentPane.add(schedulerBox);
		
		maxEventSelectorBox = new JComboBox(maxEventSelector);
		maxEventSelectorBox.setSelectedIndex(0);
		maxEventSelectorBox.setBounds(387, 58, 74, 20);
		contentPane.add(maxEventSelectorBox);
		
		tabEventsBox = new JComboBox(bool);
		tabEventsBox.setSelectedIndex(0);
		tabEventsBox.setBounds(387, 33, 74, 20);
		contentPane.add(tabEventsBox);
		
		// CheckBox
		chckbxInputPertubation = new JCheckBox("Input Pertubation ");
		chckbxInputPertubation.setBounds(245, 235, 132, 18);
		contentPane.add(chckbxInputPertubation);
		
		// Button
		btnDefaultValues = new JButton("Default Values");
		btnDefaultValues.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnDefaultValues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetDefaultValues();
			}
		});
		btnDefaultValues.setBounds(245, 256, 121, 27);
		contentPane.add(btnDefaultValues);

		btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveXML(); 
			}
		});
		btnSave.setBounds(369, 256, 92, 27);
		contentPane.add(btnSave);
		
		resetDefaultValues();
	}

	public void saveXML() {
		if(!validateField()){
			JOptionPane.showMessageDialog(null, "Automation Parameters don't valid", "Information", JOptionPane.INFORMATION_MESSAGE);
		} else{
			createGeneralParameters();
			createAutomationParameters();
			createComparatorParameters();
			createInteractionsParameters();
			if (!finalizeXml()) JOptionPane.showMessageDialog(null, "Error\nPreferences.xml was not created");
		}
	}

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

		if (screenshotBox.getSelectedIndex()!=0){
			builder.append("<entry key=\"SCREENSHOT_ENABLED\" value=\"false\"/>");
		}
		String scheduler_algorithm= new String();
		switch (schedulerBox.getSelectedIndex()) {
		case 0:
			scheduler_algorithm = "BREADTH_FIRST";
			break;
		case 1: 
			scheduler_algorithm = "DEPTH_FIRST";
			break;
		case 2:
			scheduler_algorithm = "RANDOM";
			break;
		}
		if (schedulerBox.getSelectedIndex() != 0) {
			builder.append("<entry key=\"SCHEDULER_ALGORITHM\" value=\"" + scheduler_algorithm + "\"/>");
		}
		builder.append("</map>");
	}

	private void createAutomationParameters() {
		int automation = 0;
		if (waitingEventField.getText().equals("1000")) automation++;
		if (waitingRestartField.getText().equals("0")) automation++;
		if (waitingTaskField.getText().equals("0")) automation++;
		if (waitingThrobberField.getText().equals("1000")) automation++;
		if (automation!=4) {
			builder.append("<node name=\"automation\">");
			builder.append("<map>");

			if (!waitingEventField.getText().equals("1000")){
				builder.append("<entry key=\"SLEEP_AFTER_EVENT\" value=\""+ Integer.valueOf(waitingEventField.getText()) +"\"/>");
			}
			if (!waitingRestartField.getText().equals("0")){
				builder.append("<entry key=\"SLEEP_AFTER_RESTART\" value=\""+ Integer.valueOf(waitingRestartField.getText()) +"\"/>");
			}
			if (!waitingTaskField.getText().equals("0")){
				builder.append("<entry key=\"SLEEP_AFTER_TASK\" value=\""+ Integer.valueOf(waitingTaskField.getText()) +"\"/>");
			}
			if (!waitingThrobberField.getText().equals("1000")){
				builder.append("<entry key=\"SLEEP_ON_THROBBER\" value=\""+ Integer.valueOf(waitingThrobberField.getText()) +"\"/>");
			}

			builder.append("</map>");
			builder.append("</node>");	
		}
	}

	private void createComparatorParameters() {
		if (listComparatorBox.getSelectedIndex() != 1 
				|| checkComparatorBox.getSelectedIndex() != 1){
			builder.append("<node name=\"comparator\">");
			builder.append("<map>");
			if (listComparatorBox.getSelectedIndex() != 1) builder.append("<entry key=\"COMPARE_LIST_COUNT\" value=\"true\"/>");
			if (checkComparatorBox.getSelectedIndex() != 1) builder.append("<entry key=\"COMPARE_CHECKBOX\" value=\"true\"/>");
			builder.append("</map>");
			builder.append("</node>");
		}
	}

	private void createInteractionsParameters() {
		String events = new String();
		String inputs = new String();
		int countEvent = 0;
		int countInput = 0;
		
		builder.append("<node name=\"interactions\">");
		builder.append("<map>");

		if (editTextBox.getSelectedIndex() == 0 || editTextBox.getSelectedIndex() == 2){
			events = events.concat(", editText");
		}
		if (editTextBox.getSelectedIndex() == 1 || editTextBox.getSelectedIndex() == 2){
			inputs = inputs.concat(", editText");
		}

		if (autoCompleteBox.getSelectedIndex() == 0 || autoCompleteBox.getSelectedIndex() == 2){
			events = events.concat(", autoCText");
		}
		if (autoCompleteBox.getSelectedIndex() == 1 || autoCompleteBox.getSelectedIndex() == 2){
			inputs = inputs.concat(", autoCText");
		}

		if (!events.equals("")){
			builder.append("<entry key=\"EVENTS["+ countEvent +"]\" value=\"writeText" + events + "\"/>");
			countEvent++;
		}
		if (!inputs.equals("")){
			builder.append("<entry key=\"INPUTS["+ countInput +"]\" value=\"writeText" + inputs + "\"/>");
			countInput++;
		}

		events = new String();
		inputs = new String();
		int countButton = 0;

		if (checkBox.getSelectedIndex() == 0 || checkBox.getSelectedIndex() == 2){
			events = events.concat(", check");
		}
		if (checkBox.getSelectedIndex() == 1 || checkBox.getSelectedIndex() == 2){
			inputs = inputs.concat(", check");
			countButton++;
		}

		if (toggleBox.getSelectedIndex() == 0 || toggleBox.getSelectedIndex() == 2){
			events = events.concat(", toggle");
		}
		if (toggleBox.getSelectedIndex() == 1 || toggleBox.getSelectedIndex() == 2){
			inputs = inputs.concat(", toggle");
			countButton++;
		}
		
		if (!events.equals("")){
			builder.append("<entry key=\"EVENTS["+ countEvent +"]\" value=\"click, button, menuItem, image, linearLayout" + events + "\"/>");
		}
		if (!inputs.equals("")){
			if (countButton!=2)	builder.append("<entry key=\"INPUTS["+ countInput +"]\" value=\"click, numberPickerButton" + inputs + "\"/>");
		}
		
		if (inputTextBox.getSelectedIndex() != 0){
			builder.append("<entry key=\"HASH_VALUES\" value=\"false\"/>");
		}
		if (maxEventSelectorBox.getSelectedIndex() != 3){
			builder.append("<entry key=\"MAX_NUM_EVENTS_PER_SELECTOR\" value=\""+ maxEventSelectorBox.getSelectedIndex() + "\"/>");
		}
		if (tabEventsBox.getSelectedIndex() != 1){
			builder.append("<entry key=\"TAB_EVENTS_START_ONLY\" value=\"true\"/>");
		}
		
		builder.append("</map>");
		builder.append("</node>");
		
	}

	private boolean finalizeXml() {
		builder.append("</node>");
		builder.append("</root>");
		builder.append("</preferences>");
		try {
			String folder = System.getProperty("user.dir") + "/../data";
			if (!new File(folder).exists()) new File(folder).mkdir();
			new Tools().xmlWriter(path, builder);
			if (!chckbxInputPertubation.isSelected()){
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

	private boolean validateField() {
		if (!controlValue(waitingEventField.getText())){
			return false;
		}
		if (!controlValue(waitingRestartField.getText())){
			return false;
		}
		if (!controlValue(waitingTaskField.getText())){
			return false;
		}
		if (!controlValue(waitingThrobberField.getText())){
			return false;
		}
		return true;
	}

	private boolean controlValue(String value){
		try{
			return ((Integer.valueOf(value) >= 0) && (Integer.valueOf(value) <= 10000));
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public void resetDefaultValues() {
		chckbxInputPertubation.setSelected(false);
		
		screenshotBox.setSelectedIndex(0);
		schedulerBox.setSelectedIndex(0);
		
		listComparatorBox.setSelectedIndex(1);
		checkComparatorBox.setSelectedIndex(1);
		
		waitingEventField.setText("1000");
		waitingRestartField.setText("0");
		waitingTaskField.setText("0");
		waitingThrobberField.setText("1000");

		maxEventSelectorBox.setSelectedIndex(3);
		tabEventsBox.setSelectedIndex(1);
		inputTextBox.setSelectedIndex(0);
		
		editTextBox.setSelectedIndex(1);
		autoCompleteBox.setSelectedIndex(1);
		
		checkBox.setSelectedIndex(1);
		toggleBox.setSelectedIndex(1);
	}

	public static void setFirstPath(String s) {
		firstPath = s;
	}

	public static String getAppPackage() {
		return appPackage;
	}

	public static void setAppPackage(String value) {
		appPackage = value;
	}

	public static String getAppPackageClass() {
		return appPackageClass;
	}

	public static void setAppPackageClass(String value) {
		appPackageClass = value;
	}
}
