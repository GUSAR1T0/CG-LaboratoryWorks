package store.vxdesign.cg.core.lines_intersection.ui.actors;

import store.vxdesign.cg.core.lines_intersection.utilities.DigitFieldRules;
import store.vxdesign.cg.core.lines_intersection.utilities.SkinHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SettingsTable extends Table {
    private static final float WIDTH = Gdx.graphics.getWidth() / 5f;
    private static final float HEIGHT = Gdx.graphics.getHeight() / 10f;
    private final SkinHandler skinHandler;

    public enum LineType {
        STRAIGHT, SEGMENT, RAY;

        @Override
        public String toString() {
            return String.format("%s%s", this.name().charAt(0), this.name().toLowerCase().substring(1));
        }
    }

    private class ContainerLine {
        private final SelectBox<LineType> type;

        private final Label openingParenthesisPoint1;
        private final TextField point1X;
        private final Label semicolonPoint1;
        private final TextField point1Y;
        private final Label closingParenthesisPoint1;

        private final Label openingParenthesisPoint2;
        private final TextField point2X;
        private final Label semicolonPoint2;
        private final TextField point2Y;
        private final Label closingParenthesisPoint2;

        private final Pixmap pixmap1;
        private final Pixmap pixmap2;
        private final TextureRegionDrawable color1Drawable;
        private final TextureRegionDrawable color2Drawable;

        ContainerLine(Color color1, Color color2) {
            this.type = new SelectBox<>(skinHandler.selectBoxStyle);
            this.type.setItems(LineType.STRAIGHT, LineType.SEGMENT, LineType.RAY);

            this.pixmap1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
            this.pixmap1.setColor(color1);
            this.pixmap1.fill();
            this.color1Drawable = new TextureRegionDrawable(new TextureRegion(new Texture(this.pixmap1)));

            this.openingParenthesisPoint1 = new Label("(", skinHandler.labelStyle);
            this.point1X = new TextField("0", skinHandler.textfieldStyle);
            this.point1X.setAlignment(Align.center);
            this.semicolonPoint1 = new Label(";", skinHandler.labelStyle);
            this.point1Y = new TextField("0", skinHandler.textfieldStyle);
            this.point1Y.setAlignment(Align.center);
            this.closingParenthesisPoint1 = new Label(")", skinHandler.labelStyle);

            this.pixmap2 = new Pixmap(1, 1, Pixmap.Format.RGB565);
            this.pixmap2.setColor(color2);
            this.pixmap2.fill();
            this.color2Drawable = new TextureRegionDrawable(new TextureRegion(new Texture(this.pixmap2)));

            this.openingParenthesisPoint2 = new Label("(", skinHandler.labelStyle);
            this.point2X = new TextField("0", skinHandler.textfieldStyle);
            this.point2X.setAlignment(Align.center);
            this.semicolonPoint2 = new Label(";", skinHandler.labelStyle);
            this.point2Y = new TextField("0", skinHandler.textfieldStyle);
            this.point2Y.setAlignment(Align.center);
            this.closingParenthesisPoint2 = new Label(")", skinHandler.labelStyle);

            this.point1X.setTextFieldFilter(DigitFieldRules.DIGIT_FIELD_FILTER);
            this.point1X.setTextFieldListener(DigitFieldRules.DIGIT_FIELD_LISTENER);
            this.point1Y.setTextFieldFilter(DigitFieldRules.DIGIT_FIELD_FILTER);
            this.point1Y.setTextFieldListener(DigitFieldRules.DIGIT_FIELD_LISTENER);
            this.point2X.setTextFieldFilter(DigitFieldRules.DIGIT_FIELD_FILTER);
            this.point2X.setTextFieldListener(DigitFieldRules.DIGIT_FIELD_LISTENER);
            this.point2Y.setTextFieldFilter(DigitFieldRules.DIGIT_FIELD_FILTER);
            this.point2Y.setTextFieldListener(DigitFieldRules.DIGIT_FIELD_LISTENER);
        }
    }

    private List<ContainerLine> lines;
    private Pixmap tableBackground;

    public SettingsTable(Camera camera, SkinHandler skinHandler) {
        this.skinHandler = skinHandler;
        this.lines = Arrays.asList(new ContainerLine(Color.BLUE, Color.FOREST), new ContainerLine(Color.FIREBRICK, Color.ORANGE));
        this.lines.forEach(line -> {
            this.add(line.type).width(6f / 25f * WIDTH).height(HEIGHT / 2f);

            this.add(new Image(line.color1Drawable)).width(1f / 25f * WIDTH).height(HEIGHT / 8f).padRight(1f / 50f * WIDTH);
            this.add(line.openingParenthesisPoint1).width(1f / 50f * WIDTH).height(HEIGHT / 2f);
            this.add(line.point1X).width(3f / 25f * WIDTH).height(HEIGHT / 2f);
            this.add(line.semicolonPoint1).width(1f / 50f * WIDTH).height(HEIGHT / 2f);
            this.add(line.point1Y).width(3f / 25f * WIDTH).height(HEIGHT / 2f);
            this.add(line.closingParenthesisPoint1).width(1f / 50f * WIDTH).height(HEIGHT / 2f);

            this.add(new Image(line.color2Drawable)).width(1f / 25f * WIDTH).height(HEIGHT / 8f).padRight(1f / 50f * WIDTH).padLeft(1f / 50f * WIDTH);
            this.add(line.openingParenthesisPoint2).width(1f / 50f * WIDTH).height(HEIGHT / 2f);
            this.add(line.point2X).width(3f / 25f * WIDTH).height(HEIGHT / 2f);
            this.add(line.semicolonPoint2).width(1f / 50f * WIDTH).height(HEIGHT / 2f);
            this.add(line.point2Y).width(3f / 25f * WIDTH).height(HEIGHT / 2f);
            this.add(line.closingParenthesisPoint2).width(1f / 50f * WIDTH).height(HEIGHT / 2f);

            this.row();
        });

        this.lines.get(0).point1X.setText("10");
        this.lines.get(0).point1Y.setText("0");
        this.lines.get(0).point2X.setText("20");
        this.lines.get(0).point2Y.setText("0");
        this.lines.get(1).point1X.setText("15");
        this.lines.get(1).point1Y.setText("-10");
        this.lines.get(1).point2X.setText("15");
        this.lines.get(1).point2Y.setText("10");
        this.lines.get(0).type.setSelected(LineType.SEGMENT);
        this.lines.get(1).type.setSelected(LineType.SEGMENT);

        tableBackground = new Pixmap(1, 1, Pixmap.Format.RGB565);
        tableBackground.setColor(new Color(1f, 1f, 1f, 1f));
        tableBackground.fill();

        this.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(tableBackground))));
        this.setColor(235 / 255f, 235 / 255f, 235 / 255f, 1f);
        this.setSize(WIDTH, HEIGHT);
        Vector3 center = camera.project(new Vector3(0, 0, 0));
        this.setPosition(camera.viewportWidth * 4 / 5 - 100, camera.viewportHeight * 4 / 5 - 300);
    }

    public LineType getFirstLineType() {
        return lines.get(0).type.getSelected();
    }

    public LineType getSecondLineType() {
        return lines.get(1).type.getSelected();
    }

    private Vector2 getLinePoint1(int number) {
        String p1x = lines.get(number - 1).point1X.getText().replace(",", ".");
        String p1y = lines.get(number - 1).point1Y.getText().replace(",", ".");
        return new Vector2(Float.parseFloat(p1x), Float.parseFloat(p1y));
    }

    public void setLinePoint1(int number) {
        Random random = new SecureRandom();
        List<String> list = random.doubles(2, -10, 10).mapToObj(n -> String.format("%.2f", n).replace(",", ".")).collect(Collectors.toList());
        lines.get(number - 1).point1X.setText(list.get(0));
        lines.get(number - 1).point1Y.setText(list.get(1));
    }

    /* START 1 LINE */
    public Vector2 getFirstLinePoint1() {
        return getLinePoint1(1);
    }


    /* START 2 LINE */
    public Vector2 getSecondLinePoint1() {
        return getLinePoint1(2);
    }

    private Vector2 getLinePoint2(int number) {
        String p1x = lines.get(number - 1).point2X.getText().replace(",", ".");
        String p1y = lines.get(number - 1).point2Y.getText().replace(",", ".");
        return new Vector2(Float.parseFloat(p1x), Float.parseFloat(p1y));
    }

    public void setLinePoint2(int number) {
        Random random = new SecureRandom();
        List<String> list = random.doubles(2, -10, 10).mapToObj(n -> String.format("%.2f", n).replace(",", ".")).collect(Collectors.toList());
        lines.get(number - 1).point2X.setText(list.get(0));
        lines.get(number - 1).point2Y.setText(list.get(1));
    }

    /* END 1 LINE */
    public Vector2 getFirstLinePoint2() {
        return getLinePoint2(1);
    }

    /* END 2 LINE */
    public Vector2 getSecondLinePoint2() {
        return getLinePoint2(2);
    }

    public void dispose() {
        tableBackground.dispose();
        lines.forEach(line -> {
            line.pixmap1.dispose();
            line.pixmap2.dispose();
        });
    }
}
