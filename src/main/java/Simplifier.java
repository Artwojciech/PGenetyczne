import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;

public class Simplifier {
    private String orgProg;
    private String newProg;
    private int subFormLocIndex;
    private double lfVal;
    private String ctFunction;
    private double rtVal;
    private double result;
    private int initialSubFormulaCount;
    private int programSizeReduction;

    public Simplifier() { } // constructor

    public String simplify(String program) {
        orgProg = program;
        newProg = orgProg.substring(1, orgProg.length() - 1); // strip-off outlying final parentheses.
        this.initialSubFormulaCount = countSimplifiableSubFormulasInNewProgram();

        while (countSimplifiableSubFormulasInNewProgram() > 0) {  // while there are any sub formulas left...
            locateAvailableSubFormula();
            pullAllSubformulaTokens();
            calcResultOfSubformula();
            replaceSubFormulaWithItsResult();
        }

        newProg = "(" + newProg + ")"; // add in outlying final parentheses again.
        return newProg;
    }

    public int countSimplifiableSubFormulasInNewProgram() {
        int iFirstParen;
        int iSecondParen;
        int count = 0;

        for (int i = 0; i < this.newProg.length(); i++) { // for loop through program string,
            iFirstParen = locNextParen(newProg, i);        // find index of next parenthesis starting from i
            if (iFirstParen == -1) break;                     // if no next par's left, break to return count.
            iSecondParen = locNextParen(newProg, iFirstParen + 1);    // find index of next parenthesis starting from i+1
            if (iSecondParen == -1) break;                    // if no next par's left, break to return count.
            if (isSimplifiableSubFormula(iFirstParen, iSecondParen)) {
                count++;      // count it
                i = iSecondParen; // prepare to move past 2nd par to efficiently keep looking
            } else {
                i = iSecondParen - 1; // prepare to start at 2nd par to efficiently keep looking
            }
        } // next loop
        return count;
    }

    public int locateAvailableSubFormula() {
        int iFirstParen;
        int iSecondParen;

        for (int i = 0; i < this.newProg.length(); i++) { // for loop moves through program string
            iFirstParen = locNextParen(newProg, i);        // find index of next parenthesis starting from i
            if (iFirstParen == -1) break;                     // if no next par's left, break to return -1
            iSecondParen = locNextParen(newProg, iFirstParen + 1);    // find index of next parenthesis starting from i+1
            if (iSecondParen == -1) break;                    // if no next par's left, break to return count.
            if (isSimplifiableSubFormula(iFirstParen, iSecondParen)) { // can this subformula be simplified?
                subFormLocIndex = iFirstParen;              // yes, so note where that subformula starts
                return subFormLocIndex;                         // since subformula found, return location of first parenthesis
            }
            i = iSecondParen - 1;                                 // no, so reset loop to where next parenthesis was found for more checking.
        } // next loop

        subFormLocIndex = -1; // to indicate nothing found.
        return subFormLocIndex;
    }

    public boolean isSimplifiableSubFormula(int iFirstParen, int iSecondParen) {
        boolean isOpenParFound = (newProg.charAt(iFirstParen) == '(');
        boolean isCloseParFound = (newProg.charAt(iSecondParen) == ')');
        boolean isFreeOfVars = true;

        for (int i = iFirstParen; i < iSecondParen; i++) { // search for X's
            if (newProg.charAt(i) == 'X') { // if you see any
                isFreeOfVars = false;         // then this subformula can't be simplified.
                break;
            }
        }
        return (isOpenParFound && isCloseParFound && isFreeOfVars);
    }

    public int locNextParen(String str, int i) {
        for (int j = i; j < str.length(); j++) {      // move through the string from wherever
            char ch = str.charAt(j);
            if (ch == '(' || ch == ')') return j;             // return if it's a par of either type
        }
        return -1; // no par's found.
    }

    public void pullAllSubformulaTokens() {
        String str = newProg.substring(subFormLocIndex, locNextParen(newProg, subFormLocIndex + 1) + 1); // get subformula
        String[] tokens = str.split("[ ]+");               // split up into three parts around the spaces

        tokens[0] = tokens[0].substring(1);   // drop "("
        tokens[2] = tokens[2].substring(0, tokens[2].length() - 1);  // drop ")"

        lfVal = Double.parseDouble(tokens[0]);
        ctFunction = tokens[1];
        rtVal = Double.parseDouble(tokens[2]);
    }

    public void calcResultOfSubformula() {
        switch (ctFunction) {
            case "+":
                result = lfVal + rtVal;
                break;
            case "-":
                result = lfVal - rtVal;
                break;
            case "*":
                result = lfVal * rtVal;
                break;
            case "/":
                result = lfVal / rtVal;
                break;
            default:
                throw new IllegalArgumentException("Invalid function character found: " + ctFunction);
        }
    }

    public void replaceSubFormulaWithItsResult() {
        String str = newProg.substring(subFormLocIndex, locNextParen(newProg, subFormLocIndex + 1) + 1); // get subformula
        newProg = newProg.replace(str, String.valueOf(result)); // replace subformula with its result.
    }

    private static void processFile(String inputFilePath, String outputFilePath) throws IOException {
        // Read the content of the input file
        String content = new String(Files.readAllBytes(Paths.get(inputFilePath)));

        // Create an instance of Simplifier and simplify the content
        Simplifier simplifier = new Simplifier();
        String simplifiedContent = simplifier.simplify(content);

        // Write the simplified content to the output file
        Files.write(Paths.get(outputFilePath), simplifiedContent.getBytes(), StandardOpenOption.CREATE);
    }

    public static void main(String[] args) {
        // List of input files
        String[] files = {
                "problem_1a.txt", "problem_1b.txt", "problem_1c.txt", "problem_1d.txt",
                "problem_2a.txt", "problem_2b.txt", "problem_2c.txt", "problem_2d.txt",
                "problem_3a.txt", "problem_3b.txt", "problem_3c.txt", "problem_3d.txt",
                "problem_4a.txt", "problem_4b.txt", "problem_4c.txt", "problem_4d.txt",
                "problem_5a.txt", "problem_5b.txt", "problem_5c.txt", "problem_5d.txt",
                "problem_6a.txt", "problem_6b.txt", "problem_6c.txt", "problem_6d.txt"
        };

        // Ensure the output directory exists
        Path outputDir = Paths.get("simplified_solutions");
        if (!Files.exists(outputDir)) {
            try {
                Files.createDirectory(outputDir);
            } catch (IOException e) {
                System.err.println("Failed to create output directory: " + e.getMessage());
                return;
            }
        }

        // Process each file
        try {
            for (String fileName : files) {
                String inputFilePath = fileName;  // Input files are in the main directory
                String outputFilePath = outputDir + "/simplified_" + fileName;  // Output file with "simplified_" prefix

                processFile(inputFilePath, outputFilePath);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while processing the files: " + e.getMessage());
        }
    }
}
