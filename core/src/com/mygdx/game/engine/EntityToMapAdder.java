package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.utils.EntityTextureObject;

public class EntityToMapAdder {
//    public static int totalEntities = 0;
    TiledMap tiledMap;
    ComponentGrabber cg;

    public EntityToMapAdder(TiledMap tiledMap, ComponentGrabber cg) {
        this.tiledMap = tiledMap;
        this.cg = cg;
    }

    public void addEntityToMap(Entity entity) {
        Sprite entitySprite = cg.getSprite(entity);
        Size size = cg.getSize(entity);
        Position pos = cg.getPosition(entity);
        Name name = cg.getName(entity);
        // creating the texture region
        TextureRegion textureRegion = new TextureRegion(entitySprite.texture, (int)size.width, (int)size.height);
        // use the EntityTextureMap object
        EntityTextureObject textureObject = new EntityTextureObject(textureRegion, entity);
        // setting the attributes
        textureObject.setName(name.name);
        textureObject.setX(pos.x);
        textureObject.setY(pos.y);
        // adding to the map
        tiledMap.getLayers().get("Object Layer 1").getObjects().add(textureObject);
    }

    public void addEntitiesToMap(TiledMap tiledMap, ImmutableArray<Entity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            addEntityToMap(entity);
        }
    }
}
