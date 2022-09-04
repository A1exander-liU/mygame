package com.mygdx.game.engine.systems.render.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.utils.Families;
import com.mygdx.game.engine.utils.Mappers;
import com.mygdx.game.engine.components.InventoryComponent;
import com.mygdx.game.utils.ui.InventorySlot;

import java.util.Objects;


public class ItemWindowRenderSystem extends EntitySystem {

    Entity player;
    Array<InventorySlot> inventorySlots;
    Array<InventorySlot> equipSlots;
    Skin skin;

    Stage stage;

    public ItemWindowRenderSystem() {
        super(101);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        InventoryComponent inventoryComponent = Mappers.inventory.get(player);
        inventorySlots = inventoryComponent.inventorySlots;
        equipSlots = inventoryComponent.equipSlots;

        stage = new Stage(new ScreenViewport());
//        GameScreen.inventoryMultiplexer.addProcessor(stage);
//        Gdx.input.setInputProcessor(GameScreen.inventoryMultiplexer);


        skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 9;
        BitmapFont newFont = generator.generateFont(parameter);
        skin.add("pixel2D", newFont);
        skin.addRegions(new TextureAtlas("Game_UI_Skin/Game_UI_Skin.atlas"));
        skin.load(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
        generator.dispose();

//        ItemInfoDialog dialog = new ItemInfoDialog("T", skin);
//        dialog.show(stage);
    }

    @Override
    public void update(float delta) {

        for (int i = 0; i < inventorySlots.size; i++) {
//            if (inventorySlots.get(i).getItemWindowListener().getRecentWindow() != null) {
//
//                stage = inventorySlots.get(i).getStage() == null ? new Stage(new ScreenViewport()) : inventorySlots.get(i).getStage();
//
//                removePreviousWindows();
//
//                Table root = (Table) stage.getActors().get(stage.getActors().size - 1);
//                Table inventory = (Table) (root.getCells().get(0)).getActor();
//
//                stage.addActor(inventorySlots.get(i).getItemWindowListener().getRecentWindow());
//                // set z indexes to overlap itemWindow on top
//                inventorySlots.get(i).getItemWindowListener().getRecentWindow().setZIndex(2);
//                inventorySlots.get(i).getItemWindowListener().setRecentWindow(null);
//
//            }
        }
        for (int i = 0; i < equipSlots.size; i++) {
            if (equipSlots.get(i).getItemWindowListener().getClickedItemSlot() != null) {

            }
        }
        // draw at end
        stage.act(delta);
        stage.draw();
    }

    private void removePreviousWindows() {
        for (int i = 0; i < stage.getActors().size; i++) {
            if (Objects.equals(stage.getActors().get(i).getName(), "itemWindow"))
                stage.getActors().removeIndex(i);
        }
    }

    private Actor find(String actorName) {
        for (int i = 0; i < stage.getActors().size; i++) {
            if (Objects.equals(stage.getActors().get(i).getName(), actorName))
                return stage.getActors().get(i);
        }
        return null;
    }

    private Actor find(Table table, String name) {
        for (int i = 0; i < table.getChildren().size; i++) {
            Actor actor = table.getChildren().get(i);
            if (Objects.equals(actor.getName(), name))
                return actor;
        }
        return null;
    }

}
