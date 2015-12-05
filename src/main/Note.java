package main;

import java.util.Arrays;


@SuppressWarnings({"nls"})
public class Note {

	/**
	 * 
	 */
	public final static double[] fondFreq = {32.70, 65.41, 130.81, 261.63, 523.25, 1046.50, 2093.00, 4186.01};

	/**
	 * 
	 */
	public final static String[] tons = {"do", "re", "mi", "fa", "sol", "la", "si"};

	/**
	 * 
	 */
	public final static int[] haut = {0, 2, 4, 5, 7, 9, 11};

	/**
	 * 
	 */
	public final static double echantillonageFreq = 44100f;

	/**
	 * 
	 */
	public String toneBase ;

	/**
	 * 
	 */
	public char alter ;

	/**
	 * 
	 */
	public int octave ;

	/**
	 * 
	 */
	public double freq ;

	/**
	 * 
	 */
	public double duree;

	/**
	 * 
	 */
	public double amp;

	/**
	 * 
	 */
	double[] signal ;

	/**
	 * 
	 * @param toneBase
	 * @param alter
	 * @param octave
	 * @return
	 */
	
	//Fonctionne
	private static double freqTone(String toneBase, char alter, int octave) {
		//On repère déjà on est dans quelle octave comme ça on peut trouver le f0.
		double f0 = Note.fondFreq[octave];
		
		//Ensuite on regarde on est à quelle note, comme ça on sait on est à quelle hauteur potentielle
		int n = Note.haut[Arrays.asList(Note.tons).indexOf(toneBase)];
		
		//Enfin on regarde si il y a un dièze/bémole pour ajouter/enlever une hauteur.
		if(alter=='#') {
			n = n+1;
		} 
		else if (alter=='b') {
			n = n-1;
		}

		//Ensuite pour calculer la férquence, on prend f0, et n, la hauteur, et on applique la formule.
		return f0 * Math.pow(2, n / 12.0);
	}

	/**
	 * 
	 * @param tonalite
	 * @param amplitude
	 * @param duree
	 * @param harmon
	 * @return
	 */
	//FIXME : Il peut y a avoir un soucis ici, à vérifier, lorsqu'on ajoute les harmoniques
	public static Note sToNote(String tonalite, double amplitude, double duree, boolean harmon) {
		int octave=0;
		char alteration=' ';
		String tonaliteBase="";

		if(tonalite.charAt(tonalite.length()-1)=='b' || tonalite.charAt(tonalite.length()-1)=='#') {
			alteration=tonalite.charAt(tonalite.length()-1);
			octave=Integer.valueOf(""+tonalite.charAt(tonalite.length()-2)).intValue();
			for(int i=0; i<tonalite.length()-2;i++) {
				tonaliteBase = tonaliteBase.concat(""+tonalite.charAt(i));
			}
		}
		else {
			alteration=' ';
			octave=Integer.valueOf(""+tonalite.charAt(tonalite.length()-1)).intValue();
			for(int i=0; i<tonalite.length()-1;i++) {
				tonaliteBase = tonaliteBase.concat(""+tonalite.charAt(i));
			}
		}
		
		Note note = new Note(tonaliteBase, alteration, octave, duree, amplitude);
		
		if(harmon) {
			for (int i=0; i<note.signal.length; i++) {
				note.signal[i] += (note.amp/4f) * Math.sin(2 * Math.PI * i * (0.5*note.freq) / Note.echantillonageFreq);
				note.signal[i] += (note.amp/4f) * Math.sin(2 * Math.PI * i * (2*note.freq) / Note.echantillonageFreq);
				note.signal[i] += (note.amp/8f) * Math.sin(2 * Math.PI * i * (3*note.freq) / Note.echantillonageFreq);
			}
		}
		
		return note;
	}

	/**
	 * 
	 * @param figure
	 * @param tempo
	 * @return
	 */
	public static double faceToDuration(String figure, int tempo) {
		double dureeNoire = tempo/60f ;

		switch(figure) {
		case("double-croche") :
			return dureeNoire/4f;
		case("croche") :
			return dureeNoire/2f;
		case("noire") :
			return dureeNoire;
		case("blanche") :
			return dureeNoire*2;
		case("ronde") :
			return dureeNoire*4;
		default :
			System.err.println("Figure inconnue");
			return 0;
		}
	}


	/** Le constructeur permettant de déclarer/allouer une note par
	 * Note note = new Note(ton, alter, octave, duree, amplitude);
	 * 
	 * @param tB
	 * @param alt
	 * @param oct
	 * @param dur
	 * @param amp
	 */
	//Fonctionne
	public Note(String tB, char alt, int oct, double dur, double amp){
		this.duree = dur ;
		this.alter = alt ;
		this.toneBase = tB ;
		this.octave = oct ;
		this.freq = freqTone(this.toneBase, this.alter, this.octave) ;
		this.amp = amp;

		//On calcule la taille de l'échantillon
		int N = (int) (StdAudio.SAMPLE_RATE * this.duree);
		//On alloue la mémoire pour le tableau de signal
		this.signal = new double[N+1];
		//On remplit le tableau conformément à la formule du signal
		for (int i=0; i<=N; i++) {
			this.signal[i] = amp * Math.sin(2 * Math.PI * this.freq *(i / Note.echantillonageFreq));
		}
	}

	/**
	 * 
	 * @param oldNote
	 */
	public Note(Note oldNote) {
		this.duree = oldNote.duree ;
		this.alter = oldNote.alter ;
		this.toneBase = oldNote.toneBase ;
		this.octave = oldNote.octave ;
		this.amp = oldNote.amp ;
		this.freq = oldNote.freq ;
		this.signal = oldNote.signal.clone() ;
	}

	/**
	 * 
	 */
	//Fontionne
	public void play() {
		StdAudio.play(this.signal);
	}


	/** Méthode main() de test de la classe Note
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		int i, oct ;
		if (args.length < 1) oct = 3 ; else oct = Integer.parseInt(args[0]) ;
		for (i = 0; i< 7; i++){
			Note not = new Note(Note.tons[i], ' ', oct, 1.0, 1.0);
			System.out.print(not.toneBase + ", octave " + not.octave
					+ "  f0 =" + Note.fondFreq[not.octave] + "Hz, F =");
			System.out.format("%.2f Hz%n",Double.valueOf(not.freq));
			not.play();
		}
	}


}