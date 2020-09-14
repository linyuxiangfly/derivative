package com.firefly.layers.models;

import com.firefly.layers.core.Layer;
import com.firefly.layers.core.Model;
import com.firefly.layers.core.Loss;

import java.util.ArrayList;
import java.util.List;

public class Sequential implements Model {
    List<Layer> layers;
    Loss loss;

    public Sequential(){
        layers=new ArrayList<>();
    }

    @Override
    public void add(Layer layer) {
        layers.add(layer);
    }

    @Override
    public void setLoss(Loss loss) {
        this.loss=loss;
    }

    @Override
    public void evaluate(double[] x, double[] y, int batchSize) {

    }

    @Override
    public void predict(double x, int batchSize) {

    }

    @Override
    public void fit() {

    }
}
