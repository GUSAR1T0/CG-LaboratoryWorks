package store.vxdesign.cg.core.bezier.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import store.vxdesign.cg.core.bezier.screens.BezierScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BezierLinesActor extends AbstractShapeActor {
    private SettingsTable settings;

    private List<Float> coordinates = new ArrayList<>();

    public BezierLinesActor(Camera camera, SettingsTable settings) {
        super(camera);
        this.settings = settings;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float x = Gdx.input.getX();
        float y = Gdx.input.getY();
        Vector3 vector3 = camera.unproject(new Vector3(x, y, 0));
        if (Gdx.input.isTouched() && !settings.isBlock()) {
            addCoordinates(vector3.x, vector3.y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.C)) {
            coordinates.clear();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && coordinates.size() >= 2) {
            coordinates = coordinates.stream().limit(coordinates.size() - 2).collect(Collectors.toList());
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        for (int i = 0; i < coordinates.size(); i += 2) {
            shapeRenderer.circle(coordinates.get(i), coordinates.get(i + 1), 4.5f);
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        for (int i = 2; i < coordinates.size(); i += 2) {
            shapeRenderer.line(coordinates.get(i - 2), coordinates.get(i - 1), coordinates.get(i), coordinates.get(i + 1));
        }

        shapeRenderer.setColor(Color.DARK_GRAY);
        List<Float> bezierCoordinates = getBezierCoordinates();
        for (int i = 2; i < bezierCoordinates.size(); i += 2) {
            double length = Math.pow(bezierCoordinates.get(i - 2) - bezierCoordinates.get(i), 2) + Math.pow(bezierCoordinates.get(i - 1) - bezierCoordinates.get(i + 1), 2);
            if (length < camera.viewportWidth * camera.viewportWidth) {
                shapeRenderer.rectLine(bezierCoordinates.get(i - 2), bezierCoordinates.get(i - 1), bezierCoordinates.get(i), bezierCoordinates.get(i + 1), 2);
            }
        }
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
    }

    private void addCoordinates(float x, float y) {
        if (!coordinates.isEmpty()) {
            Vector3 vector = new Vector3(coordinates.get(coordinates.size() - 2), coordinates.get(coordinates.size() - 1), 0);
            if (!(vector.x - 50 > x || vector.x + 50 < x) && !(-vector.y - 50 > -y || -vector.y + 50 < -y)) {
                return;
            }
        }
        coordinates.add(x);
        coordinates.add(y);
    }

    private List<Float> getBezierCoordinates() {
        List<Float> bezierCoordinates = new ArrayList<>();
        if (BezierScreen.flag) {
            if (coordinates.size() - 4 > 0) {
                for (int i = 4; i < coordinates.size(); i += 4) {
                    for (float t = 0; t <= 1; t += settings.getStepSlider().getValue()) {
                        float rx = (float) ((Math.pow(1 - t, 2) * settings.getWeight0Slider().getValue() * coordinates.get(i - 4) + 2 * t * (1 - t) * settings.getWeight1Slider().getValue() * coordinates.get(i - 2) + Math.pow(t, 2) * settings.getWeight2Slider().getValue() * coordinates.get(i)) /
                                (Math.pow(1 - t, 2) * settings.getWeight0Slider().getValue() + 2 * t * (1 - t) * settings.getWeight1Slider().getValue() + Math.pow(t, 2) * settings.getWeight2Slider().getValue()));
                        float ry = (float) ((Math.pow(1 - t, 2) * settings.getWeight0Slider().getValue() * coordinates.get(i - 3) + 2 * t * (1 - t) * settings.getWeight1Slider().getValue() * coordinates.get(i - 1) + Math.pow(t, 2) * settings.getWeight2Slider().getValue() * coordinates.get(i + 1)) /
                                (Math.pow(1 - t, 2) * settings.getWeight0Slider().getValue() + 2 * t * (1 - t) * settings.getWeight1Slider().getValue() + Math.pow(t, 2) * settings.getWeight2Slider().getValue()));
                        bezierCoordinates.add(rx);
                        bezierCoordinates.add(ry);
                    }
                }
            }
        } else {
            if (coordinates.size() - 6 > 0) {
//                float detX = (coordinates.get(6) - coordinates.get(2)) / (coordinates.get(0) * coordinates.get(2) - (-coordinates.get(2) * coordinates.get(4)));
//                float detY = (coordinates.get(7) - coordinates.get(3)) / (coordinates.get(1) * coordinates.get(3) - (-coordinates.get(3) * coordinates.get(5)));
                float alpha = 1;
                float beta = 1;
                float omega = (float) ((alpha >= 0 ? 1 : -1) * (1 - alpha - beta) / (2 * Math.sqrt(alpha * beta)));
                for (float t = 0; t <= 1; t += settings.getStepSlider().getValue()) {
                    float rx = (float) ((Math.pow(1 - t, 2) * settings.getWeight0Slider().getValue() * coordinates.get(0) + 2 * t * (1 - t) * omega * coordinates.get(2) + Math.pow(t, 2) * settings.getWeight2Slider().getValue() * coordinates.get(4)) /
                            (Math.pow(1 - t, 2) * settings.getWeight0Slider().getValue() + 2 * t * (1 - t) * omega + Math.pow(t, 2) * settings.getWeight2Slider().getValue()));
                    float ry = (float) ((Math.pow(1 - t, 2) * settings.getWeight0Slider().getValue() * coordinates.get(1) + 2 * t * (1 - t) * omega * coordinates.get(3) + Math.pow(t, 2) * settings.getWeight2Slider().getValue() * coordinates.get(5)) /
                            (Math.pow(1 - t, 2) * settings.getWeight0Slider().getValue() + 2 * t * (1 - t) * omega + Math.pow(t, 2) * settings.getWeight2Slider().getValue()));
                    bezierCoordinates.add(rx);
                    bezierCoordinates.add(ry);
                }
            }
        }
        return bezierCoordinates;
    }
}
