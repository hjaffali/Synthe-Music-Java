package main;

import java.util.Arrays;


@SuppressWarnings({"nls"})
public class Note {

	/** Tableau des frequences fondamentales pour les 8 octaves */
	public final static double[] fondFreq = {32.70, 65.41, 130.81, 261.63, 523.25, 1046.50, 2093.00, 4186.01};

	/** Tableau des 7 tons de bases en musique */
	public final static String[] tons = {"do", "re", "mi", "fa", "sol", "la", "si"};

	/** Tableau des hauteurs respectives des tons de bases */
	public final static int[] haut = {0, 2, 4, 5, 7, 9, 11};

	/** Frequence d'echantillonage du signal */
	public final static double echantillonageFreq = 44100f;

	/** Ton de base de la note */
	public String toneBase ;

	/** Alteration de la note ('#' dieze, ou 'b' bemole) */
	public char alter ;

	/** Octave de la note */
	public int octave ;

	/** Frequence de la note */
	public double freq ;

	/** Duree de la note (en secondes) */
	public double duree;

	/** Amplitude de la note (volume) entre 0 et 1 */
	public double amp;

	/** Tableau contenant la suite des echantillons du signal de la note */
	double[] signal ;

	/** Calcule la frequence d'une note a partir de la tonalite, de l'alteration et de l'octave.
	 * 
	 * @param toneBase tonalite de base de la note
	 * @param alter alteration de la note (# ou b)
	 * @param octave octave de la note
	 * @return la frequence de la ntoe
	 */
	private static double freqTone(String toneBase, char alter, int octave) {
		//On repère déjà dans quelle octave est la note, pour trouver le f0.
		double f0 = Note.fondFreq[octave];

		//Ensuite on regarde quelle est la tonalité, afin de savoir à quelle hauteur potentielle est la note
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

	/** Construit et retourne une note en fonction de sa tonalite, son amplitude, sa duree,
	 * et si l'on veut integrer les harmoniques au signal ou pas. 
	 * 
	 * @param tonalite tonalite de base, octave et alteration de la note
	 * @param amplitude amplitude de la note entre 0 et 1
	 * @param duree duree de la note en secondes
	 * @param harmon <code>true</code> si l'on veut integrer les harmoniques au signal, <code>false</code> sinon.
	 * @return nouvelle Note associée aux paramètres
	 */
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

	/** Retourne la duree d'une figure de note en fonction du tempo en parametre
	 * 
	 * @param figure figure de note
	 * @param tempo tempo de la partition
	 * @return duree de la figure en secondes
	 */
	public static double faceToDuration(String figure, int tempo) {
		double dureeNoire = 60f/(tempo*1f) ;

		switch(figure) {
		case("double-croche") :
			return dureeNoire/4f;
		case("croche") :
			return dureeNoire/2f;
		case("noire") :
			return dureeNoire;
		case("blanche") :
			return dureeNoire*2f;
		case("ronde") :
			return dureeNoire*4f;
		default :
			System.err.println("Figure inconnue");
			return Double.NaN;
		}
	}


	/** Le constructeur permettant de declarer/allouer une note par
	 * Note note = new Note(ton, alter, octave, duree, amplitude);
	 * 
	 * @param tB ton de base 
	 * @param alt alteration (dieze ou bemole)
	 * @param oct octave
	 * @param dur duree en secondes
	 * @param amp amplitude de la note entre 0 et 1
	 */
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

	/** Constructeur par recopie de la class Note
	 * 
	 * @param oldNote Note a copier
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

	/** Joue sur la sortie audio cette note */
	public void play() {
		StdAudio.play(this.signal);
	}


	/** Methode main() de test de la classe Note
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