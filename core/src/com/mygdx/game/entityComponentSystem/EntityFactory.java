package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.components.EntitySprite;
import com.mygdx.game.entityComponentSystem.components.Health;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Name;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;

public class EntityFactory {
    ComponentGrabber cg;
    MyGame root;

    public EntityFactory(ComponentGrabber cg, MyGame root) {
        this.cg = cg;
        this.root = root;
    }

    public void makeEnemy(String name, int width, int height, float xSpeed, float ySpeed,
                          int maxHealth, String pathToImg) {
        Entity enemy = new Entity();
        addEnemyComponents(enemy);
        modifyComponentValues(enemy, name, width, height, xSpeed, ySpeed, maxHealth, pathToImg);
        root.engine.addEntity(enemy);
    }

    private void addEnemyComponents(Entity entity) {
        entity.add(new EntitySprite());
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
        EntitySprite enemyEntitySprite = cg.getSprite(entity);
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
