package com.mygdx.game.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
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
import com.mygdx.game.entityComponentSystem.EntityToMapAdder;
import com.mygdx.game.entityComponentSystem.components.EntitySprite;
import com.mygdx.game.entityComponentSystem.components.Health;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;
import com.mygdx.game.entityComponentSystem.systems.CollisionSystem;
import com.mygdx.game.entityComponentSystem.systems.MovementSystem;

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

    EntityToMapAdder entityToMapAdder;

    public GameScreen(MyGame parent) {
        testMap = new TmxMapLoader().load("untitled.tmx");
        gameMapProperties = new GameMapProperties(testMap);
        this.parent = parent;
        parent.engine = new Engine();
        ComponentGrabber cg = new ComponentGrabber();
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
        enemy.add(new EntitySprite());
        enemy.add(new ID());
        EntitySprite enemySprite = cg.getSprite(enemy);
        enemySprite.texture = new Texture(Gdx.files.internal("testPlayer.png"));
        parent.engine.addEntity(enemy);
        entityToMapAdder = new EntityToMapAdder(testMap, cg);
        // PROBLEM: error with addEntityToMap method
        entityToMapAdder.addEntityToMap(testMap, enemy);
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
        objectLayer.getObjects().add(tmo);

        parent.engine.addSystem(new MovementSystem(cg, parent));
        parent.engine.addSystem(new CollisionSystem(cg, parent, gameMapProperties));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        player.movePlayer(gameMapProperties);
        tiledMapRenderer.setView(player.getCamera());
        tiledMapRenderer.render();
        parent.engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        // stop enemies from moving when game is paused
        parent.engine.getSystem(MovementSystem.class).setProcessing(false);
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        parent.batch.dispose();
        testMap.dispose();
    }
}
