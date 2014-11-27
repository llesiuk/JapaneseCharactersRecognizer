package com.neuralnetwork;

import java.util.ArrayList;
import java.util.List;
import com.neuralnetwork.functions.*;

public class Layer {

    private final List<Neuron> nuerons;
    private final int quantity;
    private List<Double> completeOutput;

    public Layer(int quantity, double biasValue, ActivationFunction af) {
        this.quantity = quantity;
        this.nuerons = new ArrayList<Neuron>();
        for (int i = 0; i < this.quantity; i++) {
            this.nuerons.add(new Neuron(biasValue, af));
        }
        this.completeOutput = new ArrayList<Double>();
    }

    public void setInputFirst(List<Double> input) {
        for (int i = 0; i < input.size(); i++) {
            this.nuerons.get(i).setInputFirst(input.get(i));
        }
    }
    
    public void setInput(List<Double> input) {
        for (Neuron current : this.nuerons) {
            current.setInput(input);
        }
    }
    
    public void computeOutput() {
        this.completeOutput = new ArrayList<Double>();
        for (int i = 0; i < this.quantity; i++) {
            this.nuerons.get(i).computeOutput();
            this.completeOutput.add(i, this.nuerons.get(i).getOutput());
        }
    }
    
    public void passOutput() {
        this.completeOutput = new ArrayList<Double>();
        for (int i = 0; i < this.quantity; i++) {
            this.nuerons.get(i).passOutput();
            this.completeOutput.add(i, this.nuerons.get(i).getOutput());
        }
    }

    public List<Double> getCompleteOutput() {
        return completeOutput;
    }

    public List<Neuron> getNuerons() {
        return nuerons;
    }

}
