package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;

public class HealthBarRenderSystem extends EntitySystem {
    ComponentGrabber cg;
    Entity player;
    ImmutableArray<Entity> enemies;
    ImmutableArray<Entity> characters;

    public HealthBarRenderSystem(ComponentGrabber cg) {
        super();
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        characters = MyGame.engine.getEntitiesFor(Families.characters);
    }

    @Override
    public void update(float delta) {

    }
}
