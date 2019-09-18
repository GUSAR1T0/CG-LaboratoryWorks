package store.vxdesign.cg.core.reflection.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import store.vxdesign.cg.core.reflection.utilities.SkinHandler;

public class SettingsTable extends Table {
    private Label originalCircleRadius;
    private Slider originalCircleRadiusSlider;
    private Label originalCircleRadiusInfo;

    private Label lineAngle;
    private Slider lineAngleSlider;
    private Label lineAngleInfo;

    private Label lineOffsetX;
    private Slider lineOffsetXSlider;
    private Label lineOffsetXInfo;

    private Label lineOffsetY;
    private Slider lineOffsetYSlider;
    private Label lineOffsetYInfo;

    private Pixmap tableBackground;

    public SettingsTable(Camera camera, SkinHandler skinHandler) {
        originalCircleRadius = new Label("Circle Radius", skinHandler.labelStyle);
        originalCircleRadius.setAlignment(Align.center);
        originalCircleRadiusSlider = new Slider(1, 1000, 1, false, skinHandler.sliderStyle);
        originalCircleRadiusSlider.setValue(100);
        originalCircleRadiusInfo = new Label(Integer.toString((int) originalCircleRadiusSlider.getValue()), skinHandler.labelStyle);
        originalCircleRadiusInfo.setAlignment(Align.center);

        this.add(originalCircleRadius).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(originalCircleRadiusSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(originalCircleRadiusInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        lineAngle = new Label("Line Angle", skinHandler.labelStyle);
        lineAngle.setAlignment(Align.center);
        lineAngleSlider = new Slider(0, 180, 1, false, skinHandler.sliderStyle);
        lineAngleSlider.setValue(45);
        lineAngleInfo = new Label(Integer.toString((int) lineAngleSlider.getValue()), skinHandler.labelStyle);
        lineAngleInfo.setAlignment(Align.center);

        this.add(lineAngle).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(lineAngleSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(lineAngleInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        lineOffsetX = new Label("Line Offset X", skinHandler.labelStyle);
        lineOffsetX.setAlignment(Align.center);
        lineOffsetXSlider = new Slider(-camera.viewportWidth / 2, camera.viewportWidth / 2, 1, false, skinHandler.sliderStyle);
        lineOffsetXSlider.setValue(0);
        lineOffsetXInfo = new Label(Integer.toString((int) lineOffsetXSlider.getValue()), skinHandler.labelStyle);
        lineOffsetXInfo.setAlignment(Align.center);

        this.add(lineOffsetX).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(lineOffsetXSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(lineOffsetXInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        lineOffsetY = new Label("Line Offset Y", skinHandler.labelStyle);
        lineOffsetY.setAlignment(Align.center);
        lineOffsetYSlider = new Slider(-camera.viewportHeight / 2, camera.viewportHeight / 2, 1, false, skinHandler.sliderStyle);
        lineOffsetYSlider.setValue(0);
        lineOffsetYInfo = new Label(Float.toString(lineOffsetYSlider.getValue()), skinHandler.labelStyle);
        lineOffsetYInfo.setAlignment(Align.center);

        this.add(lineOffsetY).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(lineOffsetYSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(lineOffsetYInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        tableBackground = new Pixmap(1, 1, Pixmap.Format.RGB565);
        tableBackground.setColor(new Color(225 / 255f, 225 / 255f, 225 / 255f, 1f));
        tableBackground.fill();

        this.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(tableBackground))));
        this.setColor(1, 1, 1, 0.75f);
        this.setSize(camera.viewportWidth / 5, camera.viewportHeight / 5);
        this.setPosition(camera.viewportWidth * 4 / 5 - 100, camera.viewportHeight * 4 / 5 - 100);
    }

    public Slider getOriginalCircleRadiusSlider() {
        return originalCircleRadiusSlider;
    }

    public Label getOriginalCircleRadiusInfo() {
        return originalCircleRadiusInfo;
    }

    public Slider getLineAngleSlider() {
        return lineAngleSlider;
    }

    public Label getLineAngleInfo() {
        return lineAngleInfo;
    }

    public Slider getLineOffsetXSlider() {
        return lineOffsetXSlider;
    }

    public Label getLineOffsetXInfo() {
        return lineOffsetXInfo;
    }

    public Slider getLineOffsetYSlider() {
        return lineOffsetYSlider;
    }

    public Label getLineOffsetYInfo() {
        return lineOffsetYInfo;
    }

    public void dispose() {
        tableBackground.dispose();
    }
}
