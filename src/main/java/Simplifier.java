import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Simplifier {
    private String orgProg;
    private String newProg;
    private int   subFormLocIndex;
    private double lfVal;
    private String ctFunction;
    private double rtVal;
    private double result;
    private int initialSubFormulaCount;
    private int programSizeReduction;


    public Simplifier() { } // constructor

    public String simplify(String program) {



        orgProg = program;



        newProg = orgProg.substring(1, orgProg.length()-1); // strip-off outlying final parentheses.



        this.initialSubFormulaCount = countSimplifiableSubFormulasInNewProgram();



        while ( countSimplifiableSubFormulasInNewProgram() > 0 ) {  // while there are any sub formulas left...

            locateAvailableSubFormula();

            pullAllSubformulaTokens();

            calcResultOfSubformula();

            replaceSubFormulaWithItsResult();

        }



        newProg = "("+newProg+")"; // add in outlying final parentheses again.



        return newProg;

    }

    public int countSimplifiableSubFormulasInNewProgram() {



        int iFirstParen;

        int iSecondParen;

        int count = 0;



        for (int i = 0; i < this.newProg.length(); i++) { // forloop through program string,

            iFirstParen = locNextParen(newProg,i);        // find index of next parenthesis starting from i

            if (iFirstParen == -1) break;                     // if no next par's left,  break to return count.

            iSecondParen = locNextParen(newProg,iFirstParen+1);    // find index of next parenthesis starting from i+1

            if (iSecondParen == -1) break;                    // if no next par's left,  break to return count.

            if ( isSimplifiableSubFormula(iFirstParen, iSecondParen) ) {

                count++;      // count it

                i  = iSecondParen; // prepare to move past 2nd par to efficiently keep looking

            }

            else {

                i  = iSecondParen-1; // prepare to start at 2nd par to efficiently keep looking

            }

        } // next loop



        return count;

    }

    public int locateAvailableSubFormula() {



        int iFirstParen;

        int iSecondParen;



        for (int i = 0; i < this.newProg.length(); i++) { // for loop moves through program string

            iFirstParen = locNextParen(newProg,i);        // find index of next parenthesis starting from i

            if (iFirstParen == -1) break;                     // if no next par's left,  break to return -1

            iSecondParen = locNextParen(newProg,iFirstParen+1);    // find index of next parenthesis starting from i+1

            if (iSecondParen == -1) break;                    // if no next par's left,  break to return count.

            if ( isSimplifiableSubFormula(iFirstParen, iSecondParen) ) { // can this subformula be simplified?

                subFormLocIndex = iFirstParen;              // yes, so note where that subformula starts

                return subFormLocIndex;                         // since subformula found, return location of first parenthesis

            }

            i  = iSecondParen-1;                                 // no, so reset loop to where next parenthesis was found for more checking.

        } // next loop



        subFormLocIndex =-1; // to indicate nothing found.

        return subFormLocIndex;

    }

    public boolean isSimplifiableSubFormula( int iFirstParen, int iSecondParen) {



        boolean isOpenParFound = ( newProg.charAt(iFirstParen) == '(' );

        boolean isCloseParFound = ( newProg.charAt(iSecondParen) == ')' );

        boolean isFreeOfVars = true;



        for (int i = iFirstParen; i < iSecondParen; i++) { // search for X's

            if (newProg.charAt(i) == 'X') { // if you see any

                isFreeOfVars = false;         // then this subformula can't be simplifed.

                break;

            }

        }



        return (isOpenParFound && isCloseParFound && isFreeOfVars);

    }

    public int locNextParen(String str, int i) {



        String ss = "";

        for (int j = i; j < str.length(); j++) {      // move thru the string from wherever

            ss = str.substring(j, j+1);                      // get a single character

            if ( ss.equals(")") ) return j;             // return if it's a par

            if ( ss.equals("(") ) return j;             // of either type

        }



        return -1; // no par's found.

    }

    public void pullAllSubformulaTokens() {



        String str = newProg.substring(subFormLocIndex, locNextParen(newProg,subFormLocIndex+1)+1 ); // get subformula

        String[] tokens = str.split("[ ]+");               // split up into three parts around the spaces



        tokens[0] = tokens[0].substring(1);   // drop "("

        tokens[2] = tokens[2].substring(0,tokens[2].length()-1);  // drop ")"



        lfVal         = Double.parseDouble(tokens[0]);

        ctFunction     = tokens[1];

        rtVal         = Double.parseDouble(tokens[2]);

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

                throw new IllegalArgumentException("Invalid function character found:" + ctFunction);

        }

    }

    public void replaceSubFormulaWithItsResult() {



        String str = newProg.substring(subFormLocIndex, locNextParen(newProg,subFormLocIndex+1)+1 ); // get subformula

        newProg = newProg.replace(str, String.valueOf(result)); // replace subformula with its result.

    }

    public String getOrgProg() {

        return orgProg;

    }


    public void setOrgProg(String orgProg) {

        this.orgProg = orgProg;

    }


    public String getNewProg() {

        return newProg;

    }


    public void setNewProg(String newProg) {

        this.newProg = newProg;

    }


    public int getSubFormLocIndex() {

        return subFormLocIndex;

    }


    public void setSubFormLocIndex(int subFormLocIndex) {

        this.subFormLocIndex = subFormLocIndex;

    }


    public double getLfVal() {

        return lfVal;

    }


    public void setLfVal(double lfVal) {

        this.lfVal = lfVal;

    }


    public String getCtFunction() {

        return ctFunction;

    }


    public void setCtFunction(String ctFunction) {

        this.ctFunction = ctFunction;

    }


    public double getRtVal() {

        return rtVal;

    }


    public void setRtVal(double rtVal) {

        this.rtVal = rtVal;

    }


    public double getResult() {

        return result;

    }


    public void setResult(double result) {

        this.result = result;

    }


    public int getInitialSubFormulaCount() {

        return initialSubFormulaCount;

    }


    public void setInitialSubFormulaCount(int initialSubFormulaCount) {

        this.initialSubFormulaCount = initialSubFormulaCount;

    }




    public int getProgramSizeReduction() {

        return programSizeReduction;

    }


    public void setProgramSizeReduction(int programSizeReduction) {

        this.programSizeReduction = programSizeReduction;

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
        try {
            for (int i = 1; i <= 24; i++) {
                String inputFilePath = "plik" + i + ".txt";
                String outputFilePath = "uproszczone" + i + ".txt";

                processFile(inputFilePath, outputFilePath);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while processing the files: " + e.getMessage());
        }
    }


}
