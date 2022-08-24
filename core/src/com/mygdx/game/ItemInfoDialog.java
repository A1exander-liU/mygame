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
import com.badlogic.gdx.utils.Scaling;
import com.mygdx.game.engine.ItemType;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.components.WeaponStatComponent;
import com.mygdx.game.utils.InventorySlot;

public class ItemInfoDialog extends Dialog {

    InventorySlot inventorySlot;
    Entity item;

    public ItemInfoDialog(String title, Skin skin, InventorySlot inventorySlot) {
        super(title, skin);
        getTitleLabel().setAlignment(Align.center);
        this.inventorySlot = inventorySlot;
        item = inventorySlot.getOccupiedItem();
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
            addWeaponInfo();
        }
    }

    private void addMaterialInfo() {
        Label desc = new Label(Mappers.description.get(item).description, getSkin(), "pixel2D", Color.BLACK);
        desc.setWrap(true);

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

        Label desc = new Label(Mappers.description.get(item).description, getSkin(), "pixel2D", Color.BLACK);


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
        getContentTable().add(desc);
    }
}
