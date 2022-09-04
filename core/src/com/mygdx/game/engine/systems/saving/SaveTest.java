package com.mygdx.game.engine.systems.saving;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.Size;

public class SaveTest extends EntitySystem {
    MyGame root;

    public SaveTest(MyGame root) {
        this.root = root;
        SaveTestItem item = new SaveTestItem(1);
        Json json = new Json();
        String serialized = json.toJson(item);
        System.out.println(json.prettyPrint(serialized));

    }

    @Override
    public void update(float delta) {

    }

    static class SaveTestItem {
        int id;
        Array<Entity> entities;
        Entity entity;

        public SaveTestItem(int id) {
            this.id = id;
            entities = new Array<>();
            entity = new Entity();
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
