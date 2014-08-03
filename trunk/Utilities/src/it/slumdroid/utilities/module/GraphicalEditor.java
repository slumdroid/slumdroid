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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.Font;
import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings("rawtypes")
public class GraphicalEditor extends JFrame {
	
	private JPanel contentPane;

	private static final long serialVersionUID = 1L;
	private final  String TOOL = "it.slumdroid.tool";
	private static String path = System.getProperty("user.dir") + "/../data/preferences.xml";
	private static int automation = 0;
	
	private static JComboBox modelBox;
	private static JComboBox screenshotBox;
	private static JComboBox tabBox;
	
	private static JFormattedTextField waitingEventField;
	private static JFormattedTextField waitingRestartField;
	private static JFormattedTextField waitingTaskField;
	private static JFormattedTextField waitingThrobberField;
	
	private static JComboBox comparatorBox;
	private static JComboBox listComparatorBox;
	
	private static JComboBox editTextBox;
	private static JComboBox autoCompleteBox;
	private static JComboBox checkBox;
	private static JComboBox toggleBox;
	private static JComboBox inputTextBox;
	
	private static JButton btnDefaultValues;
	private static JButton btnSave;
	
	private static String[] bool = {"true", "false"};
	private static String[] inputs = {"hash values", "random values"};
	private static String[] comparator = {"Compositional","None"};
	private static String[] interactions = {"event", "input", "both", "none"};
	private static boolean random;
	private static String firstPath = new String();
	private static StringBuilder builder = new StringBuilder();

	@SuppressWarnings("unchecked")
	public GraphicalEditor(boolean random, String expPath) {
		setType(Type.UTILITY);
		setTitle("Preference Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setFirstPath(expPath);
		setRandom(random);
		
		setBounds(100, 100, 511, 314);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Labels
		JLabel lblGeneralParameters = new JLabel("General Parameters");
		lblGeneralParameters.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGeneralParameters.setBounds(10, 11, 225, 14);
		contentPane.add(lblGeneralParameters);
		
		JLabel lblEnableModel = new JLabel("Enable Model");
		lblEnableModel.setBounds(10, 36, 126, 14);
		contentPane.add(lblEnableModel);
		
		JLabel lblScreenshotEnabled = new JLabel("Screenshot Enabled");
		lblScreenshotEnabled.setBounds(10, 61, 126, 14);
		contentPane.add(lblScreenshotEnabled);
		
		JLabel lblTabEventsOnly = new JLabel("Tab Events only Start");
		lblTabEventsOnly.setBounds(10, 86, 126, 14);
		contentPane.add(lblTabEventsOnly);
		
		JLabel lblAutomationParameters = new JLabel("Automation Parameters");
		lblAutomationParameters.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAutomationParameters.setBounds(10, 141, 225, 14);
		contentPane.add(lblAutomationParameters);
		
		JLabel lblAfterEvent = new JLabel("Waiting after Event");
		lblAfterEvent.setBounds(10, 166, 126, 14);
		contentPane.add(lblAfterEvent);
		
		JLabel lblAfterRestart = new JLabel("Waiting after Restart");
		lblAfterRestart.setBounds(10, 191, 126, 14);
		contentPane.add(lblAfterRestart);
		
		JLabel lblAfterTask = new JLabel("Waiting after Task");
		lblAfterTask.setBounds(10, 216, 126, 14);
		contentPane.add(lblAfterTask);
		
		JLabel lblOnThrobber = new JLabel("Waiting on Throbber");
		lblOnThrobber.setBounds(10, 241, 126, 14);
		contentPane.add(lblOnThrobber);
		
		JLabel lblMs0 = new JLabel("ms");
		lblMs0.setBounds(214, 166, 21, 14);
		contentPane.add(lblMs0);
		
		JLabel lblMs1 = new JLabel("ms");
		lblMs1.setBounds(214, 191, 21, 14);
		contentPane.add(lblMs1);
		
		JLabel lblMs2 = new JLabel("ms");
		lblMs2.setBounds(214, 216, 21, 14);
		contentPane.add(lblMs2);
		
		JLabel lblMs3 = new JLabel("ms");
		lblMs3.setBounds(214, 241, 21, 14);
		contentPane.add(lblMs3);
		
		JLabel lblComparatorParameters = new JLabel("Comparator Parameters");
		lblComparatorParameters.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblComparatorParameters.setBounds(255, 5, 231, 27);
		contentPane.add(lblComparatorParameters);
		
		JLabel lblComparatorType = new JLabel("Comparator Type");
		lblComparatorType.setBounds(255, 36, 107, 14);
		contentPane.add(lblComparatorType);
		
		JLabel lblCompareListCount = new JLabel("Compare List Count");
		lblCompareListCount.setBounds(255, 61, 132, 14);
		contentPane.add(lblCompareListCount);
			
		JLabel lblInteractionParameters = new JLabel("Interaction Parameters");
		lblInteractionParameters.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInteractionParameters.setBounds(255, 107, 231, 25);
		contentPane.add(lblInteractionParameters);
		
		JLabel lblEdittextInteractions = new JLabel("EditText Interactions");
		lblEdittextInteractions.setBounds(255, 143, 132, 14);
		contentPane.add(lblEdittextInteractions);
		
		JLabel lblAutocompleteInteractions = new JLabel("AutoComplete Interactions");
		lblAutocompleteInteractions.setBounds(255, 166, 142, 14);
		contentPane.add(lblAutocompleteInteractions);
		
		JLabel lblCheckboxInteractions = new JLabel("CheckBox Interactions");
		lblCheckboxInteractions.setBounds(255, 191, 132, 14);
		contentPane.add(lblCheckboxInteractions);
		
		JLabel lblToggleInteractions = new JLabel("Toggle Interactions");
		lblToggleInteractions.setBounds(255, 213, 132, 14);
		contentPane.add(lblToggleInteractions);
		
		JLabel lblTextInputs = new JLabel("Text Inputs");
		lblTextInputs.setBounds(10, 111, 107, 14);
		contentPane.add(lblTextInputs);
		
		// FormattedTextField
		waitingEventField = new JFormattedTextField();
		waitingEventField.setHorizontalAlignment(SwingConstants.RIGHT);
		waitingEventField.setText("1000");
		waitingEventField.setBounds(146, 163, 64, 20);
		contentPane.add(waitingEventField);
				
		waitingRestartField = new JFormattedTextField();
		waitingRestartField.setHorizontalAlignment(SwingConstants.RIGHT);
		waitingRestartField.setText("0");
		waitingRestartField.setBounds(146, 188, 64, 20);
		contentPane.add(waitingRestartField);
				
		waitingTaskField = new JFormattedTextField();
		waitingTaskField.setText("0");
		waitingTaskField.setHorizontalAlignment(SwingConstants.RIGHT);
		waitingTaskField.setBounds(146, 213, 64, 20);
		contentPane.add(waitingTaskField);
		
		waitingThrobberField = new JFormattedTextField();
		waitingThrobberField.setText("1000");
		waitingThrobberField.setHorizontalAlignment(SwingConstants.RIGHT);
		waitingThrobberField.setBounds(146, 238, 64, 20);
		contentPane.add(waitingThrobberField);
		
		// ComboBox
		modelBox = new JComboBox(bool);
		modelBox.setBounds(146, 33, 89, 20);
		modelBox.setSelectedIndex(0);
		contentPane.add(modelBox);
		
		screenshotBox = new JComboBox(bool);
		screenshotBox.setBounds(146, 58, 89, 20);
		screenshotBox.setSelectedIndex(0);
		contentPane.add(screenshotBox);
		
		comparatorBox = new JComboBox(comparator);
		comparatorBox.setSelectedIndex(1);
		comparatorBox.setEnabled(false);
		comparatorBox.setBounds(368, 33, 118, 20);
		contentPane.add(comparatorBox);
		
		listComparatorBox = new JComboBox(bool);
		listComparatorBox.setSelectedIndex(1);
		listComparatorBox.setBounds(397, 58, 89, 20);
		contentPane.add(listComparatorBox);
		
		tabBox = new JComboBox(bool);
		tabBox.setBounds(146, 83, 89, 20);
		tabBox.setSelectedIndex(1);
		contentPane.add(tabBox);
		
		editTextBox = new JComboBox(interactions);
		editTextBox.setBounds(397, 140, 89, 20);
		editTextBox.setSelectedIndex(1);
		contentPane.add(editTextBox);
		
		autoCompleteBox = new JComboBox(interactions);
		autoCompleteBox.setBounds(397, 163, 89, 20);
		autoCompleteBox.setSelectedIndex(1);
		contentPane.add(autoCompleteBox);
		
		checkBox = new JComboBox(interactions);
		checkBox.setBounds(397, 188, 89, 20);
		checkBox.setSelectedIndex(1);
		contentPane.add(checkBox);
		
		toggleBox = new JComboBox(interactions);
		toggleBox.setBounds(397, 210, 89, 20);
		toggleBox.setSelectedIndex(1);
		contentPane.add(toggleBox);
		
		inputTextBox = new JComboBox(inputs);
		inputTextBox.setBounds(127, 108, 108, 20);
		contentPane.add(inputTextBox);
		
		resetDefaultValues();
		
		// Button
		btnDefaultValues = new JButton("Default Values");
		btnDefaultValues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetDefaultValues();
			}
		});
		btnDefaultValues.setBounds(245, 237, 152, 23);
		contentPane.add(btnDefaultValues);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveXML(); 
			}
		});
		btnSave.setBounds(397, 237, 89, 23);
		contentPane.add(btnSave);
		
	}
	
	private void saveXML() {
		if(!validateField()){
			JOptionPane.showMessageDialog(null, "Automation Parameters don't valid", "Information", JOptionPane.INFORMATION_MESSAGE);
		} else{
			createGeneralParameters();
			createAutomationParameters();
			if (!isRandom()) createComparatorParameters();
			createInteractionsParameters();
			finalizeXml();
			System.exit(NORMAL);
		}
	}
		
	private void createGeneralParameters() {
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		builder.append("<!DOCTYPE preferences SYSTEM \"http://java.sun.com/dtd/preferences.dtd\">\n");
		builder.append("<preferences EXTERNAL_XML_VERSION=\"1.0\">\n");
		builder.append("  <root type=\"user\">\n");
		builder.append("\t<map/>\n");
		builder.append("\t<node name=\""+ TOOL +"\">\n");
		builder.append("\t\t<map>\n");
		builder.append("\t\t\t<entry key=\"PACKAGE_NAME\" value=\"app.package\"/>\n");
		builder.append("\t\t\t<entry key=\"CLASS_NAME\"   value=\"app.package.main.class\"/>\n");
		
		if (modelBox.getSelectedIndex()!=0){
			builder.append("\t\t\t<entry key=\"ENABLE_MODEL\" value=\"false\"/>\n");
		}
		if (screenshotBox.getSelectedIndex()!=0){
			builder.append("\t\t\t<entry key=\"SCREENSHOT_ENABLED\" value=\"false\"/>\n");
		}
		if (tabBox.getSelectedIndex()!=1){
			builder.append("\t\t\t<entry key=\"TAB_EVENTS_START_ONLY\" value=\"true\"/>\n");
		}
		if (isRandom()){
			builder.append("\t\t\t<entry key=\"RANDOM_SEED\"  value=\"0\"/>\n");
			builder.append("\t\t\t<entry key=\"MAX_NUM_EVENTS\"  value=\"100\"/>\n");
		}
		if (inputTextBox.getSelectedIndex()!=0){
			builder.append("\t\t\t<entry key=\"HASH_VALUES\" value=\"false\"/>\n");
		}
		builder.append("\t\t</map>\n");
	}

	private void createAutomationParameters() {
		if (waitingEventField.getText().equals("1000")) automation++;
		if (waitingRestartField.getText().equals("0")) automation++;
		if (waitingTaskField.getText().equals("0")) automation++;
		if (waitingThrobberField.getText().equals("1000")) automation++;
		if (automation==4) return;
		
		builder.append("\t\t<node name=\"automation\">\n");
		builder.append("\t\t\t<map>\n");

		if (!waitingEventField.getText().equals("1000")){
			builder.append("\t\t\t\t<entry key=\"SLEEP_AFTER_EVENT\"   value=\""+ Integer.valueOf(waitingEventField.getText()) +"\"/>\n");
		}
		if (!waitingRestartField.getText().equals("0")){
			builder.append("\t\t\t\t<entry key=\"SLEEP_AFTER_RESTART\" value=\""+ Integer.valueOf(waitingRestartField.getText()) +"\"/>\n");
		}
		if (!waitingTaskField.getText().equals("0")){
			builder.append("\t\t\t\t<entry key=\"SLEEP_AFTER_TASK\"    value=\""+ Integer.valueOf(waitingTaskField.getText()) +"\"/>\n");
		}
		if (!waitingThrobberField.getText().equals("1000")){
			builder.append("\t\t\t\t<entry key=\"SLEEP_ON_THROBBER\"   value=\""+ Integer.valueOf(waitingThrobberField.getText()) +"\"/>\n");
		}
		
		builder.append("\t\t\t</map>\n");
		builder.append("\t\t</node>\n");
	}
	
	private void createComparatorParameters() {
		builder.append("\t\t<node name=\"comparator\">\n");
		builder.append("\t\t\t<map>\n");
		builder.append("\t\t\t\t<entry key=\"COMPARATOR_TYPE\" value=\"CompositionalComparator\"/>\n");
		if (listComparatorBox.getSelectedIndex()!=1) builder.append("\t\t\t\t<entry key=\"COMPARE_LIST_COUNT\" value=\"true\"/>\n");
		builder.append("\t\t\t</map>\n");
		builder.append("\t\t</node>\n");
		
	}
	
	private void createInteractionsParameters() {
		builder.append("\t\t<node name=\"interactions\">\n");
		builder.append("\t\t\t<map>\n");
		
		String events = new String();
		String inputs = new String();
		int countEvent = 0;
		int countInput = 0;

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
			builder.append("\t\t\t\t<entry key=\"EVENTS["+ countEvent +"]\" value=\"writeText" + events + "\"/>\n");
			countEvent++;
		}
		if (!inputs.equals("")){
			builder.append("\t\t\t\t<entry key=\"INPUTS["+ countInput +"]\" value=\"writeText" + inputs + "\"/>\n");
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
			builder.append("\t\t\t\t<entry key=\"EVENTS["+ countEvent +"]\" value=\"click, button, menuItem, image, linearLayout" + events + "\"/>\n");
		}
		if (!inputs.equals("")){
			if (countButton!=2)	builder.append("\t\t\t\t<entry key=\"INPUTS["+ countInput +"]\" value=\"click, numberPickerButton" + inputs + "\"/>\n");
		}
		builder.append("\t\t\t</map>\n");
		builder.append("\t\t</node>\n");
	}
	
	private void finalizeXml() {
		builder.append("\t</node>\n");
		builder.append("  </root>\n");
		builder.append("</preferences>\n");
		
		PrintWriter outputStream1;
		try {
			String folder = System.getProperty("user.dir") + "/../data";
			if (!new File(folder).exists()) new File(folder).mkdir();
			File file = new File(path); 
			if (!file.exists()) new File(path).createNewFile();
			outputStream1 = new PrintWriter (path);
			outputStream1.write(builder.toString());
			outputStream1.close();
			if (!new File(firstPath).exists()) new File(firstPath).mkdir();
			outputStream1 = new PrintWriter (firstPath.concat("/firstboot.txt"));
			outputStream1.write("firstboot");
			outputStream1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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

	private void resetDefaultValues() {
		int i=0;
		if (isRandom()){
			i=1;
			listComparatorBox.setEnabled(false);
		}
			
		modelBox.setSelectedIndex(i);
		screenshotBox.setSelectedIndex(i);
		tabBox.setSelectedIndex(1);
		
		comparatorBox.setSelectedIndex(i);
		listComparatorBox.setSelectedIndex(1);
		
		waitingEventField.setText("1000");
		waitingRestartField.setText("0");
		waitingTaskField.setText("0");
		waitingThrobberField.setText("1000");
		
		editTextBox.setSelectedIndex(1);
		autoCompleteBox.setSelectedIndex(1);
		checkBox.setSelectedIndex(1);
		toggleBox.setSelectedIndex(1);
	}

	public static boolean isRandom() {
		return random;
	}

	public static void setRandom(boolean value) {
		GraphicalEditor.random = value;
	}

	public static void setFirstPath(String firstPath) {
		GraphicalEditor.firstPath = firstPath;
	}
}
