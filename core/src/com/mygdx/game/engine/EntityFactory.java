package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.Camera;
import com.mygdx.game.engine.components.EnemyStateMachine;
import com.mygdx.game.engine.components.Health;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.Position;
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

    public void makeEnemy(String name) {
        // name provided will determine the kind of enemy to create
        // make mobEntity only add the components
        // entityFactory will actually modify the component values
        // will also add the entity to the map
        MobEntity enemy = new MobEntity();
        modifyEnemyComponentValues(enemy, name);
        addToEngine(enemy);
        addToMap(enemy);
    }

    public void makePlayer(String name) {
        PlayerEntity player = new PlayerEntity();
        modifyPlayerComponentValues(player, name);
        addToEngine(player);
        addToMap(player);
    }

    private void modifyEnemyComponentValues(Entity entity, String name) {
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
        fillEnemyParameterValues(entity, name);
    }

    private void modifyPlayerComponentValues(Entity entity, String name) {
        Name playerName = cg.getName(entity);
        Camera playerCamera = cg.getCamera(entity);
        Health health = cg.getHealth(entity);
        Position pos = cg.getPosition(entity);
        Size size = cg.getSize(entity);
        Speed speed = cg.getSpeed(entity);
        Sprite sprite = cg.getSprite(entity);
        playerCamera.camera = new OrthographicCamera();
        playerCamera.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        playerCamera.camera.position.x = Gdx.graphics.getWidth() / 2f;
        playerCamera.camera.position.y = Gdx.graphics.getHeight() / 2f;
        playerName.name = name;
        health.maxHealth = 10;
        health.currentHealth = 10;
        pos.x = 200;
        pos.oldX = 200;
        pos.y = 200;
        pos.oldY = 200;
        size.width = 32;
        size.height = 32;
        speed.xSpeed = 5;
        speed.ySpeed = 5;
        sprite.texture = new Texture(Gdx.files.internal("testPlayer.png"));
        fillPlayerParameterValues(entity);
    }

    private void fillEnemyParameterValues(Entity entity, String name) {
        JsonValue enemy = root.jsonSearcher.findByEnemyName(name);
        ParameterComponent paramCom = cg.getParameters(entity);
        paramCom.health.maxHealth = enemy.getInt("HP");
        paramCom.health.currentHealth = enemy.getInt("HP");
        paramCom.damage = enemy.getInt("DMG");
    }

    private void fillPlayerParameterValues(Entity entity) {
        ParameterComponent paramCom = cg.getParameters(entity);
        Health health = cg.getHealth(entity);
        paramCom.health.maxHealth = health.maxHealth;
        paramCom.health.currentHealth = health.currentHealth;
        paramCom.damage = 5;
    }

    private void addToEngine(Entity entity) {
        MyGame.engine.addEntity(entity);
    }

    private void addToMap(Entity entity) {
        root.entityToMapAdder.addEntityToMap(entity);
    }
}
