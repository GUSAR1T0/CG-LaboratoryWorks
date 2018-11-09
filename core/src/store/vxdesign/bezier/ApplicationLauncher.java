package store.vxdesign.bezier;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import store.vxdesign.bezier.screens.MainScreen;
import store.vxdesign.bezier.utilities.SkinHandler;

public class ApplicationLauncher extends ApplicationAdapter {
	private Screen screen;

	@Override
	public void create() {
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, 0, 0);
		camera.update();
		Stage stage = new Stage();
		this.screen = new MainScreen(camera, stage);
	}

	@Override
	public void render() {
		screen.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose () {
		screen.dispose();
		SkinHandler.dispose();
	}
}
