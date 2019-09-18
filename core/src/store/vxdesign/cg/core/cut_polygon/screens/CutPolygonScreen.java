package store.vxdesign.cg.core.cut_polygon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import store.vxdesign.cg.core.cut_polygon.actors.PolygonActor;
import store.vxdesign.cg.core.cut_polygon.actors.CoordinateGridActor;
import store.vxdesign.cg.core.cut_polygon.actors.SettingsTable;
import store.vxdesign.cg.core.cut_polygon.utilities.handlers.SkinHandler;

import java.util.List;

public class CutPolygonScreen implements Screen {
    private final SkinHandler skinHandler;

    private Camera camera;
    private Stage stage;
    private CoordinateGridActor grid;
    private List<Label> coordinateValues;
    private PolygonActor polygon;
    private SettingsTable settings;

    public CutPolygonScreen(Camera camera) {
        skinHandler = new SkinHandler();
        this.camera = camera;
        this.stage = new Stage();
        this.show();
    }

    @Override
    public void show() {
        grid = new CoordinateGridActor(camera, skinHandler);
        coordinateValues = grid.getCoordinateLabels();
        polygon = new PolygonActor(camera);
        settings = new SettingsTable(camera, skinHandler);

        stage.addActor(grid);
        coordinateValues.forEach(stage::addActor);
        coordinateValues.forEach(value -> value.setVisible(false));
        stage.addActor(polygon);
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
        settings.getIrregularityInfo().setText(Float.toString(settings.getIrregularitySlider().getValue()));
        settings.getSpikeynessInfo().setText(Float.toString(settings.getSpikeynessSlider().getValue()));

        polygon.setAngles((int) settings.getAnglesSlider().getValue());
        polygon.setMaxLength(settings.getMaxLengthSlider().getValue());
        polygon.setIrregularity(settings.getIrregularitySlider().getValue());
        polygon.setSpikeyness(settings.getSpikeynessSlider().getValue());

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
