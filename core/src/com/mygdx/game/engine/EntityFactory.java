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

    public Entity makeEnemy(String name, int width, int height, float xSpeed, float ySpeed,
                          int maxHealth, String pathToImg) {
        Entity enemy = new Entity();
        addEnemyComponents(enemy);
        modifyComponentValues(enemy, name, width, height, xSpeed, ySpeed, maxHealth, pathToImg);
        MyGame.engine.addEntity(enemy);
        return enemy;
    }

    private void addEnemyComponents(Entity entity) {
        entity.add(new Enemy());
        entity.add(new Sprite());
        entity.add(new Health());
        entity.add(new ID());
        entity.add(new Name());
        entity.add(new Position());
        entity.add(new Size());
        entity.add(new Speed());
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
