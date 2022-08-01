package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.DetectionProximity;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.EnemyStateMachine;
import com.mygdx.game.engine.components.MovementBehavior;
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
    GameMapProperties gameMapProperties;
    BehaviorTree<MobEntity> behaviorTree;

    public MobEntity(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties, String name) {
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        addRequiredComponents();
        modifyComponentValues(name);
        addToEngine();
        addToMap();
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
        super.add(new StateComponent());
        super.add(new EnemyStateMachine(this));
        super.add(new ParameterComponent());
    }

    private void modifyComponentValues(String name) {
        Sprite entitySprite = cg.getSprite(this);
        ID id = cg.getID(this);
        Name enemyName = cg.getName(this);
        Size size = cg.getSize(this);
        Speed speed = cg.getSpeed(this);
        fillParameters(name);
        entitySprite.texture = new Texture(Gdx.files.internal("testPlayer.png"));
        enemyName.name = name;
        size.width = 32;
        size.height = 32;
        speed.xSpeed = 2;
        speed.ySpeed = 2;
//        behaviorTree.setObject(this);
    }

    private void fillParameters(String name) {
        // sets all the parameters of the enemy
        ParameterComponent parameters = cg.getParameters(this);
        JsonValue enemy = root.jsonSearcher.findByEnemyName(name);
        parameters.damage = enemy.getInt("DMG");
        parameters.health.maxHealth = enemy.getInt("HP");
        parameters.health.maxHealth = enemy.getInt("HP");
    }

    public BehaviorTree<MobEntity> getBehaviorTree() {
        return behaviorTree;
    }

    public void setBehaviorTree(BehaviorTree<MobEntity> behaviorTree) {
        this.behaviorTree = behaviorTree;
    }

    private void addToEngine() {
        MyGame.engine.addEntity(this);
    }

    private void addToMap() {
        root.entityToMapAdder.addEntityToMap(this);
    }
}
