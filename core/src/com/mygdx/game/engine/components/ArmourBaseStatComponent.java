package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;

public class ArmourBaseStatComponent implements Component {
    public int phyDef;
    public int magDef;

    public ArmourBaseStatComponent() {}

    public ArmourBaseStatComponent(int phyDef, int magDef) {
        this.phyDef = phyDef;
        this.magDef = magDef;
    }
}
