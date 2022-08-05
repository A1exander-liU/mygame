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
        if (entityMoved(cg.getPosition(player)))
            updatePlayerOrientation();
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            if (entityMoved(cg.getPosition(entity)))
                updateEnemyOrientation(entity);
        }
    }

    private boolean entityMoved(Position pos) {
        return pos.oldX != pos.x || pos.oldY != pos.y;
    }

    private void updatePlayerOrientation() {
        Position pos = cg.getPosition(player);
        direction.x = pos.x - pos.oldX;
        direction.y = pos.y - pos.oldY;
        Vector2 baseDirection = calcBaseDirection(direction);
        if (vectorsEqual(baseDirection, Direction.NORTH.direction))
            cg.getOrientation(player).orientation = Direction.NORTH;
        else if (vectorsEqual(baseDirection, Direction.NORTHEAST.direction))
            cg.getOrientation(player).orientation = Direction.NORTHEAST;
        else if (vectorsEqual(baseDirection, Direction.EAST.direction))
            cg.getOrientation(player).orientation = Direction.EAST;
        else if (vectorsEqual(baseDirection, Direction.SOUTHEAST.direction))
            cg.getOrientation(player).orientation = Direction.SOUTHEAST;
        else if (vectorsEqual(baseDirection, Direction.SOUTH.direction))
            cg.getOrientation(player).orientation = Direction.SOUTH;
        else if (vectorsEqual(baseDirection, Direction.SOUTHWEST.direction))
            cg.getOrientation(player).orientation = Direction.SOUTHWEST;
        else if (vectorsEqual(baseDirection, Direction.WEST.direction))
            cg.getOrientation(player).orientation = Direction.WEST;
        else if (vectorsEqual(baseDirection, Direction.NORTHWEST.direction))
            cg.getOrientation(player).orientation = Direction.NORTHWEST;
    }

    private void updateEnemyOrientation(Entity entity) {
        Position pos = cg.getPosition(entity);
        direction.x = pos.x - pos.oldX;
        direction.y = pos.y - pos.oldY;

        Vector2 baseDirection = calcBaseDirection(direction);
        // enemy doesn't go in 4 cardinal directions
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
        // enemy movement direction always ends up with a small value when
        // the enemy looks like it is moving in a straight line
        direction.x = clip(direction.x);
        direction.y = clip(direction.y);

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

    private float clip(float a) {
        // -0.5 - 0.5, clip this value to a 0
        // a value >= 0.5 and <= 0.5 still looks like it is travelling in a
        // straight line
        if (a >= -0.5 && a <= 0.5)
            a = 0;
        return a;
    }
}
