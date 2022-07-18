package com.mygdx.game.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.MapObjectDrawer;
import com.mygdx.game.MyGame;
import com.mygdx.game.Player;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.entityComponentSystem.EntityFactory;
import com.mygdx.game.entityComponentSystem.EntityToMapAdder;
import com.mygdx.game.entityComponentSystem.MobEntity;
import com.mygdx.game.entityComponentSystem.PlayerEntity;
import com.mygdx.game.entityComponentSystem.components.Sprite;
import com.mygdx.game.entityComponentSystem.components.Health;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;
import com.mygdx.game.entityComponentSystem.systems.CollisionSystem;
import com.mygdx.game.entityComponentSystem.systems.EnemySpawningSystem;
import com.mygdx.game.entityComponentSystem.systems.MovementAndCollision;
import com.mygdx.game.entityComponentSystem.systems.MovementSystem;
import com.mygdx.game.entityComponentSystem.systems.SpawnZoneDetectionSystem;

public class GameScreen implements Screen {
    public MyGame parent;

    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    final int worldWidth = 100;
    final int worldHeight = 120;

    GameMapProperties gameMapProperties;
    TiledMap testMap;
    MapObjectDrawer tiledMapRenderer;

    MapLayer objectLayer;
    TextureRegion textureRegion;

    Player player;

    ComponentGrabber cg;
    EntityToMapAdder entityToMapAdder;

    public GameScreen(MyGame parent) {
        testMap = new TmxMapLoader().load("untitled.tmx");
        gameMapProperties = new GameMapProperties(testMap);
        this.parent = parent;
        parent.engine = new Engine();
        cg = new ComponentGrabber(parent);
        entityToMapAdder = new EntityToMapAdder(testMap, cg);
        EntityFactory entityFactory = new EntityFactory(cg, parent);
        for (int i = 0; i < 3; i++) {
            MobEntity entity = new MobEntity(cg, parent, gameMapProperties);
            parent.engine.addEntity(entity);
            entityToMapAdder.addEntityToMap(testMap, entity);
        }
        PlayerEntity playerEntity = new PlayerEntity(cg, parent, gameMapProperties, "player");

        // to add system (now all allowed entities will move every frame)
        // you can enable and disable a system temporarily
        /* disabled systems will not update (enemies will stop moving)
        *  useful when you need to call the pause method (you can just disable
        *  the systems like movement)*/
        // parent.engine.getSystem(MovementSystem.class).setProcessing(false);
        // the engine: center of the framework
        Entity enemy = new Entity();
        // need to add entities to the engine (can also remove entities)
        // to add components to an entity
        enemy.add(new Health());
        enemy.add(new Position());
        enemy.add(new Speed());
        enemy.add(new Size());
        enemy.add(new Sprite());
        enemy.add(new ID());
        Sprite enemySprite = cg.getSprite(enemy);
        enemySprite.texture = new Texture(Gdx.files.internal("testPlayer.png"));
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

        player = new Player(Gdx.files.internal("testPlayer.png"), "player");

        objectLayer = testMap.getLayers().get("Object Layer 1");
        textureRegion = new TextureRegion(player.getPlayerSprite(), player.playerSprite.getWidth(), player.playerSprite.getHeight());

        TextureMapObject tmo = new TextureMapObject(textureRegion);
        tmo.setX(0);
        tmo.setY(0);
        tmo.setName(player.playerName);
//        objectLayer.getObjects().add(tmo);

        MovementAndCollision movementAndCollision = new MovementAndCollision(cg, parent, gameMapProperties);
        MovementSystem movementSystem = new MovementSystem(cg, parent);
        CollisionSystem collisionSystem = new CollisionSystem(cg, parent, gameMapProperties);
        EnemySpawningSystem enemySpawningSystem = new EnemySpawningSystem(cg, parent, gameMapProperties);
        SpawnZoneDetectionSystem spawnZoneDetectionSystem = new SpawnZoneDetectionSystem(cg, parent, gameMapProperties);
        parent.engine.addSystem(movementAndCollision);
//        parent.engine.addSystem(movementSystem);
//        parent.engine.addSystem(collisionSystem);
        parent.engine.addSystem(enemySpawningSystem);
        parent.engine.addSystem(spawnZoneDetectionSystem);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        player.movePlayer(gameMapProperties);
        Entity entity = parent.engine.getEntitiesFor(Family.all(com.mygdx.game.entityComponentSystem.components.Player.class).get()).get(0);
        tiledMapRenderer.setView(cg.getCamera(entity).camera);
        parent.engine.update(delta);
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
        ImmutableArray<EntitySystem> systems = parent.engine.getSystems();
        for (EntitySystem system : systems) {
            system.setProcessing(false);
        }
    }

    private void resumeSystems() {
        ImmutableArray<EntitySystem> systems = parent.engine.getSystems();
        for (EntitySystem system : systems) {
            system.setProcessing(true);
        }
    }
}
