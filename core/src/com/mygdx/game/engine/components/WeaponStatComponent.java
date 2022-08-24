package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;

public class WeaponStatComponent implements Component {
    public int minDmg;
    public int MaxDmg;
    public float attackDelay;
    public float criticalChance;
    public float criticalDmg;
}
