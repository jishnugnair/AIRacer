package org.jishnu.v2.nn;

public class NNConstants {

    public static final Activation RELU = x -> x < 0 ? 0 : x;
    public static final Activation SIGMOID = x -> 1 / (1 + Math.exp(-x));
    public static final float threshold = 0.4f;
}
