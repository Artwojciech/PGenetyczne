import java.io.BufferedWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenSinAndTan {
    public static void main(String[] args) {
        BigDecimal[] sinValues = getValues(new BigDecimal(0), new BigDecimal(2*Math.PI), 101);
        BigDecimal[] tanValues = getValues(new BigDecimal((-0.5) * Math.PI), new BigDecimal((0.5) * Math.PI), 101);

        List<BigDecimal> fxsinval = f1(sinValues);
        List<BigDecimal> fxtanval = f2(tanValues);


        writeOneArg("problem_2sin.dat", sinValues, fxsinval);
        writeOneArg("problem_3tan.dat", tanValues, fxtanval);
    }
    private static final MathContext PRECISION = new MathContext(20, RoundingMode.HALF_UP);
    public static List<BigDecimal> f1(BigDecimal[] xVals) {
        List<BigDecimal> results = new ArrayList<>();
        for (BigDecimal x : xVals) {
            BigDecimal piOver2 = new BigDecimal("3.141592").divide(BigDecimal.valueOf(2), PRECISION);
            BigDecimal input = x.add(piOver2, PRECISION);

            double sinValue = Math.sin(input.doubleValue());
            BigDecimal result = new BigDecimal(sinValue, PRECISION);

            results.add(result);
        }
        return results;
    }

    public static List<BigDecimal> f2(BigDecimal[] xVals) {
        List<BigDecimal> results = new ArrayList<>();
        for (BigDecimal x : xVals) {
            BigDecimal twoX = x.multiply(BigDecimal.valueOf(2), PRECISION);
            BigDecimal input = twoX.add(BigDecimal.ONE, PRECISION);

            double tanValue = Math.tan(input.doubleValue());
            BigDecimal result = new BigDecimal(tanValue, PRECISION);

            results.add(result);
        }
        return results;
    }
    public static BigDecimal[] getValues(BigDecimal startVal, BigDecimal stopVal, int n) {
        BigDecimal[] values = new BigDecimal[n];
        BigDecimal step = stopVal.subtract(startVal).divide(BigDecimal.valueOf(n - 1), MathContext.DECIMAL128);
        for (int i = 0; i < n; i++) {
            values[i] = startVal.add(step.multiply(BigDecimal.valueOf(i))).setScale(4, RoundingMode.HALF_UP);
        }
        return values;
    }
    public static void writeOneArg(String filename, BigDecimal[] xVals, List<BigDecimal> funVals) {
        List<Integer> header = Arrays.asList(1, 100, -20, 20, funVals.size());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(String.join(" ", header.stream().map(String::valueOf).toArray(String[]::new)) + "\n");
            for (int i = 0; i < xVals.length; i++) {
                writer.write(xVals[i] + " " + funVals.get(i) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
