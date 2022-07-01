package com.mygdx.game.entityComponentSystem.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.components.EntitySprite;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;

import java.util.Random;

public class MovementSystem extends IntervalSystem {
    private ImmutableArray<Entity> entities;
    ComponentGrabber cg;
    MyGame root;

    public MovementSystem(ComponentGrabber cg, MyGame root) {
        super(0.75f, 1);
        this.cg = cg;
        this.root = root;
    }

    @Override
    public void addedToEngine(Engine engine) {
        /* selecting all entities which have a position and speed
        * basically saying movement system for entities that have both position and speed component */
        entities = root.engine.getEntitiesFor(Family.all(EntitySprite.class, Position.class, Size.class, Speed.class, ID.class).get());
        System.out.println(entities);
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

//    @Override
//    // will update every frame
//    public void update(float deltaTime) {
//        for (int i = 0; i < entities.size() - 1; i++) {
//            System.out.println("running movement");
//            moveEnemy(getRandomDirection(), i, deltaTime);
//        }
//    }

    @Override
    protected void updateInterval() {
        System.out.println("movement system: running");
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            Position pos = cg.getPosition(entity);
            moveEnemy(getRandomDirection(), entity);
            System.out.println(pos.x + ", " + pos.y);
        }
    }

    private void moveEnemy(String direction, Entity entity) {
        Position pos = cg.getPosition(entity);
        Speed speed = cg.getSpeed(entity);
        pos.oldX = pos.x;
        pos.oldY = pos.y;
        System.out.println("Chosen Direction: " + direction);
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
}
