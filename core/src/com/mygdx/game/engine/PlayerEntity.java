package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.Camera;
import com.mygdx.game.engine.components.DetectionProximity;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.Health;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Player;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Speed;
import com.mygdx.game.engine.components.Steering;

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
        Position pos = cg.getPosition(this);
        Size size = cg.getSize(this);
        playerBox = new Rectangle(pos.x, pos.y, size.width, size.height);
        root.engine.addEntity(this);
        EntityToMapAdder entityToMapAdder = new EntityToMapAdder(gameMapProperties.tiledMap, cg);
        entityToMapAdder.addEntityToMap(gameMapProperties.tiledMap, this);
    }

    private void addRequiredComponents() {
        super.add(new Camera());
        super.add(new Sprite());
        super.add(new Health());
        super.add(new ID());
        super.add(new Name());
        super.add(new Player());
        super.add(new Position());
        super.add(new Size());
        super.add(new Speed());
        super.add(new Steering(this));
        super.add(new DetectionProximity(this, 20, root));
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
        cg.getPosition(this).oldPosition.x = cg.getPosition(this).x;
        cg.getPosition(this).oldPosition.y = cg.getPosition(this).y;
        cg.getSize(this).width = 32;
        cg.getSize(this).height = 32;
        cg.getSpeed(this).xSpeed = 5;
        cg.getSpeed(this).ySpeed = 5;
        Camera player = cg.getCamera(this);
        player.camera = new OrthographicCamera();
        player.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        player.camera.position.x = Gdx.graphics.getWidth() / 2f;
        player.camera.position.y = Gdx.graphics.getHeight() / 2f;
    }
}