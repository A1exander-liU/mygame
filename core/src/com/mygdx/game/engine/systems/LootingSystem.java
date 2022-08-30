package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.game.LootSimulation;

public class LootingSystem extends EntitySystem {
    LootSimulation lootSimulation;

    public LootingSystem() {
        lootSimulation = new LootSimulation();
    }

    @Override
    public void update(float delta) {

    }
}
