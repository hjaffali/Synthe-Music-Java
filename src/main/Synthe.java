package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/******************************************************************************
 *  Compilation:  javac Synthe.java
 *  Execution:    java Synthe input.txt tempo
 *  Dependencies: StdAudio.java Accord.java Note.java
 *
 *
 *
 ******************************************************************************/
@SuppressWarnings({"nls","hiding","resource"})
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

		Synthe.tempo = Integer.parseInt(args[1]);

		if (args.length == 3) {
			if (args[2].equals("harm")) 
				Synthe.harm = true ;
			else if (args[2].equals("guitar")) 
				Synthe.guitar = true ;
		}

		try {
			BufferedReader fichier = new BufferedReader(new FileReader(args[0]));
			String ligne;

			while ((ligne = fichier.readLine()) != null) {
				playLigne(ligne, Synthe.harm, Synthe.guitar);
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
		//On récupère chaque élément de la ligne entre chaque virgule
		String delims = "[,]";
		String [] tokens = ligne.split(delims);
		
		//On récupère les deux derniers éléments de la ligne, la durée et l'amplitude
		double amplitude = Integer.parseInt(tokens[tokens.length-1]);
		double duree = Note.faceToDuration(tokens[tokens.length-2], Synthe.tempo);
		
		//On ajoute la première note de la ligne à l'accord
		Accord accord = new Accord(Note.sToNote(tokens[0], amplitude, duree, harm));
		
		//S'il y a d'autres notes sur la ligne, on les ajoute à l'accord
		for (int i=1;i<tokens.length-2;i++) {
			accord.addNote(Note.sToNote(tokens[i], amplitude, duree, harm));
		}
		
		//Enfin, on joue l'accord
		accord.play();
	}


}


