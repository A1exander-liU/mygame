package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;
import com.mygdx.game.utils.EntityTextureObject;

public class ItemDropLabelRenderSystem extends EntitySystem {

    Entity player;
    Skin skin;
    MapObjects enemyDrops;
    Stage stage;

    public ItemDropLabelRenderSystem() {
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        skin = new Skin(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
        enemyDrops = MyGame.gameMapProperties.getMapLayer(GameMapProperties.ENEMY_DROPS).getObjects();
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemyDrops.getCount(); i++) {
            EntityTextureObject textureObject = (EntityTextureObject) enemyDrops.get(i);
        }
    }
}
