package store.vxdesign.cg.core.move_polygon.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import store.vxdesign.cg.core.move_polygon.utilities.SkinHandler;

public class SettingsTable extends Table {
    private Label angles;
    private Slider anglesSlider;
    private Label anglesInfo;

    private Label degree;
    private Slider degreeSlider;
    private Label degreeInfo;

    private Label maxLength;
    private Slider maxLengthSlider;
    private Label maxLengthInfo;

    private Label scale;
    private Slider scaleSlider;
    private Label scaleInfo;

    private Label type;
    private SelectBox<String> typeBox;

    private Pixmap tableBackground;

    private boolean block = false;

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

        degree = new Label("Degrees/sec", skinHandler.labelStyle);
        degree.setAlignment(Align.center);
        degreeSlider = new Slider(-360, 360, 1, false, skinHandler.sliderStyle);
        degreeSlider.setValue(0);
        degreeInfo = new Label(Integer.toString((int) degreeSlider.getValue()), skinHandler.labelStyle);
        degreeInfo.setAlignment(Align.center);

        this.add(degree).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(degreeSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(degreeInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        maxLength = new Label("Max length", skinHandler.labelStyle);
        maxLength.setAlignment(Align.center);
        maxLengthSlider = new Slider(50, 150, 1, false, skinHandler.sliderStyle);
        maxLengthSlider.setValue(100);
        maxLengthInfo = new Label(Integer.toString((int) maxLengthSlider.getValue()), skinHandler.labelStyle);
        maxLengthInfo.setAlignment(Align.center);

        this.add(maxLength).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(maxLengthSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(maxLengthInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        scale = new Label("Scale", skinHandler.labelStyle);
        scale.setAlignment(Align.center);
        scaleSlider = new Slider(1, 100, 1, false, skinHandler.sliderStyle);
        scaleInfo = new Label(Integer.toString((int) scaleSlider.getValue()), skinHandler.labelStyle);
        scaleInfo.setAlignment(Align.center);

        this.add(scale).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(scaleSlider).width(camera.viewportWidth / 10).height(camera.viewportHeight / 25);
        this.add(scaleInfo).width(camera.viewportWidth / 30).height(camera.viewportHeight / 25);

        this.row();

        type = new Label("Type", skinHandler.labelStyle);
        type.setAlignment(Align.center);
        typeBox = new SelectBox<>(skinHandler.selectBoxStyle);
        typeBox.setItems("MOUSE", "CUSTOM");

        this.add(type).width(camera.viewportWidth / 15).height(camera.viewportHeight / 25);
        this.add(typeBox).width(camera.viewportWidth * 2 / 15).height(camera.viewportHeight / 30).colspan(2);

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
        anglesSlider.addListener(blocker);
        degreeSlider.addListener(blocker);
        maxLengthSlider.addListener(blocker);
        scaleSlider.addListener(blocker);
        typeBox.addListener(blocker);
    }

    public Slider getAnglesSlider() {
        return anglesSlider;
    }

    public Label getAnglesInfo() {
        return anglesInfo;
    }

    public Slider getDegreeSlider() {
        return degreeSlider;
    }

    public Label getDegreeInfo() {
        return degreeInfo;
    }

    public Slider getMaxLengthSlider() {
        return maxLengthSlider;
    }

    public Label getMaxLengthInfo() {
        return maxLengthInfo;
    }

    public Slider getScaleSlider() {
        return scaleSlider;
    }

    public Label getScaleInfo() {
        return scaleInfo;
    }

    public SelectBox<String> getTypeBox() {
        return typeBox;
    }

    public boolean isBlock() {
        return block;
    }

    public void dispose() {
        tableBackground.dispose();
    }
}
