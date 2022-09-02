package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.engine.ItemFactory;
import com.mygdx.game.engine.ItemType;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.Rarity;
import com.mygdx.game.engine.components.inventory.items.individual.AffixesComponent;

import java.util.HashMap;
import java.util.Objects;


public class EquipmentGenerator {

    JsonValue items;
    ItemFactory itemFactory;

    public EquipmentGenerator(ItemFactory itemFactory) {
        JsonReader reader = new JsonReader();
        items = reader.parse(Gdx.files.internal("gameData/itemData/items.json"));
        this.itemFactory = itemFactory;
    }

    public Entity generateEquipment(int itemId) {
        for (JsonValue item: items) {
            // determine the item
            if (item.getInt("id") == itemId && isEquipment(item)) {
                return makeEquipment(item);
            }
        }
        return null;
    }

    private boolean isEquipment(JsonValue item) {
        return !Objects.equals(item.getString("itemType"), "material")
                && !Objects.equals(item.getString("itemType"), "consumable");
    }

    private Entity makeEquipment(JsonValue item) {
        Rarity itemRarity = rollForRarity();
        switch (item.getString("itemType")) {
            case "main":
                return makeMain(item, itemRarity);
            case "off":

            case "accessory":

            case "head":
                return makeArmor(item, itemRarity, ItemType.HEAD);
            case "torso":
                return makeArmor(item, itemRarity, ItemType.TORSO);
            case "leg":
                return makeArmor(item, itemRarity, ItemType.LEG);
            case "feet":
                return makeArmor(item, itemRarity, ItemType.FEET);
            default:
                return null;
        }
    }

    private Rarity rollForRarity() {
        HashMap<Rarity, Integer> rarityWeights = new HashMap<>();
        rarityWeights.put(Rarity.COMMON, 52);
        rarityWeights.put(Rarity.UNCOMMON, 21);
        rarityWeights.put(Rarity.RARE, 15);
        rarityWeights.put(Rarity.EPIC, 8);
        rarityWeights.put(Rarity.LEGENDARY, 3);
        rarityWeights.put(Rarity.MYTHICAL, 1);

        float randomWeight = RandomNumberGenerator.roll();
        float runTotal = 0;
        for (Rarity rarity: rarityWeights.keySet()) {
            runTotal += rarityWeights.get(rarity);
            if (randomWeight < runTotal)
                return rarity;
        }
        return null;
    }

    private Entity makeMain(JsonValue item, Rarity itemRarity) {
        Entity weaponEntity = itemFactory.makeWeapon2(item, itemRarity);
        HashMap<Rarity, float[]> modifiers = generateModifiers();
        HashMap<Rarity, int[]> affixes = generateAffixesAmount();
        float modifier = rollModifier(modifiers, itemRarity);
        int affixesAmount = rollAffixesAmount(affixes, itemRarity);
        // dmg values will be multiplied by the modifier
        Mappers.weaponBaseStat.get(weaponEntity).minDmg = (int)Math.ceil(item.getInt("minDmg") * modifier);
        Mappers.weaponBaseStat.get(weaponEntity).maxDmg = (int)Math.ceil(item.getInt("maxDmg") * modifier);

        // generate affixes (extra stats)
        // hashmap with affix name and value
        // str, vit, dex (for now)

        // generate each affix
        CharAttributes[] attributes = {CharAttributes.STR, CharAttributes.VIT, CharAttributes.DEX};
        Array<CharAttributes> affixesArray = generateAffixes(attributes, affixesAmount, item);
        weaponEntity.add(new AffixesComponent(affixesArray));
        return weaponEntity;
    }

    private Entity makeArmor(JsonValue item, Rarity itemRarity, ItemType itemType) {
        Entity armourEntity = itemFactory.makeArmour2(item, itemRarity, itemType);
        HashMap<Rarity, float[]> modifiers = generateModifiers();
        HashMap<Rarity, int[]> affixes = generateAffixesAmount();
        float modifier = rollModifier(modifiers, itemRarity);
        int affixesAmount = rollAffixesAmount(affixes, itemRarity);

        // update defense value according to the modifier
        Mappers.armourBaseStat.get(armourEntity).phyDef = (int)Math.ceil(item.getInt("physicalDef") * modifier);
        Mappers.armourBaseStat.get(armourEntity).magDef = (int)Math.ceil(item.getInt("magicalDef") * modifier);

        // generate each affix
        CharAttributes[] attributes = {CharAttributes.STR, CharAttributes.VIT, CharAttributes.DEX};
        Array<CharAttributes> affixesArray = generateAffixes(attributes, affixesAmount, item);
        armourEntity.add(new AffixesComponent(affixesArray));

        return armourEntity;
    }

    private HashMap<Rarity, float[]> generateModifiers() {
        HashMap<Rarity, float[]> hashMap = new HashMap<>();
        hashMap.put(Rarity.UNCOMMON, new float[] {1.01f, 1.2f});
        hashMap.put(Rarity.RARE, new float[] {1.21f, 1.4f});
        hashMap.put(Rarity.EPIC, new float[] {1.41f, 1.6f});
        hashMap.put(Rarity.LEGENDARY, new float[] {1.61f, 1.8f});
        hashMap.put(Rarity.MYTHICAL, new float[] {1.81f, 2});
        return hashMap;
    }

    private HashMap<Rarity, int[]> generateAffixesAmount() {
        HashMap<Rarity, int[]> hashMap = new HashMap<>();
        hashMap.put(Rarity.COMMON, new int[] {0, 2});
        hashMap.put(Rarity.UNCOMMON, new int[] {1, 3});
        hashMap.put(Rarity.RARE, new int[] {2, 4});
        hashMap.put(Rarity.EPIC, new int[] {3, 5});
        hashMap.put(Rarity.LEGENDARY, new int[] {4, 6});
        hashMap.put(Rarity.MYTHICAL, new int[] {7, 8});
        return hashMap;
    }

    private float rollModifier(HashMap<Rarity, float[]> modifiers, Rarity itemRarity) {
        if (itemRarity != Rarity.COMMON) {
            // get modifier of rarity
            float[] modifierRange = modifiers.get(itemRarity);
            return RandomNumberGenerator.roll(modifierRange[0], modifierRange[1]);
        }
        return 1;
    }

    private int rollAffixesAmount(HashMap<Rarity, int[]> affixes, Rarity itemRarity) {
        // common will have modifier of 1
        // uncommon: 1.01 - 1.2
        // rare: 1.21 - 1.4
        // epic: 1.41 - 1.6
        // legendary: 1.61 - 1.8
        // mythical: 1.81 - 2
        int[] affixesRange = affixes.get(itemRarity);
        return RandomNumberGenerator.roll(affixesRange[0], affixesRange[1]);
    }

    private Array<CharAttributes> generateAffixes(CharAttributes[] availableAffixes, int affixesAmount, JsonValue item) {
        Array<CharAttributes> affixesArray = new Array<>(0);
        for (int i = 0; i < affixesAmount; i++) {
            CharAttributes randomAffix = availableAffixes[RandomNumberGenerator.roll(0, availableAffixes.length)];
            int value = AffixValueGenerator.generate(item.getInt("itemLevel"));
            randomAffix.setValue(value);
            affixesArray.add(randomAffix);
        }
        return affixesArray;
    }
}
