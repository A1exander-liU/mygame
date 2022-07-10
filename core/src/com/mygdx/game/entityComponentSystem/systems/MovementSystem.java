package com.mygdx.game.entityComponentSystem.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.components.Enemy;
import com.mygdx.game.entityComponentSystem.components.Player;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Speed;

import java.util.Random;

public class MovementSystem extends EntitySystem {
    private ImmutableArray<Entity> enemies;
    private Entity player;
    ComponentGrabber cg;
    MyGame root;

    public MovementSystem(ComponentGrabber cg, MyGame root) {
        super(2);
        this.cg = cg;
        this.root = root;
    }

    @Override
    public void addedToEngine(Engine engine) {
        /* selecting all entities which have a position and speed
        * basically saying movement system for entities that have both position and speed component */
        enemies = root.engine.getEntitiesFor(Family.all(Enemy.class).get());
        player = root.engine.getEntitiesFor(Family.all(Player.class).get()).get(0);
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            moveEnemy(getRandomDirection(), entity);
        }
        playerMovement();
    }

    private void moveEnemy(String direction, Entity entity) {
        Position pos = cg.getPosition(entity);
        Speed speed = cg.getSpeed(entity);
        pos.oldX = pos.x;
        pos.oldY = pos.y;
//        if (Gdx.input.isKeyPressed(Input.Keys.UP))
//            pos.y += speed.ySpeed;
//        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
//            pos.x += speed.xSpeed;
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
//            pos.y -= speed.ySpeed;
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
//            pos.x -= speed.xSpeed;
        switch (direction) {
            case "N":
                pos.y += speed.ySpeed;
                break;
            case "NE":
                pos.x += speed.xSpeed;
                pos.y += speed.ySpeed;
                break;
            case "E":
                pos.x += speed.xSpeed;
                break;
            case "SE":
                pos.x += speed.xSpeed;
                pos.y -= speed.ySpeed;
                break;
            case "S":
                pos.y -= speed.ySpeed;
                break;
            case "SW":
                pos.x -= speed.xSpeed;
                pos.y -= speed.ySpeed;
                break;
            case "W":
                pos.x -= speed.xSpeed;
                break;
            case "NW":
                pos.x -= speed.xSpeed;
                pos.y += speed.ySpeed;
                break;
            }
    }

    private String getRandomDirection() {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        Random random = new Random();
        return directions[random.nextInt(directions.length)];
    }

    private void playerMovement() {
        Position pos = cg.getPosition(player);
        Speed speed = cg.getSpeed(player);
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
