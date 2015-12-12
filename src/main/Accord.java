package main;

import java.util.Random;

public class Accord {

	/** Tableau contenant toutes les notes de l'accord (4 au maximum) */
	Note[] notes ;
	
	/** Nombre de notes presentes dans l'accord */
	int nbNotes = 0;
	
	/** Duree de l'accord en secondes */
	double duree ;
	
	/** Signal correspondant a la moyenne des signaux des notes composant l'accord */
	double[] signal ;
	
	/** Constructeur de la classe Accord. 
	 * Initialise l'accord avec la note passee en parametre
	 * 
	 * @param note1 premiere note de l'accord
	 */
	public Accord(Note note1) {
		this.notes = new Note[4] ;
		this.notes[0] = new Note(note1) ;
		this.notes[1] = null ;
		this.notes[2] = null ;
		this.notes[3] = null ;
		this.nbNotes = 1;
		this.duree = note1.duree;
		this.signal = note1.signal.clone() ;
	}
	
	/** Ajoute la note en parametre à l'accord et reequilibre le signal de ce dernier
	 * 
	 * @param not note a ajouter a l'accord
	 */
	public void addNote(Note not) {
		this.notes[this.nbNotes] = not;
		this.nbNotes++;
		
		for(int i=0; i<this.signal.length; i++) {
			double somme = 0;
			for(int j=0; j<this.nbNotes; j++) {
				somme = somme + this.notes[j].signal[i];
			}
			this.signal[i] = somme/((double)this.nbNotes);
		}
	}
	
	/** Joue sur la sortie audio cet accord	 */
	public void play() {
		StdAudio.play(this.signal);
	}
	
	/** Methode main() de test de la classe Accord
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Random random = new Random();
		
		while(true) {
			int nbNotes = random.nextInt()%4;
			
			int randomOctave = Math.abs(random.nextInt())%8;
			int randomNote = Math.abs(random.nextInt())%7;

			Accord accord = new Accord(new Note(Note.tons[randomNote],' ',randomOctave,Note.faceToDuration("croche", 50),1));
			
			for (int i=1;i<nbNotes;i++) {
				randomOctave = Math.abs(random.nextInt())%8;
				randomNote = Math.abs(random.nextInt())%7;
				accord.addNote(new Note(Note.tons[randomNote],' ',randomOctave,Note.faceToDuration("croche", 50),1));
			}
			
			accord.play();
		}
		
	
		
		
	}
}
