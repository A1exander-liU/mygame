package com.mygdx.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Polygon extends com.badlogic.gdx.math.Polygon {
    private Array<Vector2> vertices;

    public Polygon(float[] vertices) {
        super(vertices);
    }

    public Polygon() {
        vertices = new Array<>();
    }

    public Array<Vector2> getVerticesArray() {
        return vertices;
    }

    public void addVertex(Vector2 vector) {
        vertices.add(vector);
    }

    public void clearVerticesArray() {
        vertices.clear();
    }
}
