package com.mygdx.game.engine.components.inventory.items.individual;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.engine.AcceptedEquipType;

public class EquipTypeComponent implements Component {
    public AcceptedEquipType acceptedEquipType;

    public EquipTypeComponent() {}

    public EquipTypeComponent(AcceptedEquipType acceptedEquipType) {
        this.acceptedEquipType = acceptedEquipType;
    }
}
