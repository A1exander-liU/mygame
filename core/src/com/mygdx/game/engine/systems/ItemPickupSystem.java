package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.utils.EntityTextureObject;

public class ItemPickupSystem extends EntitySystem {

    Entity player;
    MapObjects enemyDrops;

    public ItemPickupSystem() {
        super(96);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        enemyDrops = MyGame.gameMapProperties.getMapLayer(GameMapProperties.ENEMY_DROPS).getObjects();
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemyDrops.getCount(); i++) {
            EntityTextureObject textureObject = (EntityTextureObject) enemyDrops.get(i);
            if (overItemDrop(textureObject)) System.out.println("pickup");
        }
    }

    private boolean overItemDrop(EntityTextureObject itemDrop) {
        Position playerPos = Mappers.position.get(player);
        Size playerSize = Mappers.size.get(player);
        Rectangle playerRect = new Rectangle(playerPos.x, playerPos.y, playerSize.width, playerSize.height);
        Rectangle itemDropRect = new Rectangle(itemDrop.getX(), itemDrop.getY(), itemDrop.getTextureRegion().getRegionWidth(), itemDrop.getTextureRegion().getRegionHeight());
        return playerRect.overlaps(itemDropRect);
    }
}
