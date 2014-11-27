package com.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import com.neuralnetwork.functions.*;

public class Neuron {

	private final double biasValue;
    private double output, sumIW;
    private ActivationFunction activationFunction;
    private List<Double> input, weights;

    public Neuron(double biasValue, ActivationFunction af) {
    	this.biasValue = biasValue;
        this.activationFunction = af;
    }

    public void setInputFirst(Double input) {
        this.input = new ArrayList<Double>();
        this.input.add(input);
    }
    
    public void setInput(List<Double> input) {
        this.input = new ArrayList<Double>(input);
        this.input.add(0, this.biasValue);
    }

    public void setWeights(List<Double> weights) {
        this.weights = new ArrayList<Double>(weights);
    }

    public void computeOutput() {
        this.sumIW = 0.0;
        for (int i = 0; i < this.input.size(); i++) {
            this.sumIW += this.input.get(i) * this.weights.get(i);
        }
        this.output = this.activationFunction.value(this.sumIW);
    }
    
    public double getOutput() {
        return output;
    }
    
    public void passOutput() {
        this.output = this.input.get(0);
    }
}
