package store.vxdesign.cg.core.lines_intersection.screens;

import store.vxdesign.cg.core.lines_intersection.ui.actors.SettingsTable;
import store.vxdesign.cg.core.lines_intersection.ui.actors.IntersectionTable;
import store.vxdesign.cg.core.lines_intersection.ui.actors.LinesViewActor;
import store.vxdesign.cg.core.lines_intersection.ui.stages.CoordinateSystemStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import store.vxdesign.cg.core.lines_intersection.utilities.SkinHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class LinesIntersectionScreen implements Screen {
    private final SkinHandler skinHandler;

    private CoordinateSystemStage coordinateSystemStage;
    private Stage stage;
    private SettingsTable settings;
    private IntersectionTable intersection;
    private LinesViewActor linesView;

    public LinesIntersectionScreen(Camera camera) {
        skinHandler = new SkinHandler();
        this.coordinateSystemStage = new CoordinateSystemStage(camera, skinHandler);
        this.stage = new Stage();
        this.settings = new SettingsTable(camera, skinHandler);
        this.intersection = new IntersectionTable(camera, skinHandler);
        this.linesView = new LinesViewActor(camera);
        this.show();
    }

    @Override
    public void show() {
        stage.addActor(linesView);
        stage.addActor(settings);
        stage.addActor(intersection);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            if (settings.isVisible() && intersection.isVisible()) {
                settings.setVisible(false);
                intersection.setVisible(false);
            } else {
                settings.setVisible(true);
                intersection.setVisible(true);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            settings.setLinePoint1(1);
            settings.setLinePoint1(2);
            settings.setLinePoint2(1);
            settings.setLinePoint2(2);
        }

        linesView.setCoefficient(coordinateSystemStage.getCoefficient());
        linesView.setCenterCoordinateSystem(coordinateSystemStage.getCenter());
        linesView.setLineOneType(settings.getFirstLineType());
        linesView.setLineTwoType(settings.getSecondLineType());
        linesView.setLineOne(settings.getFirstLinePoint1(), settings.getFirstLinePoint2());
        linesView.setLineTwo(settings.getSecondLinePoint1(), settings.getSecondLinePoint2());
        linesView.getPositionsOfPoints();

        Vector2 firstLinePoint1 = settings.getFirstLinePoint1();
        Vector2 firstLinePoint2 = settings.getFirstLinePoint2();
        Vector2 secondLinePoint1 = settings.getSecondLinePoint1();
        Vector2 secondLinePoint2 = settings.getSecondLinePoint2();
        float[] intersectionPoint = linesView.getIntersectionPoint();
        float minX = getMin(intersectionPoint, 0, firstLinePoint1.x, firstLinePoint2.x, secondLinePoint1.x, secondLinePoint2.x);
        float maxX = getMax(intersectionPoint, 0, firstLinePoint1.x, firstLinePoint2.x, secondLinePoint1.x, secondLinePoint2.x);
        float minY = getMin(intersectionPoint, 1, firstLinePoint1.y, firstLinePoint2.y, secondLinePoint1.y, secondLinePoint2.y);
        float maxY = getMax(intersectionPoint, 1, firstLinePoint1.y, firstLinePoint2.y, secondLinePoint1.y, secondLinePoint2.y);
//        float minX = getMin(firstLinePoint1.x, firstLinePoint2.x, secondLinePoint1.x, secondLinePoint2.x);
//        float maxX = getMax(firstLinePoint1.x, firstLinePoint2.x, secondLinePoint1.x, secondLinePoint2.x);
//        float minY = getMin(firstLinePoint1.y, firstLinePoint2.y, secondLinePoint1.y, secondLinePoint2.y);
//        float maxY = getMax(firstLinePoint1.y, firstLinePoint2.y, secondLinePoint1.y, secondLinePoint2.y);
        coordinateSystemStage.update(minX, maxX, minY, maxY);

        intersection.update(settings.getFirstLineType(), settings.getSecondLineType(), intersectionPoint);

        coordinateSystemStage.getViewport().apply();
        coordinateSystemStage.act(delta);
        coordinateSystemStage.draw();

        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    private float getMin(float[] intersectionPoint, int i, Float... values) {
        List<Float> allValues = getAllValues(intersectionPoint, i, values);
        return allValues.stream().min(Float::compare).orElse(-10000000f);
    }

    private float getMin(Float... values) {
        return Stream.of(values).min(Float::compare).orElse(-10000000f);
    }

    private float getMax(float[] intersectionPoint, int i, Float... values) {
        List<Float> allValues = getAllValues(intersectionPoint, i, values);
        return allValues.stream().max(Float::compare).orElse(10000000f);
    }

    private float getMax(Float... values) {
        return Stream.of(values).max(Float::compare).orElse(10000000f);
    }

    private List<Float> getAllValues(float[] intersectionPoint, int i, Float[] values) {
        List<Float> allValues;
        if (intersectionPoint != null) {
            if (intersectionPoint.length == 2) {
                allValues = Arrays.asList(intersectionPoint[i], values[0], values[1], values[2], values[3]);
            } else if (intersectionPoint.length == 6) {
                allValues = Arrays.asList(values[0], values[1], values[2], values[3]);
            } else {
                allValues = Arrays.asList(values[0], values[1], values[2], values[3]);
            }
        } else {
            allValues = Arrays.asList(values[0], values[1], values[2], values[3]);
        }
        return allValues;
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
        settings.dispose();
        intersection.dispose();
        coordinateSystemStage.dispose();
        stage.dispose();
        skinHandler.dispose();
    }
}
