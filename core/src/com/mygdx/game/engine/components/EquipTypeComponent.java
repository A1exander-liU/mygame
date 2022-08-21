package com.mygdx.game.engine.components;

import com.mygdx.game.engine.AcceptedEquipType;

public class EquipTypeComponent {
    public AcceptedEquipType acceptedEquipType;

    public EquipTypeComponent() {}

    public EquipTypeComponent(AcceptedEquipType acceptedEquipType) {
        this.acceptedEquipType = acceptedEquipType;
    }
}
