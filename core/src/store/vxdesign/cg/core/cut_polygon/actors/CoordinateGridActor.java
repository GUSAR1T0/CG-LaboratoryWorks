package store.vxdesign.cg.core.cut_polygon.actors;

import store.vxdesign.cg.core.cut_polygon.utilities.handlers.SkinHandler;
import store.vxdesign.cg.core.cut_polygon.utilities.OS;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.List;

public class CoordinateGridActor extends AbstractShapeActor {
    private final OS os;
    private final SkinHandler skinHandler;

    public CoordinateGridActor(Camera camera, SkinHandler skinHandler) {
        super(camera);
        this.skinHandler = skinHandler;
        this.os = OS.getOS();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        for (float i = 0; i < camera.viewportWidth / 2; i += os.getCellSize()) {
            shapeRenderer.line(i, -camera.viewportHeight / 2, i, camera.viewportHeight / 2);
        }
        for (float i = 0; i > -camera.viewportWidth / 2; i -= os.getCellSize()) {
            shapeRenderer.line(i, -camera.viewportHeight / 2, i, camera.viewportHeight / 2);
        }
        for (float i = 0; i < camera.viewportHeight / 2; i += os.getCellSize()) {
            shapeRenderer.line(-camera.viewportWidth / 2, i, camera.viewportWidth / 2, i);
        }
        for (float i = 0; i > -camera.viewportHeight / 2; i -= os.getCellSize()) {
            shapeRenderer.line(-camera.viewportWidth / 2, i, camera.viewportWidth / 2, i);
        }
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.line(0, -camera.viewportHeight / 2, 0, camera.viewportHeight / 2);
        shapeRenderer.line(-camera.viewportWidth / 2, 0, camera.viewportWidth / 2, 0);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(0,0, 2f);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
    }

    public List<Label> getCoordinateLabels() {
        List<Label> labels = new ArrayList<>();
        for (float i = os.getCellSize(); i < camera.viewportWidth / 2; i += os.getCellSize()) {
            Label label = new Label(Integer.toString((int) i), skinHandler.labelStyle);
            label.setPosition(camera.viewportWidth / 2 + i, camera.viewportHeight / 2 + 10);
            labels.add(label);
        }
        for (float i = -os.getCellSize(); i > -camera.viewportWidth / 2; i -= os.getCellSize()) {
            Label label = new Label(Integer.toString((int) i), skinHandler.labelStyle);
            label.setPosition(camera.viewportWidth / 2 + i, camera.viewportHeight / 2 + 10);
            labels.add(label);
        }
//        {
//            Label label = new Label("X", SkinHandler.LABEL_STYLE);
//            label.setPosition((camera.viewportWidth) - (camera.viewportWidth / 2) % 50 + 10, camera.viewportHeight / 2 + 20);
//            labels.add(label);
//        }
        for (float i = os.getCellSize(); i < camera.viewportHeight / 2; i += os.getCellSize()) {
            Label label = new Label(Integer.toString((int) i), skinHandler.labelStyle);
            label.setPosition(camera.viewportWidth / 2 + 10, camera.viewportHeight / 2 + i);
            labels.add(label);
        }
        for (float i = -os.getCellSize(); i > -camera.viewportHeight / 2; i -= os.getCellSize()) {
            Label label = new Label(Integer.toString((int) i), skinHandler.labelStyle);
            label.setPosition(camera.viewportWidth / 2 + 10, camera.viewportHeight / 2 + i);
            labels.add(label);
        }
//        {
//            Label label = new Label("Y", SkinHandler.LABEL_STYLE);
//            label.setPosition(camera.viewportWidth / 2 - 60, (camera.viewportHeight) - (camera.viewportHeight / 2) % 50 - 20);
//            labels.add(label);
//        }
        return labels;
    }
}
