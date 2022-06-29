package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;

public class Health implements Component {
    public float maxHealth = 100;
    public float currentHealth = 100;
}
