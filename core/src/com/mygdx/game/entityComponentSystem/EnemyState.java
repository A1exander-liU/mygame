package com.mygdx.game.entityComponentSystem;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum EnemyState implements State<MobEntity> {
    IDLE() {},
    HUNT() {},
    FLEE() {};

    @Override
    public void enter(MobEntity entity) {

    }

    @Override
    public void update(MobEntity entity) {

    }

    @Override
    public void exit(MobEntity entity) {

    }

    @Override
    public boolean onMessage(MobEntity entity, Telegram telegram) {
        return false;
    }
}
