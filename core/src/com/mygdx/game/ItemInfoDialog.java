package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.ItemType;
import com.mygdx.game.engine.components.inventory.items.individual.AffixesComponent;
import com.mygdx.game.engine.components.inventory.items.individual.ArmourBaseStatComponent;
import com.mygdx.game.engine.components.inventory.items.individual.ArmourStatComponent;
import com.mygdx.game.engine.components.inventory.items.individual.WeaponBaseStatComponent;
import com.mygdx.game.utils.InventorySlot;

public class ItemInfoDialog extends Dialog {

    InventorySlot inventorySlot;
    Entity item;
    Entity player;
    InventoryUi inventoryUi;

    public ItemInfoDialog(String title, Skin skin, InventorySlot inventorySlot) {
        super(title, skin);
        getTitleLabel().setAlignment(Align.center);
        this.inventorySlot = inventorySlot;
        item = inventorySlot.getOccupiedItem();
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        inventoryUi = new InventoryUi();
        baseDialog();
        decideDialogToBuild();
    }

    public ItemInfoDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public ItemInfoDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);

    }

    @Override
    protected void result(Object object) {
        if (object.equals("close")) {
            hide();
        }
        else if (object.equals("equip")) {
            checkTargetEquipSlot();
        }
        else if (object.equals("unequip")) {
            InventorySlot inventorySlot = getFirstEmptyInventorySlot();
            inventoryUi.unequip(this.inventorySlot, inventorySlot);
        }
        else {
            cancel();
        }

    }

    private void baseDialog() {
        Label itemRarity = new Label("" + Mappers.rarity.get(item).rarity, getSkin(), "pixel2D", RarityColour.getColour(Mappers.rarity.get(item).rarity));
        itemRarity.setAlignment(Align.center);
        Image itemSprite = new Image(new TextureRegionDrawable(Mappers.sprite.get(item).texture));
        itemSprite.setScaling(Scaling.contain);

        getContentTable().add(itemRarity).grow();
        getContentTable().row();
        getContentTable().add(itemSprite).grow();

        button("Close", "close");

        getContentTable().padTop(5);
        getButtonTable().pad(5);
    }

    private void decideDialogToBuild() {
        if (Mappers.inventoryItem.get(item).acceptedItemType == ItemType.MATERIAL) {
            addMaterialInfo();
        }
        else if (Mappers.inventoryItem.get(item).acceptedItemType == ItemType.CONSUMABLE) {

        }
        else {
            if (Mappers.inventoryItem.get(item).acceptedItemType == ItemType.MAIN)
                addWeaponInfo();
            else if (Mappers.inventoryItem.get(item).acceptedItemType == ItemType.OFF) {

            }
            else if (Mappers.inventoryItem.get(item).acceptedItemType == ItemType.ACCESSORY) {

            }
            else {
                addArmourInfo();
            }
        }
    }

    private void addMaterialInfo() {
        Label desc = new Label("\"" + Mappers.description.get(item).description + "\"", getSkin(), "pixel2D", Color.BLACK);
        desc.setWrap(true);
        desc.setAlignment(Align.center);

        getContentTable().row();
        getContentTable().add(desc).grow();

    }

    public void addWeaponInfo() {
        WeaponBaseStatComponent weaponBaseStat = Mappers.weaponBaseStat.get(item);
        AffixesComponent affixes = Mappers.affixes.get(item);

        Label dmg = new Label("DMG " +weaponBaseStat.minDmg + " - " + weaponBaseStat.maxDmg, getSkin(), "pixel2D", Color.BLACK);
        Label atkSpd = new Label("Atk.Spd " + weaponBaseStat.attackDelay, getSkin(), "pixel2D", Color.BLACK);

        Label desc = new Label("\"" + Mappers.description.get(item).description + "\"", getSkin(), "pixel2D", Color.BLACK);
        desc.setAlignment(Align.center);
        desc.setWrap(true);

        Table weaponStatTable = new Table();
        weaponStatTable.defaults().expand().fill().space(5);
        weaponStatTable.add(dmg);
        weaponStatTable.row();
        weaponStatTable.add(atkSpd);
        weaponStatTable.row();

        // make label for each affix
        for (int i = 0; i < affixes.affixes.size; i++) {
            CharAttributes affix = affixes.affixes.get(i);
            // make label for name of the affix and the value
            Label affixLabel = new Label(affix.getValue() + " " + affix.getAttributeName(), getSkin(), "pixel2D", Color.BLACK);
            weaponStatTable.add(affixLabel);
            weaponStatTable.row();
        }

        getContentTable().row();
        getContentTable().add(weaponStatTable);
        getContentTable().row();
        getContentTable().add(desc).grow().padTop(10);

        if (equipped())
            button("Unequip", "unequip");
        else
            button("Equip", "equip");
    }

    private void addArmourInfo() {
        ArmourBaseStatComponent armourBaseStat = Mappers.armourBaseStat.get(item);
        AffixesComponent affixes = Mappers.affixes.get(item);

        Label pDef = new Label("Phy.Def " + armourBaseStat.phyDef, getSkin(), "pixel2D", Color.BLACK);
        Label mDef = new Label("Mag.Def " + armourBaseStat.magDef, getSkin(), "pixel2D", Color.BLACK);

        Label desc = new Label("\"" + Mappers.description.get(item).description + "\"", getSkin(), "pixel2D", Color.BLACK);
        desc.setAlignment(Align.center);
        desc.setWrap(true);

        Table armourStatTable = new Table();
        armourStatTable.defaults().expand().fill().space(5);
        armourStatTable.add(pDef);
        armourStatTable.row();
        armourStatTable.add(mDef);
        armourStatTable.row();

        for (int i = 0; i < affixes.affixes.size; i++) {
            CharAttributes affix = affixes.affixes.get(i);
            // make label for each armour affix
            Label affixLabel = new Label(affix.getValue() + " " + affix.getAttributeName(), getSkin(), "pixel2D", Color.BLACK);
            armourStatTable.add(affixLabel);
            armourStatTable.row();
        }

        getContentTable().row();
        getContentTable().add(armourStatTable);
        getContentTable().row();
        getContentTable().add(desc).grow();

        if (equipped())
            button("Unequip", "unequip");
        else
            button("Equip", "equip");
    }

    private boolean equipped() {
        Array<InventorySlot> equipSlots = Mappers.inventory.get(player).equipSlots;
        for (int i = 0; i < equipSlots.size; i++) {
            if (inventorySlot == equipSlots.get(i))
                return true;
        }
        return false;
    }

    private void checkTargetEquipSlot() {
        InventorySlot equipSlot = getEquipSlot(Mappers.inventoryItem.get(item).acceptedItemType);
        if (equipSlot != null && !equipSlot.isEmpty())
            inventoryUi.swapEquipment(inventorySlot, equipSlot);
        else
            inventoryUi.equip(inventorySlot, equipSlot);
    }

    private InventorySlot getEquipSlot(ItemType equipItemType) {
        Array<InventorySlot> equipSlots = Mappers.inventory.get(player).equipSlots;
        for (int i = 0; i < equipSlots.size; i++) {
            if (equipSlots.get(i).getAcceptedType() == equipItemType)
                return equipSlots.get(i);
        }
        return null;
    }

    private InventorySlot getFirstEmptyInventorySlot() {
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        for (int i = 0; i < inventorySlots.size; i++) {
            if (inventorySlots.get(i).isEmpty())
                return inventorySlots.get(i);
        }
        return null;
    }

}
