package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.entityComponentSystem.components.SpriteComponent;
import com.mygdx.game.entityComponentSystem.components.IDComponent;
import com.mygdx.game.entityComponentSystem.components.PositionComponent;
import com.mygdx.game.entityComponentSystem.components.SizeComponent;

public class EntityToMapAdder {
//    public static int totalEntities = 0;
    TiledMap tiledMap;
    ComponentGrabber cg;

    public EntityToMapAdder(TiledMap tiledMap, ComponentGrabber cg) {
        this.tiledMap = tiledMap;
        this.cg = cg;
    }

    public void addEntityToMap(TiledMap tiledMap, Entity entity) {
        SpriteComponent spriteComponent = cg.getSprite(entity);
        SizeComponent sizeComponent = cg.getSize(entity);
        PositionComponent pos = cg.getPosition(entity);
        IDComponent idComponent = cg.getID(entity);
//        entity.getComponent(ID.class).
//        id.ID = ++totalEntities;
        System.out.println(spriteComponent.texture);
        System.out.println(sizeComponent.width + " " + sizeComponent.height);
        System.out.println("(" + pos.x + ", " + pos.y + ")");
        System.out.println(idComponent.ID);
        TextureRegion textureRegion = new TextureRegion(spriteComponent.texture, sizeComponent.width, sizeComponent.height);
        TextureMapObject textureMapObject = new TextureMapObject(textureRegion);
        textureMapObject.setName("" + idComponent.ID);
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
