package com.mygdx.game.engine.systems.saving;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.AffixesComponent;
import com.mygdx.game.engine.components.ArmourBaseStatComponent;
import com.mygdx.game.engine.components.Camera;
import com.mygdx.game.engine.components.CollidableComponent;
import com.mygdx.game.engine.components.DescriptionComponent;
import com.mygdx.game.engine.components.DetectionProximity;
import com.mygdx.game.engine.components.ExpComponent;
import com.mygdx.game.engine.components.Health;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.InventoryComponent;
import com.mygdx.game.engine.components.InventoryItemComponent;
import com.mygdx.game.engine.components.Item;
import com.mygdx.game.engine.components.LevelComponent;
import com.mygdx.game.engine.components.ManaComponent;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Orientation;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.Player;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.QuantityComponent;
import com.mygdx.game.engine.components.RarityComponent;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Speed;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.StackableComponent;
import com.mygdx.game.engine.components.Steering;
import com.mygdx.game.engine.components.WeaponStatComponent;
import com.mygdx.game.engine.utils.componentutils.Families;
import com.mygdx.game.engine.utils.componentutils.Mappers;
import com.mygdx.game.engine.utils.entities.PlayerEntity;
import com.mygdx.game.inventory.gameitem.AcceptedEquipType;
import com.mygdx.game.inventory.gameitem.ItemType;
import com.mygdx.game.utils.ui.InventorySlot;

public class SaveTest extends EntitySystem {
    MyGame root;

    public SaveTest(MyGame root) {
        this.root = root;
        String item = "item";
        Json json = new Json();
        String serialized = json.toJson(item);
        System.out.println(json.prettyPrint(serialized));

//        PlayerEntity player = new PlayerEntity();
//        SavedPlayer savedPlayer = new SavedPlayer(player);
//        String savedPlayerJson = json.toJson(savedPlayer);
//        System.out.println(json.prettyPrint(savedPlayerJson));
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            savePlayer((PlayerEntity) MyGame.engine.getEntitiesFor(Families.player).get(0));
        }
    }

    public void savePlayer(PlayerEntity player) {
        SavedPlayer savedPlayer = new SavedPlayer(player);
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.minimal);
        json.setElementType(SavedPlayer.class, "inventorySlots", SavedSlot.class);
        String serialized = json.toJson(savedPlayer);
        System.out.println(json.prettyPrint(serialized));
    }

    static class SavedPlayer {
        Camera camera;
        ExpComponent expComponent;
        Health health;
        LevelComponent levelComponent;
        ManaComponent manaComponent;
        Name name;
        Orientation orientation;
        ParameterComponent parameterComponent;
        Position position;
        Size size;
        Speed speed;
        String textureImgPath;

        int coins;
        Array<SavedSlot> inventorySlots;
        Array<SavedSlot> equipSlots;
        Array<SavedItem> inventoryItems;
        Array<SavedItem> equipItems;

        public SavedPlayer() {}

        public SavedPlayer(PlayerEntity playerToSave) {
            camera = Mappers.camera.get(playerToSave);
            expComponent = Mappers.exp.get(playerToSave);
            health = Mappers.health.get(playerToSave);
            levelComponent = Mappers.level.get(playerToSave);
            manaComponent = Mappers.mana.get(playerToSave);
            name = Mappers.name.get(playerToSave);
            orientation = Mappers.orientation.get(playerToSave);
            parameterComponent = Mappers.parameter.get(playerToSave);
            position = Mappers.position.get(playerToSave);
            size = Mappers.size.get(playerToSave);
            speed = Mappers.speed.get(playerToSave);
            textureImgPath = ((FileTextureData) Mappers.sprite.get(playerToSave).texture.getTextureData()).getFileHandle().path();

            Array<Entity> slotItems = new Array<>();
            Array<InventorySlot> slots = Mappers.inventory.get(playerToSave).inventorySlots;
            Array<InventorySlot> equips = Mappers.inventory.get(playerToSave).equipSlots;
            inventorySlots = new Array<>();
            for (int i = 0; i < slots.size; i++) {
                SavedSlot slot = new SavedSlot(slots.get(i));
                inventorySlots.add(slot);
            }
            equipSlots = new Array<>();
            for (int i = 0; i < equips.size; i++) {
                SavedSlot slot = new SavedSlot(equips.get(i));
                equipSlots.add(slot);
            }
        }
    }

    static class SavedSlot {
//        Skin skin;
        boolean isEquipSlot;
        AcceptedEquipType acceptedEquipType;
        ItemType acceptedItemType;
        boolean clicked;

        public SavedSlot() {}

        public SavedSlot(InventorySlot inventorySlot) {
//            skin = inventorySlot.getSkin();
            isEquipSlot = inventorySlot.getIsEquipSlot();
            acceptedEquipType = inventorySlot.getAcceptedEquipType();
            acceptedItemType = inventorySlot.getAcceptedType();
            clicked = false;
        }
    }

    static class SavedItem {
        InventoryItemComponent inventoryItemComponent;
        Name name;
        String itemImgPath;
        RarityComponent rarityComponent;
        DescriptionComponent descriptionComponent;
        QuantityComponent quantityComponent;
        StackableComponent stackableComponent;
        WeaponStatComponent weaponStatComponent;
        ArmourBaseStatComponent armourBaseStatComponent;
        AffixesComponent affixesComponent;

        public SavedItem() {}

        public SavedItem(Entity item) {

        }
    }
}
