package com.mygdx.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.Rarity;

import java.util.Dictionary;

public class QuickSortButton extends ImageButton {
    Entity player;

    enum RarityValue {
        COMMON    (1),
        UNCOMMON  (2),
        RARE      (3),
        EPIC      (4),
        LEGENDARY (5),
        MYTHICAL  (6);

        int value;

        public int getValue() {
            return value;
        }

        RarityValue(int value) {
            this.value = value;
        }
    }

    public QuickSortButton(Skin skin) {
        super(skin);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("clicked");
                quickSortInventory();
            }
        });

    }

    private void quickSortInventory() {
    }
}
