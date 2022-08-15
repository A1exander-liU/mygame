package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;

public class InventoryRenderSystem extends EntitySystem {
    ComponentGrabber cg;
    Entity player;
    Stage stage;
    Skin skin;

    public InventoryRenderSystem(ComponentGrabber cg) {
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
        // takes up inventory:
        // weapons, armor, accessories, materials, consumables
        // equipped items, currency, quest items don't take inventory

        // Inventory asset types:
        // weapons, armor, accessories, materials, consumables

        // Weapon subtypes:
        // slashing, stabbing, smashing
        // slashing: swords, blades
        // stabbing: daggers, spears, rapiers

        // Armor subtypes: helmet, chest, legs, boots
        // Accessory subtypes: rings, necklaces, earrings, special
        // Consumable types:
        // restoratives: health, mana, cure statuses
        // health, mana: instant, over time(regen)
        // cure statuses: instant, status protection for fixed duration
        // buffs: fixed durations

    }

    @Override
    public void update(float delta) {
        stage.clear();

        Table root = new Table();
        root.setSize(stage.getWidth(), stage.getHeight());

        Table inventory = new Table();
        inventory.setDebug(true);
        inventory.setBackground(skin.getDrawable("player-hud-bg-01"));
        inventory.setSize(root.getWidth() * 0.75f, root.getHeight() * 0.8f);
        root.add(inventory).expand().center().width(inventory.getWidth()).height(inventory.getHeight());

        stage.act();
        stage.draw();
    }
}
