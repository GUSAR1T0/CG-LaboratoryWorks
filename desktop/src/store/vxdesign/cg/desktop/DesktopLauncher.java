package store.vxdesign.cg.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import store.vxdesign.cg.core.ApplicationLauncher;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height - 125;
		config.vSyncEnabled = true;
		config.useHDPI = true;
		config.resizable = false;
		new LwjglApplication(new ApplicationLauncher(), config);
	}
}
