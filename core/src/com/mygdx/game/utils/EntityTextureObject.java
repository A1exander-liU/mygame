package com.mygdx.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;

public class EntityTextureObject extends TextureMapObject {
    private Entity owner;

    public EntityTextureObject(TextureRegion textureRegion) {
        super(textureRegion);
    }

    public EntityTextureObject() {}

    public Entity getOwner() {
        return owner;
    }

    public void setOwner(Entity entity) {
        owner = entity;
    }
}
