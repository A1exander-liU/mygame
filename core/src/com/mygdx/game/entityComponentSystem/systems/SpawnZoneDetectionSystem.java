package com.mygdx.game.entityComponentSystem.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.Families;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;

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
        spawnZones = gameMapProperties.getMapLayer("Enemy Spawns").getObjects();
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

    @Override
    public void update(float delta) {
        Polygon playerArea = getEntityArea(player);
        for (int i = 0; i < spawnZones.getCount(); i++) {
            // spawn zones are all rectangles
            Polygon spawnZone = getEntityArea((RectangleMapObject) spawnZones.get(i));
            boolean crossedSpawnZone = Intersector.overlapConvexPolygons(playerArea, spawnZone);
            if (crossedSpawnZone)
                System.out.println("entered spawn zone");
        }
    }

    public Polygon getEntityArea(Entity entity) {
        Position pos = cg.getPosition(entity);
        Size size = cg.getSize(entity);
        float[] vertices = {
                pos.x, pos.y,
                pos.x, pos.y + size.height,
                pos.x + size.width, pos.y + size.height,
                pos.x + size.width, pos.y
        };
        return new Polygon(vertices);
    }

    public Polygon getEntityArea(RectangleMapObject rectangleMapObject) {
        Rectangle rect = rectangleMapObject.getRectangle();
        float[] vertices = {
                rect.x, rect.y,
                rect.x, rect.y + rect.height,
                rect.x + rect.width, rect.y + rect.height,
                rect.x + rect.width, rect.y
        };
        return new Polygon(vertices);
    }

}
