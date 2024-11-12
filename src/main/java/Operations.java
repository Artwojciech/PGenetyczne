public class Operations {
    public static double run() {
        if (Tiny.PC >= Tiny.program.length) {
            return 0.0; // Zabezpieczenie przed przekroczeniem długości programu
        }
        char primitive = Tiny.program[Tiny.PC++];
        if (primitive < Tiny.FSET_START)
            return (Tiny.x[primitive]);
        switch (primitive) {
            case Tiny.ADD:
                return (run() + run());
            case Tiny.SUB:
                return (run() - run());
            case Tiny.MUL:
                return (run() * run());
            case Tiny.SIN:
                return Math.sin(run());
            case Tiny.COS:
                return Math.cos(run());
            case Tiny.DIV: {
                double num = run(), den = run();
                if (Math.abs(den) <= 0.001)
                    return (num);
                else
                    return (num / den);
            }
        }
        return (0.0); // Nigdy tu nie dotrze
    }

    public static int traverse(char[] buffer, int buffercount) {
        if (buffercount >= buffer.length) {
            return buffercount;
        }

        if (buffer[buffercount] < Tiny.FSET_START)
            return (++buffercount);

        switch (buffer[buffercount]) {
            case Tiny.ADD:
            case Tiny.SUB:
            case Tiny.MUL:
            case Tiny.DIV:
                int left = traverse(buffer, ++buffercount);
                return traverse(buffer, left);
            case Tiny.SIN:
            case Tiny.COS:
                return traverse(buffer, ++buffercount);
        }
        return (++buffercount); // Dla nieznanych operatorów
    }
}
