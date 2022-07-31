package com.mygdx.game.engine;

import com.badlogic.ashley.core.Family;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.Player;

public class Families {

    public static final Family enemies = Family.all(Enemy.class).get();
    public static final Family player = Family.all(Player.class).get();
    public static final Family obstacles = Family.exclude(Enemy.class, Player.class).get();

    public Families() {}


}
