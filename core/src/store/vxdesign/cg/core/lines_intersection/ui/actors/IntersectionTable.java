package store.vxdesign.cg.core.lines_intersection.ui.actors;

import store.vxdesign.cg.core.lines_intersection.utilities.SkinHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class IntersectionTable extends Table {
    private static final float WIDTH = Gdx.graphics.getWidth() / 5f;
    private static final float HEIGHT = Gdx.graphics.getHeight() / 10f;

    private enum IntersectionTemplate {
        INTERSECT("%s 1 and %s 2 intersect:%n(%.2f; %.2f)"), NOT_INTERSECT("%s 1 and %s 2 do not intersect"),
        SET_INTERSECT_1("%s 1 and %s 2 have%nset of intersections:%ny = %.2f * x %s %.2f"),
        SET_INTERSECT_2("%s 1 and %s 2 have%nset of intersections:%nx = %.2f");

        private final String template;

        IntersectionTemplate(String template) {
            this.template = template;
        }

        @Override
        public String toString() {
            return template;
        }
    }

    private Label status;
    private Pixmap tableBackground;

    public IntersectionTable(Camera camera, SkinHandler skinHandler) {
        this.status = new Label("", skinHandler.labelStyle);
        this.status.setAlignment(Align.center);

        this.add(this.status).width(WIDTH).height(HEIGHT);

        tableBackground = new Pixmap(1, 1, Pixmap.Format.RGB565);
        tableBackground.setColor(new Color(1f, 1f, 1f, 1f));
        tableBackground.fill();

        this.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(tableBackground))));
        this.setColor(235 / 255f, 235 / 255f, 235 / 255f, 1f);
        this.setSize(WIDTH, HEIGHT);
        Vector3 center = camera.project(new Vector3(0, 0, 0));
        this.setPosition(camera.viewportWidth * 4 / 5 - 100, camera.viewportHeight * 4 / 5 - 100);
    }

    public void update(SettingsTable.LineType lineType1, SettingsTable.LineType lineType2, float[] intersectionPoint) {
        if (intersectionPoint != null) {
            if (intersectionPoint.length == 2) {
                status.setText(String.format(IntersectionTemplate.INTERSECT.toString(), lineType1, lineType2,
                        intersectionPoint[0], intersectionPoint[1]));
            } else if (intersectionPoint.length == 4) {
                status.setText(String.format(IntersectionTemplate.SET_INTERSECT_2.toString(), lineType1, lineType2,
                        intersectionPoint[0]));
            } else if (intersectionPoint.length == 6) {
                status.setText(String.format(IntersectionTemplate.SET_INTERSECT_1.toString(), lineType1, lineType2,
                        intersectionPoint[0],
                        intersectionPoint[1] >= 0 ? "+" : "-", Math.abs(intersectionPoint[1])));
            }
        } else {
            status.setText(String.format(IntersectionTemplate.NOT_INTERSECT.toString(), lineType1, lineType2));
        }
    }

    public void dispose() {
        tableBackground.dispose();
    }
}
