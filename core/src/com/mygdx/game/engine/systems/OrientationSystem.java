package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Direction;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Position;

public class OrientationSystem extends EntitySystem {
    ComponentGrabber cg;
    ImmutableArray<Entity> enemies;
    Entity player;
    Vector2 direction;

    public OrientationSystem(ComponentGrabber cg) {
        super(6);
        this.cg = cg;
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        direction = new Vector2();
    }

    @Override
    public void update(float delta) {
        updateEntityOrientation(player);
//        for (int i = 0; i < enemies.size(); i++) {
//            Entity entity = enemies.get(i);
//            updateEntityOrientation(entity);
//        }
    }

    private void updateEntityOrientation(Entity entity) {
        if (entityMoved(cg.getPosition(entity))) {
            updateOrientation(entity);
        }
    }

    private boolean entityMoved(Position pos) {
        return pos.oldX != pos.x || pos.oldY != pos.y;
    }

    private void updateOrientation(Entity entity) {
        Position pos = cg.getPosition(entity);
        direction.x = pos.x - pos.oldX;
        direction.y = pos.y - pos.oldY;
        Vector2 baseDirection = calcBaseDirection(direction);
        if (vectorsEqual(baseDirection, Direction.NORTH.direction))
            cg.getOrientation(entity).orientation = Direction.NORTH;
        else if (vectorsEqual(baseDirection, Direction.NORTHEAST.direction))
            cg.getOrientation(entity).orientation = Direction.NORTHEAST;
        else if (vectorsEqual(baseDirection, Direction.EAST.direction))
            cg.getOrientation(entity).orientation = Direction.EAST;
        else if (vectorsEqual(baseDirection, Direction.SOUTHEAST.direction))
            cg.getOrientation(entity).orientation = Direction.SOUTHEAST;
        else if (vectorsEqual(baseDirection, Direction.SOUTH.direction))
            cg.getOrientation(entity).orientation = Direction.SOUTH;
        else if (vectorsEqual(baseDirection, Direction.SOUTHWEST.direction))
            cg.getOrientation(entity).orientation = Direction.SOUTHWEST;
        else if (vectorsEqual(baseDirection, Direction.WEST.direction))
            cg.getOrientation(entity).orientation = Direction.WEST;
        else if (vectorsEqual(baseDirection, Direction.NORTHWEST.direction))
            cg.getOrientation(entity).orientation = Direction.NORTHWEST;
    }

    private Vector2 calcBaseDirection(Vector2 direction) {
        Vector2 baseDirection = new Vector2();
        // divide by the absolute value of itself so the value does not change
        // so you just end up with 0, -1, or 1
        if (direction.x != 0)
            baseDirection.x = direction.x / Math.abs(direction.x);
        if (direction.y != 0)
            baseDirection.y = direction.y / Math.abs(direction.y);
        return baseDirection;
    }

    private boolean vectorsEqual(Vector2 a, Vector2 b) {
        return a.x == b.x && a.y == b.y;
    }
}
