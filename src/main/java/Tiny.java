import java.util.*;
import java.io.*;

public class Tiny {
    double[] fitness;
    char[][] pop;
    static Random rd = new Random();
    static final int
            ADD = 110,
            SUB = 111,
            MUL = 112,
            DIV = 113,
            FSET_START = ADD,
            FSET_END = DIV;
    static double[] x = new double[FSET_START];
    static double minrandom, maxrandom;
    static char[] program;
    static int PC;
    static int varnumber, fitnesscases, randomnumber;
    static double fbestpop = 0.0, favgpop = 0.0;
    static long seed;
    static double avg_len;
    static final int
            MAX_LEN = 10000,
            POPSIZE = 100000,
            DEPTH = 5,
            GENERATIONS = 50,
            TSIZE = 2;
    public static final double
            PMUT_PER_NODE = 0.05,
            CROSSOVER_PROB = 0.9;
    static double[][] targets;

    int BEST; // Dodane: indeks najlepszego osobnika

    int grow(char[] buffer, int pos, int max, int depth) {
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
            prim = (char) (rd.nextInt(FSET_END - FSET_START + 1) + FSET_START);
            switch (prim) {
                case ADD:
                case SUB:
                case MUL:
                case DIV:
                    buffer[pos] = prim;
                    one_child = grow(buffer, pos + 1, max, depth - 1);
                    if (one_child < 0)
                        return (-1);
                    return (grow(buffer, one_child, max, depth - 1));
            }
        }
        return (0);
    }

    int print_indiv(char[] buffer, int buffercounter) {
        int a1 = 0, a2;
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
        }
        a2 = print_indiv(buffer, a1);
        System.out.print(")");
        return (a2);
    }

    // Dodane: Metoda do zapisywania najlepszego osobnika do pliku
    public int save_best_indiv(String fname, char[] buffer, int buffercounter) throws IOException {
        int a1 = 0, a2;
        FileWriter myWriter;
        if (buffer[buffercounter] < FSET_START) {
            myWriter = new FileWriter(fname, true);
            if (buffer[buffercounter] < varnumber)
                myWriter.write("X" + (buffer[buffercounter] + 1) + " ");
            else
                myWriter.write(String.valueOf(x[buffer[buffercounter]]));
            myWriter.close();
            return (++buffercounter);
        }
        switch (buffer[buffercounter]) {
            case ADD:
                myWriter = new FileWriter(fname, true);
                myWriter.write("(");
                myWriter.close();
                a1 = save_best_indiv(fname, buffer, ++buffercounter);
                myWriter = new FileWriter(fname, true);
                myWriter.write(" + ");
                myWriter.close();
                break;
            case SUB:
                myWriter = new FileWriter(fname, true);
                myWriter.write("(");
                myWriter.close();
                a1 = save_best_indiv(fname, buffer, ++buffercounter);
                myWriter = new FileWriter(fname, true);
                myWriter.write(" - ");
                myWriter.close();
                break;
            case MUL:
                myWriter = new FileWriter(fname, true);
                myWriter.write("(");
                myWriter.close();
                a1 = save_best_indiv(fname, buffer, ++buffercounter);
                myWriter = new FileWriter(fname, true);
                myWriter.write(" * ");
                myWriter.close();
                break;
            case DIV:
                myWriter = new FileWriter(fname, true);
                myWriter.write("(");
                myWriter.close();
                a1 = save_best_indiv(fname, buffer, ++buffercounter);
                myWriter = new FileWriter(fname, true);
                myWriter.write(" / ");
                myWriter.close();
                break;
        }
        a2 = save_best_indiv(fname, buffer, a1);
        myWriter = new FileWriter(fname, true);
        myWriter.write(")");
        myWriter.close();
        return (a2);
    }

    static char[] buffer = new char[MAX_LEN];

    char[] create_random_indiv(int depth) {
        char[] ind;
        int len;

        len = grow(buffer, 0, MAX_LEN, depth);

        while (len < 0)
            len = grow(buffer, 0, MAX_LEN, depth);

        ind = new char[len];

        System.arraycopy(buffer, 0, ind, 0, len);
        return (ind);
    }

    char[][] create_random_pop(int n, int depth, double[] fitness) {
        char[][] pop = new char[n][];
        int i;

        for (i = 0; i < n; i++) {
            pop[i] = create_random_indiv(depth);
            fitness[i] = Fitness.fitness_function(pop[i]);
        }
        return (pop);
    }

    void stats(double[] fitness, char[][] pop, int gen) {
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
        System.out.print("\n");
        System.out.flush();
        BEST = best; // Dodane: Zapamiętujemy indeks najlepszego osobnika
    }

    int tournament(double[] fitness, int tsize) {
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

    int negative_tournament(double[] fitness, int tsize) {
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

    char[] crossover(char[] parent1, char[] parent2) {
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

    char[] mutation(char[] parent, double pmut) {
        int len = Operations.traverse(parent, 0), i;
        int mutsite;
        char[] parentcopy = new char[len];

        System.arraycopy(parent, 0, parentcopy, 0, len);
        for (i = 0; i < len; i++) {
            if (rd.nextDouble() < pmut) {
                mutsite = i;
                if (parentcopy[mutsite] < FSET_START)
                    parentcopy[mutsite] = (char) rd.nextInt(varnumber + randomnumber);
                else
                    switch (parentcopy[mutsite]) {
                        case ADD:
                        case SUB:
                        case MUL:
                        case DIV:
                            parentcopy[mutsite] =
                                    (char) (rd.nextInt(FSET_END - FSET_START + 1)
                                            + FSET_START);
                    }
            }
        }
        return (parentcopy);
    }

    void print_parms() {
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

    void evolve() {
        int gen = 0, indivs, offspring, parent1, parent2, parent;
        double newfit;
        char[] newind;
        print_parms();
        stats(fitness, pop, 0);
        for (gen = 1; gen < GENERATIONS; gen++) {
            if (fbestpop > -1e-5) {
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

    // Dodane: Metoda do zapisywania najlepszego osobnika do pliku
    public void save(String solutionFolder, String solutionFileName) {
        try {
            String fSolName = solutionFolder + "/" + solutionFileName;
            File dir = new File(solutionFolder);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File yourFile = new File(fSolName);
            yourFile.createNewFile();
            FileWriter myWriter = new FileWriter(fSolName, false);
            myWriter.write("");
            myWriter.close();
            save_best_indiv(fSolName, pop[BEST], 0);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the best individual.");
            e.printStackTrace();
        }
    }

    // Metoda do resetowania zmiennych statycznych
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
        String[] problemFolders = {"problem_1", "problem_2"};
        char[] problemFiles = {'a', 'b', 'c', 'd'};
        long s = -1;

        for (String problemFolder : problemFolders) {
            String solutionFolder = "solution_" + problemFolder.substring(problemFolder.indexOf('_') + 1);
            for (char problemSuffix : problemFiles) {
                String problemName = problemFolder + problemSuffix;
                String fname = problemFolder + "/" + problemName + ".dat";
                System.out.println("\n=== SOLVING " + fname + " ===\n");
                try {
                    Tiny gp = new Tiny(fname, s);
                    gp.evolve();
                    // Zapisywanie najlepszego osobnika do odpowiedniego pliku
                    String solutionFileName = problemName + ".txt";
                    gp.save(solutionFolder, solutionFileName);
                } catch (Exception e) {
                    System.out.println("An error occurred while solving " + fname + ": " + e.getMessage());
                }
                // Resetowanie zmiennych statycznych przed kolejnym uruchomieniem
                resetStaticVariables();
            }
        }
    }
}
