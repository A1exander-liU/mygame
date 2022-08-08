package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;

public class HealthBarRenderSystem extends EntitySystem {
    final float fullHealth = 100;

    ComponentGrabber cg;
    Entity player;
    ImmutableArray<Entity> enemies;
    ImmutableArray<Entity> characters;

    Skin skin;
    Stage stage;

    public HealthBarRenderSystem(ComponentGrabber cg) {
        super(98);
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        characters = MyGame.engine.getEntitiesFor(Families.characters);
        skin = new Skin(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void update(float delta) {
        stage.clear();
        // draw hp bar over enemy sprites, will be drawn over their head
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            ProgressBar enemyHealthBar = new ProgressBar(0, 100, 1, false, skin, "progress-bar-enemy-health");
            ParameterComponent paramCom = cg.getParameters(entity);
            Position pos = cg.getPosition(entity);
            Size size = cg.getSize(entity);
            // knob-after
            // from 0 - 100
            // 0 is full hp and 100 is no hp
            // so if max hp is 10 and current hp is 8
            // convert to percent = 80%
            // then set value of health bar to that number
            // because 0 is full hp setting to 20 will show their is only 80% left

            float percentageHealthDone = (paramCom.health.currentHealth / paramCom.health.maxHealth) * 100;
            enemyHealthBar.setValue((float) Math.floor(fullHealth - percentageHealthDone));

            Container<ProgressBar> healthBarContainer = new Container<>(enemyHealthBar);
            healthBarContainer.setPosition(pos.x, pos.y + size.height);
            healthBarContainer.setSize(size.width, 4);
            healthBarContainer.clip();
            
            stage.addActor(healthBarContainer);
        }

        stage.act(delta);
        stage.draw();
    }
}
