package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.components.AI;
import com.mygdx.game.entityComponentSystem.components.Camera;
import com.mygdx.game.entityComponentSystem.components.Enemy;
import com.mygdx.game.entityComponentSystem.components.Sprite;
import com.mygdx.game.entityComponentSystem.components.Health;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Name;
import com.mygdx.game.entityComponentSystem.components.Player;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;

public class ComponentGrabber {
    MyGame root;

    public ComponentGrabber(MyGame root) {
        this.root = root;
    }

    public AI getAI(Entity entity) {
        ComponentMapper<AI> ai = ComponentMapper.getFor(AI.class);
        return ai.get(entity);
    }

    public Camera getCamera(Entity entity) {
        ComponentMapper<Camera> camera = ComponentMapper.getFor(Camera.class);
        return camera.get(entity);
    }

    public Enemy getEnemy(Entity entity) {
        ComponentMapper<Enemy> enemy = ComponentMapper.getFor(Enemy.class);
        return enemy.get(entity);
    }

    public Sprite getSprite(Entity entity) {
        ComponentMapper<Sprite> img = ComponentMapper.getFor(Sprite.class);
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
