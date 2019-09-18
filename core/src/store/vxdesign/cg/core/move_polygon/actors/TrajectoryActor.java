package store.vxdesign.cg.core.move_polygon.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;

import java.util.ArrayList;
import java.util.List;

public class TrajectoryActor extends AbstractShapeActor {
    private static List<Float> coordinates = new ArrayList<>();
    public static boolean isSpacePressed = false;

    private SelectBox<String> types;

    public TrajectoryActor(Camera camera, SelectBox<String> types) {
        super(camera);
        this.types = types;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (isSpacePressed) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.BLUE);
            for (int i = 2; i < coordinates.size(); i += 2) {
                shapeRenderer.line(coordinates.get(i - 2), coordinates.get(i - 1), coordinates.get(i), coordinates.get(i + 1));
            }
            shapeRenderer.end();

            isSpacePressed = false;
        } else if ("CUSTOM".equals(types.getSelected())) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLUE);
            for (int i = 0; i < coordinates.size(); i += 2) {
                shapeRenderer.circle(coordinates.get(i), coordinates.get(i + 1), 4.5f);
            }
            shapeRenderer.end();
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
    }

    public void addCoordinates(float x, float y) {
        coordinates.add(x);
        coordinates.add(y);
    }

    public static List<Float> getCoordinates() {
        return coordinates;
    }

    public static void setCoordinates(List<Float> coordinates) {
        TrajectoryActor.coordinates = coordinates;
    }
}
