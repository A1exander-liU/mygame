package com.mygdx.game.engine.systems.gameplay.looting;

import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.game.LootGenerator;

public class LootingSystem extends EntitySystem {
    LootGenerator lootGenerator;
    boolean stop = false;

    public LootingSystem() {
        lootGenerator = new LootGenerator();
    }

    @Override
    public void update(float delta) {
//        if (!stop) {
//            System.out.println("Fighting slimes...");
//            lootSimulation.fightSlimes(100);
//            System.out.println();
//            for (int i = 0; i < 1; i++)
//                lootSimulation.equipmentGeneration("Steel Sword");
//            stop = true;
//        }
    }
}
