package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.DetectionProximity;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.EnemyStateMachine;
import com.mygdx.game.engine.components.Item;
import com.mygdx.game.engine.components.MovementBehavior;
import com.mygdx.game.engine.components.Orientation;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.Spawn;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.Health;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Speed;
import com.mygdx.game.engine.components.StateComponent;
import com.mygdx.game.engine.components.Steering;

public class MobEntity extends Entity {
    // have method to get and change to behavior tree to use
    ComponentGrabber cg;
    MyGame root;
    BehaviorTree<MobEntity> behaviorTree;

    public MobEntity() {
        addRequiredComponents();
    }

    private void addRequiredComponents() {
        super.add(new DetectionProximity(this, 32));
        super.add(new Enemy());
        super.add(new StateComponent());
        super.add(new EnemyStateMachine(this));
        super.add(new Health());
        super.add(new ID());
        super.add(new Item());
        super.add(new Position());
        super.add(new Steering(this));
        super.add(new MovementBehavior(this));
        super.add(new Name());
        super.add(new Orientation());
        super.add(new ParameterComponent());
        super.add(new Size());
        super.add(new Spawn());
        super.add(new Speed());
        super.add(new Sprite());
    }
}
