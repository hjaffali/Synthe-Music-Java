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
		this.duree = note1.duree;
		this.signal = note1.signal.clone() ;
	}
	
	/**
	 * 
	 * @param not
	 */
	public void addNote(Note not) {
		
		// TODO Auto-generated method stub
	}
	
	/**
	 * 
	 */
	public void play() {
		StdAudio.play(this.signal);
	}
}
