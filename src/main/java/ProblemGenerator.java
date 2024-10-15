import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProblemGenerator{

    public static void main(String[] args) {
        BigDecimal[] x16Values1 = getValues(new BigDecimal(-10), new BigDecimal(10), 101);
        BigDecimal[] x16Values2 = getValues(new BigDecimal(0), new BigDecimal(100), 101);
        BigDecimal[] x16Values3 = getValues(new BigDecimal(-1), new BigDecimal(1), 101);
        BigDecimal[] x16Values4 = getValues(new BigDecimal(-1000), new BigDecimal(1000), 101);

        BigDecimal[] x25Values1 = getValues(new BigDecimal(-3.14), new BigDecimal(3.14), 101);
        BigDecimal[] x25Values2 = getValues(new BigDecimal(0), new BigDecimal(7), 101);
        BigDecimal[] x25Values3 = getValues(new BigDecimal(0), new BigDecimal(100), 101);
        BigDecimal[] x25Values4 = getValues(new BigDecimal(-100), new BigDecimal(100), 101);

        BigDecimal[] x3Values1 = getValues(new BigDecimal(0), new BigDecimal(4), 101);
        BigDecimal[] x3Values2 = getValues(new BigDecimal(0), new BigDecimal(9), 101);
        BigDecimal[] x3Values3 = getValues(new BigDecimal(0), new BigDecimal(99), 101);
        BigDecimal[] x3Values4 = getValues(new BigDecimal(0), new BigDecimal(999), 101);

        BigDecimal[] x4Values1 = getValues(new BigDecimal(0), new BigDecimal(1), 101);
        BigDecimal[] x4Values2 = getValues(new BigDecimal(-10), new BigDecimal(10), 101);
        BigDecimal[] x4Values3 = getValues(new BigDecimal(0), new BigDecimal(100), 101);
        BigDecimal[] x4Values4 = getValues(new BigDecimal(-1000), new BigDecimal(1000), 101);

        BigDecimal[] y4Values1 = getValues(new BigDecimal(0), new BigDecimal(1), 101);
        BigDecimal[] y4Values2 = getValues(new BigDecimal(-10), new BigDecimal(10), 101);
        BigDecimal[] y4Values3 = getValues(new BigDecimal(0), new BigDecimal(100), 101);
        BigDecimal[] y4Values4 = getValues(new BigDecimal(-1000), new BigDecimal(1000), 101);

        BigDecimal[] y5Values1 = getValues(new BigDecimal(-3.14), new BigDecimal(3.14), 101);
        BigDecimal[] y5Values2 = getValues(new BigDecimal(0), new BigDecimal(7), 101);
        BigDecimal[] y5Values3 = getValues(new BigDecimal(0), new BigDecimal(100), 101);
        BigDecimal[] y5Values4 = getValues(new BigDecimal(-100), new BigDecimal(100), 101);

        BigDecimal[] y6Values1 = getValues(new BigDecimal(-10), new BigDecimal(10), 11);
        BigDecimal[] y6Values2 = getValues(new BigDecimal(0), new BigDecimal(100), 11);
        BigDecimal[] y6Values3 = getValues(new BigDecimal(-1), new BigDecimal(1), 11);
        BigDecimal[] y6Values4 = getValues(new BigDecimal(-1000), new BigDecimal(1000), 11);


        List<BigDecimal> fx1Values1 = f1(x16Values1);
        List<BigDecimal> fx1Values2 = f1(x16Values2);
        List<BigDecimal> fx1Values3 = f1(x16Values3);
        List<BigDecimal> fx1Values4 = f1(x16Values4);
        List<BigDecimal> fx2Values1 = f2(x25Values1);
        List<BigDecimal> fx2Values2 = f2(x25Values2);
        List<BigDecimal> fx2Values3 = f2(x25Values3);
        List<BigDecimal> fx2Values4 = f2(x25Values4);
        List<BigDecimal> fx3Values1 = f3(x3Values1);
        List<BigDecimal> fx3Values2 = f3(x3Values2);
        List<BigDecimal> fx3Values3 = f3(x3Values3);
        List<BigDecimal> fx3Values4 = f3(x3Values4);

        writeDatFileWith1Arg("problem_1a.dat", x16Values1, fx1Values1);
        writeDatFileWith1Arg("problem_1b.dat", x16Values2, fx1Values2);
        writeDatFileWith1Arg("problem_1c.dat", x16Values3, fx1Values3);
        writeDatFileWith1Arg("problem_1d.dat", x16Values4, fx1Values4);
        writeDatFileWith1Arg("problem_2a.dat", x25Values1, fx2Values1);
        writeDatFileWith1Arg("problem_2b.dat", x25Values2, fx2Values2);
        writeDatFileWith1Arg("problem_2c.dat", x25Values3, fx2Values3);
        writeDatFileWith1Arg("problem_2d.dat", x25Values4, fx2Values4);
        writeDatFileWith1Arg("problem_3a.dat", x3Values1, fx3Values1);
        writeDatFileWith1Arg("problem_3b.dat", x3Values2, fx3Values2);
        writeDatFileWith1Arg("problem_3c.dat", x3Values3, fx3Values3);
        writeDatFileWith1Arg("problem_3d.dat", x3Values4, fx3Values4);


        List<BigDecimal[]> xy4Product1 = cartesianProduct(x4Values1, y4Values1);
        List<BigDecimal[]> xy4Product2 = cartesianProduct(x4Values2, y4Values2);
        List<BigDecimal[]> xy4Product3 = cartesianProduct(x4Values3, y4Values3);
        List<BigDecimal[]> xy4Product4 = cartesianProduct(x4Values4, y4Values4);
        List<BigDecimal[]> xy5Product1 = cartesianProduct(x25Values1, y5Values1);
        List<BigDecimal[]> xy5Product2 = cartesianProduct(x25Values2, y5Values2);
        List<BigDecimal[]> xy5Product3 = cartesianProduct(x25Values3, y5Values3);
        List<BigDecimal[]> xy5Product4 = cartesianProduct(x25Values4, y5Values4);
        List<BigDecimal[]> xy6Product1 = cartesianProduct(x16Values1, y6Values1);
        List<BigDecimal[]> xy6Product2 = cartesianProduct(x16Values2, y6Values2);
        List<BigDecimal[]> xy6Product3 = cartesianProduct(x16Values3, y6Values3);
        List<BigDecimal[]> xy6Product4 = cartesianProduct(x16Values4, y6Values4);

        List<BigDecimal> f4Values1 = f4(xy4Product1);
        List<BigDecimal> f4Values2 = f4(xy4Product2);
        List<BigDecimal> f4Values3 = f4(xy4Product3);
        List<BigDecimal> f4Values4 = f4(xy4Product4);
        List<BigDecimal> f5Values1 = f5(xy5Product1);
        List<BigDecimal> f5Values2 = f5(xy5Product2);
        List<BigDecimal> f5Values3 = f5(xy5Product3);
        List<BigDecimal> f5Values4 = f5(xy5Product4);
        List<BigDecimal> f6Values1 = f6(xy6Product1);
        List<BigDecimal> f6Values2 = f6(xy6Product2);
        List<BigDecimal> f6Values3 = f6(xy6Product3);
        List<BigDecimal> f6Values4 = f6(xy6Product4);

        writeDatFileWith2Arg("problem_4a.dat", xy4Product1, f4Values1);
        writeDatFileWith2Arg("problem_4b.dat", xy4Product2, f4Values2);
        writeDatFileWith2Arg("problem_4c.dat", xy4Product3, f4Values3);
        writeDatFileWith2Arg("problem_4d.dat", xy4Product4, f4Values4);
        writeDatFileWith2Arg("problem_5a.dat", xy5Product1, f5Values1);
        writeDatFileWith2Arg("problem_5b.dat", xy5Product2, f5Values2);
        writeDatFileWith2Arg("problem_5c.dat", xy5Product3, f5Values3);
        writeDatFileWith2Arg("problem_5d.dat", xy5Product4, f5Values4);
        writeDatFileWith2Arg("problem_6a.dat", xy6Product1, f6Values1);
        writeDatFileWith2Arg("problem_6b.dat", xy6Product2, f6Values2);
        writeDatFileWith2Arg("problem_6c.dat", xy6Product3, f6Values3);
        writeDatFileWith2Arg("problem_6d.dat", xy6Product4, f6Values4);
    }

    public static List<BigDecimal> f1(BigDecimal[] xVals) {
        List<BigDecimal> results = new ArrayList<>();
        for (BigDecimal x : xVals) {
            results.add(BigDecimal.valueOf(5).multiply(x.pow(3))
                    .subtract(BigDecimal.valueOf(2).multiply(x.pow(2)))
                    .add(BigDecimal.valueOf(3).multiply(x))
                    .subtract(BigDecimal.valueOf(17)));
        }
        return results;
    }

    public static List<BigDecimal> f2(BigDecimal[] xVals) {
        List<BigDecimal> results = new ArrayList<>();
        for (BigDecimal x : xVals) {
            results.add(BigDecimal.valueOf(Math.sin(x.doubleValue()))
                    .add(BigDecimal.valueOf(Math.cos(x.doubleValue()))));
        }
        return results;
    }
    public static List<BigDecimal> f3(BigDecimal[] xVals) {
        List<BigDecimal> results = new ArrayList<>();
        for (BigDecimal x : xVals) {
            BigDecimal xPlusOne = x.add(BigDecimal.ONE);
            double logValue = Math.log(xPlusOne.doubleValue());
            results.add(BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(logValue)));
        }
        return results;
    }

    public static List<BigDecimal> f4(List<BigDecimal[]> xy) {
        List<BigDecimal> results = new ArrayList<>();
        for (BigDecimal[] pair : xy) {
            results.add(pair[0].add(BigDecimal.valueOf(2).multiply(pair[1])));
        }
        return results;
    }

    public static List<BigDecimal> f5(List<BigDecimal[]> xy) {
        List<BigDecimal> results = new ArrayList<>();
        for (BigDecimal[] pair : xy) {
            results.add(BigDecimal.valueOf(Math.sin(pair[0].divide(BigDecimal.valueOf(2)).doubleValue()))
                    .add(BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(Math.cos(pair[0].doubleValue())))));
        }
        return results;
    }

    public static List<BigDecimal> f6(List<BigDecimal[]> xy) {
        List<BigDecimal> results = new ArrayList<>();
        for (BigDecimal[] pair : xy) {
            results.add(pair[0].pow(2)
                    .add(BigDecimal.valueOf(3).multiply(pair[0]).multiply(pair[1]))
                    .add(BigDecimal.valueOf(7).multiply(pair[1]))
                    .add(BigDecimal.ONE));
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

    public static void writeDatFileWith1Arg(String filename, BigDecimal[] xVals, List<BigDecimal> funVals) {
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

    public static void writeDatFileWith2Arg(String filename, List<BigDecimal[]> xyVals, List<BigDecimal> funVals) {
        List<Integer> header = Arrays.asList(2, 100, -20, 20, funVals.size());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(String.join(" ", header.stream().map(String::valueOf).toArray(String[]::new)) + "\n");
            for (int i = 0; i < xyVals.size(); i++) {
                writer.write(xyVals.get(i)[0] + " " + xyVals.get(i)[1] + " " + funVals.get(i) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<BigDecimal[]> cartesianProduct(BigDecimal[] x, BigDecimal[] y) {
        List<BigDecimal[]> product = new ArrayList<>();
        for (BigDecimal xVal : x) {
            for (BigDecimal yVal : y) {
                product.add(new BigDecimal[]{xVal, yVal});
            }
        }
        return product;
    }
}
