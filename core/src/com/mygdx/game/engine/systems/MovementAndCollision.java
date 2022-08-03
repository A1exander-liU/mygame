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

public class MovementAndCollision extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> enemies;
    private MapObjects spawnPoints;
    private Entity player;
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;

    public MovementAndCollision(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(2);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        spawnPoints = gameMapProperties.tiledMap.getLayers().get("Enemy Spawns").getObjects();
        entities = MyGame.engine.getEntitiesFor(Families.collisions);
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
        // now we can detect and resolve any collisions
        // *will need to implement broad phase detection in future*
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            if (cg.getEnemy(entity) != null)
                keepEntityInsideSpawnZone(entity);
        }
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

    private void keepEntityInsideSpawnZone(Entity entity) {
        // this makes the enemy warp back to spawn since hunting is set back to false when player too far
        // need to move enemy back inside spawn point first
        if (cg.getEnemy(entity).state == Enemy.States.WANDER) {
            ID id = cg.getID(entity);
            Position pos = cg.getPosition(entity);
            Rectangle spawnZone = ((RectangleMapObject) spawnPoints.get(id.ID - 1)).getRectangle();
            if (pos.x < spawnZone.x)
                pos.x = spawnZone.x;
            else if (pos.x > spawnZone.x + spawnZone.width)
                pos.x = spawnZone.x + spawnZone.width;
            else if (pos.y < spawnZone.y)
                pos.y = spawnZone.y;
            else if (pos.y > spawnZone.y + spawnZone.height)
                pos.y = spawnZone.y + spawnZone.height;
        }
    }
}
