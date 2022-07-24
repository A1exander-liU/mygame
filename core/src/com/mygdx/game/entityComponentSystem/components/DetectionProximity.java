package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.Families;

public class DetectionProximity implements Component, Proximity<Vector2> {
    public Entity entity;
    public Steering owner;
    public ProximityCallback<Vector2> callback;
    // the detection zone is going to be a square AABB
    // the detection radius is half the side of the square AABB
    // centered at the characters position
    public float detectionRadius;
    public int neighbourCount = 0;
    MyGame root;

    // finds agents in the immediate area
    // the agents found are passed to reportNeighbour of the specified callback
    // report neighbour is to determine if the neighbour is valid
    // true if valid else false
    // findNeighbours calls the report neighbour so neighbourCount
    // is actually updated


    // Proximity
    // build a box around the character and see if it overlaps with anything

    public DetectionProximity(Entity entity, float detectionRadius, MyGame root) {
        this.entity = entity;
        this.detectionRadius = detectionRadius;
        owner = entity.getComponent(Steering.class);
        this.callback = null;
        this.root = root;
    }

    @Override
    public Steerable<Vector2> getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Steerable<Vector2> owner) {
        this.owner = (Steering) owner;
    }

    // collisionAvoidance uses our findNeighbours implementation
    @Override
    public int findNeighbors(ProximityCallback<Vector2> callback) {
        ImmutableArray<Entity> obstacles = root.engine.getEntitiesFor(Families.obstacles);
        this.callback = callback;
        neighbourCount = 0;
        queryForNeighbors(obstacles);
        for (int i = 0; i < obstacles.size(); i++) {
            Steering steering = obstacles.get(i).getComponent(Steering.class);
        }
        this.callback = null;
//        getCharViewArea();
//        callback.reportNeighbor(owner);
        // need to go through all the neighbours here
        // determine all potential overlaps (broad phase detection)
        // all objects in the character view area
        // need to define a view area
        return neighbourCount;
    }

    private Rectangle getCharViewArea() {
        // view area is based on char position and detection radius
        Position pos = entity.getComponent(Position.class);
        Size size = entity.getComponent(Size.class);
        return new Rectangle(
                // *2 for both sides since
                pos.x - detectionRadius,
                pos.y - detectionRadius,
                size.width + detectionRadius*2,
                size.height + detectionRadius*2
        );
        // cg.getSteering(entity)
        // cg.getProximity(entity)
    }

    // determine the most imminent collision from all the neighbours
//    @Override
//    public boolean reportNeighbor(Steerable<Vector2> neighbor) {
//        // to determine potential overlaps in the world (like broad phase)
//        // need to only check nearby stuff
//        // check to see if anything overlaps this guy
//        // to determine the nearby neighbours
//        Steering owner = (Steering) neighbor;
//        Position pos  = owner.entity.getComponent(Position.class);
//        Size size = owner.entity.getComponent(Size.class);
//
//        Rectangle detectionArea = getCharViewArea();
//        Rectangle ownerArea = new Rectangle(
//                pos.x, pos.y, size.width, size.height
//        );
//        // not valid neighbour is itself, don't consider the owner itself
//
//        // if not itself check if it overlaps if it does, increase neighbour coont
//        // and return true since it's valid
//        if (owner != this.owner) {
//            // seeing if it overlaps the detection area
//            // if it overlaps that means this neighbour is nearby
//            if (detectionArea.overlaps(ownerArea)) {
//                neighbourCount++;
//                return true;
//            }
//        }
//        return false;
//    }

    private void queryForNeighbors(ImmutableArray<Entity> obstacles) {
        
    }
}
