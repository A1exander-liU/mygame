package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.game.MyGame;

public class TimeSystem extends EntitySystem {
    MyGame root;
    public static float time = 0;
    public static int second = 0;

    public TimeSystem(MyGame root) {
        this.root = root;
    }

    @Override
    public Engine getEngine() {
        return MyGame.engine;
    }

    @Override
    public void update(float delta) {
        time += delta;
        if (time - second >= 1) {
            second++;
        }
    }

}
