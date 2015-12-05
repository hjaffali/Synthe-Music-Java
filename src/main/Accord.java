package main;

public class Accord {

	/**
	 * 
	 */
	Note[] notes ;
	
	/**
	 * 
	 */
	double duree ;
	
	/**
	 * 
	 */
	double[] signal ;
	
	/** Nombre de notes présentes dans l'accord */
	int nbNotes = 0;

	/**
	 * 
	 * @param note1
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
	
	/**
	 * 
	 * @param not
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
	
	/**
	 * 
	 */
	public void play() {
		StdAudio.play(this.signal);
	}
}
