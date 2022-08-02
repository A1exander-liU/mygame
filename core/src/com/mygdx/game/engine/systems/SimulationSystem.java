package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.dongbat.jbump.World;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;

public class SimulationSystem extends EntitySystem {
    ComponentGrabber cg;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> collisions;
    ImmutableArray<Entity> enemies;
    Entity player;
    World<Entity> world;

    public SimulationSystem(ComponentGrabber cg, GameMapProperties gameMapProperties) {
        this.cg = cg;
        this.gameMapProperties = gameMapProperties;
        collisions = MyGame.engine.getEntitiesFor(Families.collisions);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        world = new World<>();
    }

    @Override
    public void update(float delta) {
    }
}
