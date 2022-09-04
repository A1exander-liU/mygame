package com.mygdx.game.engine.systems.gameplay.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.utils.componentutils.ComponentGrabber;
import com.mygdx.game.engine.utils.EnemyState;
import com.mygdx.game.engine.utils.componentutils.Families;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.systems.TimeSystem;

public class EnemyAttackSystem extends EntitySystem {
    ComponentGrabber cg;
    MyGame root;
    ImmutableArray<Entity> enemies;
    Entity player;

    public EnemyAttackSystem(ComponentGrabber cg, MyGame root) {
        super(9);
        this.cg = cg;
        this.root = root;
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
        // get the enemy to find their attack speed
        JsonValue jsonEnemy = root.jsonSearcher.findByEnemyName(cg.getName(enemy).name);
        float attackSpeed = jsonEnemy.getFloat("ATK_SPD");
        // if timeOfLastAttack == 0 means they haven't attacked once
        // and for future attacks it will check if elapsed game time is
        // greater than or equal to the lastTimeOfAttack by the value of
        // their attack speed
        if (cg.getEnemy(enemy).timeOfLastAttack == 0 ||
        TimeSystem.time - cg.getEnemy(enemy).timeOfLastAttack >= attackSpeed) {
            // update the timeOfLastAttack
            cg.getEnemy(enemy).timeOfLastAttack = TimeSystem.time;
            ParameterComponent playerParams = cg.getParameters(player);
            ParameterComponent enemyParams = cg.getParameters(enemy);
            playerParams.health.currentHealth -= enemyParams.damage;
        }
    }
}
