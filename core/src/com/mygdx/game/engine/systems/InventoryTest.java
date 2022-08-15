package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.InventoryComponent;

public class InventoryTest extends EntitySystem {
    ComponentGrabber cg;
    Entity player;

    public InventoryTest(ComponentGrabber cg) {
        super(101);
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);

    }

    @Override
    public void update(float delta) {
        toggleInventory();
    }

    private void toggleInventory() {
        InventoryComponent inventory = cg.getInventory(player);
        if (!inventory.opened && Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            inventory.opened = true;
            System.out.println("opened inventory");
        }
        else if (inventory.opened && Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            inventory.opened = false;
            System.out.println("closed inventory");
        }
    }
}
