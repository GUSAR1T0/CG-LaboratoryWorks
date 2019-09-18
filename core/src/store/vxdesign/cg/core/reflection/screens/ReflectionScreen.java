package store.vxdesign.cg.core.reflection.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import store.vxdesign.cg.core.reflection.actors.CoordinateGridActor;
import store.vxdesign.cg.core.reflection.actors.ReflectionActor;
import store.vxdesign.cg.core.reflection.actors.SettingsTable;
import store.vxdesign.cg.core.reflection.actors.StatusTable;
import store.vxdesign.cg.core.reflection.utilities.SkinHandler;

import java.util.List;

public class ReflectionScreen implements Screen {
    private final SkinHandler skinHandler;

    private Camera camera;
    private Stage stage;
    private CoordinateGridActor grid;
    private List<Label> coordinateValues;
    private SettingsTable settings;
    private ReflectionActor reflection;
    private StatusTable status;

    public ReflectionScreen(Camera camera) {
        this.skinHandler = new SkinHandler();
        this.camera = camera;
        this.stage = new Stage();
        this.show();
    }

    @Override
    public void show() {
        grid = new CoordinateGridActor(camera, skinHandler);
        settings = new SettingsTable(camera, skinHandler);
        reflection = new ReflectionActor(camera, settings);
        status = new StatusTable(camera, skinHandler);

        stage.addActor(grid);
        coordinateValues = grid.getCoordinateLabels();
        coordinateValues.forEach(stage::addActor);
        stage.addActor(reflection);
        stage.addActor(settings);
        stage.addActor(status);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            if (coordinateValues.get(0).isVisible()) {
                coordinateValues.forEach(value -> value.setVisible(false));
            } else {
                coordinateValues.forEach(value -> value.setVisible(true));
            }
        }

        settings.getOriginalCircleRadiusInfo().setText(Integer.toString((int) settings.getOriginalCircleRadiusSlider().getValue()));
        settings.getLineAngleInfo().setText(Integer.toString((int) settings.getLineAngleSlider().getValue()));
        settings.getLineOffsetXInfo().setText(Integer.toString((int) settings.getLineOffsetXSlider().getValue()));
        settings.getLineOffsetYInfo().setText(Integer.toString((int) settings.getLineOffsetYSlider().getValue()));

        Vector3 vector3 = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        status.getPositionMouseX().setText(Integer.toString((int) vector3.x));
        status.getPositionMouseY().setText(Integer.toString((int) -vector3.y));

        if (reflection.getVectorReflection() != null) {
            Vector3 vectorReflection = reflection.getVectorReflection();
            status.getPositionReflectionX().setText(Integer.toString((int) (vectorReflection.x)));
            status.getPositionReflectionY().setText(Integer.toString((int) (-vectorReflection.y)));
        }

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
        reflection.dispose();
        settings.dispose();
        status.dispose();
        stage.dispose();
        skinHandler.dispose();
    }
}
