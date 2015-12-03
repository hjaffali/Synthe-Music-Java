package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/******************************************************************************
 *  Compilation:  javac Synthe.java
 *  Execution:    java Synthe input.txt tempo
 *  Dependencies: StdAudio.java Accord.java Note.java
 *
 *
 *
 ******************************************************************************/

public class Synthe {

	/**
	 * 
	 */
	public static int tempo ;

	/**
	 * 
	 */
	public static boolean harm = false ;

	/**
	 * 
	 */
	public static boolean guitar = false ;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage : java Synthe <fichier> <tempo> <harm/guitar>");
			System.exit(-1);
		}

		tempo = Integer.parseInt(args[1]);

		if (args.length == 3) {
			if (args[2].equals("harm")) 
				harm = true ;
			else if (args[2].equals("guitar")) 
				guitar = true ;
		}

		try {
			BufferedReader fichier = new BufferedReader(new FileReader(args[0]));
			String ligne;

			while ((ligne = fichier.readLine()) != null) {
				playLigne(ligne, harm, guitar);
			}

			fichier.close();
		} catch (IOException ex) {
			ex.printStackTrace(); 
		}

		System.exit(0);
	}

	
	/**
	 * 
	 * @param ligne
	 * @param harm
	 * @param guitar
	 */
	public static void playLigne(String ligne, boolean harm, boolean guitar) {


	}


}


