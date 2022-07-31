package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.engine.EnemyState;

// state component is state only
// have system to handle the update logic of changing state of the agent
public class StateComponent implements Component {
    public EnemyState state;

    public StateComponent() {

    }

    public StateComponent(EnemyState state) {
        this.state = state;
    }
}