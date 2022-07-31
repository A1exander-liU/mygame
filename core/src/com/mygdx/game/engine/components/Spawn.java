package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;

public class Spawn implements Component {
    // so enemy can travel back to their spawn area after certain conditions
    public float spawnPosX;
    public float spawnPosY;
}
