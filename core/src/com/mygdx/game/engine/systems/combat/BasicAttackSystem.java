package com.mygdx.game.engine.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Orientation;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;

public class BasicAttackSystem extends EntitySystem {
    ComponentGrabber cg;
    GameMapProperties gameMapProperties;
    Entity player;

    public BasicAttackSystem(ComponentGrabber cg, GameMapProperties gameMapProperties) {
        super(8);
        this.cg = cg;
        this.gameMapProperties = gameMapProperties;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
    }

    @Override
    public void update(float delta) {
        // click mouse button or some key, does an attack
        // attack: arc, sweeping motion
        // check if the sweeping motion collides with an enemy
        // if they collide the enemy takes some dmg
        // different weapon type will have different attack:
        // slash, pierce
        // slash will perform a sweeping motion
        // pierce will extend outwards
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // attack in current facing direction (use orientation)
            performSlash();
        }
    }

    private void performSlash() {
        Orientation orientation = cg.getOrientation(player);
        Position pos = cg.getPosition(player);
        Size size = cg.getSize(player);
        
    }
}
