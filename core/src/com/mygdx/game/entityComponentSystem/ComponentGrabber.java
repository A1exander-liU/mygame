package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.components.CameraComponent;
import com.mygdx.game.entityComponentSystem.components.EnemyComponent;
import com.mygdx.game.entityComponentSystem.components.IDComponent;
import com.mygdx.game.entityComponentSystem.components.SpriteComponent;
import com.mygdx.game.entityComponentSystem.components.HealthComponent;
import com.mygdx.game.entityComponentSystem.components.NameComponent;
import com.mygdx.game.entityComponentSystem.components.PlayerComponent;
import com.mygdx.game.entityComponentSystem.components.PositionComponent;
import com.mygdx.game.entityComponentSystem.components.SizeComponent;
import com.mygdx.game.entityComponentSystem.components.SpeedComponent;

public class ComponentGrabber {
    MyGame root;

    public ComponentGrabber(MyGame root) {
        this.root = root;
    }

    public CameraComponent getCamera(Entity entity) {
        ComponentMapper<CameraComponent> camera = ComponentMapper.getFor(CameraComponent.class);
        return camera.get(entity);
    }

    public EnemyComponent getEnemy(Entity entity) {
        ComponentMapper<EnemyComponent> enemy = ComponentMapper.getFor(EnemyComponent.class);
        return enemy.get(entity);
    }

    public SpriteComponent getSprite(Entity entity) {
        ComponentMapper<SpriteComponent> img = ComponentMapper.getFor(SpriteComponent.class);
        return img.get(entity);
    }

    // to retrieve components of an entity (faster than <Entity>.getComponent(<Component> class))
    public HealthComponent getHealth(Entity entity) {
        ComponentMapper<HealthComponent> hp = ComponentMapper.getFor(HealthComponent.class);
        return hp.get(entity);
    }

    public IDComponent getID(Entity entity) {
        ComponentMapper<IDComponent> id = ComponentMapper.getFor(IDComponent.class);
        return id.get(entity);
    }

    public NameComponent getName(Entity entity) {
        ComponentMapper<NameComponent> name = ComponentMapper.getFor(NameComponent.class);
        return name.get(entity);
    }

    public PlayerComponent getPlayer(Entity entity) {
        ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
        return player.get(entity);
    }

    public PositionComponent getPosition(Entity entity) {
        ComponentMapper<PositionComponent> pos = ComponentMapper.getFor(PositionComponent.class);
        return pos.get(entity);
    }

    public SizeComponent getSize(Entity entity) {
        ComponentMapper<SizeComponent> size = ComponentMapper.getFor(SizeComponent.class);
        return size.get(entity);
    }

    public SpeedComponent getSpeed(Entity entity) {
        ComponentMapper<SpeedComponent> speed = ComponentMapper.getFor(SpeedComponent.class);
        return speed.get(entity);
    }

    public Entity findEntity(int entityID) {
        ImmutableArray<Entity> entities = root.engine.getEntities();
        for (Entity entity : entities) {
            int id = getID(entity).ID;
            if (id == entityID)
                return entity;
        }
        return null;
    }
}
