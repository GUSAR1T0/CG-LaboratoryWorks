package store.vxdesign.cg.core.bezier.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import store.vxdesign.cg.core.bezier.actors.BezierLinesActor;
import store.vxdesign.cg.core.bezier.actors.CoordinateGridActor;
import store.vxdesign.cg.core.bezier.actors.SettingsTable;
import store.vxdesign.cg.core.bezier.actors.StatusTable;
import store.vxdesign.cg.core.bezier.utilities.SkinHandler;

import java.util.List;

public class BezierScreen implements Screen {
    private final SkinHandler skinHandler;

    private Camera camera;
    private Stage stage;
    private CoordinateGridActor grid;
    private List<Label> coordinateValues;
    private SettingsTable settings;
    private StatusTable status;
    private BezierLinesActor bezier;

    public static boolean flag = true;

    public BezierScreen(Camera camera) {
        this.skinHandler = new SkinHandler();
        this.camera = camera;
        this.stage = new Stage();
        this.show();
    }

    @Override
    public void show() {
        grid = new CoordinateGridActor(camera, skinHandler);
        settings = new SettingsTable(camera, skinHandler);
        status = new StatusTable(camera, skinHandler);
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            if (coordinateValues.get(0).isVisible()) {
                coordinateValues.forEach(value -> value.setVisible(false));
            } else {
                coordinateValues.forEach(value -> value.setVisible(true));
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
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
        skinHandler.dispose();
    }
}
