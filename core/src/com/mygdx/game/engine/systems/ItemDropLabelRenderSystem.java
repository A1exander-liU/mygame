package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.RarityColour;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.utils.map.EntityTextureObject;

public class ItemDropLabelRenderSystem extends EntitySystem {

    Entity player;
    Skin skin;
    MapObjects enemyDrops;
    Stage stage;

    public ItemDropLabelRenderSystem() {
        super(97);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        skin = new Skin(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
        enemyDrops = MyGame.gameMapProperties.getMapLayer(GameMapProperties.ENEMY_DROPS).getObjects();
        stage = new Stage(new ScreenViewport());

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 8;
        // set to white to allow colour tinting
        parameter.color = Color.WHITE;
        BitmapFont font = generator.generateFont(parameter);
        skin.add("itemDropFont", font);

        skin.addRegions(new TextureAtlas("Game_UI_Skin/Game_UI_Skin.atlas"));
        skin.load(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemyDrops.getCount(); i++) {
            // clear previous labels
            stage.clear();
            // cast to EntityTextureObject (subclass to get the entity that belongs to it)
            EntityTextureObject textureObject = (EntityTextureObject) enemyDrops.get(i);
            Entity drop = textureObject.getOwner();
            // get the world position of the item drop
            Vector3 dropPosition = new Vector3(Mappers.position.get(drop).x, Mappers.position.get(drop).y, 0);
            // convert to screen position
            Vector3 dropScreenPosition = Mappers.camera.get(player).camera.project(dropPosition);
            // create label inside container
            Label itemName = new Label(Mappers.name.get(drop).name, skin, "itemDropFont", RarityColour.getColour(Mappers.rarity.get(drop).rarity));
            Container<Label> labelContainer = new Container<>(itemName);
            labelContainer.setBounds(dropScreenPosition.x, dropScreenPosition.y + Mappers.size.get(drop).height / 2, 32, 10);
            stage.addActor(labelContainer);
            stage.act(delta);
            stage.draw();
        }
    }
}
