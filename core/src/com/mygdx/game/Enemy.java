package com.mygdx.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Enemy {
    public static int totalEnemies;
    public int enemyID;
    public Texture enemyTexture;
    public int x;
    public int y;
    public int width;
    public int height;

    public Enemy(FileHandle enemyImg, int x, int y, int width, int height, TiledMap tiledMap) {
        enemyID = ++totalEnemies;
        enemyTexture = new Texture(enemyImg);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        tiledMap.getLayers().get("Object Layer 1").getObjects().add(createEnemyMapObject());
    }

    private TextureMapObject createEnemyMapObject() {
        TextureRegion textureRegion = new TextureRegion(enemyTexture, x, y, width, height);
        TextureMapObject textureMapObject = new TextureMapObject(textureRegion);
        textureMapObject.setName("" + enemyID);
        return textureMapObject;
    }


}
