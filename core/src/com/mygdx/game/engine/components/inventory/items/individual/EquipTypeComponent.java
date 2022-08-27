package com.mygdx.game.engine.components.inventory.items.individual;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.engine.AcceptedEquipType;
import com.mygdx.game.engine.Type;

public class EquipTypeComponent implements Component {
    public AcceptedEquipType acceptedEquipType;
    public Type acceptedType;

    public EquipTypeComponent() {}

    public EquipTypeComponent(AcceptedEquipType acceptedEquipType) {
        this.acceptedEquipType = acceptedEquipType;
    }

    public EquipTypeComponent(Type acceptedType) {
        this.acceptedType = acceptedType;
    }
}
