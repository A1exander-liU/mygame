package com.mygdx.game.entityComponentSystem.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObjects;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.Families;

public class SpawnZoneDetectionSystem extends EntitySystem {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;
    Entity player;
    MapObjects spawnZones;

    public SpawnZoneDetectionSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(5);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        player = root.engine.getEntitiesFor(Families.player).get(0);
        spawnZones = gameMapProperties.tiledMap.getLayers().get("Enemy Spawns").getObjects();
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

    @Override
    public void update(float delta) {

    }
    
}
