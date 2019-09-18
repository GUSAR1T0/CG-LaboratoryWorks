package store.vxdesign.cg.core.cut_polygon.utilities;

import com.badlogic.gdx.graphics.Color;

import java.security.SecureRandom;
import java.util.Random;

public class Shape {
    private float[] vertices;
    private Color color;

    public Shape(float[] vertices) {
        this.vertices = vertices;
        this.color = getRandomColor();
    }

    private Color getRandomColor() {
        Random random = new SecureRandom();
        int r = random.ints(1, 0, 255).sum();
        int g = random.ints(1, 0, 255).sum();
        int b = random.ints(1, 0, 255).sum();
        return new Color(r / 255f, g / 255f, b / 255f, 1);
    }

    public float[] getVertices() {
        return vertices;
    }

    public Color getColor() {
        return color;
    }
}
