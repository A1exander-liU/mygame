package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;

public class Name implements Component {
    public String name;

    public Name() {}

    public Name(String name) {
        this.name = name;
    }
}
