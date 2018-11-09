package store.vxdesign.bezier.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import store.vxdesign.bezier.utilities.SkinHandler;

public class StatusTable extends Table {
    private Label mouse;
    private Label xMouseLabel;
    private Label positionMouseX;
    private Label yMouseLabel;
    private Label positionMouseY;

    private Pixmap tableBackground;

    public StatusTable(Camera camera) {
        this.mouse = new Label("Mouse", SkinHandler.LABEL_STYLE);
        this.mouse.setAlignment(Align.center);
        this.xMouseLabel = new Label("X:", SkinHandler.LABEL_STYLE);
        this.xMouseLabel.setAlignment(Align.center);
        this.positionMouseX = new Label("0", SkinHandler.LABEL_STYLE);
        this.positionMouseX.setAlignment(Align.center);
        this.yMouseLabel = new Label("Y:", SkinHandler.LABEL_STYLE);
        this.yMouseLabel.setAlignment(Align.center);
        this.positionMouseY = new Label("0", SkinHandler.LABEL_STYLE);
        this.positionMouseY.setAlignment(Align.center);

        this.add(this.mouse).width(camera.viewportWidth / 25).height(camera.viewportHeight / 50);
        this.add(this.xMouseLabel).width(camera.viewportWidth / 30).height(camera.viewportHeight / 50);
        this.add(this.positionMouseX).width(camera.viewportWidth / 25).height(camera.viewportHeight / 50);
        this.add(this.yMouseLabel).width(camera.viewportWidth / 30).height(camera.viewportHeight / 50);
        this.add(this.positionMouseY).width(camera.viewportWidth / 25).height(camera.viewportHeight / 50);

        tableBackground = new Pixmap(1, 1, Pixmap.Format.RGB565);
        tableBackground.setColor(new Color(225 / 255f, 225 / 255f, 225 / 255f, 1f));
        tableBackground.fill();

        this.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(tableBackground))));
        this.setColor(1, 1, 1, 0.75f);
        this.setSize(camera.viewportWidth / 5, camera.viewportHeight / 20);
        this.setPosition(camera.viewportWidth / 25, camera.viewportHeight / 20);
    }

    public Label getPositionMouseX() {
        return positionMouseX;
    }

    public Label getPositionMouseY() {
        return positionMouseY;
    }

    public void dispose() {
        tableBackground.dispose();
    }
}
