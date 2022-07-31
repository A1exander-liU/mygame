package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;

public class ID implements Component {
    public static int totalEntities = 0;
    public int ID = ++totalEntities;
}
