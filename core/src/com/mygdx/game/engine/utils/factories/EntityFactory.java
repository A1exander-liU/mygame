package com.mygdx.game.engine.utils.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.utils.EnemyState;
import com.mygdx.game.engine.utils.entities.MobEntity;
import com.mygdx.game.engine.utils.entities.PlayerEntity;
import com.mygdx.game.engine.components.Camera;
import com.mygdx.game.engine.components.CollidableComponent;
import com.mygdx.game.engine.components.EnemyStateMachine;
import com.mygdx.game.engine.components.Health;
import com.mygdx.game.engine.components.Item;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.ObstacleComponent;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.SpawnArea;
import com.mygdx.game.engine.components.Speed;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.Steering;
import com.mygdx.game.engine.utils.componentutils.ComponentGrabber;

public class EntityFactory {
    ComponentGrabber cg;
    MyGame root;

    public EntityFactory(ComponentGrabber cg, MyGame root) {
        this.cg = cg;
        this.root = root;
    }

    public Entity makeEnemy(String name) {
        // name provided will determine the kind of enemy to create
        // make mobEntity only add the components
        // entityFactory will actually modify the component values
        // will also add the entity to the map
        MobEntity enemy = new MobEntity();
        modifyEnemyComponentValues(enemy, name);
        addToEngine(enemy);
        addToMap(enemy);
        return enemy;
    }

    public void makePlayer(String name) {
        PlayerEntity player = new PlayerEntity();
        modifyPlayerComponentValues(player, name);
        addToEngine(player);
        addToMap(player);
    }

    public void makeSpawn(MapObject spawn) {
        Entity spawnEntity = new Entity();
        addRequiredSpawnComponents(spawnEntity);
        modifySpawnComponentValues(spawnEntity, spawn);
        addToEngine(spawnEntity);
    }

    public void makeObstacle(Rectangle collisionRegion) {
        Entity collision = new Entity();
        addRequiredObstacleComponents(collision);
        modifyObstacleComponentValues(collision, collisionRegion);
        addToEngine(collision);
    }

    private void addRequiredSpawnComponents(Entity entity) {
        entity.add(new Position());
        entity.add(new Size());
        entity.add(new SpawnArea());
        entity.add(new Name());
    }

    private void addRequiredObstacleComponents(Entity entity) {
        entity.add(new Position());
        entity.add(new Size());
        entity.add(new Steering(entity));
        entity.add(new Item());
        entity.add(new ObstacleComponent());
        entity.add(new CollidableComponent());
    }

    private void modifyEnemyComponentValues(Entity entity, String name) {
        Name enemyName = cg.getName(entity);
        Size size = cg.getSize(entity);
        Speed speed = cg.getSpeed(entity);
        Sprite entitySprite = cg.getSprite(entity);
        EnemyStateMachine stateMachine = cg.getStateMachine(entity);
        Item item = cg.getItem(entity);
        item.item = new com.dongbat.jbump.Item<>(entity);
        enemyName.name = name;
        size.width = 32;
        size.height = 32;
        speed.xSpeed = 2;
        speed.ySpeed = 2;
        String path = root.jsonSearcher.findByEnemyName(enemyName.name).getString("SPRITE");
        entitySprite.texture = new Texture(Gdx.files.internal(path));
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
        Item item = cg.getItem(entity);
        item.item = new com.dongbat.jbump.Item<>(entity);
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

    private void modifySpawnComponentValues(Entity entity, MapObject spawn) {
        Rectangle spawnRect = ((RectangleMapObject) spawn).getRectangle();
        Position pos = cg.getPosition(entity);
        Size size = cg.getSize(entity);
        SpawnArea spawnArea = cg.getSpawnArea(entity);
        Name name = cg.getName(entity);
        pos.x = spawnRect.x;
        pos.y = spawnRect.y;
        size.width = spawnRect.width;
        size.height = spawnRect.height;
        spawnArea.xCenter = spawnRect.x + (spawnRect.width / 2);
        spawnArea.yCenter = spawnRect.y + (spawnRect.height / 2);
        name.name = spawn.getName();
    }

    private void modifyObstacleComponentValues(Entity entity, Rectangle collisionRegion) {
        Position pos = cg.getPosition(entity);
        Size size = cg.getSize(entity);
        pos.x = collisionRegion.x;
        pos.y = collisionRegion.y;
        size.width = collisionRegion.width;
        size.height = collisionRegion.height;
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