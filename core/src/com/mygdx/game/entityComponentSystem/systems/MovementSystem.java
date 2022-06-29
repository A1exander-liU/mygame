package com.mygdx.game.entityComponentSystem.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Speed;

import java.util.Random;

public class MovementSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    ComponentGrabber cg;
    MyGame root;

    public MovementSystem(ComponentGrabber cg, MyGame root) {
        super(1);
        this.cg = cg;
        this.root = root;
    }

    @Override
    public void addedToEngine(Engine engine) {
        /* selecting all entities which have a position and speed
        * basically saying movement system for entities that have both position and speed component */
        entities = root.engine.getEntitiesFor(Family.all(Position.class, Speed.class).get());
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

    @Override
    // will update every frame
    public void update(float deltaTime) {
        for (int i = 0; i < entities.size() - 1; i++) {
            moveEnemy(getRandomDirection(), i, deltaTime);
        }
    }

    private String getRandomDirection() {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        Random random = new Random();
        return directions[random.nextInt(directions.length) - 1];
    }

    private void moveEnemy(String direction, int index, float deltaTime) {
        Entity enemy = entities.get(index);
        Position pos = cg.getPosition(enemy);
        Speed speed = cg.getSpeed(enemy);
        pos.oldX = pos.x;
        pos.oldY = pos.y;
        System.out.println("Chosen Direction: " + direction);
        switch (direction) {
            case "N":
                pos.y += speed.ySpeed * deltaTime;
                break;
            case "NE":
                pos.x += speed.xSpeed * deltaTime;
                pos.y += speed.ySpeed * deltaTime;
                break;
            case "E":
                pos.x += speed.xSpeed * deltaTime;
                break;
            case "SE":
                pos.x += speed.xSpeed * deltaTime;
                pos.y -= speed.ySpeed * deltaTime;
                break;
            case "S":
                pos.y -= speed.ySpeed * deltaTime;
                break;
            case "SW":
                pos.x -= speed.xSpeed * deltaTime;
                pos.y -= speed.ySpeed * deltaTime;
                break;
            case "W":
                pos.x -= speed.xSpeed * deltaTime;
                break;
            case "NW":
                pos.x -= speed.xSpeed * deltaTime;
                pos.y += speed.ySpeed * deltaTime;
                break;
            }
    }

}
