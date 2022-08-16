package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.inventory.InventoryComponent;
import com.mygdx.game.engine.components.inventory.InventorySlotComponent;
import com.mygdx.game.engine.components.inventory.items.shared.QuantityComponent;
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
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;
        BitmapFont newFont = generator.generateFont(parameter);
        skin.add("pixel2D", newFont);
        skin.addRegions(new TextureAtlas("Game_UI_Skin/Game_UI_Skin.atlas"));
        skin.load(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));

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

        // inventory component:
        // hold an array of item entities
        // each item entity will have components like:
        // item: flag component to indicate the entity is an item
        // stackable: can have multiple in single slot
        // quantity: how much of one stackable item there is
        // equipment: something can be equipped (have enum which has equipment type)
        // rarity:

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
        inventorySlots.setSize(inventory.getWidth() * 0.55f, inventory.getHeight() * 0.95f);
        inventory.add(inventorySlots).expand().width(inventorySlots.getWidth()).height(inventorySlots.getHeight()).right();

//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                Button slot = new Button(skin);
//
//                Label random = new Label("99", skin, "pixel2D", Color.BLACK);
//                slot.add(random).expand().top().right();
//
//                slot.row();
//
//                TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("testPlayer.png")));
//                Image randomImg = new Image();
//                randomImg.setDrawable(drawable);
//                slot.add(randomImg);
//
//                inventorySlots.add(slot).expand().fill().space(5);
//            }
//            inventorySlots.row();
//        }
        int rows = 0;
        int cols = 0;
        InventoryComponent inventoryComponent = cg.getInventory(player);
        for (int i = 0; i < inventoryComponent.items.size; i++) {
            // holds the quantity and occupied item
            if (cols == 4) {
                cols = 0;
                inventorySlots.row();
            }
            Entity inventorySlot = inventoryComponent.items.get(i);
            InventorySlotComponent slotComponent = cg.getInventorySlot(inventorySlot);
            // check if slot is occupied
            if (slotComponent.itemOccupied != null) {
                placeItemInSlot(inventorySlots, slotComponent.itemOccupied);
            }
            else {
                placeEmptySlot(inventorySlots);
            }
            cols++;
        }

    }

    private void placeItemInSlot(Table inventorySlots, Entity item) {
        QuantityComponent quantity = cg.getQuantity(item);
        // no actual sprite, just using the stick man for now
        Button slot = new Button(skin);
        // the label to display the amount (top-right corner)
        Label occupiedItemQuantity = new Label("" + quantity.quantity, skin, "pixel2D", Color.BLACK);
        slot.add(occupiedItemQuantity).expand().top().right();
        // new row to place the item sprite under the quantity number
        slot.row();
        // turn the image to drawable
        TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("testPlayer.png")));
        // use the drawable now to get an image
        Image occupiedItemSprite = new Image(drawable);
        slot.add(occupiedItemSprite).grow();

        inventorySlots.add(slot).width(inventorySlots.getWidth() / 4).height(inventorySlots.getHeight() / 4);
    }

    private void placeEmptySlot(Table inventorySlots) {
        Button slot = new Button(skin);
        inventorySlots.add(slot).width(inventorySlots.getWidth() / 4).height(inventorySlots.getHeight() / 4);
    }
}
