package store.vxdesign.cg.core.reflection.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import store.vxdesign.cg.core.reflection.utilities.SkinHandler;

public class StatusTable extends Table {
    private Label mouse;
    private Label xMouseLabel;
    private Label positionMouseX;
    private Label yMouseLabel;
    private Label positionMouseY;

    private Label reflection;
    private Label xReflectionLabel;
    private Label positionReflectionX;
    private Label yReflectionLabel;
    private Label positionReflectionY;

    private Pixmap tableBackground;

    public StatusTable(Camera camera, SkinHandler skinHandler) {
        this.mouse = new Label("Mouse", skinHandler.labelStyle);
        this.mouse.setAlignment(Align.center);
        this.xMouseLabel = new Label("X:", skinHandler.labelStyle);
        this.xMouseLabel.setAlignment(Align.center);
        this.positionMouseX = new Label("0", skinHandler.labelStyle);
        this.positionMouseX.setAlignment(Align.center);
        this.yMouseLabel = new Label("Y:", skinHandler.labelStyle);
        this.yMouseLabel.setAlignment(Align.center);
        this.positionMouseY = new Label("0", skinHandler.labelStyle);
        this.positionMouseY.setAlignment(Align.center);

        this.add(this.mouse).width(camera.viewportWidth / 25).height(camera.viewportHeight / 50);
        this.add(this.xMouseLabel).width(camera.viewportWidth / 30).height(camera.viewportHeight / 50);
        this.add(this.positionMouseX).width(camera.viewportWidth / 25).height(camera.viewportHeight / 50);
        this.add(this.yMouseLabel).width(camera.viewportWidth / 30).height(camera.viewportHeight / 50);
        this.add(this.positionMouseY).width(camera.viewportWidth / 25).height(camera.viewportHeight / 50);

        this.row();

        this.reflection = new Label("Reflection", skinHandler.labelStyle);
        this.reflection.setAlignment(Align.center);
        this.xReflectionLabel = new Label("X:", skinHandler.labelStyle);
        this.xReflectionLabel.setAlignment(Align.center);
        this.positionReflectionX = new Label("0", skinHandler.labelStyle);
        this.positionReflectionX.setAlignment(Align.center);
        this.yReflectionLabel = new Label("Y:", skinHandler.labelStyle);
        this.yReflectionLabel.setAlignment(Align.center);
        this.positionReflectionY = new Label("0", skinHandler.labelStyle);
        this.positionReflectionY.setAlignment(Align.center);

        this.add(this.reflection).width(camera.viewportWidth / 25).height(camera.viewportHeight / 50);
        this.add(this.xReflectionLabel).width(camera.viewportWidth / 30).height(camera.viewportHeight / 50);
        this.add(this.positionReflectionX).width(camera.viewportWidth / 25).height(camera.viewportHeight / 50);
        this.add(this.yReflectionLabel).width(camera.viewportWidth / 30).height(camera.viewportHeight / 50);
        this.add(this.positionReflectionY).width(camera.viewportWidth / 25).height(camera.viewportHeight / 50);

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

    public Label getPositionReflectionX() {
        return positionReflectionX;
    }

    public Label getPositionReflectionY() {
        return positionReflectionY;
    }

    public void dispose() {
        tableBackground.dispose();
    }
}
