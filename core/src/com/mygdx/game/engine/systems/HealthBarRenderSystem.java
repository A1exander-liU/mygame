package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;

public class HealthBarRenderSystem extends EntitySystem {
    ComponentGrabber cg;
    Entity player;
    ImmutableArray<Entity> enemies;
    ImmutableArray<Entity> characters;

    Skin skin;
    Stage stage;

    ProgressBar.ProgressBarStyle enemyHealth;

    public HealthBarRenderSystem(ComponentGrabber cg) {
        super();
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        characters = MyGame.engine.getEntitiesFor(Families.characters);
        skin = new Skin(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
        stage = new Stage();
        enemyHealth = new ProgressBar.ProgressBarStyle(skin.get("progress-bar-enemy-health", ProgressBar.ProgressBarStyle.class));
    }

    @Override
    public void update(float delta) {
        // draw hp bar over enemy sprites, will be drawn over their head
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            ProgressBar enemyHealthBar = new ProgressBar(1, 100, 1, false, enemyHealth);
        }
    }
}
