package com.mygdx.game.engine.entityListeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.mygdx.game.engine.utils.ComponentGrabber;
import com.mygdx.game.engine.utils.Families;
import com.mygdx.game.engine.components.Item;
import com.mygdx.game.engine.systems.gameplay.movement.CollisionSystem;

public class EnemyRemovalListener implements EntityListener {
    ComponentGrabber cg;

    public EnemyRemovalListener(ComponentGrabber cg) {
        this.cg = cg;
    }

    @Override
    public void entityAdded(Entity entity) {

    }

    @Override
    public void entityRemoved(Entity entity) {
        if (Families.enemies.matches(entity)) {
            removeFromWorld(entity);
            System.out.println("enemy removed from world");
        }
    }

    private void removeFromWorld(Entity entity) {
        Item item = cg.getItem(entity);
        CollisionSystem.world.remove(item.item);
    }
}
