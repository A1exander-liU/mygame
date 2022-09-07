package com.mygdx.game.engine.systems.render.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.utils.componentutils.ComponentGrabber;
import com.mygdx.game.engine.utils.componentutils.Families;
import com.mygdx.game.engine.components.ExpComponent;
import com.mygdx.game.engine.components.ManaComponent;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.utils.ui.PauseButton;

public class PlayerHudRenderSystem extends EntitySystem {
    ComponentGrabber cg;
    Entity player;
    Stage playerHud;
    Skin skin;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    ParameterComponent playerParams;
    ManaComponent playerMana;
    ExpComponent playerExp;

    Table root;

    public PlayerHudRenderSystem(ComponentGrabber cg) {
        super(98);
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        playerHud = new Stage(new ScreenViewport());
        skin = new Skin();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        parameter.size = 16;
        BitmapFont newFont = generator.generateFont(parameter);
        skin.add("pixel2D", newFont);
        skin.addRegions(new TextureAtlas("Game_UI_Skin/Game_UI_Skin.atlas"));
        skin.load(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));

        generator.dispose();

        playerParams = cg.getParameters(player);
        playerMana = cg.getMana(player);
        playerExp = cg.getExp(player);

        // the root table
        root = new Table();
        // make root table as big as the stage
        root.setSize(playerHud.getWidth(), playerHud.getHeight());
        // add root table to the stage
        playerHud.addActor(root);
        // create nested table to display player hp and exp for now
        Table playerLevel = new Table();
        playerLevel.setDebug(true);

        createLevelUiArea(root, playerLevel);
        // nested table for health and exp
        Table playerHealthManaExp = new Table();
        playerLevel.add(playerHealthManaExp).width(playerLevel.getWidth() * (3f / 4)).pad(0, 0, 0, 0);

        createHealthStack(playerHealthManaExp);
        createManaStack(playerHealthManaExp);
        createExpStack(playerHealthManaExp);

        adjustStackCellSizes(playerLevel, playerHealthManaExp);

        PauseButton pauseButton = new PauseButton(skin);
        root.add(pauseButton).expand().top().right();


    }

    @Override
    public void update(float delta) {
        Gdx.input.setInputProcessor(playerHud);
        updateHpMpExpLevelValues();
//        playerHud.clear();
//        // the root table
//        Table root = new Table();
//        // make root table as big as the stage
//        root.setSize(playerHud.getWidth(), playerHud.getHeight());
//        // add root table to the stage
//        playerHud.addActor(root);
//        // create nested table to display player hp and exp for now
//        Table playerLevel = new Table();
//        playerLevel.setDebug(true);
//
//        createLevelUiArea(root, playerLevel);
//        // nested table for health and exp
//        Table playerHealthManaExp = new Table();
//        playerLevel.add(playerHealthManaExp).width(playerLevel.getWidth() * (3f / 4)).pad(0, 0, 0, 0);
//
//        createHealthStack(playerHealthManaExp);
//        createManaStack(playerHealthManaExp);
//        createExpStack(playerHealthManaExp);
//
//        adjustStackCellSizes(playerLevel, playerHealthManaExp);
//
//        PauseButton pauseButton = new PauseButton(skin);
//        root.add(pauseButton).expand().top().right();

        playerHud.act(delta);
        playerHud.draw();
    }

    private float calcCurrentRemainingHealth() {
        return (cg.getParameters(player).health.currentHealth /
                cg.getParameters(player).health.maxHealth * 100);
    }

    private float calcRemainingMana() {
        return (cg.getMana(player).currentMana / cg.getMana(player).maxMana * 100);
    }

    private float calcCurrentExp() {
        return ((float) cg.getExp(player).currentExp / cg.getExp(player).toNextLevel) * 100;
    }

    private void createLevelUiArea(Table root, Table playerLevel) {
        // setting the table width and height
        playerLevel.setSize(root.getWidth() / 3, root.getHeight() / 6);
        // set table background
        playerLevel.setBackground(skin.getDrawable("player-hud-bg-01"));
        // adding the table to the stage use expand, top, left to move table to top left corner
        root.add(playerLevel).expand().top().left().height(playerLevel.getHeight()).width(playerLevel.getWidth());
        // create and add level label to the table
        Label playerLevelLabel = new Label("" + cg.getLevel(player).level, skin, "pixel2D", Color.BLACK);
        playerLevelLabel.setName("playerLevelLabel");
        // center the text
        playerLevelLabel.setAlignment(0);
        playerLevel.add(playerLevelLabel).fill();
        Cell<Label> levelLabelCell = playerLevel.getCell(playerLevelLabel);
        levelLabelCell.width(playerLevel.getWidth() / 4);
    }

    private void createHealthStack(Table playerHealthManaExp) {
        // create health bar
        ProgressBar healthBar = new ProgressBar(0, 100, 1, false, skin, "progress-bar-player-health");
        healthBar.setValue(calcCurrentRemainingHealth());

        Label healthLabel = new Label((int)playerParams.health.currentHealth
                + "/" + (int)playerParams.health.maxHealth, skin, "pixel2D", Color.BLACK);
        healthLabel.setAlignment(Align.center);
        healthLabel.setFontScale(0.6f);

        // create a stack to display health bar and health numbers on top
        Stack healthStack = new Stack();
        healthStack.setName("healthStack");
        healthStack.add(healthBar);
        healthStack.add(healthLabel);
        playerHealthManaExp.add(healthStack);
        // add new row (mana bar will be below health bar)
        playerHealthManaExp.row();
    }

    private void createManaStack(Table playerHealthManaExp) {
        // create mana bar
        ProgressBar manaBar = new ProgressBar(0, 100, 1, false, skin, "progress-bar-player-mana");
        manaBar.setValue(calcRemainingMana()); // will add a mana component later

        // create label for mana bar
        Label manaLabel = new Label((int)playerMana.currentMana
                + "/" + (int)playerMana.maxMana, skin, "pixel2D", Color.BLACK);
        manaLabel.setAlignment(Align.center);
        manaLabel.setFontScale(0.6f);

        // create stack for mana
        Stack manaStack = new Stack();
        manaStack.setName("manaStack");
        manaStack.add(manaBar);
        manaStack.add(manaLabel);
        playerHealthManaExp.add(manaStack);
        // add new row
        playerHealthManaExp.row();
    }

    private void createExpStack(Table playerHealthManaExp) {
        // create exp bar
        ProgressBar expBar = new ProgressBar(0, 100, 1, false, skin, "progress-bar-player-exp");

        // create exp label
        Label expLabel = new Label(playerExp.currentExp
                + "/" + playerExp.toNextLevel, skin, "pixel2D", Color.BLACK);
        expLabel.setAlignment(Align.center);
        expLabel.setFontScale(0.6f);

        // create stack for exp
        Stack expStack = new Stack();
        expStack.setName("expStack");
        expStack.add(expBar);
        expStack.add(expLabel);
        playerHealthManaExp.add(expStack);
    }

    private void adjustStackCellSizes(Table playerLevel, Table playerHealthManaExp) {
        Cell<Stack> healthBarStackCell = playerHealthManaExp.getCell(playerHealthManaExp.<Stack>findActor("healthStack"));
        Cell<Stack> manaBarStackCell = playerHealthManaExp.getCell(playerHealthManaExp.<Stack>findActor("manaStack"));
        Cell<Stack> expBarStackCell = playerHealthManaExp.getCell(playerHealthManaExp.<Stack>findActor("expStack"));
        healthBarStackCell.height(playerLevel.getHeight() / 3).padTop(5);
        manaBarStackCell.height(playerLevel.getHeight() / 3);
        expBarStackCell.height(playerLevel.getHeight() / 3);
    }

    private void updateHpMpExpLevelValues() {
        ((ProgressBar)((Stack) playerHud.getRoot().findActor("healthStack")).getChildren().get(0)).setValue(calcCurrentRemainingHealth());
        ((Label)((Stack) playerHud.getRoot().findActor("healthStack")).getChildren().get(0)).setText(playerParams.health.currentHealth + " / " + playerParams.health.maxHealth);

        ((ProgressBar)((Stack) playerHud.getRoot().findActor("manaStack")).getChildren().get(0)).setValue(calcRemainingMana());
        ((Label)((Stack) playerHud.getRoot().findActor("healthStack")).getChildren().get(0)).setText(playerMana.currentMana + " / " + playerMana.maxMana);

        ((ProgressBar)((Stack) playerHud.getRoot().findActor("expStack")).getChildren().get(0)).setValue(calcCurrentExp());
    }
}
