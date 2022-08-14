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
        Table root = new Table();
        root.setSize(playerHud.getWidth(), playerHud.getHeight());
        playerHud.addActor(root);

        Table playerHealth = new Table();
        playerHealth.setSize(root.getWidth() / 3, root.getHeight() / 5);
        playerHealth.setBackground(skin.getDrawable("button-up"));
        root.add(playerHealth).expand().top().left().height(playerHealth.getHeight()).width(playerHealth.getWidth());

        ProgressBar playerHealthBar = new ProgressBar(0, 100, 1, false, skin, "progress-bar-player-health");
        playerHealthBar.setValue(calcCurrentRemainingHealth());
        playerHealthBar.setName("playerHealthBar");
        playerHealth.add(playerHealthBar);

        playerHealth.row();

        ProgressBar playerExpBar = new ProgressBar(0, 100, 1, false, skin, "progress-bar-player-exp");
        // value is for testing
        playerExpBar.setValue(10);
        playerExpBar.setName("playerExpBar");
        playerHealth.add(playerExpBar);

        Cell<ProgressBar> healthBarCell = playerHealth.getCell(playerHealthBar);
        healthBarCell.height(playerHealth.getHeight() / 2);
        healthBarCell.width(playerHealth.getWidth() * 1.7f / 3);
        healthBarCell.pad(3, 65, 0, 0);
        Cell<ProgressBar> expBarCell = playerHealth.getCell(playerExpBar);
        expBarCell.height(playerHealth.getHeight() / 2);
        expBarCell.width(playerHealth.getWidth() * 1.7f / 3);
        expBarCell.pad(0, 65, 0, 0);

        playerHud.act();
        playerHud.draw();
    }

    private float calcCurrentRemainingHealth() {
        return (cg.getParameters(player).health.currentHealth /
                cg.getParameters(player).health.maxHealth * 100);
    }

}
