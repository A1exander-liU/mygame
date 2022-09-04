package com.mygdx.game.engine.systems.saving;

import com.badlogic.ashley.core.EntitySystem;

public class SaveTest extends EntitySystem {

    public SaveTest() {
        SaveTestItem item = new SaveTestItem(1);
    }

    @Override
    public void update(float delta) {

    }

    static class SaveTestItem {
        int id;

        public SaveTestItem(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
