package com.mygdx.game.engine.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.EnemyState;
import com.mygdx.game.engine.Families;

public class EnemyAttackSystem extends EntitySystem {
    ComponentGrabber cg;
    ImmutableArray<Entity> enemies;

    public EnemyAttackSystem(ComponentGrabber cg) {
        super(9);
        this.cg = cg;
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);
            if (cg.getStateMachine(enemy).getCurrentState() == EnemyState.HUNT) {

            }
        }
    }
}
