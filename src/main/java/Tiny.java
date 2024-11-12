import java.util.*;
import java.io.*;

public class Tiny {
    private int BEST; // Indeks najlepszego osobnika
    private double[] fitness;
    public char[][] pop;
    private static Random rd = new Random();
    public static final int
            ADD = 110,
            SUB = 111,
            MUL = 112,
            DIV = 113,
            SIN = 114,
            COS = 115,
            FSET_START = ADD,
            FSET_END = DIV; // Zgodnie z Twoim życzeniem
    public static double[] x = new double[FSET_START];
    public static double minrandom, maxrandom;
    public static char[] program;
    public static int PC;
    public static int varnumber, fitnesscases, randomnumber;
    private static double fbestpop = 0.0, favgpop = 0.0;
    private static long seed;
    private static double avg_len;
    private static final int
            MAX_LEN = 1000,
            POPSIZE = 100000,
            DEPTH = 3,
            GENERATIONS = 30,
            TSIZE = 2;
    public static final double
            PMUT_PER_NODE = 0.05,
            CROSSOVER_PROB = 0.9;
    public static double[][] targets;

    private static char[] buffer = new char[MAX_LEN];

    public Tiny(String fname, long s) {
        fitness = new double[POPSIZE];
        seed = s;
        if (seed >= 0)
            rd.setSeed(seed);
        Fitness.setup_fitness(fname);
        for (int i = 0; i < FSET_START; i++)
            x[i] = (maxrandom - minrandom) * rd.nextDouble() + minrandom;
        pop = create_random_pop(POPSIZE, DEPTH, fitness);
    }

    private int grow(char[] buffer, int pos, int max, int depth) {
        char prim = (char) rd.nextInt(2);
        int one_child;

        if (pos >= max)
            return (-1);

        if (pos == 0)
            prim = 1;

        if (prim == 0 || depth == 0) {
            prim = (char) rd.nextInt(varnumber + randomnumber);
            buffer[pos] = prim;
            return (pos + 1);
        } else {
            // Losujemy funkcję, w tym SIN i COS
            int func = rd.nextInt(6); // 6 funkcji: ADD, SUB, MUL, DIV, SIN, COS
            switch (func) {
                case 0:
                    prim = ADD;
                    break;
                case 1:
                    prim = SUB;
                    break;
                case 2:
                    prim = MUL;
                    break;
                case 3:
                    prim = DIV;
                    break;
                case 4:
                    prim = SIN;
                    break;
                case 5:
                    prim = COS;
                    break;
            }
            buffer[pos] = prim;
            switch (prim) {
                case ADD:
                case SUB:
                case MUL:
                case DIV:
                    one_child = grow(buffer, pos + 1, max, depth - 1);
                    if (one_child < 0)
                        return (-1);
                    return (grow(buffer, one_child, max, depth - 1));
                case SIN:
                case COS:
                    return grow(buffer, pos + 1, max, depth - 1);
            }
        }
        return (0); // Nigdy tu nie dotrze
    }

    private char[] create_random_indiv(int depth) {
        char[] ind;
        int len;

        len = grow(buffer, 0, MAX_LEN, depth);

        while (len < 0)
            len = grow(buffer, 0, MAX_LEN, depth);

        ind = new char[len];

        System.arraycopy(buffer, 0, ind, 0, len);
        return (ind);
    }

    private char[][] create_random_pop(int n, int depth, double[] fitness) {
        char[][] pop = new char[n][];
        int i;

        for (i = 0; i < n; i++) {
            pop[i] = create_random_indiv(depth);
            fitness[i] = Fitness.fitness_function(pop[i]);
        }
        return (pop);
    }

    private void stats(double[] fitness, char[][] pop, int gen) {
        int i, best = rd.nextInt(POPSIZE);
        int node_count = 0;
        fbestpop = fitness[best];
        favgpop = 0.0;

        for (i = 0; i < POPSIZE; i++) {
            node_count += Operations.traverse(pop[i], 0);
            favgpop += fitness[i];
            if (fitness[i] > fbestpop) {
                best = i;
                fbestpop = fitness[i];
            }
        }
        avg_len = (double) node_count / POPSIZE;
        favgpop /= POPSIZE;
        System.out.print("Generation=" + gen + " Avg Fitness=" + (-favgpop) +
                " Best Fitness=" + (-fbestpop) + " Avg Size=" + avg_len +
                "\nBest Individual: ");

        print_indiv(pop[best], 0);
        BEST = best;
        System.out.print("\n");
        System.out.flush();
    }

    private int print_indiv(char[] buffer, int buffercounter) {
        int a1 = 0, a2;
        if (buffercounter >= buffer.length) {
            return buffercounter;
        }
        if (buffer[buffercounter] < FSET_START) {
            if (buffer[buffercounter] < varnumber)
                System.out.print("X" + (buffer[buffercounter] + 1) + " ");
            else
                System.out.print(x[buffer[buffercounter]]);
            return (++buffercounter);
        }
        switch (buffer[buffercounter]) {
            case ADD:
                System.out.print("(");
                a1 = print_indiv(buffer, ++buffercounter);
                System.out.print(" + ");
                break;
            case SUB:
                System.out.print("(");
                a1 = print_indiv(buffer, ++buffercounter);
                System.out.print(" - ");
                break;
            case MUL:
                System.out.print("(");
                a1 = print_indiv(buffer, ++buffercounter);
                System.out.print(" * ");
                break;
            case DIV:
                System.out.print("(");
                a1 = print_indiv(buffer, ++buffercounter);
                System.out.print(" / ");
                break;
            case SIN:
                System.out.print("sin( ");
                a1 = print_indiv(buffer, ++buffercounter);
                System.out.print(" )");
                return a1;
            case COS:
                System.out.print("cos( ");
                a1 = print_indiv(buffer, ++buffercounter);
                System.out.print(" )");
                return a1;
        }
        a2 = print_indiv(buffer, a1);
        System.out.print(")");
        return (a2);
    }

    private void save(String solutionFileName) {
        try {
            File yourFile = new File(solutionFileName);
            yourFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(solutionFileName, false));
            save_best_indiv(writer, pop[BEST], 0);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving the solution.");
            e.printStackTrace();
        }
    }

    private int save_best_indiv(BufferedWriter writer, char[] buffer, int buffercounter) throws IOException {
        int a1 = 0, a2;
        if (buffercounter >= buffer.length) {
            return buffercounter;
        }
        if (buffer[buffercounter] < FSET_START) {
            if (buffer[buffercounter] < varnumber)
                writer.write("X" + (buffer[buffercounter] + 1) + " ");
            else
                writer.write(String.valueOf(x[buffer[buffercounter]]));
            return (++buffercounter);
        }
        switch (buffer[buffercounter]) {
            case ADD:
                writer.write("(");
                a1 = save_best_indiv(writer, buffer, ++buffercounter);
                writer.write(" + ");
                break;
            case SUB:
                writer.write("(");
                a1 = save_best_indiv(writer, buffer, ++buffercounter);
                writer.write(" - ");
                break;
            case MUL:
                writer.write("(");
                a1 = save_best_indiv(writer, buffer, ++buffercounter);
                writer.write(" * ");
                break;
            case DIV:
                writer.write("(");
                a1 = save_best_indiv(writer, buffer, ++buffercounter);
                writer.write(" / ");
                break;
            case SIN:
                writer.write("sin( ");
                a1 = save_best_indiv(writer, buffer, ++buffercounter);
                writer.write(" )");
                return a1;
            case COS:
                writer.write("cos( ");
                a1 = save_best_indiv(writer, buffer, ++buffercounter);
                writer.write(" )");
                return a1;
        }
        a2 = save_best_indiv(writer, buffer, a1);
        writer.write(")");
        return (a2);
    }

    private int tournament(double[] fitness, int tsize) {
        int best = rd.nextInt(POPSIZE), i, competitor;
        double fbest = -1.0e34;

        for (i = 0; i < tsize; i++) {
            competitor = rd.nextInt(POPSIZE);
            if (fitness[competitor] > fbest) {
                fbest = fitness[competitor];
                best = competitor;
            }
        }
        return (best);
    }

    private int negative_tournament(double[] fitness, int tsize) {
        int worst = rd.nextInt(POPSIZE), i, competitor;
        double fworst = 1e34;

        for (i = 0; i < tsize; i++) {
            competitor = rd.nextInt(POPSIZE);
            if (fitness[competitor] < fworst) {
                fworst = fitness[competitor];
                worst = competitor;
            }
        }
        return (worst);
    }

    private char[] crossover(char[] parent1, char[] parent2) {
        int xo1start, xo1end, xo2start, xo2end;
        char[] offspring;
        int len1 = Operations.traverse(parent1, 0);
        int len2 = Operations.traverse(parent2, 0);
        int lenoff;

        xo1start = rd.nextInt(len1);
        xo1end = Operations.traverse(parent1, xo1start);

        xo2start = rd.nextInt(len2);
        xo2end = Operations.traverse(parent2, xo2start);

        lenoff = xo1start + (xo2end - xo2start) + (len1 - xo1end);

        offspring = new char[lenoff];

        System.arraycopy(parent1, 0, offspring, 0, xo1start);
        System.arraycopy(parent2, xo2start, offspring, xo1start,
                (xo2end - xo2start));
        System.arraycopy(parent1, xo1end, offspring,
                xo1start + (xo2end - xo2start),
                (len1 - xo1end));

        return (offspring);
    }

    private char[] mutation(char[] parent, double pmut) {
        int len = Operations.traverse(parent, 0), i;
        int mutsite;
        char[] parentcopy = new char[len];

        System.arraycopy(parent, 0, parentcopy, 0, len);
        for (i = 0; i < len; i++) {
            if (rd.nextDouble() < pmut) {
                mutsite = i;
                if (parentcopy[mutsite] < FSET_START)
                    parentcopy[mutsite] = (char) rd.nextInt(varnumber + randomnumber);
                else {
                    // Losujemy nową funkcję, w tym SIN i COS
                    int func = rd.nextInt(6); // 6 funkcji: ADD, SUB, MUL, DIV, SIN, COS
                    switch (func) {
                        case 0:
                            parentcopy[mutsite] = ADD;
                            break;
                        case 1:
                            parentcopy[mutsite] = SUB;
                            break;
                        case 2:
                            parentcopy[mutsite] = MUL;
                            break;
                        case 3:
                            parentcopy[mutsite] = DIV;
                            break;
                        case 4:
                            parentcopy[mutsite] = SIN;
                            break;
                        case 5:
                            parentcopy[mutsite] = COS;
                            break;
                    }
                }
            }
        }
        return (parentcopy);
    }

    private void print_parms() {
        System.out.print("-- TINY GP (Java version) --\n");
        System.out.print("SEED=" + seed + "\nMAX_LEN=" + MAX_LEN +
                "\nPOPSIZE=" + POPSIZE + "\nDEPTH=" + DEPTH +
                "\nCROSSOVER_PROB=" + CROSSOVER_PROB +
                "\nPMUT_PER_NODE=" + PMUT_PER_NODE +
                "\nMIN_RANDOM=" + minrandom +
                "\nMAX_RANDOM=" + maxrandom +
                "\nGENERATIONS=" + GENERATIONS +
                "\nTSIZE=" + TSIZE +
                "\n----------------------------------\n");
    }

    private void evolve() {
        int gen = 0, indivs, offspring, parent1, parent2, parent;
        double newfit;
        char[] newind;
        print_parms();
        stats(fitness, pop, 0);
        for (gen = 1; gen < GENERATIONS; gen++) {
            if (fbestpop > -1e-3) {
                System.out.print("PROBLEM SOLVED\n");
                return;
            }
            for (indivs = 0; indivs < POPSIZE; indivs++) {
                if (rd.nextDouble() < CROSSOVER_PROB) {
                    parent1 = tournament(fitness, TSIZE);
                    parent2 = tournament(fitness, TSIZE);
                    newind = crossover(pop[parent1], pop[parent2]);
                } else {
                    parent = tournament(fitness, TSIZE);
                    newind = mutation(pop[parent], PMUT_PER_NODE);
                }
                newfit = Fitness.fitness_function(newind);
                offspring = negative_tournament(fitness, TSIZE);
                pop[offspring] = newind;
                fitness[offspring] = newfit;
            }
            stats(fitness, pop, gen);
        }
        System.out.print("PROBLEM *NOT* SOLVED\n");
    }

    // Metoda do resetowania zmiennych statycznych przed kolejnym uruchomieniem
    private static void resetStaticVariables() {
        x = new double[FSET_START];
        minrandom = 0;
        maxrandom = 0;
        program = null;
        PC = 0;
        varnumber = 0;
        fitnesscases = 0;
        randomnumber = 0;
        fbestpop = 0.0;
        favgpop = 0.0;
        avg_len = 0;
        targets = null;
    }

    public static void main(String[] args) {
        String[] dataFiles;

        if (args.length == 0) {
            // Domyślne pliki danych
            dataFiles = new String[]{
                    "problem_2sin.dat", "problem_3tan.dat",
                    "problem_6c.dat", "problem_6d.dat"
            };

        } else {
            dataFiles = args;
        }

        for (String fname : dataFiles) {
            System.out.println("Przetwarzanie pliku: " + fname);
            long s = -1;
            Tiny gp = new Tiny(fname, s);
            gp.evolve();
            // Zapisywanie najlepszego osobnika do pliku
            String solutionFileName = fname.substring(0, fname.lastIndexOf('.')) + ".txt";
            gp.save(solutionFileName);
            // Resetowanie zmiennych statycznych przed kolejnym uruchomieniem
            resetStaticVariables();
        }
    }
}
