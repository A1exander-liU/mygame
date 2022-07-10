package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.entityComponentSystem.components.Camera;
import com.mygdx.game.entityComponentSystem.components.Enemy;
import com.mygdx.game.entityComponentSystem.components.EntitySprite;
import com.mygdx.game.entityComponentSystem.components.Health;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Name;
import com.mygdx.game.entityComponentSystem.components.Player;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;

public class ComponentGrabber {

    public ComponentGrabber() {}

    public Camera getCamera(Entity entity) {
        ComponentMapper<Camera> camera = ComponentMapper.getFor(Camera.class);
        return camera.get(entity);
    }

    public Enemy getEnemy(Entity entity) {
        ComponentMapper<Enemy> enemy = ComponentMapper.getFor(Enemy.class);
        return enemy.get(entity);
    }

    public EntitySprite getSprite(Entity entity) {
        ComponentMapper<EntitySprite> img = ComponentMapper.getFor(EntitySprite.class);
        return img.get(entity);
    }

    // to retrieve components of an entity (faster than <Entity>.getComponent(<Component> class))
    public Health getHealth(Entity entity) {
        ComponentMapper<Health> hp = ComponentMapper.getFor(Health.class);
        return hp.get(entity);
    }

    public ID getID(Entity entity) {
        ComponentMapper<ID> id = ComponentMapper.getFor(ID.class);
        return id.get(entity);
    }

    public Name getName(Entity entity) {
        ComponentMapper<Name> name = ComponentMapper.getFor(Name.class);
        return name.get(entity);
    }

    public Player getPlayer(Entity entity) {
        ComponentMapper<Player> player = ComponentMapper.getFor(Player.class);
        return player.get(entity);
    }

    public Position getPosition(Entity entity) {
        ComponentMapper<Position> pos = ComponentMapper.getFor(Position.class);
        return pos.get(entity);
    }

    public Size getSize(Entity entity) {
        ComponentMapper<Size> size = ComponentMapper.getFor(Size.class);
        return size.get(entity);
    }

    public Speed getSpeed(Entity entity) {
        ComponentMapper<Speed> speed = ComponentMapper.getFor(Speed.class);
        return speed.get(entity);
    }
}
