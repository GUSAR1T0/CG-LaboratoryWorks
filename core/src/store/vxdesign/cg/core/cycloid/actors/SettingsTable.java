package store.vxdesign.cg.core.cycloid.actors;

import store.vxdesign.cg.core.cycloid.utilities.handlers.SkinHandler;
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

public class SettingsTable extends Table {
    private Label angles;
    private Slider anglesSlider;
    private Label anglesInfo;

    private Label maxLength;
    private Slider maxLengthSlider;
    private Label maxLengthInfo;

    private Label radius;
    private Slider radiusSlider;
    private Label radiusInfo;

    private Label omega;
    private Slider omegaSlider;
    private Label omegaInfo;

    private Pixmap tableBackground;

    public SettingsTable(Camera camera, SkinHandler skinHandler) {
        angles = new Label("Angles", skinHandler.labelStyle);
        angles.setAlignment(Align.center);
        anglesSlider = new Slider(3, 100, 1, false, skinHandler.sliderStyle);
        anglesSlider.setValue(6);
        anglesInfo = new Label(Integer.toString((int) anglesSlider.getValue()), skinHandler.labelStyle);
        anglesInfo.setAlignment(Align.center);

        this.add(angles).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(anglesSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(anglesInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        maxLength = new Label("Max length", skinHandler.labelStyle);
        maxLength.setAlignment(Align.center);
        maxLengthSlider = new Slider(100, 1000, 1, false, skinHandler.sliderStyle);
        maxLengthSlider.setValue(500);
        maxLengthInfo = new Label(Integer.toString((int) maxLengthSlider.getValue()), skinHandler.labelStyle);
        maxLengthInfo.setAlignment(Align.center);

        this.add(maxLength).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(maxLengthSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(maxLengthInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        radius = new Label("Radius", skinHandler.labelStyle);
        radius.setAlignment(Align.center);
        radiusSlider = new Slider(50, 300, 1, false, skinHandler.sliderStyle);
        radiusSlider.setValue(75);
        radiusInfo = new Label(Integer.toString((int) radiusSlider.getValue()), skinHandler.labelStyle);
        radiusInfo.setAlignment(Align.center);

        this.add(radius).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(radiusSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(radiusInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        omega = new Label("Omega", skinHandler.labelStyle);
        omega.setAlignment(Align.center);
        omegaSlider = new Slider(1, 10, 1, false, skinHandler.sliderStyle);
        omegaSlider.setValue(3);
        omegaInfo = new Label(Integer.toString((int) omegaSlider.getValue()), skinHandler.labelStyle);
        omegaInfo.setAlignment(Align.center);

        this.add(omega).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(omegaSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(omegaInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        tableBackground = new Pixmap(1, 1, Pixmap.Format.RGB565);
        tableBackground.setColor(new Color(225 / 255f, 225 / 255f, 225 / 255f, 1f));
        tableBackground.fill();

        this.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(tableBackground))));
        this.setColor(1, 1, 1, 0.75f);
        this.setSize(camera.viewportWidth / 5, camera.viewportHeight / 5);
        this.setPosition(camera.viewportWidth * 4 / 5 - 100, camera.viewportHeight * 4 / 5 - 100);
    }

    public Slider getAnglesSlider() {
        return anglesSlider;
    }

    public Label getAnglesInfo() {
        return anglesInfo;
    }

    public Slider getMaxLengthSlider() {
        return maxLengthSlider;
    }

    public Label getMaxLengthInfo() {
        return maxLengthInfo;
    }

    public Slider getRadiusSlider() {
        return radiusSlider;
    }

    public Label getRadiusInfo() {
        return radiusInfo;
    }

    public Slider getOmegaSlider() {
        return omegaSlider;
    }

    public Label getOmegaInfo() {
        return omegaInfo;
    }

    public void dispose() {
        tableBackground.dispose();
    }
}