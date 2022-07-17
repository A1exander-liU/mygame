package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;

public class IDComponent implements Component {
    public static int totalEntities = 0;
    public int ID = ++totalEntities;
}
