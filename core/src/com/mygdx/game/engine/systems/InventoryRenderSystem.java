package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.systems.combat.BasicAttackSystem;
import com.mygdx.game.engine.systems.combat.EnemyAttackSystem;
import com.mygdx.game.engine.systems.enemyai.SteeringSystem;

public class InventoryRenderSystem extends EntitySystem {
    ComponentGrabber cg;
    Entity player;
    Stage stage;
    Skin skin;
    boolean inventoryOpened = false;

    public InventoryRenderSystem(ComponentGrabber cg) {
        super(99);
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

        toggleInventory();
        toggleMovements();
        if (inventoryOpened) {
            stage.clear();

            Table root = new Table();
            root.setSize(stage.getWidth(), stage.getHeight());
            stage.addActor(root);

            Table inventory = new Table();
            inventory.setDebug(false);
            inventory.setBackground(skin.getDrawable("player-hud-bg-01"));
            inventory.setSize(root.getWidth() * 0.75f, root.getHeight() * 0.60f);
            root.add(inventory).expand().center().width(inventory.getWidth()).height(inventory.getHeight());

            addInventorySlots(inventory);

            stage.act();
            stage.draw();
        }
    }

    private void toggleInventory() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)
                && !inventoryOpened)
            inventoryOpened = true;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.E)
                && inventoryOpened)
            inventoryOpened = false;
    }

    private void toggleMovements() {
        if (inventoryOpened) {
            MyGame.engine.getSystem(MovementSystem.class).setProcessing(false);
            MyGame.engine.getSystem(SteeringSystem.class).setProcessing(false);
            MyGame.engine.getSystem(BasicAttackSystem.class).setProcessing(false);
            MyGame.engine.getSystem(EnemyAttackSystem.class).setProcessing(false);
            MyGame.engine.getSystem(TimeSystem.class).setProcessing(false);
        }
        else {
            MyGame.engine.getSystem(MovementSystem.class).setProcessing(true);
            MyGame.engine.getSystem(SteeringSystem.class).setProcessing(true);
            MyGame.engine.getSystem(BasicAttackSystem.class).setProcessing(true);
            MyGame.engine.getSystem(EnemyAttackSystem.class).setProcessing(true);
            MyGame.engine.getSystem(TimeSystem.class).setProcessing(true);
        }
    }

    private void addInventorySlots(Table inventory) {
        Table inventorySlots = new Table();
        inventorySlots.setDebug(true);
        inventorySlots.setSize(inventory.getWidth() * 0.5f, inventory.getHeight() * 0.95f);
        inventory.add(inventorySlots).expand().width(inventorySlots.getWidth()).height(inventorySlots.getHeight()).right();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                ImageButton slot = new ImageButton(skin);
                inventorySlots.add(slot).expand().fill().space(5);
            }
            inventorySlots.row();
        }
    }
}
