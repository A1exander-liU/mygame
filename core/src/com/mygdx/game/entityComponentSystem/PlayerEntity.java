package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.components.CameraComponent;
import com.mygdx.game.entityComponentSystem.components.SpriteComponent;
import com.mygdx.game.entityComponentSystem.components.HealthComponent;
import com.mygdx.game.entityComponentSystem.components.IDComponent;
import com.mygdx.game.entityComponentSystem.components.NameComponent;
import com.mygdx.game.entityComponentSystem.components.PlayerComponent;
import com.mygdx.game.entityComponentSystem.components.PositionComponent;
import com.mygdx.game.entityComponentSystem.components.SizeComponent;
import com.mygdx.game.entityComponentSystem.components.SpeedComponent;

public class PlayerEntity extends Entity {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;

    Rectangle playerBox;

    public PlayerEntity(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties, String name) {
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        addRequiredComponents();
        modifyComponentValues(name);
        PositionComponent pos = cg.getPosition(this);
        SizeComponent sizeComponent = cg.getSize(this);
        playerBox = new Rectangle(pos.x, pos.y, sizeComponent.width, sizeComponent.height);
        root.engine.addEntity(this);
        EntityToMapAdder entityToMapAdder = new EntityToMapAdder(gameMapProperties.tiledMap, cg);
        entityToMapAdder.addEntityToMap(gameMapProperties.tiledMap, this);
    }

    private void addRequiredComponents() {
        super.add(new CameraComponent());
        super.add(new SpriteComponent());
        super.add(new HealthComponent());
        super.add(new IDComponent());
        super.add(new NameComponent());
        super.add(new PlayerComponent());
        super.add(new PositionComponent());
        super.add(new SizeComponent());
        super.add(new SpeedComponent());
    }

    private void modifyComponentValues(String playerName) {
        cg.getSprite(this).texture = new Texture(Gdx.files.internal("testPlayer.png"));
        cg.getHealth(this).maxHealth = 100;
        cg.getHealth(this).currentHealth = 100;
        cg.getName(this).name = playerName;
        cg.getPosition(this).x = 200;
        cg.getPosition(this).y = 200;
        cg.getPosition(this).oldX = cg.getPosition(this).x;
        cg.getPosition(this).oldY = cg.getPosition(this).y;
        cg.getPosition(this).futureX = cg.getPosition(this).x;
        cg.getPosition(this).futureY = cg.getPosition(this).y;
        cg.getPosition(this).position.x = cg.getPosition(this).x;
        cg.getPosition(this).position.y = cg.getPosition(this).y;
        cg.getPosition(this).oldPosition = cg.getPosition(this).position;
        cg.getSize(this).width = 32;
        cg.getSize(this).height = 32;
        cg.getSpeed(this).xSpeed = 5;
        cg.getSpeed(this).ySpeed = 5;
        CameraComponent player = cg.getCamera(this);
        player.camera = new OrthographicCamera();
        player.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        player.camera.position.x = Gdx.graphics.getWidth() / 2f;
        player.camera.position.y = Gdx.graphics.getHeight() / 2f;
    }
}
