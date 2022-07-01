package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;

import java.util.Objects;

public class Player {
    public Rectangle playerBox;
    public Texture playerSprite;
    public String playerName;

    public int xSpeed = 5;
    public int ySpeed = 5;

    float oldPlayerX;
    float oldPlayerY;

    float oldCameraX;
    float oldCameraY;

    boolean collided;
    boolean canTranslateCamera;

    OrthographicCamera camera;

    public Player(FileHandle playerImg, String playerName) {
        this.playerName = playerName;
        playerSprite = new Texture(playerImg);

        playerBox = new Rectangle();
        playerBox.setCenter(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        playerBox.setWidth(playerSprite.getWidth());
        playerBox.setHeight(playerSprite.getHeight());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.x = playerBox.x;
        camera.position.y = playerBox.y;
    }

    public Texture getPlayerSprite() {
        return playerSprite;
    }

    public void setPlayerSprite(FileHandle playerImg) {
        playerSprite = new Texture(playerImg);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void movePlayer(GameMapProperties gameMapProperties) {
        camera.update();
        oldCameraX = camera.position.x;
        oldCameraY = camera.position.y;
        handleMovementInput();
        resolveCollisions(gameMapProperties);
        keepPlayerInsideMap(gameMapProperties);
        matchCamPosToPlayer();
        updateTileMapPlayerSprite(gameMapProperties);
    }

    private void handleMovementInput() {
        oldPlayerX = playerBox.x;
        oldPlayerY = playerBox.y;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerBox.y += ySpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerBox.x += xSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerBox.y -= ySpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerBox.x -= xSpeed;
        }
    }

    private void keepPlayerInsideMap(GameMapProperties gameMapProperties) {
        if (playerBox.x < 0) {
            playerBox.x = 0;
        }
        else if (playerBox.x + playerSprite.getWidth() > gameMapProperties.mapWidth) {
            playerBox.x = gameMapProperties.mapWidth - playerSprite.getWidth();
        }
        else if (playerBox.y < 0) {
            playerBox.y = 0;
        }
        else if (playerBox.y + playerSprite.getHeight() > gameMapProperties.mapHeight) {
            playerBox.y = gameMapProperties.mapHeight - playerSprite.getHeight();
        }
    }

    private void matchCamPosToPlayer() {
        camera.position.x = playerBox.x;
        camera.position.y = playerBox.y;
    }

    public void resolveCollisions(GameMapProperties gameMapProperties) {
        collided = false;
        xSpeed = 5;
        ySpeed = 5;

        MapObjects obstacles = gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects();
        // removing the player object, it was added last, so its last obj
        for (int i = 0; i < obstacles.getCount() - 1; i++) {
            // getting the properties of the collision area rect
//            MapProperties objectProps = obstacles.get(i).getProperties();
            if (!Objects.equals(obstacles.get(i).getName(), playerName)) {
                Rectangle collisionArea = null;
                /* RectangleMapObjects have a Rectangle member, TextureMapObjects do not,
                *  meaning a new Rectangle has to be instantiated from the TextureMapObject's
                *  values (x, y, width, height). The values can be obtained from the
                *  TextureRegion member. */
                /* More checks needed later if different map objects will be added */
                if (obstacles.get(i) instanceof RectangleMapObject)
                    collisionArea = ((RectangleMapObject) obstacles.get(i)).getRectangle();
                if (obstacles.get(i) instanceof TextureMapObject) {
                    // getting TextureRegion of the TextureMapObject
                    TextureRegion textureRegion = ((TextureMapObject) obstacles.get(i)).getTextureRegion();
                    // Grabbing the values from the TextureRegion
                    float objX = ((TextureMapObject) obstacles.get(i)).getX();
                    float objY = ((TextureMapObject) obstacles.get(i)).getY();
                    float objWidth = textureRegion.getRegionWidth();
                    float objHeight = textureRegion.getRegionHeight();
                    // assign collisionArea a new Rectangle object
                    collisionArea = new Rectangle(objX, objY, objWidth, objHeight);
                }
                // overlap means if the 2 regions of the rectangle are on top of each other
                if (collisionArea != null && playerBox.overlaps(collisionArea)) {
                    collided = true;
                    // set the player position back to old position where they have not collided yet
                    playerBox.x = oldPlayerX;
                    playerBox.y = oldPlayerY;
                }
            }
        }
    }

    private void updateTileMapPlayerSprite(GameMapProperties gameMapProperties) {
        TextureMapObject character = (TextureMapObject) gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects().get(playerName);
        character.setX(playerBox.x);
        character.setY(playerBox.y);
    }

    private void trimCamera(GameMapProperties gameMapProperties) {
            canTranslateCamera = true;
        if (notInLeftRightBounds(gameMapProperties) || notInTopBottomBounds(gameMapProperties)) {
            canTranslateCamera = false;
            camera.position.x = oldCameraX;
            camera.position.y = oldCameraY;
        }
    }

    private boolean notInLeftRightBounds(GameMapProperties gameMapProperties) {
        if (camera.position.x < camera.viewportWidth / 2 || camera.position.x > gameMapProperties.mapWidth - camera.viewportWidth / 2)
            return true;
        return false;
    }

    private boolean notInTopBottomBounds(GameMapProperties gameMapProperties) {
        if (camera.position.y < camera.viewportHeight / 2 || camera.position.y > gameMapProperties.mapHeight - camera.viewportHeight / 2)
            return true;
        return false;
    }
}
