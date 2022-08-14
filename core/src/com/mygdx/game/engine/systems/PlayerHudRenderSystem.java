package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    BitmapFont newFont;


    public PlayerHudRenderSystem(ComponentGrabber cg) {
        super(98);
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        playerHud = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        parameter.size = 12;
        newFont = generator.generateFont(parameter);
        generator.dispose();
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
        playerLevel.setSize(root.getWidth() / 3, root.getHeight() / 6);
        // set table background
        playerLevel.setBackground(skin.getDrawable("player-hud-bg-01"));
        // adding the table to the stage use expand, top, left to move table to top left corner
        root.add(playerLevel).expand().top().left().height(playerLevel.getHeight()).width(playerLevel.getWidth());
        // create and add level label to the table
        Label playerLevelLabel = new Label("" + cg.getLevel(player).level, skin);
        // center the text
        playerLevelLabel.setAlignment(0);
        playerLevel.add(playerLevelLabel).fill();
        Cell<Label> levelLabelCell = playerLevel.getCell(playerLevelLabel);
        levelLabelCell.width(playerLevel.getWidth() / 4);

        // nested table for health and exp
        Table playerHealthManaExp = new Table();
        playerLevel.add(playerHealthManaExp).width(playerLevel.getWidth() * (3f / 4)).pad(0, 0, 0, 0);

        // create health bar
        ProgressBar healthBar = new ProgressBar(0, 100, 1, false, skin, "progress-bar-player-health");
        healthBar.setValue(calcCurrentRemainingHealth());
        playerHealthManaExp.add(healthBar);
        // add new row (mana bar will be below health bar)
        playerHealthManaExp.row();

        // create mana bar
        ProgressBar manaBar = new ProgressBar(0, 100, 1, false, skin, "progress-bar-player-mana");
        manaBar.setValue(calcRemainingMana()); // will add a mana component later
        playerHealthManaExp.add(manaBar);
        // add new row
        playerHealthManaExp.row();

        // create exp bar
        ProgressBar expBar = new ProgressBar(0, 100, 1, false, skin, "progress-bar-player-exp");
        playerHealthManaExp.add(expBar);

        // adjust exp and health bar cell sizes to move them closer together
        Cell<ProgressBar> healthBarCell = playerHealthManaExp.getCell(healthBar);
        Cell<ProgressBar> manaBarCell = playerHealthManaExp.getCell(manaBar);
        Cell<ProgressBar> expBarCell = playerHealthManaExp.getCell(expBar);
        healthBarCell.height(playerLevel.getHeight() / 3).padTop(4);
        manaBarCell.height(playerLevel.getHeight() / 3);
        expBarCell.height(playerLevel.getHeight() / 3);

        playerHud.act();
        playerHud.draw();
        // draw player hud, then draw the text so it appears on top
        playerHud.getBatch().begin();
        newFont.draw(playerHud.getBatch(), "10/10", healthBar.getX(), healthBar.getY());
        playerHud.getBatch().end();
    }

    private float calcCurrentRemainingHealth() {
        return (cg.getParameters(player).health.currentHealth /
                cg.getParameters(player).health.maxHealth * 100);
    }

    private float calcRemainingMana() {
        return (cg.getMana(player).currentMana / cg.getMana(player).maxMana * 100);
    }

}
