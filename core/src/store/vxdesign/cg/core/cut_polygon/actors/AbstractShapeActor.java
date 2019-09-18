package store.vxdesign.cg.core.cut_polygon.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class AbstractShapeActor extends Actor {
    Camera camera;
    ShapeRenderer shapeRenderer;

    AbstractShapeActor(Camera camera) {
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setAutoShapeType(true);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
