package com.mygdx.game.engine.components.inventory.items.shared;

import com.badlogic.ashley.core.Component;

public class DescriptionComponent implements Component {
    public String description;

    public DescriptionComponent() {}

    public DescriptionComponent(String description) {
        this.description = description;
    }
}
