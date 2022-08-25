package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.mygdx.game.engine.AcceptedEquipType;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.ItemType;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.components.inventory.items.individual.ArmourStatComponent;
import com.mygdx.game.engine.components.inventory.items.individual.WeaponStatComponent;
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
        if (Mappers.inventoryItem.get(item).itemType == ItemType.MATERIAL) {
            addMaterialInfo();
        }
        else if (Mappers.inventoryItem.get(item).itemType == ItemType.CONSUMABLE) {

        }
        else if (Mappers.inventoryItem.get(item).itemType == ItemType.EQUIPMENT) {
            if (Mappers.equipType.get(item).acceptedEquipType == AcceptedEquipType.MAIN)
                addWeaponInfo();
            else if (Mappers.equipType.get(item).acceptedEquipType == AcceptedEquipType.OFF) {

            }
            else if (Mappers.equipType.get(item).acceptedEquipType == AcceptedEquipType.ACCESSORY) {

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
        WeaponStatComponent weaponStat = Mappers.weaponStat.get(item);
        Label dmgTitle = new Label("DMG", getSkin(), "pixel2D", Color.BLACK);
        Label attackDelayTitle = new Label("Attack Delay", getSkin(), "pixel2D", Color.BLACK);
        Label critTitle = new Label("Crit", getSkin(), "pixel2D", Color.BLACK);
        Label critDmgTitle = new Label("CritDmg", getSkin(), "pixel2D", Color.BLACK);

        Label dmg = new Label(weaponStat.minDmg + "-" + weaponStat.maxDmg, getSkin(), "pixel2D", Color.BLACK);
        Label attackDelay = new Label(weaponStat.attackDelay + "s", getSkin(), "pixel2D", Color.BLACK);
        Label critChance = new Label(weaponStat.critChance + "%", getSkin(), "pixel2D", Color.BLACK);
        Label critDmg = new Label(weaponStat.critDmg + "x", getSkin(), "pixel2D", Color.BLACK);

        Label desc = new Label("\"" + Mappers.description.get(item).description + "\"", getSkin(), "pixel2D", Color.BLACK);
        desc.setAlignment(Align.center);
        desc.setWrap(true);

        Table weaponStatTable = new Table();
        weaponStatTable.defaults().expand().fill().space(5);
        weaponStatTable.add(dmgTitle);
        weaponStatTable.add(dmg);
        weaponStatTable.row();
        weaponStatTable.add(attackDelayTitle);
        weaponStatTable.add(attackDelay);
        weaponStatTable.row();
        weaponStatTable.add(critTitle);
        weaponStatTable.add(critChance);
        weaponStatTable.row();
        weaponStatTable.add(critDmgTitle);
        weaponStatTable.add(critDmg);

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
        ArmourStatComponent armourStat = Mappers.armourStat.get(item);

        Label pDefTitle = new Label("Phy.Def", getSkin(), "pixel2D", Color.BLACK);
        Label mDefTitle = new Label("Mag.Def", getSkin(), "pixel2D", Color.BLACK);
        Label hpTitle = new Label("HP", getSkin(), "pixel2D", Color.BLACK);
        Label mpTitle = new Label("MP", getSkin(), "pixel2D", Color.BLACK);

        Label pDef = new Label("" + armourStat.physicalDef, getSkin(), "pixel2D", Color.BLACK);
        Label mDef = new Label("" + armourStat.magicalDef, getSkin(), "pixel2D", Color.BLACK);
        Label hp = new Label("" + armourStat.hp, getSkin(), "pixel2D", Color.BLACK);
        Label mp = new Label("" + armourStat.mp, getSkin(), "pixel2D", Color.BLACK);

        Label desc = new Label("\"" + Mappers.description.get(item).description + "\"", getSkin(), "pixel2D", Color.BLACK);
        desc.setAlignment(Align.center);
        desc.setWrap(true);

        Table armourStatTable = new Table();
        armourStatTable.defaults().expand().fill().space(5);
        armourStatTable.add(pDefTitle);
        armourStatTable.add(pDef);
        armourStatTable.row();
        armourStatTable.add(mDefTitle);
        armourStatTable.add(mDef);
        armourStatTable.row();
        armourStatTable.add(hpTitle);
        armourStatTable.add(hp);
        armourStatTable.row();
        armourStatTable.add(mpTitle);
        armourStatTable.add(mp);

        getContentTable().row();
        getContentTable().add(armourStatTable);
        getContentTable().row();
        getContentTable().add(desc);

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
        InventorySlot equipSlot = getEquipSlot(Mappers.equipType.get(item).acceptedEquipType);
        if (equipSlot != null && !equipSlot.isEmpty())
            inventoryUi.swapEquipment(inventorySlot, equipSlot);
        else
            inventoryUi.equip(inventorySlot, equipSlot);
    }

    private InventorySlot getEquipSlot(AcceptedEquipType equipType) {
        Array<InventorySlot> equipSlots = Mappers.inventory.get(player).equipSlots;
        for (int i = 0; i < equipSlots.size; i++) {
            if (equipSlots.get(i).getAcceptedEquipType() == equipType)
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
