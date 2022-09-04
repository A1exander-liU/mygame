package com.mygdx.game.engine;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.JsonItemFinder;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.inventory.items.individual.ArmourBaseStatComponent;
import com.mygdx.game.engine.components.inventory.items.individual.ArmourStatComponent;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.inventory.items.individual.StackableComponent;
import com.mygdx.game.engine.components.inventory.items.individual.WeaponBaseStatComponent;
import com.mygdx.game.engine.components.inventory.items.individual.WeaponStatComponent;
import com.mygdx.game.engine.components.inventory.items.shared.DescriptionComponent;
import com.mygdx.game.engine.components.inventory.items.shared.InventoryItemComponent;
import com.mygdx.game.engine.components.inventory.items.shared.QuantityComponent;
import com.mygdx.game.engine.components.inventory.items.shared.RarityComponent;

public class ItemFactory {
    JsonItemFinder itemFinder;

    public ItemFactory(JsonItemFinder itemFinder) {
        this.itemFinder = itemFinder;
    }

    public Entity makeMaterial(String materialName, int amount) {
        JsonValue material = itemFinder.findMaterialByName(materialName);
        Entity materialEntity = new Entity();
        materialEntity.add(new Name());
        materialEntity.add(new Sprite());
        materialEntity.add(new RarityComponent());
        materialEntity.add(new DescriptionComponent());
        materialEntity.add(new QuantityComponent());
        materialEntity.add(new InventoryItemComponent(ItemType.MATERIAL));
        materialEntity.add(new StackableComponent());

        Mappers.name.get(materialEntity).name = material.getString("name");
        Mappers.sprite.get(materialEntity).texture = new Texture(Gdx.files.internal(material.getString("sprite")));
        Mappers.rarity.get(materialEntity).rarity = determineRarity(material.getString("rarity"));
        Mappers.description.get(materialEntity).description = material.getString("desc");
        Mappers.quantity.get(materialEntity).quantity = amount;

        addToEngine(materialEntity);
        return materialEntity;
    }

    public Entity makeWeapon(JsonValue item, Rarity rarity) {
        Entity weaponEntity = new Entity();
        weaponEntity.add(new Name(item.getString("name")));
        weaponEntity.add(new Sprite(new Texture(Gdx.files.internal(item.getString("sprite")))));
        weaponEntity.add(new RarityComponent(rarity));
        weaponEntity.add(new DescriptionComponent(item.getString("desc")));
        weaponEntity.add(new WeaponBaseStatComponent());
        weaponEntity.add(new InventoryItemComponent(ItemType.MAIN));

        Mappers.weaponBaseStat.get(weaponEntity).attackDelay = item.getFloat("attackDelay");
        addToEngine(weaponEntity);
        return weaponEntity;
    }

    public Entity makeArmour(JsonValue item, Rarity rarity, ItemType itemType) {
        Entity armourEntity = new Entity();
        armourEntity.add(new Name(item.getString("name")));
        armourEntity.add(new Sprite(new Texture(Gdx.files.internal(item.getString("sprite")))));
        armourEntity.add(new RarityComponent(rarity));
        armourEntity.add(new DescriptionComponent(item.getString("desc")));
        armourEntity.add(new ArmourBaseStatComponent());
        armourEntity.add(new InventoryItemComponent(itemType));

        addToEngine(armourEntity);
        return armourEntity;
    }

    private Rarity determineRarity(String rarity) {
        switch (rarity) {
            case "COMMON":
                return Rarity.COMMON;
            case "UNCOMMON":
                return Rarity.UNCOMMON;
            case "RARE":
                return Rarity.RARE;
            case "EPIC":
                return Rarity.EPIC;
            case "LEGENDARY":
                return Rarity.LEGENDARY;
            case "MYTHICAL":
                return Rarity.MYTHICAL;
            default:
                return null;
        }
    }

    private void addToEngine(Entity entity) {
        MyGame.engine.addEntity(entity);
    }
}
