package store.vxdesign.cg.core.lines_intersection.ui.stages;

import store.vxdesign.cg.core.lines_intersection.ui.actors.CoordinateGridActor;
import store.vxdesign.cg.core.lines_intersection.utilities.SkinHandler;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CoordinateSystemStage extends Stage {
    private Camera camera;
    private final CoordinateGridActor grid;
    private final SkinHandler skinHandler;
    private final List<Label> labelsX;
    private final List<Label> labelsY;
    private float coefficient = 1f;
    private Vector2 center = new Vector2(0, 0);

    public CoordinateSystemStage(Camera camera, SkinHandler skinHandler) {
        this.camera = camera;
        this.grid = new CoordinateGridActor(camera);
        this.skinHandler = skinHandler;
        this.addActor(this.grid);

        Map<CoordinateGridActor.CoordinateKey, List<Vector2>> positions = this.grid.getInitialPositions();
        this.labelsX = createLabelsX(positions.get(CoordinateGridActor.CoordinateKey.X),
                positions.get(CoordinateGridActor.CoordinateKey.X_L).get(0));
        this.labelsY = createLabelsY(positions.get(CoordinateGridActor.CoordinateKey.Y),
                positions.get(CoordinateGridActor.CoordinateKey.Y_L).get(0));
        this.labelsX.forEach(this::addActor);
        this.labelsY.forEach(this::addActor);
    }

    private List<Label> createLabelsX(List<Vector2> positionsX, Vector2 positionAxisNameLabel) {
        int align = Align.left;
        float indentationX = grid.getCellSize() / 3f;
        float indentationY = -grid.getCellSize();
        List<Label> labels = positionsX.stream().map(point -> createLabel(point, Float.toString(point.x), align, indentationX, indentationY)).collect(Collectors.toList());
        labels.add(createLabel(positionAxisNameLabel, "X", Align.center, 0, 0));
        return labels;
    }

    private List<Label> createLabelsY(List<Vector2> positionsY, Vector2 positionAxisNameLabel) {
        int align = Align.right;
        float indentationX = -(7f / 3) * grid.getCellSize();
        float indentationY = 0;
        List<Label> labels = positionsY.stream().map(point -> createLabel(point, Float.toString(point.y), align, indentationX, indentationY)).collect(Collectors.toList());
        labels.add(createLabel(positionAxisNameLabel, "Y", Align.center, 0, 0));
        return labels;
    }

    private Label createLabel(Vector2 position, String labelName, int align, float indentationX, float indentationY) {
        Vector3 projectPoint = camera.project(new Vector3(position.x, position.y, 0));
        Label label = new Label(labelName, skinHandler.labelStyle);
        label.setAlignment(align);
        label.setSize(2 * grid.getCellSize(), grid.getCellSize());
        label.setPosition(projectPoint.x + indentationX, projectPoint.y + indentationY);
        return label;
    }

    public void update(float minX, float maxX, float minY, float maxY) {
        float difX1 = maxX - minX;
        float difY1 = maxY - minY;
        minX -= 2f * CoordinateGridActor.getCellSize(difX1);
        maxX += 2f * CoordinateGridActor.getCellSize(difX1);
        minY -= 2f * CoordinateGridActor.getCellSize(difY1);
        maxY += 2f * CoordinateGridActor.getCellSize(difY1);
        float difX2 = maxX - minX;
        float difY2 = maxY - minY;
        float resolution = camera.viewportWidth / camera.viewportHeight;

        List<Float> positionX = new ArrayList<>();
        List<Float> positionY = new ArrayList<>();
        float cellSize;
        if (difX2 > difY2 * resolution) {
            cellSize = CoordinateGridActor.getCellSize(difX2);
            float height = difX2 * camera.viewportHeight / camera.viewportWidth;
            center.set(minX + difX2 / 2f, minY + difY2 / 2f);

            positionX.add(center.x);
            for (float i = center.x - cellSize; i > center.x - difX2 / 2f; i -= cellSize) {
                positionX.add(i);
            }
            for (float i = center.x + cellSize; i < center.x + difX2 / 2f; i += cellSize) {
                positionX.add(i);
            }

            positionY.add(center.y);
            for (float i = center.y - cellSize; i > center.y - height / 2f; i -= cellSize) {
                positionY.add(i);
            }
            for (float i = center.y + cellSize; i < center.y + height / 2f; i += cellSize) {
                positionY.add(i);
            }
        } else {
            cellSize = CoordinateGridActor.getCellSize(difY2 * resolution);
            float width = difY2 * camera.viewportWidth / camera.viewportHeight;
            center.set(minX + difX2 / 2f, minY + difY2 / 2f);

            positionX.add(center.x);
            for (float i = center.x - cellSize; i > center.x - width / 2f; i -= cellSize) {
                positionX.add(i);
            }
            for (float i = center.x + cellSize; i < center.x + width / 2f; i += cellSize) {
                positionX.add(i);
            }

            positionY.add(center.y);
            for (float i = center.y - cellSize; i > center.y - difY2 / 2f; i -= cellSize) {
                positionY.add(i);
            }
            for (float i = center.y + cellSize; i < center.y + difY2 / 2f; i += cellSize) {
                positionY.add(i);
            }
        }
        coefficient = grid.getCellSize() / cellSize;

        positionX = positionX.stream().sorted().collect(Collectors.toList());
        for (int i = 0; i < labelsX.size() - 1; i++) {
            if (!"X".equals(labelsX.get(i).getText().toString())) {
                labelsX.get(i).setText(String.format("%.2f", positionX.get(i)).replace(",", "."));
            }
        }
        positionY = positionY.stream().sorted().collect(Collectors.toList());
        for (int i = 0; i < labelsY.size() - 1; i++) {
            if (!"Y".equals(labelsY.get(i).getText().toString())) {
                labelsY.get(i).setText(String.format("%.2f", positionY.get(i)).replace(",", "."));
            }
        }
    }

    public float getCoefficient() {
        return coefficient;
    }

    public Vector2 getCenter() {
        return center;
    }

    @Override
    public void dispose() {
        super.dispose();
        grid.dispose();
    }
}
