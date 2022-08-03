package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObjects;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.Player;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.utils.EntityTextureObject;

public class MapUpdateSystem extends EntitySystem {
    ComponentGrabber cg;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> characters;

    public MapUpdateSystem(ComponentGrabber cg, GameMapProperties gameMapProperties) {
        this.cg = cg;
        this.gameMapProperties = gameMapProperties;
        characters = MyGame.engine.getEntitiesFor(Family.one(Player.class, Enemy.class).get());
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < characters.size(); i++) {
            Entity entity = characters.get(i);
            updateEntityInMap(entity);
        }
    }

    private void updateEntityInMap(Entity entity) {
        Position pos = cg.getPosition(entity);
        EntityTextureObject textureObject = findSameOwner(entity);
        if (textureObject != null) {
            textureObject.setX(pos.x);
            textureObject.setY(pos.y);
        }
    }

    private EntityTextureObject findSameOwner(Entity entity) {
        MapObjects collisions = gameMapProperties.getMapLayer(GameMapProperties.COLLISIONS).getObjects();
        // going through each item in collision layer
        // check if it is EntityTextureMap object
        // check if the owner of it is same as the owner we are trying to find the texture for
        for (int i = 0; i < collisions.getCount(); i++) {
            // EntityTextureObject is a character
            if (collisions.get(i) instanceof EntityTextureObject) {
                EntityTextureObject textureObject = (EntityTextureObject) collisions.get(i);
                if (textureObject.getOwner() == entity)
                    return textureObject;
            }
        }
        return null;
    }
}
