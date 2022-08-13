package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.EnemyState;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Camera;
import com.mygdx.game.engine.components.EnemyStateMachine;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;

public class HealthBarRenderSystem extends EntitySystem {
    final float fullHealth = 99;

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
        stage = new Stage();
    }

    @Override
    public void update(float delta) {
        stage.clear();
        // draw hp bar over enemy sprites, will be drawn over their head
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            EnemyStateMachine stateMachine = cg.getStateMachine(entity);
            // only draw the health bar of the enemy if they are "hunting"
            // the player
            if (stateMachine.getCurrentState() == EnemyState.HUNT) {
                ProgressBar enemyHealthBar = new ProgressBar(0, 100, 1, false, skin, "progress-bar-enemy-health");
                enemyHealthBar.setValue(calcRemainingHealthPercentage(entity));
                stage.addActor(makeEnemyHealthBarContainer(entity, enemyHealthBar));
            }
        }
        stage.act();
        stage.draw();
    }

    private float calcRemainingHealthPercentage(Entity entity) {
        // knob-after
        // from 0 - 100
        // 0 is full hp and 100 is no hp
        // so if max hp is 10 and current hp is 8
        // convert to percent = 80%
        // then set value of health bar to that number
        // because 0 is full hp setting to 20 will show their is only 80% left
        // then fullHealth - calculated % : (100 - 80) = 20
        ParameterComponent paramCom = cg.getParameters(entity);
        return (float) Math.floor(fullHealth - ((paramCom.health.currentHealth / paramCom.health.maxHealth) * 100));
    }

    private Container<ProgressBar> makeEnemyHealthBarContainer(Entity entity, ProgressBar healthBar) {
        Container<ProgressBar> healthBarContainer = new Container<>(healthBar);
        Position pos = cg.getPosition(entity);
        Size size = cg.getSize(entity);
        Camera camera = cg.getCamera(player);
        // reason why health bars move along with the camera is because they only move
        // along with the screen and not the map itself.
        // so setting the coordinates based on enemy location won't work since
        // the health bars are not rendered through the map but on the screen
        // so when the position is set to be on top of enemy like (200, 200)
        // it will set position of (200, 200) on the screen which is 200 pixels
        // up and 200 right from bottom left of the screen and not in the actual world

        // basically the "world" coordinates of the enemies needs to be converted to
        // "screen" coordinates

        // the vector3 object which is the world coordinates of the enemy
        Vector3 worldCoordinates = new Vector3(pos.x, pos.y + size.height, 0);
        // project converts "world" coordinates to "screen" coordinates
        Vector3 screenCoordinates = camera.camera.project(worldCoordinates);
        // set bounds in order to render properly
        healthBarContainer.setBounds(screenCoordinates.x, screenCoordinates.y, size.width, 4);
        healthBarContainer.width(size.width);
        healthBarContainer.height(4);
        healthBarContainer.clip();
        return healthBarContainer;
    }
}
