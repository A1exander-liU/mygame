package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.Health;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Speed;

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
    }

    private void modifyComponentValues
            (Entity entity, String name, int width, int height,float xSpeed, float ySpeed,
             int maxHealth, String pathToImg) {
        Sprite enemyEntitySprite = cg.getSprite(entity);
        Health enemyHealth = cg.getHealth(entity);
        Name enemyName = cg.getName(entity);
        Size enemySize = cg.getSize(entity);
        Speed enemySpeed = cg.getSpeed(entity);
        enemyName.name = name;
        enemySize.width = width;
        enemySize.height = height;
        enemySpeed.xSpeed = xSpeed;
        enemySpeed.ySpeed = ySpeed;
        enemyHealth.maxHealth = maxHealth;
        enemyHealth.currentHealth = maxHealth;
        enemyEntitySprite.texture = new Texture(Gdx.files.internal(pathToImg));
    }
}
