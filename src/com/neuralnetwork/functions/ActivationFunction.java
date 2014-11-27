package com.neuralnetwork.functions;

public interface ActivationFunction {

    public double value(double arg);

    public double derivative(double arg);
}