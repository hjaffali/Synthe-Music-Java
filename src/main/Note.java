package main;

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
	private static double freqTone(String toneBase, char alter, int octave) {

		//On repère déjà on est dans quelle octave comme ça on peut trouver le f0.
		//Ensuite on regarde on est à quelle note, comme ça on sait on est à quelle hauteur potentielle
		//Enfin on regarde si il y a un dièze/bémole pour ajouter/enlever une hauteur.
		
		//Ensuite pour calculer la férquence, on prend f0, et n, la hauteur, et on applique la formule.
		
		return 0;
	}

	/**
	 * 
	 * @param tonalite
	 * @param amplitude
	 * @param duree
	 * @param harmon
	 * @return
	 */
	public static Note sToNote(String tonalite, double amplitude, double duree, boolean harmon) {
		// TODO Auto-generated method stub

		return null;
	}

	/**
	 * 
	 * @param figure
	 * @param tempo
	 * @return
	 */
	public static double faceToDuration(String figure, int tempo) {
		// TODO Auto-generated method stub
		return 0;
	}


	/**
	 * Le constructeur permettant de déclarer/allouer une note par
	 * Note note = new Note(ton, alter, octave, duree, amplitude);
	 * 
	 * @param tB
	 * @param alt
	 * @param oct
	 * @param dur
	 * @param amp
	 */
	public Note(String tB, char alt, int oct, double dur, double amp){
		this.duree = dur ;
		this.alter = alt ;
		this.toneBase = tB ;
		this.octave = oct ;
		this.freq = freqTone(this.toneBase, this.alter, this.octave) ;
		this.amp = amp;
		
		//Ici la définition de **signal**
		int i=0;
		double t=0;
		while(t<dur) {
			this.signal[i]=amp*Math.sin(2*Math.PI*this.freq*t);
			i++;
			t+=1/Note.echantillonageFreq;
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
	public void play() {
		StdAudio.play(this.signal);
	}


	/**
	 * méthode main() de test de la classe Note
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
			System.out.format("%.2f Hz%n",not.freq);
			not.play();
		}
	}


}