import java.io.*;
import java.util.StringTokenizer;

public class Fitness {
    public static void setup_fitness(String fname) {
        try {
            int i, j;
            String line;

            BufferedReader in = new BufferedReader(new FileReader(fname));
            line = in.readLine();
            StringTokenizer tokens = new StringTokenizer(line);
            Tiny.varnumber = Integer.parseInt(tokens.nextToken().trim());
            Tiny.randomnumber = Integer.parseInt(tokens.nextToken().trim());
            Tiny.minrandom = Double.parseDouble(tokens.nextToken().trim());
            Tiny.maxrandom = Double.parseDouble(tokens.nextToken().trim());
            Tiny.fitnesscases = Integer.parseInt(tokens.nextToken().trim());
            Tiny.targets = new double[Tiny.fitnesscases][Tiny.varnumber + 1];

            if (Tiny.varnumber + Tiny.randomnumber >= Tiny.FSET_START)
                System.out.println("Za dużo zmiennych i stałych");

            for (i = 0; i < Tiny.fitnesscases; i++) {
                line = in.readLine();
                tokens = new StringTokenizer(line);
                for (j = 0; j <= Tiny.varnumber; j++) {
                    Tiny.targets[i][j] = Double.parseDouble(tokens.nextToken().trim());
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Proszę dostarczyć plik danych");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("ERROR: Nieprawidłowy format danych");
            System.exit(0);
        }
    }

    public static double fitness_function(char[] Prog) {
        int i, len;
        double result, fit = 0.0;

        len = Operations.traverse(Prog, 0);
        for (i = 0; i < Tiny.fitnesscases; i++) {
            for (int j = 0; j < Tiny.varnumber; j++)
                Tiny.x[j] = Tiny.targets[i][j];
            Tiny.program = Prog;
            Tiny.PC = 0;
            result = Operations.run();
            fit += Math.abs(result - Tiny.targets[i][Tiny.varnumber]);
        }
        return (-fit);
    }
}
