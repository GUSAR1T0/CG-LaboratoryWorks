package store.vxdesign.cg.core.bezier.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import store.vxdesign.cg.core.bezier.utilities.SkinHandler;

public class SettingsTable extends Table {
    private Label weight0;
    private Slider weight0Slider;
    private Label weight0Info;

    private Label weight1;
    private Slider weight1Slider;
    private Label weight1Info;

    private Label weight2;
    private Slider weight2Slider;
    private Label weight2Info;

    private Label step;
    private Slider stepSlider;
    private Label stepInfo;

    private Pixmap tableBackground;

    private boolean block = false;

    public SettingsTable(Camera camera, SkinHandler skinHandler) {
        weight0 = new Label("Weight 0", skinHandler.labelStyle);
        weight0.setAlignment(Align.center);
        weight0Slider = new Slider(-10, 10, 1, false, skinHandler.sliderStyle);
        weight0Slider.setValue(1);
        weight0Info = new Label(Integer.toString((int) weight0Slider.getValue()), skinHandler.labelStyle);
        weight0Info.setAlignment(Align.center);

        this.add(weight0).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(weight0Slider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(weight0Info).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        weight1 = new Label("Weight 1", skinHandler.labelStyle);
        weight1.setAlignment(Align.center);
        weight1Slider = new Slider(-10, 10, 1, false, skinHandler.sliderStyle);
        weight1Slider.setValue(1);
        weight1Info = new Label(Integer.toString((int) weight1Slider.getValue()), skinHandler.labelStyle);
        weight1Info.setAlignment(Align.center);

        this.add(weight1).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(weight1Slider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(weight1Info).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        weight2 = new Label("Weight 2", skinHandler.labelStyle);
        weight2.setAlignment(Align.center);
        weight2Slider = new Slider(-10, 10, 1, false, skinHandler.sliderStyle);
        weight2Slider.setValue(1);
        weight2Info = new Label(Integer.toString((int) weight2Slider.getValue()), skinHandler.labelStyle);
        weight2Info.setAlignment(Align.center);

        this.add(weight2).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(weight2Slider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(weight2Info).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        step = new Label("Step", skinHandler.labelStyle);
        step.setAlignment(Align.center);
        stepSlider = new Slider(0.001f, 1, 0.001f, false, skinHandler.sliderStyle);
        stepInfo = new Label(Float.toString(stepSlider.getValue()), skinHandler.labelStyle);
        stepInfo.setAlignment(Align.center);

        this.add(step).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(stepSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(stepInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        tableBackground = new Pixmap(1, 1, Pixmap.Format.RGB565);
        tableBackground.setColor(new Color(225 / 255f, 225 / 255f, 225 / 255f, 1f));
        tableBackground.fill();

        this.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(tableBackground))));
        this.setColor(1, 1, 1, 0.75f);
        this.setSize(camera.viewportWidth / 5, camera.viewportHeight / 5);
        this.setPosition(camera.viewportWidth * 4 / 5 - 100, camera.viewportHeight * 4 / 5 - 100);

        ClickListener blocker = new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                block = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                block = false;
            }
        };
        weight0Slider.addListener(blocker);
        weight1Slider.addListener(blocker);
        weight2Slider.addListener(blocker);
        stepSlider.addListener(blocker);
    }

    public Slider getWeight0Slider() {
        return weight0Slider;
    }

    public Label getWeight0Info() {
        return weight0Info;
    }

    public Slider getWeight1Slider() {
        return weight1Slider;
    }

    public Label getWeight1Info() {
        return weight1Info;
    }

    public Slider getWeight2Slider() {
        return weight2Slider;
    }

    public Label getWeight2Info() {
        return weight2Info;
    }

    public Slider getStepSlider() {
        return stepSlider;
    }

    public Label getStepInfo() {
        return stepInfo;
    }

    public boolean isBlock() {
        return block;
    }

    public void dispose() {
        tableBackground.dispose();
    }
}
