package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.components.DetectionProximity;
import com.mygdx.game.entityComponentSystem.components.Enemy;
import com.mygdx.game.entityComponentSystem.components.MovementBehavior;
import com.mygdx.game.entityComponentSystem.components.Spawn;
import com.mygdx.game.entityComponentSystem.components.Sprite;
import com.mygdx.game.entityComponentSystem.components.Health;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Name;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;
import com.mygdx.game.entityComponentSystem.components.Steering;

public class MobEntity extends Entity {
    // have method to get and change to behavior tree to use
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;
    BehaviorTree<MobEntity> behaviorTree;

    public MobEntity(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        addRequiredComponents();
        modifyComponentValues();
        // leaf task: overall behavior
        // Leaf Task: Hunt
        behaviorTree = new BehaviorTree<>();
        behaviorTree.setObject(this);
    }

    private void addRequiredComponents() {
        super.add(new Enemy());
        super.add(new Sprite());
        super.add(new Health());
        super.add(new ID());
        super.add(new Name());
        super.add(new Position());
        super.add(new Spawn());
        super.add(new Size());
        super.add(new Speed());
        super.add(new Steering(this));
        super.add(new DetectionProximity(this, 32, root));
        super.add(new MovementBehavior(this));
    }

    private void modifyComponentValues() {
        Sprite entitySprite = cg.getSprite(this);
        ID id = cg.getID(this);
        Name name = cg.getName(this);
        Size size = cg.getSize(this);
        Speed speed = cg.getSpeed(this);
        entitySprite.texture = new Texture(Gdx.files.internal("testPlayer.png"));
        name.name = "" + id.ID;
        size.width = 32;
        size.height = 32;
        speed.xSpeed = 2;
        speed.ySpeed = 2;
    }

    public BehaviorTree<MobEntity> getBehaviorTree() {
        return behaviorTree;
    }

    public void setBehaviorTree(BehaviorTree<MobEntity> behaviorTree) {
        this.behaviorTree = behaviorTree;
    }
}
