package store.vxdesign.cg.core.move_polygon.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import store.vxdesign.cg.core.move_polygon.utilities.Coordinates2D;
import store.vxdesign.cg.core.move_polygon.utilities.PolygonHandler;

public class PolygonActor extends AbstractShapeActor {
    private SettingsTable settings;

    private Polygon polygon;
    private Coordinates2D diagonalsIntersectionCoordinates;
    private Coordinates2D previousPosition, currentCursorPosition, currentPosition;

    private float timer = 0;

    public PolygonActor(Camera camera, SettingsTable settings) {
        super(camera);
        this.settings = settings;
        this.shapeRenderer.setProjectionMatrix(this.camera.combined);
        generatePolygon();
    }

    public void generatePolygon() {
        polygon = PolygonHandler.generateConvexPolygon((int) settings.getAnglesSlider().getValue(), (int) settings.getMaxLengthSlider().getValue());
        diagonalsIntersectionCoordinates = PolygonHandler.getGeometricMassCenter(polygon);
        previousPosition = new Coordinates2D();
        currentCursorPosition = new Coordinates2D();
        currentPosition = new Coordinates2D();
        scalePolygon();
    }

    public void scalePolygon() {
        PolygonHandler.scale(polygon, (int) settings.getScaleSlider().getValue());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        if ("MOUSE".equals(settings.getTypeBox().getSelected())) {
            Vector3 vector3 = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            currentCursorPosition.setCoordinates(vector3.x, vector3.y);
            draw();
        } else if (TrajectoryActor.isSpacePressed) {
            float x = (float) currentCursorPosition.getX();
            float y = (float) currentCursorPosition.getY();
            if (timer >= 0.01f) {
                x = TrajectoryActor.getCoordinates().remove(0);
                y = TrajectoryActor.getCoordinates().remove(0);
                timer = 0;
            } else {
                timer += Gdx.graphics.getDeltaTime();
            }
            currentCursorPosition.setCoordinates(x, y);
            draw();
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            draw();
        }

        batch.begin();
    }

    private void draw() {
        currentPosition.setCoordinates(currentCursorPosition.getX() - diagonalsIntersectionCoordinates.getX(),
                currentCursorPosition.getY() - diagonalsIntersectionCoordinates.getY());

        PolygonHandler.transfer(polygon, previousPosition, currentPosition);
        PolygonHandler.rotate(polygon, currentCursorPosition, (int) settings.getDegreeSlider().getValue());
        previousPosition.setCoordinates(currentPosition);

        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.polygon(polygon.getTransformedVertices());
        shapeRenderer.circle((float) currentCursorPosition.getX(), (float) currentCursorPosition.getY(), 4.5f);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public Coordinates2D getCurrentCursorPosition() {
        return currentCursorPosition;
    }
}
