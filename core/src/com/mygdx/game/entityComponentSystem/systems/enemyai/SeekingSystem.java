package com.mygdx.game.entityComponentSystem.systems.enemyai;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.Families;

public class SeekingSystem extends EntitySystem {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> enemies;

    public SeekingSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(4);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        enemies = root.engine.getEntitiesFor(Families.enemies);
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

    @Override
    public void update(float delta) {

    }


}
