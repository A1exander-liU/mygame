package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.Player;

public class MapUpdateSystem extends EntitySystem {
    ComponentGrabber cg;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> characters;

    public MapUpdateSystem(ComponentGrabber cg, GameMapProperties gameMapProperties) {
        this.cg = cg;
        this.gameMapProperties = gameMapProperties;
        characters = MyGame.engine.getEntitiesFor(Family.one(Player.class, Enemy.class).get());
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < characters.size(); i++) {
            Entity entity = characters.get(i);
        }
    }
}
