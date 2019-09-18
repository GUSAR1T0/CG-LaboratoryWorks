package store.vxdesign.cg.core.cut_polygon.utilities.handlers;

import store.vxdesign.cg.core.cut_polygon.utilities.Shape;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static Polygon generateConcavePolygon(int n, float maxLength, float irregularity, float spikeyness) {
        float[] floats = new float[n * 2];
        List<Vector2> vectors = generateRandomConcavePolygon(0, 0, n, maxLength, irregularity, spikeyness);
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

    private static List<Vector2> generateRandomConcavePolygon(float ctrX, float ctrY, int angles, float aveRadius, float irregularity, float spikeyness) {
        irregularity = (float) (clip(irregularity, 0, 1) * 2 * Math.PI / angles);
        spikeyness = clip(spikeyness, 0, 1) * aveRadius;

        List<Float> angleSteps = new ArrayList<>();
        float lower = (float) ((2 * Math.PI / angles) - irregularity);
        float upper = (float) ((2 * Math.PI / angles) + irregularity);
        float sum = 0;
        for (int i = 0; i < angles; i++) {
            float tmp = lower != upper ? (float) RANDOM.doubles(1, lower, upper).sum() : lower;
            angleSteps.add(tmp);
            sum = sum + tmp;
        }

        float k = (float) (sum / (2 * Math.PI));
        for (int i = 0; i < angles; i++) {
            angleSteps.set(i, angleSteps.get(i) / k);
        }

        List<Vector2> points = new ArrayList<>();
        float angle = (float) RANDOM.doubles(1, 0, 2 * Math.PI).sum();
        for (int i = 0; i < angles; i++) {
            float r_i = clip((float) RANDOM.nextGaussian() * spikeyness + aveRadius, 0, 2 * aveRadius);
            float x = (float) (ctrX + r_i * Math.cos(angle));
            float y = (float) (ctrY + r_i * Math.sin(angle));
            points.add(new Vector2(x, y));
            angle += angleSteps.get(i);
        }

        return points;
    }

    private static float clip(float x, float min, float max) {
        if (min > max) {
            return x;
        } else if (x < min) {
            return min;
        } else if (x > max) {
            return max;
        } else {
            return x;
        }
    }

    public static List<Shape> cutPolygon(List<Vector2> vertices, List<Shape> shapes) {
        List<Vector2> verticesBefore = new ArrayList<>(vertices);
        minPoly(vertices);

        boolean flag = vertices.size() == verticesBefore.size();
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).x != verticesBefore.get(i).x) {
                flag = false;
            } else if (vertices.get(i).y != verticesBefore.get(i).y) {
                flag = false;
            }
        }

        int n = vertices.size() - 1;

        if (conv2(vertices) == 1) {
            float[] result = new float[2 * vertices.size()];
            for (int i = 0; i < 2 * vertices.size(); i += 2) {
                result[i] = vertices.get(i / 2).x;
                result[i + 1] = vertices.get(i / 2).y;
            }
            shapes.add(new Shape(result));
            return shapes;
        }

        float d = dirTest(vertices);
        int k = 0;

        while (d * nf2(vertices.get(0), vertices.get(1), vertices.get(2)) < 0) {
            Vector2 point = vertices.remove(0);
            vertices.remove(vertices.size() - 1);
            vertices.add(point);
            vertices.add(vertices.get(0));
        }

        Map<String, Float> map = new HashMap<>();
        float theta = 0;
        Vector2 q = new Vector2();

        for (int i = 2; i < n - 1; i++) {
            float c = crossSeg(vertices.get(0), vertices.get(1), vertices.get(i), vertices.get(i + 1), map);
            if (c < 0) {
                continue;
            }
            if (!(map.get("t") > 1 && (map.get("tau") >= 0 && map.get("tau") <= 1))) {
                continue;
            }
            if (k == 0) {
                theta = map.get("t");
                k = i;
                q.set(map.get("qx"), map.get("qy"));
                continue;
            }
            if (theta <= map.get("t")) {
                continue;
            }
            theta = map.get("t");
            k = i;
            q.set(map.get("qx"), map.get("qy"));
        }

        if (k != 0) {
            List<Vector2> polygon1 = new ArrayList<>();
            polygon1.add(new Vector2(vertices.get(0)));
            polygon1.add(new Vector2(q));
            for (int i = k + 1; i < n; i++) {
                polygon1.add(new Vector2(vertices.get(i)));
            }
            polygon1.add(new Vector2(vertices.get(0)));

            List<Vector2> polygon2 = new ArrayList<>();
            polygon2.add(new Vector2(vertices.get(1)));
            for (int i = 2; i < k + 1; i++) {
                polygon2.add(new Vector2(vertices.get(i)));
            }
            polygon2.add(new Vector2(q));
            polygon2.add(new Vector2(vertices.get(1)));

            cutPolygon(polygon1, shapes);
            cutPolygon(polygon2, shapes);
        } else {
            float[] result = new float[2 * vertices.size()];
            for (int i = 0; i < 2 * vertices.size(); i += 2) {
                result[i] = vertices.get(i / 2).x;
                result[i + 1] = vertices.get(i / 2).y;
            }
            shapes.add(new Shape(result));
        }

        return shapes;
    }

    private static void minPoly(List<Vector2> vertices) {
        int m = 0;
        for (int i = 1; i < vertices.size(); i++) {
            Vector2 V = new Vector2(vertices.get(i).x - vertices.get(m).x, vertices.get(i).y - vertices.get(m).y);
            if (getScalarProductResult(V, V) == 0) {
                continue;
            }
            if (m == 0) {
                m++;
                vertices.get(m).set(vertices.get(i));
                continue;
            }
            if (nf2(vertices.get(m - 1), vertices.get(m), vertices.get(i)) != 0) {
                m++;
                vertices.get(m).set(vertices.get(i));
                continue;
            }
            Vector2 W = new Vector2(vertices.get(m).x - vertices.get(m - 1).x, vertices.get(m).y - vertices.get(m - 1).y);
            if (getScalarProductResult(V, W) > 0) {
                vertices.get(m).set(vertices.get(i));
                continue;
            }
            Vector2 N = new Vector2(vertices.get(i).x - vertices.get(m - 1).x, vertices.get(i).y - vertices.get(m - 1).y);
            if (getScalarProductResult(N, W) > 0) {
                continue;
            }
            vertices.get(m - 1).set(vertices.get(m));
            vertices.get(m).set(vertices.get(i));
        }
        if (nf2(vertices.get(m - 1), vertices.get(0), vertices.get(1)) == 0) {
            List<Vector2> copy = new ArrayList<>(vertices);
            vertices.clear();
            vertices.add(copy.get(1));
            for (int i = 2; i < m; i++) {
                vertices.add(copy.get(i));
            }
            vertices.add(copy.get(1));
        } else {
            List<Vector2> copy = new ArrayList<>(vertices);
            vertices.clear();
            for (int i = 0; i < m + 1; i++) {
                vertices.add(copy.get(i));
            }
        }
    }

    private static float nf2(Vector2 a, Vector2 b, Vector2 p) {
        Vector2 M1 = new Vector2(p.x - a.x, p.y - a.y);
        Vector2 M2 = new Vector2(b.x - a.x, b.y - a.y);
        Vector2 rightRotResult = rightRot(M2);
        return getScalarProductResult(M1, rightRotResult);
    }

    private static Vector2 rightRot(Vector2 a) {
        return new Vector2(a.y, -a.x);
    }

    private static float conv2(List<Vector2> vertices) {
        int n = vertices.size() - 1;
        float s = nf2(vertices.get(n), vertices.get(1), vertices.get(2));
        for (int i = 1; i < n; i++) {
            float f = nf2(vertices.get(i - 1), vertices.get(i), vertices.get(i + 1));
            if (s * f < 0) return -1;
            if (s * f > 0) continue;
            if (s * f == 0) return 0;
        }
        return 1;
    }

    private static float dirTest(List<Vector2> vertices) {
        float sum = 0;
        for (int i = 1; i < vertices.size() - 3; i++) {
            sum += nf2(vertices.get(0), vertices.get(i + 1), vertices.get(i));
        }
        return Math.signum(sum);
    }

    private static float crossSeg(Vector2 a, Vector2 b, Vector2 c, Vector2 d, Map<String, Float> map) {
        float fa = nf2(c, d, a);
        float fb = nf2(c, d, b);
        float fc = nf2(a, b, c);
        float fd = nf2(a, b, d);

        if (fa == fb) {
            return -1;
        }

        map.put("t", fa / (fa - fb));
        map.put("tau", fc / (fc - fd));
        map.put("qx", a.x + (b.x - a.x) * map.get("t"));
        map.put("qy", a.y + (b.y - a.y) * map.get("t"));
        return (map.get("t") >= 0 && map.get("t") <= 1) && (map.get("tau") >= 0 && map.get("tau") <= 1) ? 1 : 0;
    }

    private static float getScalarProductResult(Vector2 a, Vector2 b) {
        return a.x * b.x + a.y * b.y;
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