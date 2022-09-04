package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;

public class WeaponBaseStatComponent implements Component {
    public int minDmg;
    public int maxDmg;
    public float attackDelay;

    public WeaponBaseStatComponent() {}

    public WeaponBaseStatComponent(int minDmg, int maxDmg, float attackDelay) {
        this.minDmg = minDmg;
        this.maxDmg = maxDmg;
        this.attackDelay = attackDelay;
    }
}
