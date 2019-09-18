package store.vxdesign.cg.core.cut_polygon.actors;

import store.vxdesign.cg.core.cut_polygon.utilities.handlers.SkinHandler;
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

    private Label irregularity;
    private Slider irregularitySlider;
    private Label irregularityInfo;

    private Label spikeyness;
    private Slider spikeynessSlider;
    private Label spikeynessInfo;

    private Pixmap tableBackground;

    public SettingsTable(Camera camera, SkinHandler skinHandler) {
        angles = new Label("Angles", skinHandler.labelStyle);
        angles.setAlignment(Align.center);
        anglesSlider = new Slider(3, 100, 1, false, skinHandler.sliderStyle);
        anglesSlider.setValue(20);
        anglesInfo = new Label(Integer.toString((int) anglesSlider.getValue()), skinHandler.labelStyle);
        anglesInfo.setAlignment(Align.center);

        this.add(angles).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(anglesSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(anglesInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        maxLength = new Label("Max length", skinHandler.labelStyle);
        maxLength.setAlignment(Align.center);
        maxLengthSlider = new Slider(100, 500, 1, false, skinHandler.sliderStyle);
        maxLengthSlider.setValue(250);
        maxLengthInfo = new Label(Integer.toString((int) maxLengthSlider.getValue()), skinHandler.labelStyle);
        maxLengthInfo.setAlignment(Align.center);

        this.add(maxLength).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(maxLengthSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(maxLengthInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        irregularity = new Label("Irregularity", skinHandler.labelStyle);
        irregularity.setAlignment(Align.center);
        irregularitySlider = new Slider(0, 1, 0.01f, false, skinHandler.sliderStyle);
        irregularitySlider.setValue(0.5f);
        irregularityInfo = new Label(Integer.toString((int) irregularitySlider.getValue()), skinHandler.labelStyle);
        irregularityInfo.setAlignment(Align.center);

//        this.add(irregularity).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
//        this.add(irregularitySlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
//        this.add(irregularityInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        spikeyness = new Label("Spikeyness", skinHandler.labelStyle);
        spikeyness.setAlignment(Align.center);
        spikeynessSlider = new Slider(0, 1, 0.01f, false, skinHandler.sliderStyle);
        spikeynessSlider.setValue(0.25f);
        spikeynessInfo = new Label(Integer.toString((int) spikeynessSlider.getValue()), skinHandler.labelStyle);
        spikeynessInfo.setAlignment(Align.center);

//        this.add(spikeyness).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
//        this.add(spikeynessSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
//        this.add(spikeynessInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

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

    public Slider getIrregularitySlider() {
        return irregularitySlider;
    }

    public Label getIrregularityInfo() {
        return irregularityInfo;
    }

    public Slider getSpikeynessSlider() {
        return spikeynessSlider;
    }

    public Label getSpikeynessInfo() {
        return spikeynessInfo;
    }

    public void dispose() {
        tableBackground.dispose();
    }
}