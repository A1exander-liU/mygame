package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;

public class MapChar extends TextureMapObject implements Component {

    public MapChar() {

    }

    public MapChar(TextureRegion textureRegion) {
        super(textureRegion);
    }
}
