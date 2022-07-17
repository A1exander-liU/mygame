package com.mygdx.game.entityComponentSystem.systems.enemyai;

import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;

public class SeekingSystem extends EntitySystem {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;

    public SeekingSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
    }
}
