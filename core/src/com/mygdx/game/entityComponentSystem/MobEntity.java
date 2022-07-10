package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.components.Enemy;
import com.mygdx.game.entityComponentSystem.components.EntitySprite;
import com.mygdx.game.entityComponentSystem.components.Health;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Name;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;

public class MobEntity extends Entity {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;


    public MobEntity(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        addRequiredComponents();
    }

    private void addRequiredComponents() {
        super.add(new Enemy());
        super.add(new EntitySprite());
        super.add(new Health());
        super.add(new ID());
        super.add(new Name());
        super.add(new Position());
        super.add(new Size());
        super.add(new Speed());
    }
}
