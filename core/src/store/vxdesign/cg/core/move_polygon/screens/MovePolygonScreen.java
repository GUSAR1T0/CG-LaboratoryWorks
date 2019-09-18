package store.vxdesign.cg.core.move_polygon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import store.vxdesign.cg.core.move_polygon.actors.CoordinateGridActor;
import store.vxdesign.cg.core.move_polygon.actors.PolygonActor;
import store.vxdesign.cg.core.move_polygon.actors.SettingsTable;
import store.vxdesign.cg.core.move_polygon.actors.StatusTable;
import store.vxdesign.cg.core.move_polygon.actors.TrajectoryActor;
import store.vxdesign.cg.core.move_polygon.utilities.SkinHandler;
import store.vxdesign.cg.core.move_polygon.actors.*;

import java.util.List;
import java.util.stream.Collectors;

public class MovePolygonScreen implements Screen {
    private final SkinHandler skinHandler;

    private Camera camera;
    private Stage stage;
    private CoordinateGridActor grid;
    private List<Label> coordinateValues;
    private SettingsTable settings;
    private PolygonActor polygon;
    private TrajectoryActor trajectory;
    private StatusTable status;

    public MovePolygonScreen(Camera camera) {
        skinHandler = new SkinHandler();
        this.camera = camera;
        this.stage = new Stage();
        this.show();
    }

    @Override
    public void show() {
        grid = new CoordinateGridActor(camera, skinHandler);
        settings = new SettingsTable(camera, skinHandler);
        polygon = new PolygonActor(camera, settings);
        trajectory = new TrajectoryActor(camera, settings.getTypeBox());
        status = new StatusTable(camera, skinHandler);

        EventListener polygonGenerationListener = event -> {
            if (event.isHandled()) {
                polygon.generatePolygon();
            }
            return true;
        };
        settings.getAnglesSlider().addListener(polygonGenerationListener);
        settings.getMaxLengthSlider().addListener(polygonGenerationListener);
        settings.getScaleSlider().addListener(event -> {
            if (event.isHandled()) {
                polygon.scalePolygon();
            }
            return true;
        });

        stage.addActor(grid);
        coordinateValues = grid.getCoordinateLabels();
        coordinateValues.forEach(stage::addActor);
        stage.addActor(polygon);
        stage.addActor(trajectory);
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

        settings.getAnglesInfo().setText(Integer.toString((int) settings.getAnglesSlider().getValue()));
        settings.getDegreeInfo().setText(Integer.toString((int) settings.getDegreeSlider().getValue()));
        settings.getMaxLengthInfo().setText(Integer.toString((int) settings.getMaxLengthSlider().getValue()));
        settings.getScaleInfo().setText(Integer.toString((int) settings.getScaleSlider().getValue()));

        status.getPositionPolygonX().setText(Integer.toString((int) polygon.getCurrentCursorPosition().getX()));
        status.getPositionPolygonY().setText(Integer.toString((int) -polygon.getCurrentCursorPosition().getY()));
        Vector3 vector3 = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        status.getPositionMouseX().setText(Integer.toString((int) vector3.x));
        status.getPositionMouseY().setText(Integer.toString((int) -vector3.y));

        if ("CUSTOM".equals(settings.getTypeBox().getSelected())) {
            if (Gdx.input.isTouched() && !settings.isBlock()) {
                trajectory.addCoordinates(vector3.x, vector3.y);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                TrajectoryActor.getCoordinates().clear();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && TrajectoryActor.getCoordinates().size() >= 2) {
                TrajectoryActor.setCoordinates(TrajectoryActor.getCoordinates().stream()
                        .limit(TrajectoryActor.getCoordinates().size() - 2).collect(Collectors.toList()));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !TrajectoryActor.isSpacePressed &&
                    TrajectoryActor.getCoordinates().size() >= 2) {
                TrajectoryActor.isSpacePressed = true;
            }
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
        polygon.dispose();
        settings.dispose();
        status.dispose();
        trajectory.dispose();
        stage.dispose();
        skinHandler.dispose();
    }
}
