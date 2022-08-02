package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;

import java.util.HashSet;
import java.util.Set;

import javax.print.attribute.SetOfIntegerSyntax;

import jdk.javadoc.internal.doclets.toolkit.taglets.SeeTaglet;

public class SimulationSystem extends EntitySystem {
    ComponentGrabber cg;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> collisions;
    ImmutableArray<Entity> enemies;
    Entity player;
    World<Entity> world;

    public SimulationSystem(ComponentGrabber cg, GameMapProperties gameMapProperties) {
        this.cg = cg;
        this.gameMapProperties = gameMapProperties;
        collisions = MyGame.engine.getEntitiesFor(Families.collisions);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        world = new World<>();
        for (int i = 0; i < collisions.size(); i++) {
            Entity entity = collisions.get(i);
            entity.add(new com.mygdx.game.engine.components.Item());
            Position pos = cg.getPosition(entity);
            Size size = cg.getSize(entity);
            Item<Entity> item = new Item<>(entity);
            cg.getItem(entity).item = item;
            world.add(item, pos.x, pos.y, size.width, size.height);
        }
    }

    @Override
    public void update(float delta) {
    }
}
