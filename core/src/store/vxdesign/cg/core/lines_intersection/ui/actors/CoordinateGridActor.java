package store.vxdesign.cg.core.lines_intersection.ui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CoordinateGridActor extends AbstractShapeActor {
    private final static int COUNT = 20;

    private static class CoordinateLine {
        final Vector2 point1;
        final Vector2 point2;

        private CoordinateLine(float x1, float y1, float x2, float y2) {
            this.point1 = new Vector2(x1, y1);
            this.point2 = new Vector2(x2, y2);
        }
    }

    public enum CoordinateKey {
        X, X_L, Y, Y_L
    }

    private List<CoordinateLine> linesX;
    private CoordinateLine coordinateLineX;
    private Polyline triangleX;
    private List<CoordinateLine> linesY;
    private CoordinateLine coordinateLineY;
    private Polyline triangleY;

    public CoordinateGridActor(Camera camera) {
        super(camera);
        create();
    }

    private void create() {
        linesX = new ArrayList<>();
        linesY = new ArrayList<>();

        float minX, maxX;
        float minY, maxY;

        linesX.add(new CoordinateLine(0, -camera.viewportHeight, 0, camera.viewportHeight));
        for (float i = -getCellSize(); i > -camera.viewportWidth / 2f; i -= getCellSize()) {
            linesX.add(new CoordinateLine(i, -camera.viewportHeight, i, camera.viewportHeight));
        }
        minX = linesX.get(linesX.size() - 1).point1.x;
        for (float i = getCellSize(); i < camera.viewportWidth / 2f; i += getCellSize()) {
            linesX.add(new CoordinateLine(i, -camera.viewportHeight, i, camera.viewportHeight));
        }
        maxX = linesX.get(linesX.size() - 1).point1.x;

        linesY.add(new CoordinateLine(-camera.viewportWidth, 0, camera.viewportWidth, 0));
        for (float i = -getCellSize(); i > -camera.viewportHeight / 2f; i -= getCellSize()) {
            linesY.add(new CoordinateLine(-camera.viewportWidth, i, camera.viewportWidth, i));
        }
        minY = linesY.get(linesY.size() - 1).point1.y;
        for (float i = getCellSize(); i < camera.viewportHeight / 2f; i += getCellSize()) {
            linesY.add(new CoordinateLine(-camera.viewportWidth, i, camera.viewportWidth, i));
        }
        maxY = linesY.get(linesY.size() - 1).point1.y;

        coordinateLineX = new CoordinateLine(minX, minY, maxX, minY);
        coordinateLineY = new CoordinateLine(minX, minY, minX, maxY);

        triangleX = new Polyline(new float[]{coordinateLineX.point1.x - getTriangleOffset(), coordinateLineY.point2.y - getTriangleOffset(),
                coordinateLineX.point1.x + getTriangleOffset(), coordinateLineY.point2.y - getTriangleOffset(),
                coordinateLineX.point1.x, coordinateLineY.point2.y + getTriangleOffset()});

        triangleY = new Polyline(new float[]{coordinateLineX.point2.x - getTriangleOffset(), coordinateLineY.point1.y - getTriangleOffset(),
                coordinateLineX.point2.x - getTriangleOffset(), coordinateLineY.point1.y + getTriangleOffset(),
                coordinateLineX.point2.x + getTriangleOffset(), coordinateLineY.point1.y});
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.LIGHT_GRAY);
        linesX.forEach(line -> shapeRenderer.rectLine(line.point1, line.point2, getLineWidth()));
        linesY.forEach(line -> shapeRenderer.rectLine(line.point1, line.point2, getLineWidth()));

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.circle(coordinateLineX.point1.x, coordinateLineX.point1.y, getPointRadius());
        shapeRenderer.rectLine(coordinateLineX.point1, coordinateLineX.point2, getCoordinateLineWidth());
        shapeRenderer.rectLine(coordinateLineY.point1, coordinateLineY.point2, getCoordinateLineWidth());
        shapeRenderer.triangle(triangleX.getVertices()[0], triangleX.getVertices()[1],
                triangleX.getVertices()[2], triangleX.getVertices()[3],
                triangleX.getVertices()[4], triangleX.getVertices()[5]);
        shapeRenderer.triangle(triangleY.getVertices()[0], triangleY.getVertices()[1],
                triangleY.getVertices()[2], triangleY.getVertices()[3],
                triangleY.getVertices()[4], triangleY.getVertices()[5]);

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
    }

    public Map<CoordinateKey, List<Vector2>> getInitialPositions() {
        List<Float> positionsX = linesX.stream().map(line -> line.point1.x).sorted().collect(Collectors.toList());
        List<Float> positionsY = linesY.stream().map(line -> line.point1.y).sorted().collect(Collectors.toList());

        Map<CoordinateKey, List<Vector2>> map = new TreeMap<>();
        map.put(CoordinateKey.X, positionsX.stream().map(x -> new Vector2(x, positionsY.get(0))).collect(Collectors.toList()));
        map.put(CoordinateKey.Y, positionsY.stream().map(y -> new Vector2(positionsX.get(0), y)).collect(Collectors.toList()));
        map.put(CoordinateKey.X_L, Collections.singletonList(new Vector2(positionsX.get(positionsX.size() - 1), positionsY.get(0))));
        map.put(CoordinateKey.Y_L, Collections.singletonList(new Vector2(positionsX.get(0), positionsY.get(positionsY.size() - 1))));
        return map;
    }

    public float getCellSize() {
        return getCellSize(camera.viewportWidth);
    }

    public static float getCellSize(float length) {
        return length / COUNT;
    }

    private int getPointRadius() {
        return (int) (getCellSize() / 30);
    }

    private int getLineWidth() {
        return (int) (getCellSize() / 40);
    }

    private int getCoordinateLineWidth() {
        return (int) (getCellSize() / 20);
    }

    private float getTriangleOffset(){
        return getCellSize() / 10f;
    }
}
