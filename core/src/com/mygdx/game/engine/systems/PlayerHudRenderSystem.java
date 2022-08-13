package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;

public class PlayerHudRenderSystem extends EntitySystem {
    ComponentGrabber cg;
    Entity player;
    Stage playerHud;

    public PlayerHudRenderSystem(ComponentGrabber cg) {
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        playerHud = new Stage(new ScreenViewport());
    }

    @Override
    public void update(float delta) {
        playerHud.clear();
        Table root = new Table();
        root.setSize(playerHud.getWidth(), playerHud.getHeight());

        Table playerHealth = new Table();
        playerHealth.setSize(root.getWidth() / 3, root.getHeight() / 5);
        root.add(playerHealth);
        
        playerHud.act();
        playerHud.draw();
    }

}
