package com.mygdx.game.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.engine.systems.saving.SaveTest;
import com.mygdx.game.engine.utils.componentutils.Families;
import com.mygdx.game.jsonreaders.JsonEnemyFinder;
import com.mygdx.game.jsonreaders.JsonItemFinder;
import com.mygdx.game.engine.utils.componentutils.ComponentGrabber;
import com.mygdx.game.utils.map.MapObjectDrawer;
import com.mygdx.game.MyGame;
import com.mygdx.game.utils.map.GameMapProperties;
import com.mygdx.game.engine.utils.factories.EntityFactory;
import com.mygdx.game.engine.utils.EntityToMapAdder;
import com.mygdx.game.engine.utils.factories.ItemFactory;
import com.mygdx.game.engine.entityListeners.EnemyRemovalListener;
import com.mygdx.game.engine.systems.EnemyDeathSystem;
import com.mygdx.game.engine.systems.gameplay.looting.EnemyDropSystem;
import com.mygdx.game.engine.systems.gameplay.EnemySpawningSystem;
import com.mygdx.game.engine.systems.EntityRemovalSystem;
import com.mygdx.game.engine.systems.render.HealthBarRenderSystem;
import com.mygdx.game.engine.systems.render.ui.InventoryRenderSystem;
import com.mygdx.game.engine.systems.render.ui.InventoryTest;
import com.mygdx.game.engine.systems.render.ItemDropLabelRenderSystem;
import com.mygdx.game.engine.systems.gameplay.looting.ItemPickupSystem;
import com.mygdx.game.engine.systems.render.ui.ItemWindowRenderSystem;
import com.mygdx.game.engine.systems.gameplay.looting.LootingSystem;
import com.mygdx.game.engine.systems.render.MapUpdateSystem;
import com.mygdx.game.engine.systems.gameplay.movement.MovementSystem;
import com.mygdx.game.engine.systems.gameplay.movement.CollisionSystem;
import com.mygdx.game.engine.systems.gameplay.movement.OrientationSystem;
import com.mygdx.game.engine.systems.render.ui.PlayerHudRenderSystem;
import com.mygdx.game.engine.systems.TimeSystem;
import com.mygdx.game.engine.systems.gameplay.combat.BasicAttackSystem;
import com.mygdx.game.engine.systems.gameplay.combat.EnemyAttackSystem;
import com.mygdx.game.engine.systems.gameplay.enemyai.StateSystem;
import com.mygdx.game.engine.systems.gameplay.enemyai.SteeringSystem;

public class GameScreen implements Screen {
    public MyGame parent;

    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    final int worldWidth = 100;
    final int worldHeight = 120;

    public static ItemFactory itemFactory;

    TiledMap testMap;
    MapObjectDrawer tiledMapRenderer;
    EntityFactory entityFactory;

    ComponentGrabber cg;

    public static InputMultiplexer inventoryMultiplexer;

    public GameScreen(MyGame parent) {
        initializeObjects();
        // now player is loaded from slot and is in engine
        // check if no player entity exists means it was loaded from slot that was empty
        if (MyGame.engine.getEntitiesFor(Families.player).size() == 0)
            // make new player
            entityFactory.makePlayer("player");
        // if loaded from non-empty slot, PlayerEntity already exists
        
        inventoryMultiplexer = new InputMultiplexer();

        parent.batch = new SpriteBatch();
        // to display the map as orthogonal (top down)
        tiledMapRenderer = new MapObjectDrawer(testMap);

        MovementSystem movementSystem = new MovementSystem(cg);
        EnemySpawningSystem enemySpawningSystem = new EnemySpawningSystem(cg, entityFactory);
        SteeringSystem steeringSystem = new SteeringSystem(cg);
        TimeSystem timeSystem = new TimeSystem();
        StateSystem stateSystem = new StateSystem(cg);
        EntityRemovalSystem entityRemovalSystem = new EntityRemovalSystem(cg);
        CollisionSystem collisionSystem = new CollisionSystem(cg);
        MapUpdateSystem mapUpdateSystem = new MapUpdateSystem(cg, tiledMapRenderer);
        OrientationSystem orientationSystem = new OrientationSystem(cg);
        BasicAttackSystem basicAttackSystem = new BasicAttackSystem(cg, MyGame.gameMapProperties);
        HealthBarRenderSystem healthBarRenderSystem = new HealthBarRenderSystem(cg);
        EnemyAttackSystem enemyAttackSystem = new EnemyAttackSystem(cg, parent);
        PlayerHudRenderSystem playerHudRenderSystem = new PlayerHudRenderSystem(cg);
        InventoryRenderSystem inventoryRenderSystem = new InventoryRenderSystem(cg);
        InventoryTest inventoryTest = new InventoryTest(cg, itemFactory);
        ItemWindowRenderSystem itemWindowRenderSystem = new ItemWindowRenderSystem();
        LootingSystem lootingSystem = new LootingSystem();
        EnemyDeathSystem enemyDeathSystem = new EnemyDeathSystem();
        EnemyDropSystem enemyDropSystem = new EnemyDropSystem(itemFactory);
        ItemDropLabelRenderSystem itemDropLabelRenderSystem = new ItemDropLabelRenderSystem();
        ItemPickupSystem itemPickupSystem = new ItemPickupSystem();
        SaveTest saveTest = new SaveTest(parent);
        MyGame.engine.addSystem(movementSystem);
        MyGame.engine.addSystem(enemySpawningSystem);
        MyGame.engine.addSystem(steeringSystem);
        MyGame.engine.addSystem(timeSystem);
        MyGame.engine.addSystem(stateSystem);
        MyGame.engine.addSystem(entityRemovalSystem);
        MyGame.engine.addSystem(collisionSystem);
        MyGame.engine.addSystem(mapUpdateSystem);
        MyGame.engine.addSystem(orientationSystem);
        MyGame.engine.addSystem(basicAttackSystem);
        MyGame.engine.addSystem(healthBarRenderSystem);
        MyGame.engine.addSystem(enemyAttackSystem);
        MyGame.engine.addSystem(playerHudRenderSystem);
        MyGame.engine.addSystem(inventoryRenderSystem);
        MyGame.engine.addSystem(inventoryTest);
        MyGame.engine.addSystem(itemWindowRenderSystem);
        MyGame.engine.addSystem(lootingSystem);
        MyGame.engine.addSystem(enemyDeathSystem);
        MyGame.engine.addSystem(enemyDropSystem);
        MyGame.engine.addSystem(itemDropLabelRenderSystem);
        MyGame.engine.addSystem(itemPickupSystem);
        MyGame.engine.addSystem(saveTest);
        checkPriorities();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        MyGame.engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        pauseSystems();
    }

    @Override
    public void resume() {
        resumeSystems();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        parent.batch.dispose();
        testMap.dispose();
    }

    private void pauseSystems() {
        ImmutableArray<EntitySystem> systems = MyGame.engine.getSystems();
        for (EntitySystem system : systems) {
            system.setProcessing(false);
        }
    }

    private void resumeSystems() {
        ImmutableArray<EntitySystem> systems = MyGame.engine.getSystems();
        for (EntitySystem system : systems) {
            system.setProcessing(true);
        }
    }

    private void checkPriorities() {
        ImmutableArray<EntitySystem> systems = MyGame.engine.getSystems();
        for (EntitySystem system : systems) {
            System.out.println(system.getClass() + ": " + system.priority);
        }
    }

    private void initializeObjects() {
        parent.jsonSearcher = new JsonEnemyFinder();
        parent.itemFinder = new JsonItemFinder();
        itemFactory = new ItemFactory(parent.itemFinder);
        ItemFactory itemFactory = new ItemFactory(parent.itemFinder);
        testMap = new TmxMapLoader().load("untitled.tmx");
        this.parent = parent;
        cg = new ComponentGrabber();
        MyGame.engine.addEntityListener(new EnemyRemovalListener(cg));
        entityFactory = new EntityFactory(cg, parent);
        MyGame.gameMapProperties = new GameMapProperties(testMap, entityFactory);
        parent.entityToMapAdder = new EntityToMapAdder(cg);
    }
}
