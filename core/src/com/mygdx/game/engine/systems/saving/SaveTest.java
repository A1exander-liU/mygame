package com.mygdx.game.engine.systems.saving;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.Camera;
import com.mygdx.game.engine.components.CollidableComponent;
import com.mygdx.game.engine.components.DetectionProximity;
import com.mygdx.game.engine.components.ExpComponent;
import com.mygdx.game.engine.components.Health;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.InventoryComponent;
import com.mygdx.game.engine.components.Item;
import com.mygdx.game.engine.components.LevelComponent;
import com.mygdx.game.engine.components.ManaComponent;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Orientation;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.Player;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Speed;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.Steering;
import com.mygdx.game.engine.utils.componentutils.Mappers;
import com.mygdx.game.engine.utils.entities.PlayerEntity;

public class SaveTest extends EntitySystem {
    MyGame root;

    public SaveTest(MyGame root) {
        this.root = root;
        String item = "item";
        Json json = new Json();
        String serialized = json.toJson(item);
        System.out.println(json.prettyPrint(serialized));

    }

    @Override
    public void update(float delta) {

    }

    static class SaveTestItem {
        int id;
        Array<Entity> entities;
        Entity entity;

        public SaveTestItem(int id) {
            this.id = id;
            entities = new Array<>();
            entity = new Entity();
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    static class SavedPlayer {
        Camera camera;
        DetectionProximity detectionProximity;
        ExpComponent expComponent;
        Health health;
        ID id;
        InventoryComponent inventoryComponent;
        Item item;
        LevelComponent levelComponent;
        ManaComponent manaComponent;
        Name name;
        Orientation orientation;
        ParameterComponent parameterComponent;
        Player player;
        Position position;
        Size size;
        Speed speed;
        Sprite sprite;
        Steering steering;
        CollidableComponent collidableComponent;

        public SavedPlayer(PlayerEntity playerToSave) {
            camera = Mappers.camera.get(playerToSave);
            detectionProximity = Mappers.detectionProximity.get(playerToSave);
            expComponent = Mappers.exp.get(playerToSave);
            health = Mappers.health.get(playerToSave);
            id = Mappers.id.get(playerToSave);
            inventoryComponent = Mappers.inventory.get(playerToSave);
            item = Mappers.item.get(playerToSave);
            levelComponent = Mappers.level.get(playerToSave);
            manaComponent = Mappers.mana.get(playerToSave);
            name = Mappers.name.get(playerToSave);
            orientation = Mappers.orientation.get(playerToSave);
            parameterComponent = Mappers.parameter.get(playerToSave);
            player = Mappers.player.get(playerToSave);
            position = Mappers.position.get(playerToSave);
            size = Mappers.size.get(playerToSave);
            speed =  Mappers.speed.get(playerToSave);
            sprite = Mappers.sprite.get(playerToSave);
            steering = Mappers.steering.get(playerToSave);
            collidableComponent = Mappers.collidable.get(playerToSave);
        }
    }
}
