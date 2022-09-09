package com.mygdx.game.engine.entityListeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.dongbat.jbump.Item;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.systems.gameplay.movement.CollisionSystem;
import com.mygdx.game.engine.utils.componentutils.Mappers;
import com.mygdx.game.screens.GameScreen;

public class WorldListener implements EntityListener {
    // will remove and add collidable  entities to the world
    @Override
    public void entityAdded(Entity entity) {
        // checking if they are collidable
        if (Mappers.collidable.get(entity) != null) {
            // add the collidable entity to the world
            Position position = Mappers.position.get(entity);
            Size size = Mappers.size.get(entity);
            GameScreen.world.add(Mappers.item.get(entity).item, position.x, position.y, size.width, size.height);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (Mappers.collidable.get(entity) != null) {
            GameScreen.world.remove(Mappers.item.get(entity).item);
        }
    }
}
