package com.mygdx.game.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.mygdx.game.engine.systems.saving.SaveTest;
import com.mygdx.game.engine.utils.componentutils.Families;
import com.mygdx.game.jsonreaders.JsonEnemyFinder;
import com.mygdx.game.jsonreaders.JsonItemFinder;
import com.mygdx.game.engine.utils.componentutils.ComponentGrabber;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.utils.factories.EntityFactory;
import com.mygdx.game.engine.utils.EntityToMapAdder;
import com.mygdx.game.engine.utils.factories.ItemFactory;
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
import com.mygdx.game.utils.map.EntityTextureObject;
import com.mygdx.game.utils.map.GameMapProperties;

public class GameScreen implements Screen {
    public MyGame parent;

    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    final int worldWidth = 100;
    final int worldHeight = 120;

    public static ItemFactory itemFactory;

    ComponentGrabber cg;

    public static InputMultiplexer inventoryMultiplexer;

    int loads;

    public GameScreen(MyGame parent) {
        this.parent = parent;
        initializeObjects();

//        MovementSystem movementSystem = new MovementSystem(parent.cg);
//        EnemySpawningSystem enemySpawningSystem = new EnemySpawningSystem(parent.cg, parent.entityFactory);
//        SteeringSystem steeringSystem = new SteeringSystem(parent.cg);
//        TimeSystem timeSystem = new TimeSystem();
//        StateSystem stateSystem = new StateSystem(parent.cg);
//        EntityRemovalSystem entityRemovalSystem = new EntityRemovalSystem(parent.cg);
//        CollisionSystem collisionSystem = new CollisionSystem(parent.cg);
//        MapUpdateSystem mapUpdateSystem = new MapUpdateSystem(parent.cg, parent.tiledMapRenderer);
//        OrientationSystem orientationSystem = new OrientationSystem(parent.cg);
//        BasicAttackSystem basicAttackSystem = new BasicAttackSystem(parent.cg, MyGame.gameMapProperties);
//        HealthBarRenderSystem healthBarRenderSystem = new HealthBarRenderSystem(parent.cg);
//        EnemyAttackSystem enemyAttackSystem = new EnemyAttackSystem(parent.cg, parent);
//        PlayerHudRenderSystem playerHudRenderSystem = new PlayerHudRenderSystem(parent.cg, parent);
//        InventoryRenderSystem inventoryRenderSystem = new InventoryRenderSystem(parent.cg);
//        InventoryTest inventoryTest = new InventoryTest(parent.cg, itemFactory);
//        ItemWindowRenderSystem itemWindowRenderSystem = new ItemWindowRenderSystem();
//        LootingSystem lootingSystem = new LootingSystem();
//        EnemyDeathSystem enemyDeathSystem = new EnemyDeathSystem();
//        EnemyDropSystem enemyDropSystem = new EnemyDropSystem(itemFactory);
//        ItemDropLabelRenderSystem itemDropLabelRenderSystem = new ItemDropLabelRenderSystem();
//        ItemPickupSystem itemPickupSystem = new ItemPickupSystem();
//        SaveTest saveTest = new SaveTest(parent);
//        MyGame.engine.addSystem(movementSystem);
//        MyGame.engine.addSystem(enemySpawningSystem);
//        MyGame.engine.addSystem(steeringSystem);
//        MyGame.engine.addSystem(timeSystem);
//        MyGame.engine.addSystem(stateSystem);
//        MyGame.engine.addSystem(entityRemovalSystem);
//        MyGame.engine.addSystem(collisionSystem);
//        MyGame.engine.addSystem(mapUpdateSystem);
//        MyGame.engine.addSystem(orientationSystem);
//        MyGame.engine.addSystem(basicAttackSystem);
//        MyGame.engine.addSystem(healthBarRenderSystem);
//        MyGame.engine.addSystem(enemyAttackSystem);
//        MyGame.engine.addSystem(playerHudRenderSystem);
//        MyGame.engine.addSystem(inventoryRenderSystem);
//        MyGame.engine.addSystem(inventoryTest);
//        MyGame.engine.addSystem(itemWindowRenderSystem);
//        MyGame.engine.addSystem(lootingSystem);
//        MyGame.engine.addSystem(enemyDeathSystem);
//        MyGame.engine.addSystem(enemyDropSystem);
//        MyGame.engine.addSystem(itemDropLabelRenderSystem);
//        MyGame.engine.addSystem(itemPickupSystem);
//        MyGame.engine.addSystem(saveTest);
//        checkPriorities();
    }

    @Override
    public void show() {
        loads++;
        if (loads > 1) {
            System.out.println("loaded more than once");
        }
        MovementSystem movementSystem = new MovementSystem(parent.cg);
        EnemySpawningSystem enemySpawningSystem = new EnemySpawningSystem(parent.cg, parent.entityFactory);
        SteeringSystem steeringSystem = new SteeringSystem(parent.cg);
        TimeSystem timeSystem = new TimeSystem();
        StateSystem stateSystem = new StateSystem(parent.cg);
        EntityRemovalSystem entityRemovalSystem = new EntityRemovalSystem(parent.cg);
        CollisionSystem collisionSystem = new CollisionSystem(parent.cg);
        MapUpdateSystem mapUpdateSystem = new MapUpdateSystem(parent.cg, parent.tiledMapRenderer);
        OrientationSystem orientationSystem = new OrientationSystem(parent.cg);
        BasicAttackSystem basicAttackSystem = new BasicAttackSystem(parent.cg, MyGame.gameMapProperties);
        HealthBarRenderSystem healthBarRenderSystem = new HealthBarRenderSystem(parent.cg);
        EnemyAttackSystem enemyAttackSystem = new EnemyAttackSystem(parent.cg, parent);
        PlayerHudRenderSystem playerHudRenderSystem = new PlayerHudRenderSystem(parent.cg, parent);
        InventoryRenderSystem inventoryRenderSystem = new InventoryRenderSystem(parent.cg);
        InventoryTest inventoryTest = new InventoryTest(parent.cg, itemFactory);
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
        // now that all entities in engine are removed
        // need to rebuild all the obstacles as entities since they were removed too
        // plus the are only built inside the constructor of GameMapProperties

        // make sure there are no entities of obstacles before rebuilding them
        // when game screen is no longer current screen, no entities should exist
        if (MyGame.engine.getEntitiesFor(Families.obstacles).size() == 0) {
            MyGame.gameMapProperties.makeEntitiesFromCollisions();
        }
        // rebuild spawn entities (to spawn enemies and all entities are removed when changing
        // screens)
        if (MyGame.engine.getEntitiesFor(Families.spawns).size() == 0) {
            MyGame.engine.getSystem(EnemySpawningSystem.class).makeSpawnAreaEntities();;
        }

        // now player is loaded from slot and is in engine
        // check if no player entity exists means it was loaded from slot that was empty
        if (MyGame.engine.getEntitiesFor(Families.player).size() == 0)
            // make new player
            parent.entityFactory.makePlayer("player");
        // if loaded from non-empty slot, PlayerEntity already exists
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
        // when screen is moved back to game screen, all previous entities still exist
        // need to remove all entities
        MyGame.engine.removeAllEntities();

        MyGame.gameMapProperties.removeNonPersistingMapObjects();

        System.out.println(MyGame.world.getItems().size());
    }

    @Override
    public void dispose() {
        parent.batch.dispose();
        parent.testMap.dispose();
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
        itemFactory = new ItemFactory(parent.itemFinder);
        parent.entityToMapAdder = new EntityToMapAdder(cg);
        inventoryMultiplexer = new InputMultiplexer();
        parent.batch = new SpriteBatch();
    }
}
