package store.vxdesign.cg.core.cycloid.utilities.handlers;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public final class PolygonHandler {
    private static final Random RANDOM = new SecureRandom();

    private PolygonHandler() {
    }

    public static Polygon generateConvexPolygon(int n, float maxLength) {
        float[] floats = new float[n * 2];
        List<Vector2> vectors = generateRandomConvexPolygon(n, maxLength);
        for (int i = 0; i < vectors.size() * 2; i += 2) {
            floats[i] = vectors.get(i / 2).x;
            floats[i + 1] = vectors.get(i / 2).y;
        }

        Polygon polygon = new Polygon(floats);
        Vector2 geometricMassCenter = getGeometricMassCenter(polygon);
        polygon.setPosition(-geometricMassCenter.x, -geometricMassCenter.y);
        polygon.setRotation(0);
        return new Polygon(polygon.getTransformedVertices());
    }

    private static List<Vector2> generateRandomConvexPolygon(int n, float maxLength) {
        List<Double> xPool = RANDOM.doubles(n, 0, maxLength).boxed().collect(Collectors.toList());
        List<Double> yPool = RANDOM.doubles(n, 0, maxLength).boxed().collect(Collectors.toList());

        Collections.sort(xPool);
        Collections.sort(yPool);

        Double minX = xPool.get(0);
        Double maxX = xPool.get(n - 1);
        Double minY = yPool.get(0);
        Double maxY = yPool.get(n - 1);

        List<Double> xVec = new ArrayList<>(n);
        List<Double> yVec = new ArrayList<>(n);

        double lastTop = minX, lastBot = minX;

        for (int i = 1; i < n - 1; i++) {
            double x = xPool.get(i);

            if (RANDOM.nextBoolean()) {
                xVec.add(x - lastTop);
                lastTop = x;
            } else {
                xVec.add(lastBot - x);
                lastBot = x;
            }
        }

        xVec.add(maxX - lastTop);
        xVec.add(lastBot - maxX);

        double lastLeft = minY, lastRight = minY;

        for (int i = 1; i < n - 1; i++) {
            double y = yPool.get(i);

            if (RANDOM.nextBoolean()) {
                yVec.add(y - lastLeft);
                lastLeft = y;
            } else {
                yVec.add(lastRight - y);
                lastRight = y;
            }
        }

        yVec.add(maxY - lastLeft);
        yVec.add(lastRight - maxY);

        Collections.shuffle(yVec);

        List<Vector2> vectors = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            vectors.add(new Vector2(xVec.get(i).floatValue(), yVec.get(i).floatValue()));
        }

        vectors.sort(Comparator.comparingDouble(v -> Math.atan2(v.y, v.x)));

        float x = 0, y = 0;
        float minPolygonX = 0;
        float minPolygonY = 0;
        List<Vector2> points = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            points.add(new Vector2(x, y));

            x += vectors.get(i).x;
            y += vectors.get(i).y;

            minPolygonX = Math.min(minPolygonX, x);
            minPolygonY = Math.min(minPolygonY, y);
        }

        float xShift = (float) (minX - minPolygonX);
        float yShift = (float) (minY - minPolygonY);

        for (int i = 0; i < n; i++) {
            Vector2 p = points.get(i);
            points.set(i, new Vector2(p.x + xShift, p.y + yShift));
        }

        return points;
    }

    public static Vector2 getGeometricMassCenter(Polygon polygon) {
        float x = 0;
        float y = 0;
        float[] floats = polygon.getVertices();

        for (int i = 0; i < floats.length; i += 2) {
            x += floats[i];
            y += floats[i + 1];
        }
        x /= (floats.length / 2);
        y /= (floats.length / 2);

        return new Vector2(x, y);
    }

    public static void transfer(Polygon polygon, Vector2 previousPosition, Vector2 currentPosition) {
        float[] previousCoordinates = polygon.getVertices();
        float[] floats = new float[previousCoordinates.length];
        for (int i = 0; i < previousCoordinates.length; i += 2) {
            floats[i] = (float) (previousCoordinates[i] + (currentPosition.x - previousPosition.x));
            floats[i + 1] = (float) (previousCoordinates[i + 1] + (currentPosition.y - previousPosition.y));
        }
        polygon.setVertices(floats);
    }

    public static void rotate(Polygon polygon, Vector2 currentPosition, float degree) {
        polygon.setOrigin(currentPosition.x, currentPosition.y);
        polygon.rotate(degree / 30f);
    }

    public static void scale(Polygon polygon, int scaleSize) {
        polygon.setScale(scaleSize, scaleSize);
    }
}