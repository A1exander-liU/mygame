package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.engine.components.EnemyStateMachine;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.MapChar;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Speed;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.StateComponent;

public class EntityFactory {
    ComponentGrabber cg;

    public EntityFactory(ComponentGrabber cg) {
        this.cg = cg;
    }

    // also need method to create a player character

    public void makeEnemy(String name) {
        // name provided will determine the kind of enemy to create
        // make mobEntity only add the components
        // entityFactory will actually modify the component values
        // will also add the entity to the map
        MobEntity enemy = new MobEntity();
        modifyComponentValues(enemy, name);
    }

    private void modifyComponentValues(Entity entity, String name) {
        Name enemyName = cg.getName(entity);
        Size size = cg.getSize(entity);
        Speed speed = cg.getSpeed(entity);
        Sprite entitySprite = cg.getSprite(entity);
        EnemyStateMachine stateMachine = cg.getStateMachine(entity);
        enemyName.name = name;
        size.width = 32;
        size.height = 32;
        speed.xSpeed = 2;
        speed.ySpeed = 2;
        entitySprite.texture = new Texture(Gdx.files.internal("testPlayer.png"));
        stateMachine.setInitialState(EnemyState.IDLE);
    }
}
