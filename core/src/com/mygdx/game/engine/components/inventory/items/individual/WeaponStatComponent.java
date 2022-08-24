package com.mygdx.game.engine.components.inventory.items.individual;

import com.badlogic.ashley.core.Component;

public class WeaponStatComponent implements Component {
    public int minDmg;
    public int maxDmg;
    public float attackDelay;
    public float critChance;
    public float critDmg;
}
