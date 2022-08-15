package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class InventoryComponent implements Component {
    public Array<Entity> items = new Array<>();
}
