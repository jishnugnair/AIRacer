package org.jishnu.v2.nn;

public class NNConstants {
    public static final double epsilon = 0.01;

    public static final Activation RELU = (x) -> {
        return (x < 0 ? 0 : x);
    };
    public static final Activation SIGMOID = (x) -> {
        return 1 / (1 + Math.exp(-x));
    };
}
