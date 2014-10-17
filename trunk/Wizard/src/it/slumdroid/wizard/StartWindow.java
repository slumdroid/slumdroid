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

package it.slumdroid.wizard;

import it.slumdroid.wizard.CommandLine;
import it.slumdroid.wizard.ExternalProcess;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.*;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import static it.slumdroid.wizard.CommandLine.*;

public class StartWindow {

	private JFrame frmWizard;

	private static JTextField randomevents;
	private static JTextField textFieldAUTpackage;
	private static JTextField textFieldAUTClass;

	private static PathTextField textFieldAndroidPath;
	private static PathTextField textFieldResults;

	private static AppPathTextField textFieldAUTPath;
	private static AvdComboBox comboBoxAVDs;

	private static JCheckBox chckbxRandom;

	private static JLabel lblCycles;
	private static JButton btnDeploy;
	private static JButton btnGenerateReport;
	private static JButton btnLoadAvds;
	private static JButton btnOpenResultsFolder;
	private static JButton btnResults;
	private static JButton btnRunRipper;
	private static JButton btnSelect_1;
	private static JButton btnFirstBoot;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartWindow window = new StartWindow();
					window.frmWizard.addWindowListener(new WindowAdapter () {
						@Override
						public void windowClosing(WindowEvent e) {
							ExternalProcess.killAll();
						}
					}); 
					window.frmWizard.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public StartWindow() throws ParseException {
		initialize();
		String path = getAndroidDir();
		textFieldAndroidPath.setPath(path);
		checkJavaVersion();
		checkSdk();
		comboBoxAVDs.loadDevices();
	}

	private void initialize() throws ParseException {
		frmWizard = new JFrame();
		frmWizard.setResizable(false);
		frmWizard.setTitle("SlumDroid Wizard");
		frmWizard.setFont(new Font("Arial", Font.PLAIN, 10));
		frmWizard.setBounds(100, 100, 343, 496);
		frmWizard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWizard.getContentPane().setLayout(null);

		btnOpenResultsFolder = new JButton("Open");
		btnOpenResultsFolder.setEnabled(false);
		btnOpenResultsFolder.setFont(new Font("Tahoma", Font.BOLD, 9));
		btnOpenResultsFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = new File(textFieldResults.getPath());
				file.toURI();
				try {
					Desktop.getDesktop().open(file);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Can't open the results folder!", "Folder not found", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnOpenResultsFolder.setBounds(240, 360, 90, 23);
		frmWizard.getContentPane().add(btnOpenResultsFolder);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 392, 321, 2);
		frmWizard.getContentPane().add(separator);

		randomevents = new JTextField();
		randomevents.setBounds(176, 361, 54, 20);
		randomevents.setVisible(true);
		frmWizard.getContentPane().add(randomevents);

		JLabel lblEmulatorPath = new JLabel("Android SDK Path");
		lblEmulatorPath.setBounds(10, 26, 149, 14);
		frmWizard.getContentPane().add(lblEmulatorPath);

		JLabel lblAvdName = new JLabel("AVD Name");
		lblAvdName.setBounds(10, 81, 62, 14);
		frmWizard.getContentPane().add(lblAvdName);

		JLabel lblAutPath = new JLabel("AUT Path");
		lblAutPath.setBounds(10, 137, 62, 14);
		frmWizard.getContentPane().add(lblAutPath);

		JLabel lblAutPackage = new JLabel("AUT Package");
		lblAutPackage.setBounds(10, 193, 97, 14);
		frmWizard.getContentPane().add(lblAutPackage);

		JLabel lblAutClass = new JLabel("AUT Class");
		lblAutClass.setBounds(10, 249, 72, 14);
		frmWizard.getContentPane().add(lblAutClass);

		textFieldAUTpackage = new JTextField();
		textFieldAUTpackage.setEditable(false);
		textFieldAUTpackage.setBounds(10, 218, 321, 20);
		frmWizard.getContentPane().add(textFieldAUTpackage);
		textFieldAUTpackage.setColumns(10);

		textFieldAUTClass = new JTextField();
		textFieldAUTClass.setEditable(false);
		textFieldAUTClass.setBounds(10, 274, 321, 20);
		frmWizard.getContentPane().add(textFieldAUTClass);
		textFieldAUTClass.setColumns(10);

		textFieldAndroidPath = new AndroidPathTextField();
		textFieldAndroidPath.setEditable(false);
		textFieldAndroidPath.setBounds(10, 50, 321, 20);
		frmWizard.getContentPane().add(textFieldAndroidPath);
		textFieldAndroidPath.setColumns(10);

		textFieldAUTPath = new AppPathTextField();
		textFieldAUTPath.setEditable(false);
		textFieldAUTPath.setBounds(10, 162, 220, 20);
		frmWizard.getContentPane().add(textFieldAUTPath);
		textFieldAUTPath.setColumns(10);

		comboBoxAVDs = new AvdComboBox();
		comboBoxAVDs.setBounds(10, 106, 220, 20);
		frmWizard.getContentPane().add(comboBoxAVDs);

		btnSelect_1 = textFieldAUTPath.getChangeButton();
		btnSelect_1.setBounds(240, 161, 89, 23);
		frmWizard.getContentPane().add(btnSelect_1);
		btnSelect_1.setFont(new Font("Tahoma", Font.BOLD, 9));

		btnLoadAvds = comboBoxAVDs.getLoadButton();
		btnLoadAvds.setBounds(240, 105, 89, 23);
		frmWizard.getContentPane().add(btnLoadAvds);

		textFieldResults = new ResultsPathTextField();
		textFieldResults.setEditable(false);
		textFieldResults.setColumns(10);
		textFieldResults.setBounds(10, 330, 220, 20);
		frmWizard.getContentPane().add(textFieldResults);

		btnResults = textFieldResults.getChangeButton();
		btnResults.setBounds(240, 329, 89, 23);
		btnResults.setFont(new Font("Tahoma", Font.BOLD, 9));
		frmWizard.getContentPane().add(btnResults);

		JLabel label = new JLabel("Results Path");
		label.setBounds(10, 305, 72, 14);
		frmWizard.getContentPane().add(label);

		btnGenerateReport = new JButton("Generate Reports");
		btnGenerateReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cycles = (chckbxRandom.isSelected())?randomevents.getText():"0";
				DownSide(false);
				BackWorker bw = new BackWorker();
				bw.setFile(getResultPath()+File.separator+"artifact.txt");
				bw.execute();
				String thePackage = textFieldAUTpackage.getText();
				String commandLine = CommandLine.get(POST_PROCESS, CYCLES, cycles ,PACKAGE, thePackage);
				ExternalProcess.executeCommand(commandLine);
			}
		});
		btnGenerateReport.setFont(new Font("Tahoma", Font.BOLD, 9));
		btnGenerateReport.setEnabled(false);
		btnGenerateReport.setBounds(160, 433, 172, 23);
		frmWizard.getContentPane().add(btnGenerateReport);

		btnFirstBoot = new JButton("Start AVD");
		btnFirstBoot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean success = true;
				try{
					if (!chckbxRandom.isSelected()) success = true;
					else{
						int number = Integer.parseInt(randomevents.getText());
						if (number<1) success=false;
					}
				}catch (NumberFormatException ex){
					success = false;
				}
				if (success==true){
					String command = (chckbxRandom.isSelected())?FIRST_BOOT_RANDOM:FIRST_BOOT;
					String avd = comboBoxAVDs.getDevice();
					String thePackage = textFieldAUTpackage.getText();
					String theClass = textFieldAUTClass.getText();
					try{
						String commandLine = CommandLine.get(command, DEVICE, avd, PACKAGE, thePackage, CLASS, theClass);
						DownSide(false);
						Upside(false);				

						BackWorker bw = new BackWorker();
						bw.setFile(getResultPath()+File.separator+"firstboot.txt");
						bw.execute();
						ExternalProcess.executeCommand(commandLine);	
					}catch (Exception e){
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "AVD List doesn't loaded", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "#Events must be a number greater than zero", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnFirstBoot.setFont(new Font("Tahoma", Font.BOLD, 9));
		btnFirstBoot.setEnabled(false);
		btnFirstBoot.setBounds(10, 405, 149, 23);
		frmWizard.getContentPane().add(btnFirstBoot);

		btnDeploy = new JButton("Deploy");
		btnDeploy.setFont(new Font("Tahoma", Font.BOLD, 9));
		btnDeploy.setEnabled(false);
		btnDeploy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String avd = comboBoxAVDs.getDevice();
				String thePackage = textFieldAUTpackage.getText();
				String theClass = textFieldAUTClass.getText();
				String commandLine = CommandLine.get(DEPLOY, DEVICE, avd, PACKAGE, thePackage, CLASS, theClass);	
				DownSide(false);
				Upside(false);				
				BackWorker bw = new BackWorker();
				bw.setFile(getResultPath()+File.separator+"build.txt");
				bw.execute();
				ExternalProcess.executeCommand(commandLine);
			}
		});
		btnDeploy.setBounds(160, 405, 170, 23);
		frmWizard.getContentPane().add(btnDeploy);

		btnRunRipper = new JButton("Run SlumDroid");
		btnRunRipper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String command = (chckbxRandom.isSelected())?RANDOM_TEST:SYSTEMATIC_TEST;
				String thePackage = textFieldAUTpackage.getText();
				String cycles = (chckbxRandom.isSelected())?randomevents.getText():"0";
				boolean success = true;
				try{
					if (!chckbxRandom.isSelected()) success = true;
					else{
						int number = Integer.parseInt(randomevents.getText());
						if (number<1) success=false;
					}
				}catch (NumberFormatException ex){
					success = false;
				}
				if (success==true){
					String commandLine = CommandLine.get(command, DEVICE, comboBoxAVDs.getDevice(), PACKAGE, thePackage, CYCLES, cycles);	

					DownSide(false);
					Upside(false);

					BackWorker bw = new BackWorker();
					bw.setFile(getResultPath()+File.separator+"ripper.txt");
					bw.execute();

					ExternalProcess.executeCommand(commandLine);
				}
				else{
					JOptionPane.showMessageDialog(null, "#Cycles must be a number greater than zero", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnRunRipper.setFont(new Font("Tahoma", Font.BOLD, 9));
		btnRunRipper.setEnabled(false);
		btnRunRipper.setBounds(10, 433, 149, 23);
		frmWizard.getContentPane().add(btnRunRipper);

		chckbxRandom = new JCheckBox("Random Ripper");
		chckbxRandom.setFont(new Font("Tahoma", Font.BOLD, 9));
		chckbxRandom.setBounds(10, 360, 110, 23);
		frmWizard.getContentPane().add(chckbxRandom);

		lblCycles = new JLabel("# Events");
		lblCycles.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblCycles.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCycles.setVisible(true);
		lblCycles.setBounds(112, 364, 54, 14);
		frmWizard.getContentPane().add(lblCycles);
	}

	public static String getAndroidDir () {
		String path = System.getenv("ANDROID_HOME");
		return path;
	}

	public void checkSdk () {
		boolean ret = false;
		try{
			ret = new File(CommandLine.get(SDK_CHECK)).exists();
		} catch (Exception e){
			e.printStackTrace();
		}
		if(!ret) {
			JOptionPane.showMessageDialog(null, "Please, set the ANDROID_HOME environment variable!", "Android SDK Folder not found", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}

	public static boolean checkPackage () {
		boolean ret = textFieldAUTpackage.getText().equals("");
		if(ret) {
			JOptionPane.showMessageDialog(null, "Please, make sure that you're selecting a proper Android application.", "Application not found", JOptionPane.ERROR_MESSAGE);
			disableDeploy();
		}
		return !ret;
	}

	public static boolean ianpkg(){
		boolean ret = textFieldAUTpackage.getText().equals("");
		if (ret) return !ret;
		else ret = textFieldAUTClass.getText().equals("");
		return !ret;
	}

	public static boolean checkClass () {
		boolean ret = textFieldAUTClass.getText().equals("");
		if(ret) {
			JOptionPane.showMessageDialog(null, "Please, make sure that you're selecting a proper Android application," + System.getProperty("line.separator") + "with at least one activity! (Perhaps it's a test project?)", "Main Activity not found", JOptionPane.ERROR_MESSAGE);
			disableDeploy();
		}
		return !ret;
	}

	public void checkJavaVersion () {
		String version = System.getProperty("java.version");
		boolean ret = version.startsWith("1.7") || version.startsWith("1.8");
		if(!ret) {
			JOptionPane.showMessageDialog(null, "Detected Java " + version + ". Please, set the JAVA_HOME environment variable with the path Java JDK!", "Warning", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}	
	}

	public static int checkApp(){
		return textFieldAUTPath.getText().length();
	}

	public static int checkPkg(){
		return textFieldAUTpackage.getText().length();
	}

	public static int checkExp(){
		return textFieldResults.getText().length();
	}

	public static void detect() {
		String path = textFieldAUTPath.getPath();
		AppData app = AppData.getFromSource(path);
		textFieldAUTpackage.setText(app.getPackage());
		if (checkPackage()) {
			textFieldAUTClass.setText(app.getClassName());
			checkClass();
		}
		else clearText();
	}

	public static void enableStart(){
		btnFirstBoot.setEnabled(true);
	}

	public static void postFirstBoot(){
		btnDeploy.setEnabled(true);
	}

	public static void disableDeploy(){
		btnDeploy.setEnabled(false);
	}

	public static void enableReporting() {
		btnGenerateReport.setEnabled(true);
	}

	public static void enableOpenResultFolder() {
		btnOpenResultsFolder.setEnabled(true);
	}

	public static String getResultPath() {
		return textFieldResults.getText();
	}

	public static void Upside(boolean b){
		btnLoadAvds.setEnabled(b);
		btnResults.setEnabled(b);
		btnSelect_1.setEnabled(b);
		chckbxRandom.setEnabled(b);
		comboBoxAVDs.setEnabled(b);
		lblCycles.setEnabled(b);
		randomevents.setEnabled(b);
	}

	public static void DownSide(boolean b){
		btnFirstBoot.setEnabled(b);
		btnDeploy.setEnabled(b);
		btnGenerateReport.setEnabled(b);
		btnOpenResultsFolder.setEnabled(b);
		btnRunRipper.setEnabled(b);
	}

	public static void postExec(boolean b){
		btnDeploy.setEnabled(!b);
		btnRunRipper.setEnabled(b);
		btnGenerateReport.setEnabled(!b);
		btnOpenResultsFolder.setEnabled(!b);
	}

	public static void clearText(){
		try{
			textFieldAUTPath.setText("");
			textFieldAUTpackage.setText("");
			textFieldAUTClass.setText("");
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
}