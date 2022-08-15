package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;

public class InventoryTest extends EntitySystem {
    ComponentGrabber cg;
    Entity player;

    public InventoryTest(ComponentGrabber cg) {
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);

    }

    @Override
    public void update(float delta) {

    }
}
