class Operations {
    static double run() {
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
            case Tiny.DIV: {
                double num = run(), den = run();
                if (Math.abs(den) <= 0.001)
                    return (num);
                else
                    return (num / den);
            }
            case Tiny.SIN:
                return (Math.sin(run()));
            case Tiny.COS:
                return (Math.cos(run()));
        }
        return (0.0); // should never get here
    }

    static int traverse(char[] buffer, int buffercount) {
        if (buffer[buffercount] < Tiny.FSET_START)
            return (++buffercount);

        switch (buffer[buffercount]) {
            case Tiny.ADD:
            case Tiny.SUB:
            case Tiny.MUL:
            case Tiny.DIV:
                return (traverse(buffer, traverse(buffer, ++buffercount)));
            case Tiny.SIN:
            case Tiny.COS:
        }
        return (0); // should never get here
    }
}
