package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.engine.components.Camera;
import com.mygdx.game.engine.components.DetectionProximity;
import com.mygdx.game.engine.components.Item;
import com.mygdx.game.engine.components.ManaComponent;
import com.mygdx.game.engine.components.Orientation;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.Health;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Player;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Speed;
import com.mygdx.game.engine.components.Steering;

public class PlayerEntity extends Entity {

    public PlayerEntity() {
        addRequiredComponents();
    }

    private void addRequiredComponents() {
        super.add(new Camera());
        super.add(new DetectionProximity(this, 20));
        super.add(new Health());
        super.add(new ID());
        super.add(new Item());
        super.add(new ManaComponent());
        super.add(new Name());
        super.add(new Orientation());
        super.add(new ParameterComponent());
        super.add(new Player());
        super.add(new Position());
        super.add(new Size());
        super.add(new Speed());
        super.add(new Sprite());
        super.add(new Steering(this));
    }
}
