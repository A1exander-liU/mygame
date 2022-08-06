package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.EnemyStateMachine;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Speed;
import com.mygdx.game.engine.components.Sprite;

public class EntityFactory {
    ComponentGrabber cg;
    MyGame root;

    public EntityFactory(ComponentGrabber cg, MyGame root) {
        this.cg = cg;
        this.root = root;
    }

    // also need method to create a player character

    public void makeEnemy(String name) {
        // name provided will determine the kind of enemy to create
        // make mobEntity only add the components
        // entityFactory will actually modify the component values
        // will also add the entity to the map
        MobEntity enemy = new MobEntity();
        modifyComponentValues(enemy, name);
        addToEngine(enemy);
        addToMap(enemy);
    }

    public void makePlayer(String name) {
        PlayerEntity player = new PlayerEntity();
    }

    private void modifyComponentValues(Entity entity, String name) {
        Name enemyName = cg.getName(entity);
        Size size = cg.getSize(entity);
        Speed speed = cg.getSpeed(entity);
        Sprite entitySprite = cg.getSprite(entity);
        EnemyStateMachine stateMachine = cg.getStateMachine(entity);
        enemyName.name = name;
        size.width = 32;
        size.height = 32;
        speed.xSpeed = 2;
        speed.ySpeed = 2;
        entitySprite.texture = new Texture(Gdx.files.internal("testPlayer.png"));
        stateMachine.setInitialState(EnemyState.IDLE);
        fillParameterValues(entity, name);
    }

    private void fillParameterValues(Entity entity, String name) {
        JsonValue enemy = root.jsonSearcher.findByEnemyName(name);
        ParameterComponent paramCom = cg.getParameters(entity);
        paramCom.health.maxHealth = enemy.getInt("HP");
        paramCom.health.currentHealth = enemy.getInt("HP");
        paramCom.damage = enemy.getInt("DMG");
    }

    private void addToEngine(Entity entity) {
        MyGame.engine.addEntity(entity);
    }

    private void addToMap(Entity entity) {
        root.entityToMapAdder.addEntityToMap(entity);
    }
}
