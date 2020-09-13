package com.firefly.layers.models;

import com.firefly.layers.core.Layer;
import com.firefly.layers.core.Model;
import com.firefly.layers.loss.Loss;

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
    public void compile(Loss loss) {
        this.loss=loss;
    }
}
