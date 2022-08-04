package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Speed;

public class MovementSystem extends EntitySystem {
    private final ImmutableArray<Entity> enemies;
    private final MapObjects spawnPoints;
    private final Entity player;
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;

    public MovementSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(3);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        spawnPoints = gameMapProperties.tiledMap.getLayers().get("Enemy Spawns").getObjects();
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        // there is only one player hence we just get index 0
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
    }

    @Override
    public Engine getEngine() {
        return MyGame.engine;
    }

    @Override
    public void update(float delta) {
        // get player move
        playerMovement();
    }

    private void playerMovement() {
        Position pos = cg.getPosition(player);
        Speed speed = cg.getSpeed(player);
        // old pos is position before move change
        pos.oldX = pos.x;
        pos.oldY = pos.y;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            pos.y += speed.ySpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            pos.x += speed.xSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            pos.y -= speed.ySpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            pos.x -= speed.xSpeed;
        }
    }
}
