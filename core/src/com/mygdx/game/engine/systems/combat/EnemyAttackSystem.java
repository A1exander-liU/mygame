package com.mygdx.game.engine.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.EnemyState;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.ParameterComponent;

public class EnemyAttackSystem extends EntitySystem {
    ComponentGrabber cg;
    ImmutableArray<Entity> enemies;
    Entity player;

    public EnemyAttackSystem(ComponentGrabber cg) {
        super(9);
        this.cg = cg;
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);
            if (cg.getStateMachine(enemy).getCurrentState() == EnemyState.HUNT && closeToPlayer(enemy)) {
                performAttack(enemy);
            }
        }
    }

    private boolean closeToPlayer(Entity enemy) {
        Vector2 playerPos = new Vector2(cg.getPosition(player).x, cg.getPosition(player).y);
        Vector2 enemyPos = new Vector2(cg.getPosition(enemy).x, cg.getPosition(enemy).y);
        return enemyPos.dst(playerPos) <= 60;
    }

    private void performAttack(Entity enemy) {
        ParameterComponent playerParams = cg.getParameters(player);
        ParameterComponent enemyParams = cg.getParameters(enemy);
        playerParams.health.currentHealth -= enemyParams.damage;
    }
}
