package com.mygdx.game.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.JsonSearcher;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.MapObjectDrawer;
import com.mygdx.game.MyGame;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.engine.EntityFactory;
import com.mygdx.game.engine.EntityToMapAdder;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.PlayerEntity;
import com.mygdx.game.engine.systems.EnemySpawningSystem;
import com.mygdx.game.engine.systems.EntityRemovalSystem;
import com.mygdx.game.engine.systems.MapUpdateSystem;
import com.mygdx.game.engine.systems.MovementSystem;
import com.mygdx.game.engine.systems.CollisionSystem;
import com.mygdx.game.engine.systems.OrientationSystem;
import com.mygdx.game.engine.systems.TimeSystem;
import com.mygdx.game.engine.systems.combat.BasicAttackSystem;
import com.mygdx.game.engine.systems.enemyai.StateSystem;
import com.mygdx.game.engine.systems.enemyai.SteeringSystem;

public class GameScreen implements Screen {
    public MyGame parent;

    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    final int worldWidth = 100;
    final int worldHeight = 120;

    GameMapProperties gameMapProperties;
    TiledMap testMap;
    MapObjectDrawer tiledMapRenderer;

    ComponentGrabber cg;

    public GameScreen(MyGame parent) {
        parent.jsonSearcher = new JsonSearcher(Gdx.files.internal("gameData/enemies.json"));
        testMap = new TmxMapLoader().load("untitled.tmx");
        this.parent = parent;
        MyGame.engine = new Engine();
        cg = new ComponentGrabber(parent);
        MyGame.gameMapProperties = new GameMapProperties(testMap);
        gameMapProperties = new GameMapProperties(testMap, parent);
        parent.entityToMapAdder = new EntityToMapAdder(testMap, cg);
        EntityFactory entityFactory = new EntityFactory(cg, parent);
        PlayerEntity playerEntity = new PlayerEntity(cg, parent, gameMapProperties, "player");

        // to add system (now all allowed entities will move every frame)
        // you can enable and disable a system temporarily
        /* disabled systems will not update (enemies will stop moving)
        *  useful when you need to call the pause method (you can just disable
        *  the systems like movement)*/
        // parent.engine.getSystem(MovementSystem.class).setProcessing(false);
        // the engine: center of the framework
//        Entity enemy = new Entity();
        // need to add entities to the engine (can also remove entities)
        // to add components to an entity
//        enemy.add(new Health());
//        enemy.add(new Position());
//        enemy.add(new Speed());
//        enemy.add(new Size());
//        enemy.add(new Sprite());
//        enemy.add(new ID());
//        Sprite enemySprite = cg.getSprite(enemy);
//        enemySprite.texture = new Texture(Gdx.files.internal("testPlayer.png"));
//        parent.engine.addEntity(enemy);
//        entityToMapAdder.addEntityToMap(testMap, enemy);

        // families (entities with same collection of components)
        /* here: creating a family called obstacles (meaning obstacles will only
        have the components: position and size) */
        /* can add more options to add more controls:
        *  one(): choose between one of the provided components
        *  exclude(): can't have this component */

        parent.batch = new SpriteBatch();
        // to display the map as orthogonal (top down)
        tiledMapRenderer = new MapObjectDrawer(testMap);

        // system priority:
        // timeSystem: 1
        // spawnSystem: 2
        // movementSystem: 3
        // stateSystem: 4
        // steeringSystem: 5
        // collisionSystem: 6
        // mapUpdateSystem: 98
        // removalSystem: 99

        MovementSystem movementAndCollision = new MovementSystem(cg, parent, gameMapProperties);
        EnemySpawningSystem enemySpawningSystem = new EnemySpawningSystem(cg, parent, gameMapProperties);
        SteeringSystem steeringSystem = new SteeringSystem(cg, parent, gameMapProperties);
        TimeSystem timeSystem = new TimeSystem(parent);
        StateSystem stateSystem = new StateSystem(cg, gameMapProperties);
        EntityRemovalSystem entityRemovalSystem = new EntityRemovalSystem(cg, gameMapProperties);
        CollisionSystem simulationSystem = new CollisionSystem(cg, gameMapProperties);
        MapUpdateSystem mapUpdateSystem = new MapUpdateSystem(cg, gameMapProperties);
        OrientationSystem orientationSystem = new OrientationSystem(cg);
        BasicAttackSystem basicAttackSystem = new BasicAttackSystem(cg, gameMapProperties);
        MyGame.engine.addSystem(movementAndCollision);
        MyGame.engine.addSystem(enemySpawningSystem);
        MyGame.engine.addSystem(steeringSystem);
        MyGame.engine.addSystem(timeSystem);
        MyGame.engine.addSystem(stateSystem);
        MyGame.engine.addSystem(entityRemovalSystem);
        MyGame.engine.addSystem(simulationSystem);
        MyGame.engine.addSystem(mapUpdateSystem);
        MyGame.engine.addSystem(orientationSystem);
        MyGame.engine.addSystem(basicAttackSystem);
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
        Entity entity = MyGame.engine.getEntitiesFor(Families.player).get(0);
        tiledMapRenderer.setView(cg.getCamera(entity).camera);
        MyGame.engine.update(delta);
        tiledMapRenderer.render();
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
}
