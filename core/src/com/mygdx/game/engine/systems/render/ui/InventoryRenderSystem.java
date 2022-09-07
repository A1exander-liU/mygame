package com.mygdx.game.engine.systems.render.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.engine.systems.gameplay.movement.MovementSystem;
import com.mygdx.game.engine.systems.TimeSystem;
import com.mygdx.game.utils.ui.ItemInfoDialog;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.utils.componentutils.ComponentGrabber;
import com.mygdx.game.engine.utils.componentutils.Families;
import com.mygdx.game.engine.utils.componentutils.Mappers;
import com.mygdx.game.engine.components.InventoryComponent;
import com.mygdx.game.engine.systems.gameplay.combat.BasicAttackSystem;
import com.mygdx.game.engine.systems.gameplay.combat.EnemyAttackSystem;
import com.mygdx.game.engine.systems.gameplay.enemyai.SteeringSystem;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.ui.InventoryFilterListener;
import com.mygdx.game.utils.ui.InventorySlot;
import com.mygdx.game.utils.ui.ItemFilterBox;
import com.mygdx.game.utils.ui.QuickSortButton;
import com.mygdx.game.utils.ui.RarityFilterBox;

public class InventoryRenderSystem extends EntitySystem {
    ComponentGrabber cg;
    Entity player;
    Stage stage;
    Skin skin;
    boolean inventoryOpened = false;

    boolean canDraw = false;

    DragAndDrop dragAndDrop;

    Table root;
    Table inventory;
    Table equipAndCoins;
    Table equipSlots;
    Label coins;
    Table outerTable;
    Table inventorySettings;
    ScrollPane inventoryScroll;

    public InventoryRenderSystem(ComponentGrabber cg) {
        super(100);
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        stage = new Stage(new ScreenViewport());
        GameScreen.inventoryMultiplexer.addProcessor(stage);

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

        root = new Table();
        root.setName("root");
        root.setFillParent(true);
        root.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(root);
        inventory = new Table();
        inventory.setName("inventory");
        inventory.setDebug(false);
        inventory.setBackground(skin.getDrawable("player-hud-bg-01"));
        inventory.setSize(root.getWidth() * 0.75f, root.getHeight() * 0.60f);
        root.add(inventory).expand().center().width(inventory.getWidth()).height(inventory.getHeight());

        equipAndCoins = new Table();
        equipAndCoins.setName("equipAndCoins");

        // build the tables in constructor
        // only slots will be cleared and added each frame
        equipSlots = new Table();
        equipSlots.setName("equipSlots");

        // setting up the equipSlots table
//        equipSlots.setSize(inventory.getWidth() * 0.4f, inventory.getHeight() * 0.95f);
//        inventory.add(equipSlots).expand().width(equipSlots.getWidth()).height(equipSlots.getHeight());

        equipAndCoins.setSize(inventory.getWidth() * 0.4f, inventory.getHeight() * 0.9f);
        inventory.add(equipAndCoins).expand().width(equipAndCoins.getWidth()).height(equipAndCoins.getHeight());

        equipAndCoins.add(equipSlots).expand().width(equipAndCoins.getWidth()).height(equipAndCoins.getHeight() * 0.85f);
        coins = new Label("Coins: " + Mappers.inventory.get(player).coinPouch, skin, "pixel2D", Color.BLACK);
        equipAndCoins.row();
        equipAndCoins.add(coins);

        equipSlots.defaults().expand().width(equipSlots.getWidth() / 3).height(equipSlots.getHeight() / 4);

        // setting up the inventorySlots table
        inventoryScroll = new ScrollPane(new Table(), skin, "scroll-pane-inventory");
        inventoryScroll.layout();
        inventoryScroll.setDebug(false);
        inventoryScroll.setFadeScrollBars(false);
        inventoryScroll.setFlickScroll(false);
        inventoryScroll.setVariableSizeKnobs(false);
        inventoryScroll.setSmoothScrolling(true);

        outerTable = new Table();
        outerTable.setName("outerTable");
        outerTable.setDebug(false);
        outerTable.defaults().space(5);

        inventorySettings = new Table();

        outerTable.add(inventorySettings).width(inventory.getWidth() * 0.55f).height(inventory.getHeight() * 0.1f).fill();

        RarityFilterBox rarityFilterBox = new RarityFilterBox(skin, "invRarityFilter");
        ItemFilterBox itemFilterBox = new ItemFilterBox(skin, "invItemFilter");
        InventoryFilterListener listener = new InventoryFilterListener(rarityFilterBox, itemFilterBox);
        rarityFilterBox.addListener(listener);
        itemFilterBox.addListener(listener);

        inventorySettings.defaults().space(5).width(outerTable.getCell(inventorySettings).getPrefWidth() / 3);
        inventorySettings.add(rarityFilterBox);
        inventorySettings.add(itemFilterBox);
        inventorySettings.add(new QuickSortButton(skin, "invQuickSort"));

        outerTable.row();
        outerTable.add(inventoryScroll).width(inventory.getWidth() * 0.55f).height(inventory.getHeight() * 0.85f).fill();

        inventory.add(outerTable);
    }

    @Override
    public void update(float delta) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        root.setSize(stage.getWidth(), stage.getHeight());
        inventory.setSize(root.getWidth() * 0.75f, root.getHeight() * 0.60f);

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

        if (inventoryOpened) {
            Gdx.input.setInputProcessor(stage);
            stage.setScrollFocus(inventoryScroll);
            // immediately set to false so the inventory is only drawn once
            // when it is opened once
            canDraw = false;
            updateCoinsLabel();
            addInventorySlots();
            stage.act(delta);
            stage.draw();
        }
    }

    private void toggleInventory() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E) && !inventoryOpened) {
            // set canDraw back to when you open the inventory again
            canDraw = true;
            inventoryOpened = true;
            // set to original inventory when opening the inventory
            ((RarityFilterBox) inventorySettings.findActor("invRarityFilter")).setCurrentInventory(Mappers.inventory.get(player).inventorySlots);
            ((ItemFilterBox) inventorySettings.findActor("invItemFilter")).setCurrentInventory(Mappers.inventory.get(player).inventorySlots);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.E) && inventoryOpened) {
            inventoryOpened = false;
            // also clear stage so inventory wont be displayed
            // (since all ui elements are removed from the stage)

            // set inventorySlots array in player back to original inventory
            Mappers.inventory.get(player).inventorySlots = ((RarityFilterBox) inventorySettings.findActor("invRarityFilter")).getCurrentInventory();
            // set rarity filter item to "all"
            ((RarityFilterBox) inventorySettings.findActor("invRarityFilter")).setSelected("All");
            ((ItemFilterBox) inventorySettings.findActor("invItemFilter")).setSelected("All");
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

    private void addInventorySlots() {
        Table inventorySlots = new Table();
        inventorySlots.clearChildren();
        inventoryScroll.setActor(inventorySlots);

        addEquipSlots();

        int cols = 0;
        InventoryComponent inventoryComponent = cg.getInventory(player);
        for (int i = 0; i < inventoryComponent.inventorySlots.size; i++) {
            if (cols == 4) {
                cols = 0;
                inventorySlots.row();
            }
            InventorySlot inventorySlot = inventoryComponent.inventorySlots.get(i);

            inventorySlot.clearChildren();

            Stack stack = new Stack();
            inventorySlot.add(stack).grow();
            if (!inventorySlot.isEmpty()) {
                TextureRegionDrawable drawable = new TextureRegionDrawable(cg.getSprite(inventorySlot.getOccupiedItem()).texture);
                Image image = new Image(drawable);
                image.setScaling(Scaling.contain);

                Label label = new Label("", skin, "pixel2D", Color.BLACK);
                if (cg.getQuantity(inventorySlot.getOccupiedItem()) != null)
                    label.setText("" + cg.getQuantity(inventorySlot.getOccupiedItem()).quantity);
                label.setAlignment(Align.topRight);

                stack.add(image);
                stack.add(label);
            }

            Cell<ScrollPane> scrollPaneCell = outerTable.getCell(inventoryScroll);

            inventorySlots.add(inventorySlot).width(scrollPaneCell.getPrefWidth() / 5).height(scrollPaneCell.getPrefHeight() / 5);

            cols++;
        }

        inventoryScroll.validate();
        inventoryScroll.updateVisualScroll();

        for (int i = 0; i < inventoryComponent.inventorySlots.size; i++) {
            InventorySlot inventorySlot = inventoryComponent.inventorySlots.get(i);
            if (inventorySlot.getItemWindowListener().getClickedItemSlot() != null) {
                ItemInfoDialog dialog = new ItemInfoDialog("", skin, inventorySlot);
                dialog.getTitleLabel().setText(cg.getName(inventorySlot.getOccupiedItem()).name);
                dialog.show(stage);
                dialog.setWidth(200);
                inventorySlot.getItemWindowListener().reset();
            }
        }
        for (int i = 0; i < inventoryComponent.equipSlots.size; i++) {
            InventorySlot inventorySlot = inventoryComponent.equipSlots.get(i);
            if (inventorySlot.getItemWindowListener().getClickedItemSlot() != null) {
                ItemInfoDialog dialog = new ItemInfoDialog("", skin, inventorySlot);
                dialog.getTitleLabel().setText(cg.getName(inventorySlot.getOccupiedItem()).name);
                dialog.show(stage);
                dialog.setWidth(200);
                inventorySlot.getItemWindowListener().reset();
            }
        }
    }

    private void addEquipSlots() {
        InventoryComponent inventoryComponent = cg.getInventory(player);
        equipSlots.clearChildren();
        Table equipSlotsInner = new Table();
        equipSlots.add(equipSlotsInner).expand().fill();
        equipSlotsInner.clearChildren();

        equipSlotsInner.defaults().expand().width(equipSlots.getHeight() / 4.5f).height(equipSlots.getHeight() / 4.5f);

        // need to add an image of equipped item sprite
        for (int i = 0; i < inventoryComponent.equipSlots.size; i++) {
            // check if an item is equipped
            if (inventoryComponent.equipSlots.get(i).getChildren().size > 0) {
                inventoryComponent.equipSlots.get(i).getChildren().get(0).remove();
            }
            if (inventoryComponent.equipSlots.get(i).getOccupiedItem() != null) {
                TextureRegionDrawable drawable = new TextureRegionDrawable(cg.getSprite(inventoryComponent.equipSlots.get(i).getOccupiedItem()).texture);
                Image image = new Image(drawable);
                image.setScaling(Scaling.contain);
                inventoryComponent.equipSlots.get(i).add(image).grow();
            }
            
        }

        equipSlotsInner.add(inventoryComponent.equipSlots.get(0)).colspan(3).center();
        equipSlotsInner.row();
        equipSlotsInner.add(inventoryComponent.equipSlots.get(4));
        equipSlotsInner.add(inventoryComponent.equipSlots.get(1));
        equipSlotsInner.add(inventoryComponent.equipSlots.get(5));
        equipSlotsInner.row();
        equipSlotsInner.add(inventoryComponent.equipSlots.get(2)).colspan(3).center();
        equipSlotsInner.row();
        equipSlotsInner.add(inventoryComponent.equipSlots.get(6));
        equipSlotsInner.add(inventoryComponent.equipSlots.get(3));
        equipSlotsInner.add(inventoryComponent.equipSlots.get(7));
    }

    private void updateCoinsLabel() {
        coins.setText("Coins: " + Mappers.inventory.get(player).coinPouch);
    }
}
