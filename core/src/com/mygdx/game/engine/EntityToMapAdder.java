package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.utils.map.EntityTextureObject;

public class EntityToMapAdder {
    ComponentGrabber cg;

    public EntityToMapAdder(ComponentGrabber cg) {
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
        MyGame.gameMapProperties.getMapLayer(GameMapProperties.COLLISIONS).getObjects().add(textureObject);
    }
}
