package com.neuralnetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.neuralnetwork.functions.*;

public class Network {

    private final List<Layer> layers;
    private final List<Integer> quantities;
	
    public Network(Context context) {
        BufferedReader br;
        String line;
        int numberOfLayers;
        this.quantities = new ArrayList<Integer>();
        this.layers = new ArrayList<Layer>();
        try {        	
            br = new BufferedReader(new InputStreamReader(context.getResources().getAssets()
            		.open("network.dat")));
            line = br.readLine();
            numberOfLayers = Integer.parseInt(line);
            for (int i = 0; i < numberOfLayers + 1; i++) {
                line = br.readLine();
                this.quantities.add(Integer.parseInt(line));
                this.layers.add(new Layer(this.quantities.get(i), 1.0, new Sigmoid()));
            }
            for (int i = 1; i < numberOfLayers + 1; i++) {
                for (int j = 0; j < this.quantities.get(i); j++) {
                    line = br.readLine();
                    this.layers.get(i).getNuerons().get(j).setWeights(toList(line));
                }
            }
            br.close();
        } catch (IOException iOException) {
            System.out.println("Problem z odczytem pliku.");
        }
    }
    
    private List<Double> toList(String weights) {
        ArrayList<Double> result = new ArrayList<Double>();
        String split[] = weights.split(" ");
        for (String split1 : split) {
            result.add(Double.parseDouble(split1));
        }
        return result;
    }
    
    public void setInputFirst(List<Double> input) {
        this.layers.get(0).setInputFirst(input);
    }

    public void computeOutput() {
        this.layers.get(0).passOutput();
        for (int i = 1; i < this.quantities.size(); i++) {
            this.layers.get(i).setInput(this.layers.get(i - 1).getCompleteOutput());
            this.layers.get(i).computeOutput();
        }
    }
    
    public List<Double> getNetworkOutput() {
        return this.layers.get(quantities.size() - 1).getCompleteOutput();
    }
    	
}
