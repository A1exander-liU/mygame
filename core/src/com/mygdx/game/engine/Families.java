package com.mygdx.game.engine;

import com.badlogic.ashley.core.Family;
import com.mygdx.game.engine.components.CollidableComponent;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.inventory.items.shared.InventoryItemComponent;
import com.mygdx.game.engine.components.ObstacleComponent;
import com.mygdx.game.engine.components.Player;
import com.mygdx.game.engine.components.SpawnArea;

public class Families {

    public static final Family enemies = Family.all(Enemy.class).get();
    public static final Family player = Family.all(Player.class).get();
    public static final Family obstacles = Family.all(ObstacleComponent.class).get();
    public static final Family spawns = Family.all(SpawnArea.class).get();
    public static final Family collisions = Family.all(CollidableComponent.class).get();
    public static final Family characters = Family.one(Player.class, Enemy.class).get();
    public static final Family items = Family.all(InventoryItemComponent.class).get();

    public Families() {}
    
}
