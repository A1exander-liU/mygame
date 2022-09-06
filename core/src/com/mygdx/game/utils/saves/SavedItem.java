package com.mygdx.game.utils.saves;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.mygdx.game.engine.components.AffixesComponent;
import com.mygdx.game.engine.components.ArmourBaseStatComponent;
import com.mygdx.game.engine.components.DescriptionComponent;
import com.mygdx.game.engine.components.InventoryItemComponent;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.QuantityComponent;
import com.mygdx.game.engine.components.RarityComponent;
import com.mygdx.game.engine.components.StackableComponent;
import com.mygdx.game.engine.components.WeaponBaseStatComponent;
import com.mygdx.game.engine.utils.componentutils.Mappers;

public class SavedItem {
    InventoryItemComponent inventoryItemComponent;
    Name name;
    String itemImgPath;
    RarityComponent rarityComponent;
    DescriptionComponent descriptionComponent;
    QuantityComponent quantityComponent;
    StackableComponent stackableComponent;
    WeaponBaseStatComponent weaponBaseStatComponent;
    ArmourBaseStatComponent armourBaseStatComponent;
    AffixesComponent affixesComponent;

    public SavedItem() {}

    public SavedItem(Entity item) {
        inventoryItemComponent = Mappers.inventoryItem.get(item);
        name = Mappers.name.get(item);
        itemImgPath = ((FileTextureData) Mappers.sprite.get(item).texture.getTextureData()).getFileHandle().path();
        rarityComponent = Mappers.rarity.get(item);
        descriptionComponent = Mappers.description.get(item);
        quantityComponent = Mappers.quantity.get(item);
        stackableComponent = Mappers.stackable.get(item);
        weaponBaseStatComponent = Mappers.weaponBaseStat.get(item);
        armourBaseStatComponent = Mappers.armourBaseStat.get(item);
        affixesComponent = Mappers.affixes.get(item);
    }
}
