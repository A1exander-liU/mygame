package com.mygdx.game.engine.utils;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.engine.components.AttackRange;
import com.mygdx.game.engine.components.DetectionProximity;
import com.mygdx.game.engine.components.EnemyStateMachine;
import com.mygdx.game.engine.components.ExpComponent;
import com.mygdx.game.engine.components.InventoryComponent;
import com.mygdx.game.engine.components.DescriptionComponent;
import com.mygdx.game.engine.components.InventoryItemComponent;
import com.mygdx.game.engine.components.InventorySlotComponent;
import com.mygdx.game.engine.components.Item;
import com.mygdx.game.engine.components.LevelComponent;
import com.mygdx.game.engine.components.ManaComponent;
import com.mygdx.game.engine.components.MovementBehavior;
import com.mygdx.game.engine.components.Orientation;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.SpawnArea;
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
import com.mygdx.game.engine.components.QuantityComponent;
import com.mygdx.game.engine.components.RarityComponent;

public class ComponentGrabber {

    public ComponentGrabber() {}

    public AttackRange getAttackRange(Entity entity) {
        ComponentMapper<AttackRange> attackRange = ComponentMapper.getFor(AttackRange.class);
        return attackRange.get(entity);
    }

    public Camera getCamera(Entity entity) {
        ComponentMapper<Camera> camera = ComponentMapper.getFor(Camera.class);
        return camera.get(entity);
    }

    public DescriptionComponent getDescription(Entity entity) {
        ComponentMapper<DescriptionComponent> description = ComponentMapper.getFor(DescriptionComponent.class);
        return description.get(entity);
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

    public ExpComponent getExp(Entity entity) {
        ComponentMapper<ExpComponent> exp = ComponentMapper.getFor(ExpComponent.class);
        return exp.get(entity);
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

    public InventoryComponent getInventory(Entity entity) {
        ComponentMapper<InventoryComponent> inventory = ComponentMapper.getFor(InventoryComponent.class);
        return inventory.get(entity);
    }

    public InventoryItemComponent getInventoryItem(Entity entity) {
        ComponentMapper<InventoryItemComponent> inventoryItem = ComponentMapper.getFor(InventoryItemComponent.class);
        return inventoryItem.get(entity);
    }

    public InventorySlotComponent getInventorySlot(Entity entity) {
        ComponentMapper<InventorySlotComponent> inventorySlot = ComponentMapper.getFor(InventorySlotComponent.class);
        return inventorySlot.get(entity);
    }

    public Item getItem(Entity entity) {
        ComponentMapper<Item> item = ComponentMapper.getFor(Item.class);
        return item.get(entity);
    }

    public LevelComponent getLevel(Entity entity) {
        ComponentMapper<LevelComponent> level = ComponentMapper.getFor(LevelComponent.class);
        return level.get(entity);
    }

    public ManaComponent getMana(Entity entity) {
        ComponentMapper<ManaComponent> mana = ComponentMapper.getFor(ManaComponent.class);
        return mana.get(entity);
    }

    public MovementBehavior getMovementBehavior(Entity entity) {
        ComponentMapper<MovementBehavior> movementBehavior = ComponentMapper.getFor(MovementBehavior.class);
        return movementBehavior.get(entity);
    }

    public Name getName(Entity entity) {
        ComponentMapper<Name> name = ComponentMapper.getFor(Name.class);
        return name.get(entity);
    }

    public Orientation getOrientation(Entity entity) {
        ComponentMapper<Orientation> orientation = ComponentMapper.getFor(Orientation.class);
        return orientation.get(entity);
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

    public QuantityComponent getQuantity(Entity entity) {
        ComponentMapper<QuantityComponent> quantity = ComponentMapper.getFor(QuantityComponent.class);
        return quantity.get(entity);
    }

    public RarityComponent getRarity(Entity entity) {
        ComponentMapper<RarityComponent> rarity = ComponentMapper.getFor(RarityComponent.class);
        return rarity.get(entity);
    }

    public Size getSize(Entity entity) {
        ComponentMapper<Size> size = ComponentMapper.getFor(Size.class);
        return size.get(entity);
    }

    public Spawn getSpawn(Entity entity) {
        ComponentMapper<Spawn> spawn = ComponentMapper.getFor(Spawn.class);
        return spawn.get(entity);
    }

    public SpawnArea getSpawnArea(Entity entity) {
        ComponentMapper<SpawnArea> spawnArea = ComponentMapper.getFor(SpawnArea.class);
        return spawnArea.get(entity);
    }

    public Speed getSpeed(Entity entity) {
        ComponentMapper<Speed> speed = ComponentMapper.getFor(Speed.class);
        return speed.get(entity);
    }

    public Sprite getSprite(Entity entity) {
        ComponentMapper<Sprite> img = ComponentMapper.getFor(Sprite.class);
        return img.get(entity);
    }

    public StateComponent getStateComponent(Entity entity) {
        ComponentMapper<StateComponent> stateComponent = ComponentMapper.getFor(StateComponent.class);
        return stateComponent.get(entity);
    }

    public Steering getSteering(Entity entity) {
        ComponentMapper<Steering> steering = ComponentMapper.getFor(Steering.class);
        return steering.get(entity);
    }
}
