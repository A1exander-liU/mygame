package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
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
        Table playerHealthAndLevel = new Table();
        // setting the table width and height
        playerHealthAndLevel.setSize(root.getWidth() / 3, root.getHeight() / 5);
        // set table background
        playerHealthAndLevel.setBackground(skin.getDrawable("button-up"));
        // adding the table to the stage use expand, top, left to move table to top left corner
        root.add(playerHealthAndLevel).expand().top().left().height(playerHealthAndLevel.getHeight()).width(playerHealthAndLevel.getWidth());

        // create player health bar
        ProgressBar playerHealthBar = new ProgressBar(0, 100, 1, false, skin, "progress-bar-player-health");
        // determine the hp percent to display
        playerHealthBar.setValue(calcCurrentRemainingHealth());
        playerHealthBar.setName("playerHealthBar");
        // adding to the nested table
        playerHealthAndLevel.add(playerHealthBar);
        // create new row to add exp bar under health bar
        playerHealthAndLevel.row();

        // create player exp bar
        ProgressBar playerExpBar = new ProgressBar(0, 100, 1, false, skin, "progress-bar-player-exp");
        // value is for testing
        playerExpBar.setValue(10);
        playerExpBar.setName("playerExpBar");
        // add to the nested table
        playerHealthAndLevel.add(playerExpBar);

        // grabbing the cells to adjust their size
        Cell<ProgressBar> healthBarCell = playerHealthAndLevel.getCell(playerHealthBar);
        // adjust width and height to leave space for other ui
        // add padding to move the bars towards right edge of the table
        healthBarCell.height(playerHealthAndLevel.getHeight() / 2);
        healthBarCell.width(playerHealthAndLevel.getWidth() * 1.7f / 3);
        healthBarCell.pad(3, 65, 0, 0);
        Cell<ProgressBar> expBarCell = playerHealthAndLevel.getCell(playerExpBar);
        expBarCell.height(playerHealthAndLevel.getHeight() / 2);
        expBarCell.width(playerHealthAndLevel.getWidth() * 1.7f / 3);
        expBarCell.pad(0, 65, 0, 0);

        playerHud.act();
        playerHud.draw();
    }

    private float calcCurrentRemainingHealth() {
        return (cg.getParameters(player).health.currentHealth /
                cg.getParameters(player).health.maxHealth * 100);
    }

}
