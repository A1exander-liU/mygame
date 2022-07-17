package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.components.EnemyComponent;
import com.mygdx.game.entityComponentSystem.components.NameComponent;
import com.mygdx.game.entityComponentSystem.components.SpriteComponent;
import com.mygdx.game.entityComponentSystem.components.HealthComponent;
import com.mygdx.game.entityComponentSystem.components.IDComponent;
import com.mygdx.game.entityComponentSystem.components.PositionComponent;
import com.mygdx.game.entityComponentSystem.components.SizeComponent;
import com.mygdx.game.entityComponentSystem.components.SpeedComponent;

public class MobEntity extends Entity {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;


    public MobEntity(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        addRequiredComponents();
        modifyComponentValues();
    }

    private void addRequiredComponents() {
        super.add(new EnemyComponent());
        super.add(new SpriteComponent());
        super.add(new HealthComponent());
        super.add(new IDComponent());
        super.add(new NameComponent());
        super.add(new PositionComponent());
        super.add(new SizeComponent());
        super.add(new SpeedComponent());
    }

    private void modifyComponentValues() {
        SpriteComponent spriteComponent = cg.getSprite(this);
        IDComponent idComponent = cg.getID(this);
        NameComponent nameComponent = cg.getName(this);
        SizeComponent sizeComponent = cg.getSize(this);
        SpeedComponent speedComponent = cg.getSpeed(this);
        spriteComponent.texture = new Texture(Gdx.files.internal("testPlayer.png"));
        nameComponent.name = "" + idComponent.ID;
        sizeComponent.width = 32;
        sizeComponent.height = 32;
        speedComponent.xSpeed = 2;
        speedComponent.ySpeed = 2;
    }
}
