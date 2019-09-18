package store.vxdesign.cg.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import store.vxdesign.cg.core.bezier.screens.BezierScreen;
import store.vxdesign.cg.core.cut_polygon.screens.CutPolygonScreen;
import store.vxdesign.cg.core.cycloid.screens.CycloidScreen;
import store.vxdesign.cg.core.lines_intersection.screens.LinesIntersectionScreen;
import store.vxdesign.cg.core.move_polygon.screens.MovePolygonScreen;
import store.vxdesign.cg.core.reflection.screens.ReflectionScreen;

import java.util.function.Supplier;

public class ApplicationLauncher extends ApplicationAdapter {
    private Screen screen;

    @Override
    public void create() {
        screen = createMovePolygonScreen();
    }

    private static OrthographicCamera prepareCamera() {
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, 0, 0);
		camera.update();
		return camera;
	}

    private static MovePolygonScreen createMovePolygonScreen() {
        return new MovePolygonScreen(prepareCamera());
    }

    private static BezierScreen createBezierScreen() {
        return new BezierScreen(prepareCamera());
    }

    private static ReflectionScreen createReflectionScreen() {
        return new ReflectionScreen(prepareCamera());
    }

    private static LinesIntersectionScreen createLinesIntersectionScreen() {
        return new LinesIntersectionScreen(prepareCamera());
    }

    private static CycloidScreen createCycloidScreen() {
        return new CycloidScreen(prepareCamera());
    }

    private static CutPolygonScreen createCutPolygonScreen() {
        return new CutPolygonScreen(prepareCamera());
    }

    private void changeScreen(Supplier<Screen> changeFunc) {
        if (screen != null) {
            screen.dispose();
        }
        screen = changeFunc.get();
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                changeScreen(ApplicationLauncher::createMovePolygonScreen);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                changeScreen(ApplicationLauncher::createBezierScreen);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                changeScreen(ApplicationLauncher::createReflectionScreen);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                changeScreen(ApplicationLauncher::createLinesIntersectionScreen);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
                changeScreen(ApplicationLauncher::createCycloidScreen);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
                changeScreen(ApplicationLauncher::createCutPolygonScreen);
            }
        }

        if (screen != null) {
            screen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void dispose() {
        if (screen != null) {
            screen.dispose();
        }
    }
}
