package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.inventory.InventoryComponent;
import com.mygdx.game.engine.components.inventory.InventorySlotComponent;
import com.mygdx.game.engine.systems.combat.BasicAttackSystem;
import com.mygdx.game.engine.systems.combat.EnemyAttackSystem;
import com.mygdx.game.engine.systems.enemyai.SteeringSystem;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.InventorySlot;

public class InventoryRenderSystem extends EntitySystem {
    ComponentGrabber cg;
    Entity player;
    Stage stage;
    Skin skin;
    boolean inventoryOpened = false;

    boolean canDraw = false;

    DragAndDrop dragAndDrop;

    public InventoryRenderSystem(ComponentGrabber cg) {
        super(100);
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        stage = new Stage(new ScreenViewport());
        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 9;
        BitmapFont newFont = generator.generateFont(parameter);
        skin.add("pixel2D", newFont);
        skin.addRegions(new TextureAtlas("Game_UI_Skin/Game_UI_Skin.atlas"));
        skin.load(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
        generator.dispose();
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

        dragAndDrop = new DragAndDrop();
    }

    @Override
    public void update(float delta) {
        // inventory button states don't work properly
        // problem: might be b/c it is redrawing every frame, the button states
        // are not registering

        toggleInventory();
        toggleMovements();

        // how to make it only draw once when inventory is opened?
        // create a boolean to determine when the inventory can be drawn
        // new condition is when inventory is opened and it can be drawn
        // to make it draw only once, set the boolean to false when you draw it
        // so future frames won't keep redrawing

        if (inventoryOpened && canDraw) {
            // immediately set to false so the inventory is only drawn once
            // when it is opened once
            canDraw = false;
            Table root = new Table();
            root.setFillParent(true);
            root.setSize(stage.getWidth(), stage.getHeight());
            stage.addActor(root);

            Table inventory = new Table();
            inventory.setDebug(false);
            inventory.setBackground(skin.getDrawable("player-hud-bg-01"));
            inventory.setSize(root.getWidth() * 0.75f, root.getHeight() * 0.60f);
            root.add(inventory).expand().center().width(inventory.getWidth()).height(inventory.getHeight());

            addInventorySlots(inventory);
        }
        stage.act(delta);
        stage.draw();
    }

    private void toggleInventory() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)
                && !inventoryOpened) {
            // set canDraw back to when you open the inventory again
            canDraw = true;
            inventoryOpened = true;
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.E)
                && inventoryOpened) {
            inventoryOpened = false;
            // also clear stage so inventory wont be displayed
            // (since all ui elements are removed from the stage)
            stage.clear();
        }
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
        // Items can be dragged to a different slot
        // inventory component needs to know this and update
        // inventory right now is just an array of entities which represent
        // the items
        // use the index of the slots to determine the swapping

        // ex. have an item in slot 1 and want to drag it to slot 5
        // since inventory right now is 4 x 4, slot 5 is under slot 1
        // once the drop is confirmed, inventory will need to update
        // loop through all the inventory slots on the table
        // set inventory array entities to each item of the table

        // need some inventory features:
        // drag and drop:
            // swapping items
            // stacking
            // splitting
        Table inventorySlots = new Table();

        inventorySlots.setDebug(false);
        inventorySlots.setSize(inventory.getWidth() * 0.55f, inventory.getHeight() * 0.95f);
        inventory.add(inventorySlots).expand().width(inventorySlots.getWidth()).height(inventorySlots.getHeight()).right();

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
                placeItemInSlot(inventorySlots, inventorySlot);
            }
            else {
                placeEmptySlot(inventorySlots);
            }
            cols++;
        }
    }

    private void placeItemInSlot(Table inventorySlots, Entity inventorySlot) {
        Entity inventoryItem = cg.getInventorySlot(inventorySlot).itemOccupied;
        int quantity = cg.getQuantity(inventoryItem).quantity;
        // no actual sprite, just using the stick man for now
        InventorySlot slot = new InventorySlot(skin);
        // set the reference of the inventory item
        slot.setOccupiedItem(inventoryItem);
        // for drag and drop operations b/c label and the image is separate
        // need to have both when the item is being dragged
        Stack itemStack = new Stack();

        // the label to display the amount (top-right corner)
        Label occupiedItemQuantity = new Label("" + quantity, skin, "pixel2D", Color.BLACK);
        occupiedItemQuantity.setAlignment(Align.topRight);
        // turn the image to drawable
        TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("testPlayer.png")));
        // use the drawable now to get an image
        Image occupiedItemSprite = new Image(drawable);

        itemStack.add(occupiedItemSprite);
        itemStack.add(occupiedItemQuantity);

        slot.add(itemStack).grow();

        slot.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                InventorySlot slot = (InventorySlot) actor;
                Entity slotItem = slot.getOccupiedItem();
                System.out.println("Name: " + cg.getName(slotItem).name);
                System.out.println(cg.getRarity(slotItem).rarity);
                System.out.println(cg.getDescription(slotItem).description);
                System.out.println("Quantity: " + cg.getQuantity(slotItem).quantity);
            }
        });

        inventorySlots.add(slot).width(inventorySlots.getWidth() / 4).height(inventorySlots.getHeight() / 4);
    }

    private void placeEmptySlot(Table inventorySlots) {
        Button slot = new Button(skin);

        slot.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("empty slot");
            }
        });

        inventorySlots.add(slot).width(inventorySlots.getWidth() / 4).height(inventorySlots.getHeight() / 4);
    }
}
