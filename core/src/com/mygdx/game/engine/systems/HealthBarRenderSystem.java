package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.ParameterComponent;

public class HealthBarRenderSystem extends EntitySystem {
    final float fullHealth = 100;

    ComponentGrabber cg;
    Entity player;
    ImmutableArray<Entity> enemies;
    ImmutableArray<Entity> characters;

    Skin skin;
    Stage stage;

    ProgressBar.ProgressBarStyle enemyHealthBarStyle;

    public HealthBarRenderSystem(ComponentGrabber cg) {
        super();
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        characters = MyGame.engine.getEntitiesFor(Families.characters);
        skin = new Skin(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
        stage = new Stage();
        enemyHealthBarStyle = new ProgressBar.ProgressBarStyle(skin.get("progress-bar-enemy-health", ProgressBar.ProgressBarStyle.class));
    }

    @Override
    public void update(float delta) {
        // draw hp bar over enemy sprites, will be drawn over their head
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            ProgressBar enemyHealthBar = new ProgressBar(0, 100, 1, false, enemyHealthBarStyle);
            ParameterComponent paramCom = cg.getParameters(entity);
            // knob-after
            // from 0 - 100
            // 0 is full hp and 100 is no hp
            // so if max hp is 10 and current hp is 8
            // convert to percent = 20%
            // then set value of health bar to that number
            // because 0 is full hp setting to 20 will show their is only 80% left
            float percentageHealthDone = (paramCom.health.currentHealth / paramCom.health.maxHealth) * 100;
            enemyHealthBar.setValue((float) Math.floor(percentageHealthDone));
        }
    }
}
