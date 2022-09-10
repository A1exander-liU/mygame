package com.mygdx.game.engine.entityListeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.utils.componentutils.Families;
import com.mygdx.game.engine.utils.componentutils.Mappers;

public class WorldListener implements EntityListener {
    // will remove and add collidable  entities to the world
    @Override
    public void entityAdded(Entity entity) {
        // checking if they are collidable
        if (Mappers.collidable.get(entity) != null) {
            // add the collidable entity to the world
            if (Families.enemies.matches(entity)) System.out.println("enemy added to world");
            if (Families.player.matches(entity)) System.out.println("player added to world");
            if (Families.obstacles.matches(entity)) System.out.println("obstacle added to world");
            Position position = Mappers.position.get(entity);
            Size size = Mappers.size.get(entity);
            MyGame.world.add(Mappers.item.get(entity).item, position.x, position.y, size.width, size.height);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (Mappers.collidable.get(entity) != null) {
            System.out.println("removed from world");
            MyGame.world.remove(Mappers.item.get(entity).item);
        }
    }
}
