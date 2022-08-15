package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;

public class InventoryRenderSystem extends EntitySystem {
    ComponentGrabber cg;
    Entity player;
    Stage stage;

    public InventoryRenderSystem(ComponentGrabber cg) {
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void update(float delta) {
        stage.clear();

        Table root = new Table();
        root.setSize(stage.getWidth(), stage.getHeight());

        Table inventory = new Table();
        inventory.setDebug(true);
        root.add(inventory);

        stage.act();
        stage.draw();
    }
}
