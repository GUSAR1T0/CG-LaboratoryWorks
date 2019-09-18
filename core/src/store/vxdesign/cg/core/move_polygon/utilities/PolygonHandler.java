package store.vxdesign.cg.core.move_polygon.utilities;

import com.badlogic.gdx.math.Polygon;

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

    public static Polygon generateConvexPolygon(int n, int maxLength) {
        float[] floats = new float[n * 2];
        List<Coordinates2D> coordinates2Ds = generateRandomConvexPolygon(n, maxLength);
        for (int i = 0; i < coordinates2Ds.size() * 2; i += 2) {
            floats[i] = (float) coordinates2Ds.get(i / 2).getX();
            floats[i + 1] = (float) coordinates2Ds.get(i / 2).getY();
        }

        Polygon polygon = new Polygon(floats);
        polygon.setRotation(0);
        return polygon;
    }

    private static List<Coordinates2D> generateRandomConvexPolygon(int n, int maxLength) {
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

        List<Coordinates2D> vectors = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            vectors.add(new Coordinates2D(xVec.get(i), yVec.get(i)));
        }

        vectors.sort(Comparator.comparingDouble(v -> Math.atan2(v.getY(), v.getX())));

        double x = 0, y = 0;
        double minPolygonX = 0;
        double minPolygonY = 0;
        List<Coordinates2D> points = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            points.add(new Coordinates2D(x, y));

            x += vectors.get(i).getX();
            y += vectors.get(i).getY();

            minPolygonX = Math.min(minPolygonX, x);
            minPolygonY = Math.min(minPolygonY, y);
        }

        double xShift = minX - minPolygonX;
        double yShift = minY - minPolygonY;

        for (int i = 0; i < n; i++) {
            Coordinates2D p = points.get(i);
            points.set(i, new Coordinates2D(p.getX() + xShift, p.getY() + yShift));
        }

        return points;
    }

    public static Coordinates2D getGeometricMassCenter(Polygon polygon) {
        float x = 0;
        float y = 0;
        float[] floats = polygon.getVertices();

        for (int i = 0; i < floats.length; i += 2) {
            x += floats[i];
            y += floats[i + 1];
        }
        x /= (floats.length / 2);
        y /= (floats.length / 2);

        return new Coordinates2D(x, y);
    }

    public static void transfer(Polygon polygon, Coordinates2D previousPosition, Coordinates2D currentPosition) {
        float[] previousCoordinates = polygon.getVertices();
        float[] floats = new float[previousCoordinates.length];
        for (int i = 0; i < previousCoordinates.length; i += 2) {
            floats[i] = (float) (previousCoordinates[i] + (currentPosition.getX() - previousPosition.getX()));
            floats[i + 1] = (float) (previousCoordinates[i + 1] + (currentPosition.getY() - previousPosition.getY()));
        }
        polygon.setVertices(floats);
    }

    public static void rotate(Polygon polygon, Coordinates2D currentPosition, float degree) {
        polygon.setOrigin((float) currentPosition.getX(), (float) currentPosition.getY());
        polygon.rotate(degree / 30f);
    }

    public static void scale(Polygon polygon, int scaleSize) {
        polygon.setScale(scaleSize, scaleSize);
    }
}
