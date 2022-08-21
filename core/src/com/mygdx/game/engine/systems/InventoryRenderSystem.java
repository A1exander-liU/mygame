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
import com.mygdx.game.engine.AcceptedEquipType;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.InventorySlotSource;
import com.mygdx.game.engine.InventorySlotTarget;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.components.inventory.InventoryComponent;
import com.mygdx.game.engine.components.inventory.InventorySlotComponent;
import com.mygdx.game.engine.systems.combat.BasicAttackSystem;
import com.mygdx.game.engine.systems.combat.EnemyAttackSystem;
import com.mygdx.game.engine.systems.enemyai.SteeringSystem;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.InventorySlot;

import org.graalvm.compiler.debug.CSVUtil;

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
//        stage.clear();
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
        Table inventorySlots = new Table();

        addEquipSlots(inventory);

        inventorySlots.setDebug(false);
        inventorySlots.setSize(inventory.getWidth() * 0.55f, inventory.getHeight() * 0.95f);
        inventory.add(inventorySlots).expand().width(inventorySlots.getWidth()).height(inventorySlots.getHeight()).right();

        int cols = 0;
        InventoryComponent inventoryComponent = cg.getInventory(player);
        for (int i = 0; i < inventoryComponent.inventorySlots.size; i++) {
            if (cols == 4) {
                cols = 0;
                inventorySlots.row();
            }
            InventorySlot inventorySlot = inventoryComponent.inventorySlots.get(i);
//            inventorySlot.setOccupiedItem(inventorySlot.getOccupiedItem());
//            Entity inventoryItem = inventorySlot.getOccupiedItem();

            if (inventorySlot.getChildren().size > 0) {
                inventorySlot.getChildren().get(0).remove();
            }

            Stack stack = new Stack();
            inventorySlot.add(stack);
            if (!inventorySlot.isEmpty()) {
                TextureRegionDrawable drawable = new TextureRegionDrawable(cg.getSprite(inventorySlot.getOccupiedItem()).texture);
                Image image = new Image(drawable);

                Label label = new Label("", skin, "pixel2D", Color.BLACK);
                if (cg.getQuantity(inventorySlot.getOccupiedItem()) != null)
                    label.setText("" + cg.getQuantity(inventorySlot.getOccupiedItem()).quantity);
                label.setAlignment(Align.topRight);

                stack.add(image);
                stack.add(label);
            }

            inventorySlot.add(stack).grow();

            inventorySlot.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    InventorySlot bagSlot = (InventorySlot) actor;
                    System.out.println(bagSlot.getOccupiedItem());
                }
            });

            inventorySlots.add(inventorySlot).width(inventorySlots.getWidth() / 4).height(inventorySlots.getHeight() / 4);

//            inventoryComponent.inventorySlots.set(i, inventorySlot);

            cols++;
        }
    }

    private void addEquipSlots(Table inventory) {
        InventoryComponent inventoryComponent = cg.getInventory(player);
        Table equipSlots = new Table();
        equipSlots.setDebug(false);

        equipSlots.setSize(inventory.getWidth() * 0.4f, inventory.getHeight() * 0.95f);
        inventory.add(equipSlots).expand().width(equipSlots.getWidth()).height(equipSlots.getHeight());

        equipSlots.defaults().expand().width(equipSlots.getWidth() / 3).height(equipSlots.getHeight() / 4);

        // need to add an image of equipped item sprite
        for (int i = 0; i < inventoryComponent.equipSlots.size; i++) {
            // check if an item is equipped
            if (inventoryComponent.equipSlots.get(i).getChildren().size > 0) {
                inventoryComponent.equipSlots.get(i).getChildren().get(0).remove();
            }
            if (inventoryComponent.equipSlots.get(i).getOccupiedItem() != null) {
                TextureRegionDrawable drawable = new TextureRegionDrawable(cg.getSprite(inventoryComponent.equipSlots.get(i).getOccupiedItem()).texture);
                Image image = new Image(drawable);
                inventoryComponent.equipSlots.get(i).add(image).grow();
            }
        }

        equipSlots.add(inventoryComponent.equipSlots.get(0)).colspan(3).center();
        equipSlots.row();
        equipSlots.add(inventoryComponent.equipSlots.get(4));
        equipSlots.add(inventoryComponent.equipSlots.get(1));
        equipSlots.add(inventoryComponent.equipSlots.get(5));
        equipSlots.row();
        equipSlots.add(inventoryComponent.equipSlots.get(2)).colspan(3).center();
        equipSlots.row();
        equipSlots.add(inventoryComponent.equipSlots.get(6));
        equipSlots.add(inventoryComponent.equipSlots.get(3));
        equipSlots.add(inventoryComponent.equipSlots.get(7));

    }
}
