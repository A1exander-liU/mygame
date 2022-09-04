package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObjects;
import com.mygdx.game.utils.map.GameMapProperties;
import com.mygdx.game.utils.map.MapObjectDrawer;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Camera;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.Player;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.utils.map.EntityTextureObject;

public class MapUpdateSystem extends EntitySystem {
    ComponentGrabber cg;
    ImmutableArray<Entity> characters;
    ImmutableArray<Entity> itemDrops;
    Entity player;

    MapObjectDrawer tiledMapRenderer;

    public MapUpdateSystem(ComponentGrabber cg, MapObjectDrawer tiledMapRenderer) {
        super(96);
        this.cg = cg;
        this.tiledMapRenderer = tiledMapRenderer;
        characters = MyGame.engine.getEntitiesFor(Family.one(Player.class, Enemy.class).get());
        itemDrops = MyGame.engine.getEntitiesFor(Families.itemDrops);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < characters.size(); i++) {
            Entity entity = characters.get(i);
            updateEntityInMap(entity, GameMapProperties.COLLISIONS);
        }
        for (int i = 0; i < itemDrops.size(); i++) {
            Entity itemDrop = itemDrops.get(i);
            updateEntityInMap(itemDrop, GameMapProperties.ENEMY_DROPS);
        }
        updatePlayerCamPosition();
        tiledMapRenderer.setView(cg.getCamera(player).camera);
        tiledMapRenderer.render();
    }

    private void updateEntityInMap(Entity entity, String mapLayer) {
        Position pos = cg.getPosition(entity);
        EntityTextureObject textureObject = findSameOwner(entity, mapLayer);
        if (textureObject != null) {
            textureObject.setX(pos.x);
            textureObject.setY(pos.y);
        }
    }

    private EntityTextureObject findSameOwner(Entity entity, String mapLayer) {
        MapObjects layer = MyGame.gameMapProperties.getMapLayer(mapLayer).getObjects();
        // going through each item in collision layer
        // check if it is EntityTextureMap object
        // check if the owner of it is same as the owner we are trying to find the texture for
        for (int i = 0; i < layer.getCount(); i++) {
            // EntityTextureObject is a character
            if (layer.get(i) instanceof EntityTextureObject) {
                EntityTextureObject textureObject = (EntityTextureObject) layer.get(i);
                if (textureObject.getOwner() == entity)
                    return textureObject;
            }
        }
        return null;
    }

    private void updatePlayerCamPosition() {
        Camera camera = cg.getCamera(player);
        Position pos = cg.getPosition(player);
        camera.camera.update();
        camera.camera.position.x = pos.x;
        camera.camera.position.y = pos.y;
    }
}
