package com.mygdx.game.engine.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Direction;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.AttackRange;
import com.mygdx.game.engine.components.Orientation;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;

public class BasicAttackSystem extends EntitySystem {
    ComponentGrabber cg;
    GameMapProperties gameMapProperties;
    Entity player;

    public BasicAttackSystem(ComponentGrabber cg, GameMapProperties gameMapProperties) {
        super(8);
        this.cg = cg;
        this.gameMapProperties = gameMapProperties;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isButtonJustPressed(Input.Keys.SPACE)) {
            performSlash();
        }
    }

    private void performSlash() {
        Orientation orientation = cg.getOrientation(player);
        Position pos = cg.getPosition(player);
        Size size = cg.getSize(player);
        Entity attack = makeAttackAreaEntity();
        AttackRange attackRange = cg.getAttackRange(attack);
        if (orientation.orientation == Direction.EAST) {
            Vector2 start = new Vector2(
                    pos.x + size.width,
                    pos.y + (size.height / 2));
            Vector2 end = new Vector2(start.x + attackRange.range, start.y);
            Ray<Vector2> ray = new Ray<>(start, end);
        }
    }

    private Entity makeAttackAreaEntity() {
        return new Entity()
                .add(new Position())
                .add(new AttackRange());
    }
}
