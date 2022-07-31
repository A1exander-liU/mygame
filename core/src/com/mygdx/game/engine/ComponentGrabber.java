package com.mygdx.game.engine;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.DetectionProximity;
import com.mygdx.game.engine.components.EnemyStateMachine;
import com.mygdx.game.engine.components.MovementBehavior;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.StateComponent;
import com.mygdx.game.engine.components.Steering;
import com.mygdx.game.engine.components.Camera;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.Spawn;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.Health;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Player;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Speed;

public class ComponentGrabber {
    MyGame root;

    public ComponentGrabber(MyGame root) {
        this.root = root;
    }

    public Camera getCamera(Entity entity) {
        ComponentMapper<Camera> camera = ComponentMapper.getFor(Camera.class);
        return camera.get(entity);
    }

    public DetectionProximity getDetectionProximity(Entity entity) {
        ComponentMapper<DetectionProximity> detectionProximity = ComponentMapper.getFor(DetectionProximity.class);
        return detectionProximity.get(entity);
    }

    public Enemy getEnemy(Entity entity) {
        ComponentMapper<Enemy> enemy = ComponentMapper.getFor(Enemy.class);
        return enemy.get(entity);
    }

    public EnemyStateMachine getStateMachine(Entity entity) {
        ComponentMapper<EnemyStateMachine> enemyStateMachine = ComponentMapper.getFor(EnemyStateMachine.class);
        return enemyStateMachine.get(entity);
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

    public MovementBehavior getMovementBehavior(Entity entity) {
        ComponentMapper<MovementBehavior> movementBehavior = ComponentMapper.getFor(MovementBehavior.class);
        return movementBehavior.get(entity);
    }

    public Name getName(Entity entity) {
        ComponentMapper<Name> name = ComponentMapper.getFor(Name.class);
        return name.get(entity);
    }

    public ParameterComponent getParameters(Entity entity) {
        ComponentMapper<ParameterComponent> parameterComponent = ComponentMapper.getFor(ParameterComponent.class);
        return parameterComponent.get(entity);
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

    public Spawn getSpawn(Entity entity) {
        ComponentMapper<Spawn> spawn = ComponentMapper.getFor(Spawn.class);
        return spawn.get(entity);
    }

    public Speed getSpeed(Entity entity) {
        ComponentMapper<Speed> speed = ComponentMapper.getFor(Speed.class);
        return speed.get(entity);
    }

    public StateComponent getStateComponent(Entity entity) {
        ComponentMapper<StateComponent> stateComponent = ComponentMapper.getFor(StateComponent.class);
        return stateComponent.get(entity);
    }

    public Steering getSteering(Entity entity) {
        ComponentMapper<Steering> steering = ComponentMapper.getFor(Steering.class);
        return steering.get(entity);
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
