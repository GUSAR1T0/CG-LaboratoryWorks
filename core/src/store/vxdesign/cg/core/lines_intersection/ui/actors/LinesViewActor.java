package store.vxdesign.cg.core.lines_intersection.ui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LinesViewActor extends AbstractShapeActor {
    private Polyline lineOne;
    private Polyline lineTwo;
    private SettingsTable.LineType lineOneType;
    private SettingsTable.LineType lineTwoType;
    private float[] point;
    private float coefficient;
    private Vector2 centerCoordinateSystem;

    private Vector2 lineOnePoint1;
    private Vector2 lineOnePoint2;
    private Vector2 lineTwoPoint1;
    private Vector2 lineTwoPoint2;

    public LinesViewActor(Camera camera) {
        super(camera);
        lineOne = new Polyline();
        lineTwo = new Polyline();
        lineOnePoint1 = new Vector2();
        lineOnePoint2 = new Vector2();
        lineTwoPoint1 = new Vector2();
        lineTwoPoint2 = new Vector2();
    }

    private float[] getIntersectionOfTwoLineSegments() {
        float xa = lineOnePoint1.x, ya = lineOnePoint1.y;
        float xb = lineTwoPoint1.x, yb = lineTwoPoint1.y;
        float xc = lineOnePoint2.x, yc = lineOnePoint2.y;
        float xd = lineTwoPoint2.x, yd = lineTwoPoint2.y;

        float[] coefficients1 = getEquationLineCoefficients2(lineOne);
        float[] coefficients2 = getEquationLineCoefficients2(lineTwo);

        if (roundFloat(coefficients1[0]) == roundFloat(coefficients2[0]) && roundFloat(coefficients1[1]) == roundFloat(coefficients2[1])) {
            if (!(lineOnePoint1.x == lineOnePoint2.x && lineTwoPoint1.x == lineTwoPoint2.x)) {
                List<Vector2> list = Stream.of(lineOnePoint1, lineOnePoint2, lineTwoPoint1, lineTwoPoint2).sorted((vector1, vector2) -> Float.compare(vector1.x, vector2.x)).skip(1).limit(2).collect(Collectors.toList());
                return new float[]{coefficients1[0], coefficients1[1], list.get(0).x, list.get(0).y, list.get(1).x, list.get(1).y};
            } else if (Float.isInfinite(coefficients1[0]) && Float.isInfinite(coefficients2[0])) {
                List<Float> listY = Stream.of(lineOnePoint1.y, lineOnePoint2.y, lineTwoPoint1.y, lineTwoPoint2.y).sorted().skip(1).limit(2).collect(Collectors.toList());
                return new float[]{lineOnePoint1.x, listY.get(0), lineOnePoint1.x, listY.get(1)};
            }
        }

        if (test(lineOnePoint1, lineTwoPoint1, lineTwoPoint2)) {
            return new float[]{lineOnePoint1.x, lineOnePoint1.y};
        }
        if (test(lineOnePoint2, lineTwoPoint1, lineTwoPoint2)) {
            return new float[]{lineOnePoint2.x, lineOnePoint2.y};
        }
        if (test(lineTwoPoint1, lineOnePoint1, lineOnePoint2)) {
            return new float[]{lineTwoPoint1.x, lineTwoPoint1.y};
        }
        if (test(lineTwoPoint2, lineOnePoint1, lineOnePoint2)) {
            return new float[]{lineTwoPoint2.x, lineTwoPoint2.y};
        }

        float xac = xc - xa, yac = yc - ya;
        float xbd = xd - xb, ybd = yd - yb;

        float a1 = -yac;
        float b1 = xac;
        float d1 = -(a1 * xa + b1 * ya);

        float a2 = -ybd;
        float b2 = xbd;
        float d2 = -(a2 * xb + b2 * yb);

        float seg1_line2_start = a2 * xa + b2 * ya + d2;
        float seg1_line2_end = a2 * xc + b2 * yc + d2;

        float seg2_line1_start = a1 * xb + b1 * yb + d1;
        float seg2_line1_end = a1 * xd + b1 * yd + d1;

        if (seg1_line2_start * seg1_line2_end >= 0 || seg2_line1_start * seg2_line1_end >= 0) {
            return null;
        }

        float u = seg1_line2_start / (seg1_line2_start - seg1_line2_end);
        float x = xa + u * xac, y = ya + u * yac;

        return new float[]{x, y};
    }

    private boolean test(Vector2 possible, Vector2 p1, Vector2 p2) {
        float q = (possible.x - p1.x) / (p2.x - p1.x) - (possible.y - p1.y) / (p2.y - p1.y);
        double epsilon = 1e-10;
        if (!Float.isInfinite(q) && !Float.isNaN(q)) {
            return (Math.pow(q, 2) <= epsilon);
        } else if (Math.pow(Math.abs(p2.x - p1.x), 2) <= epsilon && possible.x == p1.x && possible.x == p2.x) {
            float minY = Math.min(p1.y, p2.y);
            float maxY = Math.max(p1.y, p2.y);
            return minY <= possible.y && maxY >= possible.y;
        } else if (Math.pow(Math.abs(p2.y - p1.y), 2) <= epsilon && possible.y == p1.y && possible.y == p2.y) {
            float minX = Math.min(p1.x, p2.x);
            float maxX = Math.max(p1.x, p2.x);
            return minX <= possible.x && maxX >= possible.x;
        }
        return false;
    }

    private Vector2 getOverPlus(Polyline line) {
        return getOver(line, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private Vector2 getOverMinus(Polyline line) {
        return getOver(line, -Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
    }

    private Vector2 getOver(Polyline line, float infX, float infY) {
        float x, y;
        if (line.getVertices()[0] - line.getVertices()[2] == 0) {
            x = getShiftX(line.getVertices()[0]);
            y = getShiftY(infY);
        } else if (line.getVertices()[1] - line.getVertices()[3] == 0) {
            x = getShiftX(infX);
            y = getShiftY(line.getVertices()[1]);
        } else {
            float[] equationLineCoefficients = getEquationLineCoefficients(line);
            x = infX;
            y = equationLineCoefficients[0] * infX + equationLineCoefficients[1];
        }
        return new Vector2(x, y);
    }

    private float[] getEquationLineCoefficients(Polyline line) {
        float k = (line.getVertices()[1] - line.getVertices()[3]) / (line.getVertices()[0] - line.getVertices()[2]);
        float b = getShiftY(line.getVertices()[1]) - k * getShiftX(line.getVertices()[0]);
        return new float[]{k, b};
    }

    private float[] getEquationLineCoefficients2(Polyline line) {
        float k = (line.getVertices()[1] - line.getVertices()[3]) / (line.getVertices()[0] - line.getVertices()[2]);
        float b = line.getVertices()[1] - k * line.getVertices()[0];
        return new float[]{k, b};
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rectLine(lineOnePoint1, lineOnePoint2, 3f);
        shapeRenderer.rectLine(lineTwoPoint1, lineTwoPoint2, 3f);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(getShiftX(lineOne.getVertices()[0]), getShiftY(lineOne.getVertices()[1]), 4f);
        shapeRenderer.setColor(Color.FOREST);
        shapeRenderer.circle(getShiftX(lineOne.getVertices()[2]), getShiftY(lineOne.getVertices()[3]), 4f);
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.circle(getShiftX(lineTwo.getVertices()[0]), getShiftY(lineTwo.getVertices()[1]), 4f);
        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.circle(getShiftX(lineTwo.getVertices()[2]), getShiftY(lineTwo.getVertices()[3]), 4f);

        if (point != null) {
            shapeRenderer.setColor(Color.RED);
            if (point.length == 2) {
                shapeRenderer.circle(point[0], point[1], 3f);
            } else if (point.length == 4) {
                shapeRenderer.rectLine(point[0], point[1], point[2], point[3], 3f);
            } else if (point.length == 6) {
                shapeRenderer.rectLine(point[2], point[3], point[4], point[5], 3f);
            }
        }

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
    }

    private void getPositionsOfPoints(SettingsTable.LineType type, Polyline line, Vector2 point1, Vector2 point2) {
        switch (type) {
            case STRAIGHT:
                point1.set(getOverPlus(line));
                point2.set(getOverMinus(line));
                break;
            case SEGMENT:
                point1.set(getShiftX(line.getVertices()[0]), getShiftY(line.getVertices()[1]));
                point2.set(getShiftX(line.getVertices()[2]), getShiftY(line.getVertices()[3]));
                break;
            case RAY:
                if (line.getVertices()[0] - line.getVertices()[2] == 0) {
                    if (line.getVertices()[1] - line.getVertices()[3] < 0) {
                        point1.set(getOverPlus(line));
                    } else {
                        point1.set(getOverMinus(line));
                    }
                } else {
                    if (line.getVertices()[0] - line.getVertices()[2] < 0) {
                        point1.set(getOverPlus(line));
                    } else {
                        point1.set(getOverMinus(line));
                    }
                }
                point2.set(getShiftX(line.getVertices()[0]), getShiftY(line.getVertices()[1]));
                break;
            default:
                point1.set(line.getVertices()[0], line.getVertices()[1]);
                point2.set(line.getVertices()[2], line.getVertices()[3]);
        }
    }

    public void getPositionsOfPoints() {
        getPositionsOfPoints(lineOneType, lineOne, lineOnePoint1, lineOnePoint2);
        getPositionsOfPoints(lineTwoType, lineTwo, lineTwoPoint1, lineTwoPoint2);
        point = getIntersectionOfTwoLineSegments();
    }

    public void setLineOne(Vector2 start, Vector2 end) {
        lineOne.setVertices(new float[]{start.x, start.y, end.x, end.y});
        lineOnePoint1.set(getShiftX(start.x), getShiftY(start.y));
        lineOnePoint2.set(getShiftX(end.x), getShiftY(end.y));
    }

    public void setLineTwo(Vector2 start, Vector2 end) {
        lineTwo.setVertices(new float[]{start.x, start.y, end.x, end.y});
        lineTwoPoint1.set(getShiftX(start.x), getShiftY(start.y));
        lineTwoPoint2.set(getShiftX(end.x), getShiftY(end.y));
    }

    public void setLineOneType(SettingsTable.LineType type) {
        lineOneType = type;
    }

    public void setLineTwoType(SettingsTable.LineType type) {
        lineTwoType = type;
    }

    public float[] getIntersectionPoint() {
        if (point != null) {
            if (point.length == 2) {
                return new float[]{
                        roundFloat(getNonShiftX(point[0])),
                        roundFloat(getNonShiftY(point[1]))
                };
            } else if (point.length == 4) {
                return new float[]{
                        roundFloat(getNonShiftX(point[0])),
                        roundFloat(getNonShiftY(point[1])),
                        roundFloat(getNonShiftX(point[2])),
                        roundFloat(getNonShiftY(point[3]))
                };
            } else {
                return new float[]{
                        point[0], point[1],
                        roundFloat(getNonShiftX(point[2])),
                        roundFloat(getNonShiftY(point[3])),
                        roundFloat(getNonShiftX(point[4])),
                        roundFloat(getNonShiftY(point[5]))
                };
            }
        } else {
            return null;
        }
    }

    public void setCoefficient(float coefficient) {
        this.coefficient = coefficient;
    }

    public void setCenterCoordinateSystem(Vector2 centerCoordinateSystem) {
        this.centerCoordinateSystem = centerCoordinateSystem;
    }

    private float getShiftX(float original) {
        return (original - centerCoordinateSystem.x) * coefficient;
    }

    private float getNonShiftX(float original) {
        return original / coefficient + centerCoordinateSystem.x;
    }

    private float getShiftY(float original) {
        return (original - centerCoordinateSystem.y) * coefficient;
    }

    private float getNonShiftY(float original) {
        return original / coefficient + centerCoordinateSystem.y;
    }

    private float roundFloat(float f) {
        return Float.parseFloat(String.format("%.2f", f).replace(",", "."));
    }
}
