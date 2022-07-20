package com.mygdx.game.entityComponentSystem.systems.enemyai;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.Families;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Steering;

public class SeekingSystem extends EntitySystem {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> enemies;
    Entity player;

    public SeekingSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(4);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        enemies = root.engine.getEntitiesFor(Families.enemies);
        player = root.engine.getEntitiesFor(Families.player).get(0);
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

    @Override
    public void update(float delta) {
    }
}
