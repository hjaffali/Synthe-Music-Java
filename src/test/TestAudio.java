package test;

import java.util.Random;
import main.StdAudio;

/**
 * Created by zulupero on 19/10/15.
 */
public class TestAudio {

    public static Random rand = new Random();

    public static void main(String[] args) {
        final double F_ECH = 44100.0 ;
        double duration = 1.0 ;
        int i;
        if (args.length == 1) duration = Double.parseDouble(args[0]);

        double[] signal =  new double[(int)Math.round(duration * F_ECH)];

        for (i = 0; i< signal.length; i++) {
            signal[i] = rand.nextDouble()*2.0 -1.0 ;
        }
        for (i = 0; i< signal.length; i++) {
            StdAudio.play(signal[i]);
        }

    }
}
