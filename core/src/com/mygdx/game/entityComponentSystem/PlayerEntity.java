package com.mygdx.game.entityComponentSystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.components.Camera;
import com.mygdx.game.entityComponentSystem.components.EntitySprite;
import com.mygdx.game.entityComponentSystem.components.Health;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Name;
import com.mygdx.game.entityComponentSystem.components.Player;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;

public class PlayerEntity extends Entity {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;

    Rectangle playerBox;
    OrthographicCamera camera;

    public PlayerEntity(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties, String name) {
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        addRequiredComponents();
    }

    private void addRequiredComponents() {
        super.add(new Camera());
        super.add(new EntitySprite());
        super.add(new Health());
        super.add(new ID());
        super.add(new Name());
        super.add(new Player());
        super.add(new Position());
        super.add(new Size());
        super.add(new Speed());
    }
}
