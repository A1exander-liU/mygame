package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.components.EnemyComponent;
import com.mygdx.game.entityComponentSystem.components.NameComponent;
import com.mygdx.game.entityComponentSystem.components.SpriteComponent;
import com.mygdx.game.entityComponentSystem.components.HealthComponent;
import com.mygdx.game.entityComponentSystem.components.IDComponent;
import com.mygdx.game.entityComponentSystem.components.PositionComponent;
import com.mygdx.game.entityComponentSystem.components.SizeComponent;
import com.mygdx.game.entityComponentSystem.components.SpeedComponent;

public class EntityFactory {
    ComponentGrabber cg;
    MyGame root;

    public EntityFactory(ComponentGrabber cg, MyGame root) {
        this.cg = cg;
        this.root = root;
    }

    public Entity makeEnemy(String name, int width, int height, float xSpeed, float ySpeed,
                          int maxHealth, String pathToImg) {
        Entity enemy = new Entity();
        addEnemyComponents(enemy);
        modifyComponentValues(enemy, name, width, height, xSpeed, ySpeed, maxHealth, pathToImg);
        root.engine.addEntity(enemy);
        return enemy;
    }

    private void addEnemyComponents(Entity entity) {
        entity.add(new EnemyComponent());
        entity.add(new SpriteComponent());
        entity.add(new HealthComponent());
        entity.add(new IDComponent());
        entity.add(new NameComponent());
        entity.add(new PositionComponent());
        entity.add(new SizeComponent());
        entity.add(new SpeedComponent());
    }

    private void modifyComponentValues
            (Entity entity, String name, int width, int height,float xSpeed, float ySpeed,
             int maxHealth, String pathToImg) {
        SpriteComponent enemySpriteComponent = cg.getSprite(entity);
        HealthComponent enemyHealthComponent = cg.getHealth(entity);
        NameComponent enemyNameComponent = cg.getName(entity);
        SizeComponent enemySizeComponent = cg.getSize(entity);
        SpeedComponent enemySpeedComponent = cg.getSpeed(entity);
        enemyNameComponent.name = name;
        enemySizeComponent.width = width;
        enemySizeComponent.height = height;
        enemySpeedComponent.xSpeed = xSpeed;
        enemySpeedComponent.ySpeed = ySpeed;
        enemyHealthComponent.maxHealth = maxHealth;
        enemyHealthComponent.currentHealth = maxHealth;
        enemySpriteComponent.texture = new Texture(Gdx.files.internal(pathToImg));
    }
}
