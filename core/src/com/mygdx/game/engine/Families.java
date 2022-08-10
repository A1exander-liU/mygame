package com.mygdx.game.engine;

import com.badlogic.ashley.core.Family;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.Player;
import com.mygdx.game.engine.components.SpawnArea;

public class Families {

    public static final Family enemies = Family.all(Enemy.class).get();
    public static final Family player = Family.all(Player.class).get();
    public static final Family obstacles = Family.exclude(Enemy.class, Player.class, SpawnArea.class).get();
    public static final Family spawns = Family.all(SpawnArea.class).get();
    public static final Family collisions = Family.exclude(SpawnArea.class).get();
    public static final Family characters = Family.one(Player.class, Enemy.class).get();

    public Families() {}
    
}
