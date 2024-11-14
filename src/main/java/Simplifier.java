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

    public Simplifier() { }

    public String simplify(String program) {
        orgProg = program;
        newProg = orgProg.substring(1, orgProg.length() - 1);
        this.initialSubFormulaCount = countSimplifiableSubFormulasInNewProgram();

        while (countSimplifiableSubFormulasInNewProgram() > 0) {
            locateAvailableSubFormula();
            pullAllSubformulaTokens();
            calcResultOfSubformula();
            replaceSubFormulaWithItsResult();
        }

        newProg = "(" + newProg + ")";
        return newProg;
    }

    public int countSimplifiableSubFormulasInNewProgram() {
        int iFirstParen;
        int iSecondParen;
        int count = 0;

        for (int i = 0; i < this.newProg.length(); i++) {
            iFirstParen = locNextParen(newProg, i);
            if (iFirstParen == -1) break;
            iSecondParen = locNextParen(newProg, iFirstParen + 1);
            if (iSecondParen == -1) break;
            if (isSimplifiableSubFormula(iFirstParen, iSecondParen)) {
                count++;
                i = iSecondParen;
            } else {
                i = iSecondParen - 1;
            }
        }
        return count;
    }

    public int locateAvailableSubFormula() {
        int iFirstParen;
        int iSecondParen;

        for (int i = 0; i < this.newProg.length(); i++) {
            iFirstParen = locNextParen(newProg, i);
            if (iFirstParen == -1) break;
            iSecondParen = locNextParen(newProg, iFirstParen + 1);
            if (iSecondParen == -1) break;
            if (isSimplifiableSubFormula(iFirstParen, iSecondParen)) {
                subFormLocIndex = iFirstParen;
                return subFormLocIndex;
            }
            i = iSecondParen - 1;
        }
        subFormLocIndex = -1;
        return subFormLocIndex;
    }

    public boolean isSimplifiableSubFormula(int iFirstParen, int iSecondParen) {
        boolean isOpenParFound = (newProg.charAt(iFirstParen) == '(');
        boolean isCloseParFound = (newProg.charAt(iSecondParen) == ')');
        boolean isFreeOfVars = true;

        for (int i = iFirstParen; i < iSecondParen; i++) {
            if (newProg.charAt(i) == 'X') {
                isFreeOfVars = false;
                break;
            }
        }
        return (isOpenParFound && isCloseParFound && isFreeOfVars);
    }

    public int locNextParen(String str, int i) {
        for (int j = i; j < str.length(); j++) {
            char ch = str.charAt(j);
            if (ch == '(' || ch == ')') return j;
        }
        return -1;
    }

    public void pullAllSubformulaTokens() {
        String str = newProg.substring(subFormLocIndex, locNextParen(newProg, subFormLocIndex + 1) + 1);
        String[] tokens = str.split("[ ]+");

        tokens[0] = tokens[0].substring(1);
        tokens[2] = tokens[2].substring(0, tokens[2].length() - 1);

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
        String str = newProg.substring(subFormLocIndex, locNextParen(newProg, subFormLocIndex + 1) + 1);
        newProg = newProg.replace(str, String.valueOf(result));
    }

    private static void processFile(String inputFilePath, String outputFilePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(inputFilePath)));
        Simplifier simplifier = new Simplifier();
        String simplifiedContent = simplifier.simplify(content);
        Files.write(Paths.get(outputFilePath), simplifiedContent.getBytes(), StandardOpenOption.CREATE);
    }

    public static void main(String[] args) {
        String[] files = {
                "problem_1a.txt", "problem_1b.txt", "problem_1c.txt", "problem_1d.txt",
                "problem_2a.txt", "problem_2b.txt", "problem_2c.txt", "problem_2d.txt",
                "problem_3a.txt", "problem_3b.txt", "problem_3c.txt", "problem_3d.txt",
                "problem_4a.txt", "problem_4b.txt", "problem_4c.txt", "problem_4d.txt",
                "problem_5a.txt", "problem_5b.txt", "problem_5c.txt", "problem_5d.txt",
                "problem_6a.txt", "problem_6b.txt", "problem_6c.txt", "problem_6d.txt"
        };

        Path outputDir = Paths.get("simplified_solutions");
        if (!Files.exists(outputDir)) {
            try {
                Files.createDirectory(outputDir);
            } catch (IOException e) {
                System.err.println("Failed to create output directory: " + e.getMessage());
                return;
            }
        }

        try {
            for (String fileName : files) {
                String inputFilePath = fileName;
                String outputFilePath = outputDir + "/simplified_" + fileName;
                processFile(inputFilePath, outputFilePath);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while processing the files: " + e.getMessage());
        }
    }
}
