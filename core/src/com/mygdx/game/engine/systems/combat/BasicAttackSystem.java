package com.mygdx.game.engine.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Direction;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.AttackRange;
import com.mygdx.game.engine.components.Orientation;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.utils.GameRaycastCollisionDetector;

public class BasicAttackSystem extends EntitySystem {
    ComponentGrabber cg;
    GameMapProperties gameMapProperties;
    Entity player;
    ImmutableArray<Entity> enemies;

    public BasicAttackSystem(ComponentGrabber cg, GameMapProperties gameMapProperties) {
        super(8);
        this.cg = cg;
        this.gameMapProperties = gameMapProperties;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            performSlash();
        }
    }

    private void performSlash() {
        Orientation orientation = cg.getOrientation(player);
        Position pos = cg.getPosition(player);
        Size size = cg.getSize(player);
        Entity attack = makeAttackAreaEntity();
        AttackRange attackRange = cg.getAttackRange(attack);
        if (orientation.orientation == Direction.EAST) {
            attackRange.xRange = 16;
            Vector2 start = new Vector2(
                    pos.x + size.width,
                    pos.y + (size.height / 2));
            Vector2 end = new Vector2(start.x + attackRange.xRange, start.y);
            Ray<Vector2> ray = new Ray<>(start, end);
            Entity attackedEnemy = attackLandedWho(ray);
            if (someoneWasAttacked(attackedEnemy)) {
                ParameterComponent playerParams = cg.getParameters(player);
                ParameterComponent enemyParams = cg.getParameters(attackedEnemy);
                enemyParams.health.currentHealth -= playerParams.damage;
            }
        }
        MyGame.engine.removeEntity(attack);
    }

    private Entity makeAttackAreaEntity() {
        return new Entity()
                .add(new Position())
                .add(new AttackRange());
    }

    private Entity attackLandedWho(Ray<Vector2> attackRay) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            boolean collisionDetected = checkCollisions(attackRay, entity);
            if (collisionDetected)
                return entity;
        }
        return null;
    }

    private boolean checkCollisions(Ray<Vector2> ray, Entity enemy) {
        Vector2 intersection = new Vector2();
        Position pos = cg.getPosition(enemy);
        Size size = cg.getSize(enemy);
        boolean left = Intersector.intersectLines(
                ray.start, ray.end,
                new Vector2(pos.x, pos.y), new Vector2(pos.x, pos.y + size.height),
                intersection
        );
        boolean top = Intersector.intersectLines(
                ray.start, ray.end,
                new Vector2(pos.x, pos.y + size.height), new Vector2(pos.x + size.width, pos.y + size.height),
                intersection
        );
        boolean right = Intersector.intersectLines(
                ray.start, ray.end,
                new Vector2(pos.x + size.width, pos.y + size.height), new Vector2(pos.x + size.width, pos.y),
                intersection
        );
        boolean bottom = Intersector.intersectLines(
                ray.start, ray.end,
                new Vector2(pos.x + size.width, pos.y), new Vector2(pos.x, pos.y),
                intersection
        );
        if (left || top || right || bottom)
            return true;
        return false;
    }

    private boolean someoneWasAttacked(Entity enemy) {
        return enemy != null;
    }
}
