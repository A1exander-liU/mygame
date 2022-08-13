package com.mygdx.game.engine.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
            // also need to think about inventory system

            // have system for different melee attack styles
            // have system for stab,slash,smash
            // stab: use rays, fast, low dmg, small aoe
            // slash: use arcs, average, average dmg, medium aoe
            // smash: use circles, slow, high dmg, large aoe

            // animation would be played here
            // if player moves during the attack animation, attack animation and
            // the attack is cancelled
            // if (attackAnimationIsPlaying)
            //    if (playerMoved)
            //       cancelAnimation()
            //       return (to prevent perform stab from happening)
            performStab();
        }
    }

    private void performStab() {
        Orientation orientation = cg.getOrientation(player);
        Position pos = cg.getPosition(player);
        Size size = cg.getSize(player);
        Entity attack = makeAttackAreaEntity();
        AttackRange attackRange = cg.getAttackRange(attack);
        Vector2 start = new Vector2();
        Ray<Vector2> ray = new Ray<>(new Vector2(), new Vector2());
        if (orientation.orientation == Direction.EAST) {
            // set the range
            // create the start and end points for ray
            // iterate through enemies to see if an enemy was hit
            // apply damage if they were hit
            attackRange.xRange = 16;
            start.x = pos.x + size.width;
            start.y = pos.y + (size.height / 2);
            Vector2 length = new Vector2(attackRange.xRange, attackRange.yRange)
                    .scl(Direction.EAST.direction);
            Vector2 end = length.add(start);
            ray = new Ray<>(start, end);
        }
        else if (orientation.orientation == Direction.SOUTH) {
            attackRange.yRange = 16;
            start.x = pos.x + (size.width / 2);
            start.y = pos.y;
            Vector2 length = new Vector2(attackRange.xRange, attackRange.yRange)
                    .scl(Direction.SOUTH.direction);
            Vector2 end = length.add(start);
            ray = new Ray<>(start, end);

        }
        else if (orientation.orientation == Direction.WEST) {
            attackRange.xRange = 16;
            start.x = pos.x;
            start.y = pos.y + (size.height / 2);
            Vector2 length = new Vector2(attackRange.xRange, attackRange.yRange)
                    .scl(Direction.WEST.direction);
            Vector2 end = length.add(start);
            ray = new Ray<>(start, end);

        }
        else if (orientation.orientation == Direction.NORTH) {
            attackRange.yRange = 16;
            start.x = pos.x + (size.width / 2);
            start.y = pos.y + size.height;
            Vector2 length = new Vector2(attackRange.xRange, attackRange.yRange)
                    .scl(Direction.NORTH.direction);
            Vector2 end = length.add(start);
            ray = new Ray<>(start, end);
        }
        else if (orientation.orientation == Direction.NORTHEAST) {
            getDiagonalRange(attackRange, 16);
            start.x = pos.x + size.width;
            start.y = pos.y + size.height;
            Vector2 length = new Vector2(attackRange.xRange, attackRange.yRange)
                    .scl(Direction.NORTHEAST.direction);
            Vector2 end = length.add(start);
            ray = new Ray<>(start, end);
        }
        else if (orientation.orientation == Direction.SOUTHEAST) {
            getDiagonalRange(attackRange, 16);
            start.x = pos.x + size.width;
            start.y = pos.y;
            Vector2 length = new Vector2(attackRange.xRange, attackRange.yRange)
                    .scl(Direction.SOUTHEAST.direction);
            Vector2 end = length.add(start);
            ray = new Ray<>(start, end);
        }
        else if (orientation.orientation == Direction.SOUTHWEST) {
            getDiagonalRange(attackRange, 16);
            start.x = pos.x;
            start.y = pos.y;
            Vector2 length = new Vector2(attackRange.xRange, attackRange.yRange).
                    scl(Direction.SOUTHWEST.direction);
            Vector2 end = length.add(start);
            ray = new Ray<>(start, end);
        }
        else if (orientation.orientation == Direction.NORTHWEST) {
            getDiagonalRange(attackRange, 16);
            start.x = pos.x;
            start.y = pos.y + size.height;
            Vector2 length = new Vector2(attackRange.xRange, attackRange.yRange).
                    scl(Direction.NORTHWEST.direction);
            Vector2 end = length.add(start);
            ray = new Ray<>(start, end);
        }


        Array<Entity> smackedEnemies = attackLandedWho(ray);
        applyDamageToEnemies(smackedEnemies);
        MyGame.engine.removeEntity(attack);
    }

    private Entity makeAttackAreaEntity() {
        return new Entity()
                .add(new Position())
                .add(new AttackRange());
    }

    private Array<Entity> attackLandedWho(Ray<Vector2> attackRay) {
        Array<Entity> smackedEnemies = new Array<>(0);
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            boolean collisionDetected = checkCollisions(attackRay, entity);
            if (collisionDetected)
                smackedEnemies.add(entity);
        }
        return smackedEnemies;
    }

    private boolean checkCollisions(Ray<Vector2> ray, Entity enemy) {
        Polygon enemyRegion = makeEnemyRegion(enemy);
        return Intersector.intersectLinePolygon(ray.start, ray.end, enemyRegion);
    }

    private boolean someoneWasAttacked(Entity enemy) {
        return enemy != null;
    }

    private Polygon makeEnemyRegion(Entity enemy) {
        Position pos = cg.getPosition(enemy);
        Size size = cg.getSize(enemy);
        float[] vertices = {
                pos.x, pos.y, pos.x, pos.y + size.height,
                pos.x + size.width, pos.y + size.height,
                pos.x + size.width, pos.y
        };
        return new Polygon(vertices);
    }

    private void getDiagonalRange(AttackRange attackRange, float range) {
        attackRange.xRange = range;
        attackRange.yRange = range;
    }

    private void applyDamageToEnemies(Array<Entity> smackedEnemies) {
        for (int i = 0; i < smackedEnemies.size; i++) {
            Entity enemy = smackedEnemies.get(i);
            if (someoneWasAttacked(enemy)) {
                System.out.println("smacked");
                ParameterComponent playerParams = cg.getParameters(player);
                ParameterComponent enemyParams = cg.getParameters(enemy);
                enemyParams.health.currentHealth -= playerParams.damage;
            }
        }
    }
}
