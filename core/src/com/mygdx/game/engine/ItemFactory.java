package com.mygdx.game.engine;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.JsonItemFinder;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.inventory.items.individual.ArmourStatComponent;
import com.mygdx.game.engine.components.inventory.items.individual.EquipTypeComponent;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.inventory.items.individual.StackableComponent;
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
        materialEntity.add(new InventoryItemComponent());
        materialEntity.add(new StackableComponent());
        materialEntity.add(new EquipTypeComponent());

        Mappers.name.get(materialEntity).name = material.getString("name");
        Mappers.sprite.get(materialEntity).texture = new Texture(Gdx.files.internal(material.getString("sprite")));
        Mappers.rarity.get(materialEntity).rarity = determineRarity(material.getString("rarity"));
        Mappers.description.get(materialEntity).description = material.getString("desc");
        Mappers.quantity.get(materialEntity).quantity = amount;
        Mappers.inventoryItem.get(materialEntity).itemType = ItemType.MATERIAL;

        addToEngine(materialEntity);
        return materialEntity;
    }

    public Entity makeWeapon(String weaponName) {
        JsonValue weapon = itemFinder.findWeaponByName(weaponName);
        Entity weaponEntity = new Entity();
        weaponEntity.add(new Name());
        weaponEntity.add(new Sprite());
        weaponEntity.add(new RarityComponent());
        weaponEntity.add(new DescriptionComponent());
        weaponEntity.add(new InventoryItemComponent());
        weaponEntity.add(new EquipTypeComponent());
        weaponEntity.add(new WeaponStatComponent());

        Mappers.name.get(weaponEntity).name = weapon.getString("name");
        Mappers.sprite.get(weaponEntity).texture = new Texture(Gdx.files.internal(weapon.getString("sprite")));
        Mappers.rarity.get(weaponEntity).rarity = determineRarity(weapon.getString("rarity"));
        Mappers.description.get(weaponEntity).description = weapon.getString("desc");
        Mappers.inventoryItem.get(weaponEntity).itemType = ItemType.EQUIPMENT;
        Mappers.equipType.get(weaponEntity).acceptedEquipType = AcceptedEquipType.MAIN;

        WeaponStatComponent weaponStat = Mappers.weaponStat.get(weaponEntity);
        weaponStat.minDmg = weapon.getInt("minDmg");
        weaponStat.maxDmg = weapon.getInt("maxDmg");
        weaponStat.attackDelay = weapon.getFloat("attackDelay");
        weaponStat.critChance = weapon.getFloat("critChance");
        weaponStat.critDmg = weapon.getFloat("critDmg");

        addToEngine(weaponEntity);
        return weaponEntity;
    }

    public Entity makeArmour(String name, AcceptedEquipType armourType) {
        JsonValue armour = null;
        if (armourType == AcceptedEquipType.TORSO)
            armour = itemFinder.findChestArmourByName(name);
        Entity armourEntity = new Entity();
        armourEntity.add(new Name());
        armourEntity.add(new Sprite());
        armourEntity.add(new RarityComponent());
        armourEntity.add(new DescriptionComponent());
        armourEntity.add(new InventoryItemComponent());
        armourEntity.add(new EquipTypeComponent());
        armourEntity.add(new ArmourStatComponent());

        Mappers.name.get(armourEntity).name = armour.getString("name");
        Mappers.sprite.get(armourEntity).texture = new Texture(Gdx.files.internal(armour.getString("sprite")));
        Mappers.rarity.get(armourEntity).rarity = determineRarity(armour.getString("rarity"));
        Mappers.description.get(armourEntity).description = armour.getString("desc");
        Mappers.inventoryItem.get(armourEntity).itemType = ItemType.EQUIPMENT;
        Mappers.equipType.get(armourEntity).acceptedEquipType = AcceptedEquipType.TORSO;

        ArmourStatComponent armourStat = Mappers.armourStat.get(armourEntity);
        armourStat.physicalDef = armour.getInt("physicalDef");
        armourStat.magicalDef = armour.getInt("magicalDef");
        armourStat.hp = armour.getInt("hp");
        armourStat.mp = armour.getInt("mp");

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
