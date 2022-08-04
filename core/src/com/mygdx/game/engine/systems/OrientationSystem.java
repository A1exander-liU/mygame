package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
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
        updatePlayerOrientation();
    }

    private void updatePlayerOrientation() {
        if (playerMoved(cg.getPosition(player))) {
            updateOrientation(player);
        }
    }

    private boolean playerMoved(Position pos) {
        return pos.oldX != pos.x || pos.oldY != pos.y;
    }

    private void updateOrientation(Entity entity) {
        Position pos = cg.getPosition(entity);
        direction.x = pos.x - pos.oldX;
        direction.y = pos.y - pos.oldY;
        Vector2 baseDirection = calcBaseDirection(direction);
    }

    private Vector2 calcBaseDirection(Vector2 direction) {
        return new Vector2(
                direction.x / Math.abs(direction.x),
                direction.y / Math.abs(direction.y)
        );
    }
}
