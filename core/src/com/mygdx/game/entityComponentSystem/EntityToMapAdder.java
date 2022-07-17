package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.entityComponentSystem.components.Sprite;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;

public class EntityToMapAdder {
//    public static int totalEntities = 0;
    TiledMap tiledMap;
    ComponentGrabber cg;

    public EntityToMapAdder(TiledMap tiledMap, ComponentGrabber cg) {
        this.tiledMap = tiledMap;
        this.cg = cg;
    }

    public void addEntityToMap(TiledMap tiledMap, Entity entity) {
        Sprite entitySprite = cg.getSprite(entity);
        Size size = cg.getSize(entity);
        Position pos = cg.getPosition(entity);
        ID id = cg.getID(entity);
//        entity.getComponent(ID.class).
//        id.ID = ++totalEntities;
        System.out.println(entitySprite.texture);
        System.out.println(size.width + " " + size.height);
        System.out.println("(" + pos.x + ", " + pos.y + ")");
        System.out.println(id.ID);
        TextureRegion textureRegion = new TextureRegion(entitySprite.texture, size.width, size.height);
        TextureMapObject textureMapObject = new TextureMapObject(textureRegion);
        textureMapObject.setName("" + id.ID);
        textureMapObject.setX(pos.x);
        textureMapObject.setY(pos.y);
        tiledMap.getLayers().get("Object Layer 1").getObjects().add(textureMapObject);
    }

    public void addEntitiesToMap(TiledMap tiledMap, ImmutableArray<Entity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            addEntityToMap(tiledMap, entity);
        }
    }
}
