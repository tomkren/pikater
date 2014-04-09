package org.pikater.core.agents.system;

import jade.wrapper.StaleProxyException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.pikater.core.agents.PikaterAgent;


public class Agent_GUI extends PikaterAgent {

	private static final long serialVersionUID = -3908734088006529947L;

	public static String filePath = "core" + System.getProperty("file.separator") +
			"inputs" + System.getProperty("file.separator") +
			"inputsKlara" + System.getProperty("file.separator");
	
	@Override
	protected void setup() {
		initDefault();
		registerWithDF();

		System.out.println(
				"--------------------------------------------------------------------------------\n" +
				"| System Pikater: Multiagents system                                           |\n" +
				"--------------------------------------------------------------------------------\n" +
				"  Hi I'm Klara's GUI console Agent ..." +
				"\n"
				);

		if (System.console() == null) {
			
			System.out.println("Error, console not found.");
			
			this.takeDown();
			return;
		}

		char[] inputPasswd =
				System.console().readPassword("Please enter your password: ");

		char[] correctPassword = { '1', '2', '3' };

		if (! Arrays.equals (inputPasswd, correctPassword)) {

			System.out.println(" Sorry you are not Klara :-(");
			return;
		}

		System.out.println(" I welcome you Klara !!!");
		
		String defaultFileName = "input.xml";
		File testFile = new File(filePath + defaultFileName);
		
		if(testFile.exists() && !testFile.isDirectory()) {

			System.out.println(" Do you wish to run experiment from file "
					+ filePath + defaultFileName + " ? (y/n)");
			System.out.print(">");
			
			if (System.console().readLine().equals("y")) {
				runFile(filePath + defaultFileName);
			}

		}

		while (true) {

			System.out.print(">");
			String input = System.console().readLine();

			if (input.equals("--help")) {
				System.out.println(" Help:\n" +
						" Help             --help\n" +
						" Shutdown         --shutdown\n" +
						" Run Experiment   --run <file.xml>\n"
						);
			
			} else if (input.startsWith("--shutdown")) {
				break;
	
			} else if (input.startsWith("--run")) {
				
			} else {
				System.out.println(
						"Sorry, I don't understand you. \n" +
						" --help"
						);
			}
			
		}
	}

	private void runFile(String fileName) {

		System.out.println("Loading experiment from file: " + fileName);
		//TODO:
	}

}

