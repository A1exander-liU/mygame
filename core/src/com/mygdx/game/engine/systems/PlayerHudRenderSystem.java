package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;

public class PlayerHudRenderSystem extends EntitySystem {
    ComponentGrabber cg;
    Entity player;
    Stage playerHud;
    Skin skin;

    public PlayerHudRenderSystem(ComponentGrabber cg) {
        super(98);
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        playerHud = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
    }

    @Override
    public void update(float delta) {
        playerHud.clear();
        // the root table
        Table root = new Table();
        // make root table as big as the stage
        root.setSize(playerHud.getWidth(), playerHud.getHeight());
        // add root table to the stage
        playerHud.addActor(root);
        // create nested table to display player hp and exp for now
        Table playerLevel = new Table();
        playerLevel.setDebug(true);
        // setting the table width and height
        playerLevel.setSize(root.getWidth() / 3, root.getHeight() / 5);
        // set table background
        playerLevel.setBackground(skin.getDrawable("button-up"));
        // adding the table to the stage use expand, top, left to move table to top left corner
        root.add(playerLevel).expand().top().left().height(playerLevel.getHeight()).width(playerLevel.getWidth());
        // create and add level label to the table
        Label playerLevelLabel = new Label("1", skin);
        // center the text
        playerLevelLabel.setAlignment(0);
        playerLevel.add(playerLevelLabel).fill();
        Cell<Label> levelLabelCell = playerLevel.getCell(playerLevelLabel);
        levelLabelCell.width(playerLevel.getWidth() / 3);

        // nested table for health and exp
        Table playerHealthAndExp = new Table();
        playerLevel.add(playerHealthAndExp);

        

        playerHud.act();
        playerHud.draw();
    }

    private float calcCurrentRemainingHealth() {
        return (cg.getParameters(player).health.currentHealth /
                cg.getParameters(player).health.maxHealth * 100);
    }

}
