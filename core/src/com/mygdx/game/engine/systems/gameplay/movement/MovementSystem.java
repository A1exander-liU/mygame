package com.mygdx.game.engine.systems.gameplay.movement;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.utils.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Speed;

public class MovementSystem extends EntitySystem {
    private final Entity player;
    ComponentGrabber cg;

    public MovementSystem(ComponentGrabber cg) {
        super(3);
        this.cg = cg;
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
}
