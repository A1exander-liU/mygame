package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;

public class ParameterComponent implements Component {
    public Health health;
    public int damage;

    public ParameterComponent() {
        health = new Health();
    }
}
