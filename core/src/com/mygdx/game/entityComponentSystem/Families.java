package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.Family;
import com.mygdx.game.entityComponentSystem.components.Enemy;
import com.mygdx.game.entityComponentSystem.components.Player;

public class Families {

    public static final Family enemies = Family.all(Enemy.class).get();
    public static final Family player = Family.all(Player.class).get();

    public Families() {}


}