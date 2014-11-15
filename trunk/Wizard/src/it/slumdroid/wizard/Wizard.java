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

import it.slumdroid.wizard.guielements.AppPathTextField;
import it.slumdroid.wizard.guielements.AvdComboBox;
import it.slumdroid.wizard.guielements.PathTextField;
import it.slumdroid.wizard.guielements.ResultsPathTextField;
import it.slumdroid.wizard.tools.AppData;
import it.slumdroid.wizard.tools.BackWorker;
import it.slumdroid.wizard.tools.CommandLine;
import it.slumdroid.wizard.tools.ExternalProcess;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.*;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import static it.slumdroid.wizard.tools.CommandLine.*;

public class Wizard {

	private JFrame frmWizard;

	private static JTextField randomevents;
	private static JTextField textFieldAAuTpackage;
	private static JTextField textFieldAAuTClass;
	private static PathTextField textFieldResults;

	private static AppPathTextField textFieldAAuTPath;
	private static AvdComboBox comboBoxAVDs;

	private static JCheckBox chckbxRandom;
	private static JButton btnDeploy;
	private static JButton btnGenerateReports;
	private static JButton btnLoadAvds;
	private static JButton btnOpenResultsFolder;
	private static JButton btnResults;
	private static JButton btnRunSlumDroid;
	private static JButton btnSelectAutPath;
	private static JButton btnFirstBoot;
	private JSeparator separator_1;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Wizard window = new Wizard();
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

	public Wizard() throws ParseException {
		checkJavaVersion();
		checkSdk();
		initialize();
		comboBoxAVDs.loadDevices();
	}

	private void initialize() throws ParseException {
		frmWizard = new JFrame();
		frmWizard.setTitle("SlumDroid Wizard");
		frmWizard.setFont(new Font("Arial", Font.PLAIN, 10));
		frmWizard.setBounds(100, 100, 343, 381);
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
		btnOpenResultsFolder.setBounds(227, 142, 90, 20);
		frmWizard.getContentPane().add(btnOpenResultsFolder);

		randomevents = new JTextField();
		randomevents.setHorizontalAlignment(SwingConstants.RIGHT);
		randomevents.setBounds(227, 186, 90, 20);
		randomevents.setEnabled(false);
		frmWizard.getContentPane().add(randomevents);

		JLabel lblAvdName = new JLabel("AVD Name");
		lblAvdName.setBounds(10, 11, 62, 20);
		frmWizard.getContentPane().add(lblAvdName);

		JLabel lblAutPath = new JLabel("AAuT Path");
		lblAutPath.setBounds(10, 40, 62, 20);
		frmWizard.getContentPane().add(lblAutPath);

		JLabel lblAutPackage = new JLabel("AAuT Package");
		lblAutPackage.setBounds(10, 64, 90, 20);
		frmWizard.getContentPane().add(lblAutPackage);

		JLabel lblAutClass = new JLabel("AAuT Class");
		lblAutClass.setBounds(10, 89, 90, 20);
		frmWizard.getContentPane().add(lblAutClass);

		textFieldAAuTpackage = new JTextField();
		textFieldAAuTpackage.setEditable(false);
		textFieldAAuTpackage.setBounds(104, 64, 213, 20);
		frmWizard.getContentPane().add(textFieldAAuTpackage);
		textFieldAAuTpackage.setColumns(10);

		textFieldAAuTClass = new JTextField();
		textFieldAAuTClass.setEditable(false);
		textFieldAAuTClass.setBounds(104, 89, 213, 20);
		frmWizard.getContentPane().add(textFieldAAuTClass);
		textFieldAAuTClass.setColumns(10);

		textFieldAAuTPath = new AppPathTextField();
		textFieldAAuTPath.setEditable(false);
		textFieldAAuTPath.setBounds(104, 40, 116, 20);
		frmWizard.getContentPane().add(textFieldAAuTPath);
		textFieldAAuTPath.setColumns(10);

		comboBoxAVDs = new AvdComboBox();
		comboBoxAVDs.setBounds(104, 11, 116, 20);
		frmWizard.getContentPane().add(comboBoxAVDs);

		btnSelectAutPath = textFieldAAuTPath.getChangeButton();
		btnSelectAutPath.setBounds(227, 40, 90, 20);
		frmWizard.getContentPane().add(btnSelectAutPath);
		btnSelectAutPath.setFont(new Font("Tahoma", Font.BOLD, 9));

		btnLoadAvds = comboBoxAVDs.getLoadButton();
		btnLoadAvds.setBounds(227, 11, 90, 20);
		frmWizard.getContentPane().add(btnLoadAvds);

		textFieldResults = new ResultsPathTextField();
		textFieldResults.setEditable(false);
		textFieldResults.setColumns(10);
		textFieldResults.setBounds(10, 142, 211, 20);
		frmWizard.getContentPane().add(textFieldResults);

		btnResults = textFieldResults.getChangeButton();
		btnResults.setBounds(104, 114, 213, 20);
		btnResults.setFont(new Font("Tahoma", Font.BOLD, 9));
		frmWizard.getContentPane().add(btnResults);

		JLabel label = new JLabel("Results Path");
		label.setBounds(10, 117, 72, 14);
		frmWizard.getContentPane().add(label);

		btnGenerateReports = new JButton("Generate Reports");
		btnGenerateReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cycles = (chckbxRandom.isSelected())?randomevents.getText():"0";
				DownSide(false);
				BackWorker bw = new BackWorker();
				bw.setFile(getResultPath()+File.separator+"artifact.txt");
				bw.execute();
				String thePackage = textFieldAAuTpackage.getText();
				String commandLine = CommandLine.get(POST_PROCESS, CYCLES, cycles ,PACKAGE, thePackage);
				ExternalProcess.executeCommand(commandLine);
			}
		});
		btnGenerateReports.setFont(new Font("Tahoma", Font.BOLD, 9));
		btnGenerateReports.setEnabled(false);
		btnGenerateReports.setBounds(10, 305, 307, 28);
		frmWizard.getContentPane().add(btnGenerateReports);

		btnFirstBoot = new JButton("Start AVD");
		btnFirstBoot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (getResultPath().equals(textFieldAAuTPath.getPath())){
					JOptionPane.showMessageDialog(null, "AAuT Path and Results Path must be different", "Information", JOptionPane.INFORMATION_MESSAGE);
				}else{
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
						String thePackage = textFieldAAuTpackage.getText();
						String theClass = textFieldAAuTClass.getText();
						
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
			}
		});
		btnFirstBoot.setFont(new Font("Tahoma", Font.BOLD, 9));
		btnFirstBoot.setEnabled(false);
		btnFirstBoot.setBounds(10, 215, 307, 28);
		frmWizard.getContentPane().add(btnFirstBoot);

		btnDeploy = new JButton("Deploy");
		btnDeploy.setFont(new Font("Tahoma", Font.BOLD, 9));
		btnDeploy.setEnabled(false);
		btnDeploy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String avd = comboBoxAVDs.getDevice();
				String thePackage = textFieldAAuTpackage.getText();
				String theClass = textFieldAAuTClass.getText();
				String commandLine = CommandLine.get(DEPLOY, DEVICE, avd, PACKAGE, thePackage, CLASS, theClass);	
				DownSide(false);
				Upside(false);				
				BackWorker bw = new BackWorker();
				bw.setFile(getResultPath()+File.separator+"build.txt");
				bw.execute();
				ExternalProcess.executeCommand(commandLine);
			}
		});
		btnDeploy.setBounds(10, 245, 307, 28);
		frmWizard.getContentPane().add(btnDeploy);

		btnRunSlumDroid = new JButton("Run SlumDroid");
		btnRunSlumDroid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String command = (chckbxRandom.isSelected())?RANDOM_TEST:SYSTEMATIC_TEST;
				String thePackage = textFieldAAuTpackage.getText();
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
		btnRunSlumDroid.setFont(new Font("Tahoma", Font.BOLD, 9));
		btnRunSlumDroid.setEnabled(false);
		btnRunSlumDroid.setBounds(10, 275, 307, 28);
		frmWizard.getContentPane().add(btnRunSlumDroid);

		chckbxRandom = new JCheckBox("Random Ripper                      # Events");
		chckbxRandom.setFont(new Font("Tahoma", Font.BOLD, 9));
		chckbxRandom.setBounds(9, 186, 211, 20);
		chckbxRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				randomevents.setEnabled(chckbxRandom.isSelected());
			}
		});
		frmWizard.getContentPane().add(chckbxRandom);
		
		separator_1 = new JSeparator();
		separator_1.setBounds(10, 173, 307, 4);
		frmWizard.getContentPane().add(separator_1);
	}

	public void checkSdk () {
		boolean ret = false;
		try{
			ret = new File(System.getenv("ANDROID_HOME")).exists();
		} catch (Exception e){			
			e.printStackTrace();
		}
		if(!ret) {
			JOptionPane.showMessageDialog(null, "Please, set the ANDROID_HOME environment variable!", "Android SDK Folder not found", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}

	public static boolean checkPackage () {
		boolean ret = textFieldAAuTpackage.getText().equals("");
		if(ret) {
			JOptionPane.showMessageDialog(null, "Please, make sure that you're selecting a proper Android application.", "Application not found", JOptionPane.ERROR_MESSAGE);
			disableDeploy();
		}
		return !ret;
	}

	public static boolean ianpkg(){
		boolean ret = textFieldAAuTpackage.getText().equals("");
		if (ret) return !ret;
		else ret = textFieldAAuTClass.getText().equals("");
		return !ret;
	}

	public static boolean checkClass () {
		boolean ret = textFieldAAuTClass.getText().equals("");
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
		return textFieldAAuTPath.getText().length();
	}

	public static int checkPkg(){
		return textFieldAAuTpackage.getText().length();
	}

	public static int checkExp(){
		return textFieldResults.getText().length();
	}

	public static void detect() {
		String path = textFieldAAuTPath.getPath();
		AppData app = AppData.getFromSource(path);
		textFieldAAuTpackage.setText(app.getPackage());
		if (checkPackage()) {
			textFieldAAuTClass.setText(app.getClassName());
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
		btnGenerateReports.setEnabled(true);
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
		btnSelectAutPath.setEnabled(b);
		chckbxRandom.setEnabled(b);
		comboBoxAVDs.setEnabled(b);
		randomevents.setEnabled(b);
	}

	public static void DownSide(boolean b){
		btnFirstBoot.setEnabled(b);
		btnDeploy.setEnabled(b);
		btnGenerateReports.setEnabled(b);
		btnOpenResultsFolder.setEnabled(b);
		btnRunSlumDroid.setEnabled(b);
	}

	public static void postExec(boolean b){
		btnDeploy.setEnabled(!b);
		btnRunSlumDroid.setEnabled(b);
		btnGenerateReports.setEnabled(!b);
		btnOpenResultsFolder.setEnabled(!b);
	}

	public static void clearText(){
		try{
			textFieldAAuTPath.setText("");
			textFieldAAuTpackage.setText("");
			textFieldAAuTClass.setText("");
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
}