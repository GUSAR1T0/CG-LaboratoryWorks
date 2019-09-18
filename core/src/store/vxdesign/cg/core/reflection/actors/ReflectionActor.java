package store.vxdesign.cg.core.reflection.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector3;

public class ReflectionActor extends AbstractShapeActor {
    private SettingsTable settings;

    private Circle originalCircle;
    private Polyline line;
    private Vector3 spectator;
    private Vector3 vectorReflection;

    public ReflectionActor(Camera camera, SettingsTable settings) {
        super(camera);
        this.shapeRenderer.setAutoShapeType(true);
        this.settings = settings;
        originalCircle = new Circle(-200, -500, settings.getOriginalCircleRadiusSlider().getValue());
        line = new Polyline(new float[]{-camera.viewportWidth, 0, camera.viewportWidth, 0});
        spectator = new Vector3(-camera.viewportWidth / 2 + 100, -(camera.viewportHeight / 2 - 100), 0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            Vector3 vector3 = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            originalCircle.set(vector3.x, vector3.y, settings.getOriginalCircleRadiusSlider().getValue());
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            spectator = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        } else {
            originalCircle.setRadius(settings.getOriginalCircleRadiusSlider().getValue());
        }

        line.setRotation(-settings.getLineAngleSlider().getValue());
        line.setPosition(settings.getLineOffsetXSlider().getValue(), -settings.getLineOffsetYSlider().getValue());
        float[] projectionAndDistance = getProjectionAndDistance(originalCircle.x, originalCircle.y);
        vectorReflection = new Vector3((2 * projectionAndDistance[2]) * MathUtils.cosDeg(90 - settings.getLineAngleSlider().getValue()) + originalCircle.x,
                (2 * projectionAndDistance[2]) * MathUtils.sinDeg(90 - settings.getLineAngleSlider().getValue()) + originalCircle.y,
                0);
        float[] tangentPoints = intersectTwoCircles();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rectLine(line.getTransformedVertices()[0], line.getTransformedVertices()[1],
                line.getTransformedVertices()[2], line.getTransformedVertices()[3], 2);

        if (tangentPoints != null) {
            float[] intersection1 = getIntersectionOfTwoLineSegments(tangentPoints[0], tangentPoints[1]);
            float[] intersection2 = getIntersectionOfTwoLineSegments(tangentPoints[2], tangentPoints[3]);

            if (intersection1 != null || intersection2 != null) {
                float[] tangentPointsProjection = getProjectionAndDistanceForTangentPoints(tangentPoints);

                if (intersection1 != null && intersection2 != null) {
                    shapeRenderer.setColor(Color.OLIVE);
                    shapeRenderer.rectLine(spectator.x, spectator.y, intersection1[0], intersection1[1], 3);
                    shapeRenderer.rectLine(spectator.x, spectator.y, intersection2[0], intersection2[1], 3);
                    shapeRenderer.rectLine(intersection2[0], intersection2[1], tangentPoints[2], tangentPoints[3], 3);
                    shapeRenderer.rectLine(intersection1[0], intersection1[1], tangentPoints[0], tangentPoints[1], 3);

                    shapeRenderer.setColor(Color.NAVY);
                    shapeRenderer.rectLine(intersection1[0], intersection1[1],
                            tangentPointsProjection[0] - tangentPointsProjection[2] * MathUtils.sinDeg(settings.getLineAngleSlider().getValue()),
                            tangentPointsProjection[1] - tangentPointsProjection[2] * MathUtils.cosDeg(settings.getLineAngleSlider().getValue()), 3);
                    shapeRenderer.rectLine(intersection2[0], intersection2[1],
                            tangentPointsProjection[3] - tangentPointsProjection[5] * MathUtils.sinDeg(settings.getLineAngleSlider().getValue()),
                            tangentPointsProjection[4] - tangentPointsProjection[5] * MathUtils.cosDeg(settings.getLineAngleSlider().getValue()), 3);
                } else if (intersection1 == null) {
                    shapeRenderer.setColor(Color.OLIVE);
                    shapeRenderer.rectLine(spectator.x, spectator.y, intersection2[0], intersection2[1], 3);
                    shapeRenderer.rectLine(spectator.x, spectator.y, tangentPoints[0], tangentPoints[1], 3);
                    shapeRenderer.rectLine(intersection2[0], intersection2[1], tangentPoints[2], tangentPoints[3], 3);

                    shapeRenderer.setColor(Color.NAVY);
                    shapeRenderer.rectLine(intersection2[0], intersection2[1],
                            tangentPointsProjection[3] - Math.abs(tangentPointsProjection[5]) * MathUtils.sinDeg(settings.getLineAngleSlider().getValue()),
                            tangentPointsProjection[4] - Math.abs(tangentPointsProjection[5]) * MathUtils.cosDeg(settings.getLineAngleSlider().getValue()), 3);
                } else {
                    shapeRenderer.setColor(Color.OLIVE);
                    shapeRenderer.rectLine(spectator.x, spectator.y, intersection1[0], intersection1[1], 3);
                    shapeRenderer.rectLine(spectator.x, spectator.y, tangentPoints[2], tangentPoints[3], 3);
                    shapeRenderer.rectLine(intersection1[0], intersection1[1], tangentPoints[0], tangentPoints[1], 3);

                    shapeRenderer.setColor(Color.NAVY);
                    shapeRenderer.rectLine(intersection1[0], intersection1[1],
                            tangentPointsProjection[0] - Math.abs(tangentPointsProjection[2]) * MathUtils.sinDeg(settings.getLineAngleSlider().getValue()),
                            tangentPointsProjection[1] - Math.abs(tangentPointsProjection[2]) * MathUtils.cosDeg(settings.getLineAngleSlider().getValue()), 3);
                }
            } else {
                shapeRenderer.setColor(Color.OLIVE);
                shapeRenderer.rectLine(spectator.x, spectator.y, tangentPoints[0], tangentPoints[1], 3);
                shapeRenderer.rectLine(spectator.x, spectator.y, tangentPoints[2], tangentPoints[3], 3);
            }
        }

//        shapeRenderer.setColor(Color.ORANGE);
//        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.circle(spectator.x, spectator.y, getLengthBetweenTwoCirclesViaTangentPoint());
//
//        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.circle(projectionAndDistance[0], projectionAndDistance[1], 4.5f);

        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.circle(vectorReflection.x, vectorReflection.y, originalCircle.radius);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(vectorReflection.x, vectorReflection.y, 4.5f);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(originalCircle.x, originalCircle.y, originalCircle.radius);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(originalCircle.x, originalCircle.y, 4.5f);

        shapeRenderer.setColor(Color.SKY);
        shapeRenderer.circle(spectator.x, spectator.y, 10);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(spectator.x, spectator.y, 4.5f);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
    }

    private float[] getProjectionAndDistance(float x3, float y3) {
        float x1 = line.getTransformedVertices()[0], y1 = line.getTransformedVertices()[1];
        float x2 = line.getTransformedVertices()[2], y2 = line.getTransformedVertices()[3];

        float[] projection = getProjection(x1, y1, x2, y2, x3, y3);

        float dx = projection[0] - x3;
        float dy = projection[1] - y3;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        return new float[]{projection[0], projection[1], distance * (isAbove() ? -1 : 1)};
    }

    private float[] getProjection(float x1, float y1, float x2, float y2, float x3, float y3) {
        float px = x2 - x1;
        float py = y2 - y1;
        float u = ((x3 - x1) * px + (y3 - y1) * py) / ((px * px) + (py * py));

        if (u > 1) {
            u = 1;
        } else if (u < 0) {
            u = 0;
        }

        float x = x1 + u * px;
        float y = y1 + u * py;

        return new float[]{x, y};
    }

    private boolean isAbove() {
        float x1 = line.getTransformedVertices()[0], y1 = line.getTransformedVertices()[1];
        float x2 = line.getTransformedVertices()[2], y2 = line.getTransformedVertices()[3];
        float x3 = originalCircle.x, y3 = originalCircle.y;
        float res = (x3 - x1) / (x2 - x1) - (y3 - y1) / (y2 - y1);
        return (res >= 0 && settings.getLineAngleSlider().getValue() >= 1 && settings.getLineAngleSlider().getValue() <= 90) ||
                (res <= 0 && (settings.getLineAngleSlider().getValue() == 0 || settings.getLineAngleSlider().getValue() > 90));
    }

    private float getLengthBetweenTwoCirclesViaTangentPoint() {
        if (Math.pow(spectator.x - vectorReflection.x, 2) + Math.pow(spectator.y - vectorReflection.y, 2) > Math.pow(originalCircle.radius, 2)) {
            float AO = (float) Math.sqrt(Math.pow(spectator.x - vectorReflection.x, 2) + Math.pow(spectator.y - vectorReflection.y, 2));
            return (float) Math.sqrt(Math.pow(AO, 2) - Math.pow(originalCircle.radius, 2));
        } else {
            return 0;
        }
    }

    private float[] intersectTwoCircles() {
        float x1 = spectator.x, x2 = vectorReflection.x;
        float y1 = spectator.y, y2 = vectorReflection.y;
        float r1 = getLengthBetweenTwoCirclesViaTangentPoint(), r2 = originalCircle.radius;

        float centerdx = x1 - x2;
        float centerdy = y1 - y2;
        float R = (float) Math.sqrt(centerdx * centerdx + centerdy * centerdy);
        if (!(Math.abs(r1 - r2) <= R && R <= r1 + r2)) {
            return null;
        }

        float R2 = R * R;
        float R4 = R2 * R2;
        float a = (r1 * r1 - r2 * r2) / (2 * R2);
        float r2r2 = (r1 * r1 - r2 * r2);
        float c = (float) Math.sqrt(2 * (r1 * r1 + r2 * r2) / R2 - (r2r2 * r2r2) / R4 - 1);

        float fx = (x1 + x2) / 2 + a * (x2 - x1);
        float gx = c * (y2 - y1) / 2;
        float ix1 = fx + gx;
        float ix2 = fx - gx;

        float fy = (y1 + y2) / 2 + a * (y2 - y1);
        float gy = c * (x1 - x2) / 2;
        float iy1 = fy + gy;
        float iy2 = fy - gy;

        return new float[]{ix1, iy1, ix2, iy2};
    }

    private float[] getProjectionAndDistanceForTangentPoints(float[] tangentPoints) {
        if (tangentPoints != null) {
            float[] p1 = getProjectionAndDistance(tangentPoints[0], tangentPoints[1]);
            float[] p2 = getProjectionAndDistance(tangentPoints[2], tangentPoints[3]);

            return new float[]{p1[0], p1[1], p1[2], p2[0], p2[1], p2[2]};
        } else {
            return null;
        }
    }

private float[] getIntersectionOfTwoLineSegments(float xd, float yd) {
    float xa = line.getTransformedVertices()[0], ya = line.getTransformedVertices()[1];
    float xb = spectator.x, yb = spectator.y;
    float xc = line.getTransformedVertices()[2], yc = line.getTransformedVertices()[3];

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

    float x = ((xd - xb) * (yc - ya) * xa - (xd - xb) * (xc - xa) * ya - (xc - xa) * (yd - yb) * xb + (xc - xa) * (xd - xb) * yb) /
            ((xd - xb) * (yc - ya) - (xc - xa) * (yd - yb));
    float y = ((yd - yb) * (x - xb) + (xd - xb) * yb) / (xd - xb);

    return new float[]{x, y};
}

    public Vector3 getVectorReflection() {
        return vectorReflection;
    }
}
