package store.vxdesign.bezier.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import store.vxdesign.bezier.actors.BezierLinesActor;
import store.vxdesign.bezier.actors.CoordinateGridActor;
import store.vxdesign.bezier.actors.SettingsTable;
import store.vxdesign.bezier.actors.StatusTable;

import java.util.List;

public class MainScreen implements Screen {
    private Camera camera;
    private Stage stage;
    private CoordinateGridActor grid;
    private List<Label> coordinateValues;
    private SettingsTable settings;
    private StatusTable status;
    private BezierLinesActor bezier;

    private float timer = 0;
    public static boolean flag = true;

    public MainScreen(Camera camera, Stage stage) {
        this.camera = camera;
        this.stage = stage;
        this.show();
    }

    @Override
    public void show() {
        grid = new CoordinateGridActor(camera);
        settings = new SettingsTable(camera);
        status = new StatusTable(camera);
        bezier = new BezierLinesActor(camera, settings);

        stage.addActor(grid);
        coordinateValues = grid.getCoordinateLabels();
        coordinateValues.forEach(stage::addActor);
        stage.addActor(settings);
        stage.addActor(status);
        stage.addActor(bezier);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (timer >= 0.5f) {
            timer = 0;
        } else if (timer > 0) {
            timer += Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.K) && (timer >= 0.5f || timer == 0)) {
            if (coordinateValues.get(0).isVisible()) {
                coordinateValues.forEach(value -> value.setVisible(false));
            } else {
                coordinateValues.forEach(value -> value.setVisible(true));
            }
            timer = delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            flag = !flag;
        }

        settings.getWeight0Info().setText(Integer.toString((int) settings.getWeight0Slider().getValue()));
        settings.getWeight1Info().setText(Integer.toString((int) settings.getWeight1Slider().getValue()));
        settings.getWeight2Info().setText(Integer.toString((int) settings.getWeight2Slider().getValue()));
        settings.getStepInfo().setText(Float.toString(settings.getStepSlider().getValue()));

        Vector3 vector3 = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        status.getPositionMouseX().setText(Integer.toString((int) vector3.x));
        status.getPositionMouseY().setText(Integer.toString((int) -vector3.y));

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        grid.dispose();
        bezier.dispose();
        settings.dispose();
        status.dispose();
        stage.dispose();
    }
}
