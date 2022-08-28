package com.mygdx.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;

public class ItemFilterBox extends SelectBox<String> {

    Entity player;
    Array<InventorySlot> currentInventory;

    public ItemFilterBox(Skin skin) {
        super(skin);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        setItems("All", "MainHand", "OffHand", "Accessory", "Head", "Torso", "Leg", "Feet",
                 "Consumable", "Material");
        getList().setAlignment(Align.left);
        getStyle().listStyle.font.getData().setScale(.8f);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
    }

    public ItemFilterBox(Skin skin, String actorName) {
        super(skin);
        setName(actorName);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        setItems("All", "MainHand", "OffHand", "Accessory", "Head", "Torso", "Leg", "Feet",
                "Consumable", "Material");
        getList().setAlignment(Align.left);
        getStyle().listStyle.font.getData().setScale(.8f);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
    }

    public Array<InventorySlot> getCurrentInventory() {
        return currentInventory;
    }

    public void setCurrentInventory(Array<InventorySlot> currentInventory) {
        this.currentInventory = currentInventory;
    }
}
