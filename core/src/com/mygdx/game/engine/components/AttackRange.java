package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;

public class AttackRange implements Component {
    public float range;
    // will also serve as the length for the attacks
    // base will be 1 which be adjusted depending on the weapon
    // player can attack in multiple directions so the range of the attack
    // will not extend in one axis
    public float xRange = 1;
    public float yRange = 1;
}
