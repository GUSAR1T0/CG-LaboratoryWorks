package store.vxdesign.cg.core.cycloid.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import store.vxdesign.cg.core.cycloid.actors.CoordinateGridActor;
import store.vxdesign.cg.core.cycloid.actors.CycloidActor;
import store.vxdesign.cg.core.cycloid.actors.SettingsTable;
import store.vxdesign.cg.core.cycloid.utilities.handlers.SkinHandler;

import java.util.List;

public class CycloidScreen implements Screen {
    private final SkinHandler skinHandler;

    private Camera camera;
    private Stage stage;
    private CoordinateGridActor grid;
    private List<Label> coordinateValues;
    private CycloidActor cycloid;
    private SettingsTable settings;

    public CycloidScreen(Camera camera) {
        skinHandler = new SkinHandler();
        this.camera = camera;
        this.stage = new Stage();
        this.show();
    }

    @Override
    public void show() {
        grid = new CoordinateGridActor(camera, skinHandler);
        cycloid = new CycloidActor(camera);
        coordinateValues = grid.getCoordinateLabels();
        settings = new SettingsTable(camera, skinHandler);

        stage.addActor(grid);
        coordinateValues.forEach(stage::addActor);
        coordinateValues.forEach(value -> value.setVisible(false));
        stage.addActor(cycloid);
        stage.addActor(settings);

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

        settings.getAnglesInfo().setText(Integer.toString((int) settings.getAnglesSlider().getValue()));
        settings.getMaxLengthInfo().setText(Float.toString(settings.getMaxLengthSlider().getValue()));
        settings.getRadiusInfo().setText(Float.toString(settings.getRadiusSlider().getValue()));
        settings.getOmegaInfo().setText(Float.toString(settings.getOmegaSlider().getValue()));

        cycloid.setAngles((int) settings.getAnglesSlider().getValue());
        cycloid.setMaxLength(settings.getMaxLengthSlider().getValue());
        cycloid.setRadius(settings.getRadiusSlider().getValue());
        cycloid.setOmega(settings.getOmegaSlider().getValue());

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
        settings.dispose();
        stage.dispose();
        skinHandler.dispose();
    }
}
