package store.vxdesign.cg.core.cycloid.actors;

import store.vxdesign.cg.core.cycloid.utilities.handlers.PolygonHandler;
import store.vxdesign.cg.core.cycloid.utilities.shapes.CircularArc;
import store.vxdesign.cg.core.cycloid.utilities.shapes.PolylineShape;
import store.vxdesign.cg.core.cycloid.utilities.shapes.Segment;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.ShortArray;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CycloidActor extends AbstractShapeActor {
    private final PolygonSpriteBatch polygonSpriteBatch = new PolygonSpriteBatch();
    private PolygonSprite polygonSprite;

    private int numberShape, numberPoint;
    private float t;

    private float[] polygonVertices;
    private List<PolylineShape> shapes;
    private Circle cycloidCircle;
    private List<Float> coordinatesCycloid;

    private int angles = 6;
    private float maxLength = 500;
    private float radius = 75;
    private float omega = 3;
    private boolean isCycloidVisible = true;

    public CycloidActor(Camera camera) {
        super(camera);
        shapes = new ArrayList<>();
        coordinatesCycloid = new ArrayList<>();
        cycloidCircle = new Circle();
        generatePolygon();
        updateActor();
        numberShape = 0;
        numberPoint = 0;
        t = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            generatePolygon();
            updateActor();
            numberShape = 0;
            numberPoint = 0;
            t = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            isCycloidVisible = !isCycloidVisible;
        }

        if (numberPoint + 2 >= shapes.get(numberShape).getPoints().length) {
//            numberPoint = (int) (2 * omega) - (shapes.get(numberShape).getPoints().length - numberPoint);
            numberPoint = 0;
            if (numberShape + 1 >= shapes.size()) {
                numberShape = 0;
            } else {
                numberShape++;
            }
        } else {
            numberPoint += 2;
        }

        if (t > 2 * Math.PI) {
            t = 0;
        } else {
            t += 0.01f;
        }

        shapeRenderer.setProjectionMatrix(camera.combined);
        polygonSpriteBatch.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        polygonSpriteBatch.begin();
        polygonSprite.draw(polygonSpriteBatch);
        polygonSpriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < shapes.size(); i++) {
            for (int j = 2; j < shapes.get(i).getPoints().length; j += 2) {
                shapeRenderer.rectLine(
                        shapes.get(i).getPoints()[j - 2],
                        shapes.get(i).getPoints()[j - 1],
                        shapes.get(i).getPoints()[j],
                        shapes.get(i).getPoints()[j + 1],
                        2f);
            }
            if (i != 0 && i + 1 != shapes.size()) {
                shapeRenderer.rectLine(
                        shapes.get(i).getPoints()[shapes.get(i).getPoints().length - 2],
                        shapes.get(i).getPoints()[shapes.get(i).getPoints().length - 1],
                        shapes.get(i + 1).getPoints()[0],
                        shapes.get(i + 1).getPoints()[1],
                        2f);
            } else if (i + 1 == shapes.size()) {
                shapeRenderer.rectLine(
                        shapes.get(i).getPoints()[shapes.get(i).getPoints().length - 2],
                        shapes.get(i).getPoints()[shapes.get(i).getPoints().length - 1],
                        shapes.get(0).getPoints()[0],
                        shapes.get(0).getPoints()[1],
                        2f);
            }
        }

        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.circle(shapes.get(numberShape).getPoints()[numberPoint],
                shapes.get(numberShape).getPoints()[numberPoint + 1], radius);

        updateCycloidCirclePosition();

        if (isCycloidVisible) {
            shapeRenderer.setColor(Color.SKY);
            for (int i = 2; i < coordinatesCycloid.size(); i += 2) {
                shapeRenderer.rectLine(coordinatesCycloid.get(i - 2), coordinatesCycloid.get(i - 1),
                        coordinatesCycloid.get(i), coordinatesCycloid.get(i + 1), 3f);
            }
        }

        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.circle(cycloidCircle.x, cycloidCircle.y, 10f);

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
    }

    private void generatePolygon() {
        Polygon polygon = PolygonHandler.generateConvexPolygon(angles, maxLength);
        polygonVertices = polygon.getTransformedVertices();

        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray triangleIndices = triangulator.computeTriangles(polygonVertices);

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.FOREST);
        pix.fill();
        Texture texture = new Texture(pix);
        TextureRegion textureRegion = new TextureRegion(texture);
        PolygonRegion polygonRegion = new PolygonRegion(textureRegion, polygonVertices, triangleIndices.toArray());
        polygonSprite = new PolygonSprite(polygonRegion);
    }

    private void updateActor() {
        shapes.clear();
        coordinatesCycloid.clear();

        List<Float> externalPolygonVertex = new ArrayList<>();
        for (int i = 2; i < polygonVertices.length; i += 2) {
            float[] normalPoints = getNormalPoints(polygonVertices[i - 2], polygonVertices[i - 1],
                    polygonVertices[i], polygonVertices[i + 1]);
            externalPolygonVertex.add(normalPoints[0]);
            externalPolygonVertex.add(normalPoints[1]);
            externalPolygonVertex.add(normalPoints[2]);
            externalPolygonVertex.add(normalPoints[3]);

            if (i + 2 == polygonVertices.length) {
                normalPoints = getNormalPoints(polygonVertices[i], polygonVertices[i + 1],
                        polygonVertices[0], polygonVertices[1]);
                externalPolygonVertex.add(normalPoints[0]);
                externalPolygonVertex.add(normalPoints[1]);
                externalPolygonVertex.add(normalPoints[2]);
                externalPolygonVertex.add(normalPoints[3]);
            }
        }

        Segment.SegmentCollector collector = new Segment.SegmentCollector();
        collector.add(externalPolygonVertex.get(0), externalPolygonVertex.get(1));
        for (int i = 2; i < polygonVertices.length; i += 2) {
            float OAx = externalPolygonVertex.get(i * 2 - 2) - polygonVertices[i];
            float OAy = externalPolygonVertex.get(i * 2 - 1) - polygonVertices[i + 1];
            float OBx = externalPolygonVertex.get(i * 2) - polygonVertices[i];
            float OBy = externalPolygonVertex.get(i * 2 + 1) - polygonVertices[i + 1];

            collector.add(externalPolygonVertex.get(2 * i - 2), externalPolygonVertex.get(2 * i - 1));
            shapes.add(collector.create());
            shapes.add(new CircularArc(getThetas(polygonVertices[i], polygonVertices[i + 1], OAx, OAy, OBx, OBy)));
            collector.add(externalPolygonVertex.get(2 * i), externalPolygonVertex.get(2 * i + 1));

            if (i + 2 == polygonVertices.length) {
                OAx = externalPolygonVertex.get(i * 2 + 2) - polygonVertices[0];
                OAy = externalPolygonVertex.get(i * 2 + 3) - polygonVertices[1];
                OBx = externalPolygonVertex.get(0) - polygonVertices[0];
                OBy = externalPolygonVertex.get(1) - polygonVertices[1];

                collector.add(externalPolygonVertex.get(2 * i + 2), externalPolygonVertex.get(2 * i + 3));
                shapes.add(collector.create());
                shapes.add(new CircularArc(getThetas(polygonVertices[0], polygonVertices[1], OAx, OAy, OBx, OBy)));
            }
        }
    }

    private float[] getNormalPoints(float x1, float y1, float x2, float y2) {
        float ax = x2 - x1, ay = y2 - y1;
        if (ax != 0 && ay != 0) {
            float bx = (float) (radius * ay / Math.sqrt(ax * ax + ay * ay));
            float by = -ax * bx / ay;
            return new float[]{x1 + bx, y1 + by, x2 + bx, y2 + by};
        } else if (ax == 0) {
            return new float[]{x1 + radius, y1, x2 + radius, y2};
        } else {
            return new float[]{x1, y1 + radius, x2, y2 + radius};
        }
    }

    private List<Float> getThetas(float floatsX, float floatsY, float OAx, float OAy, float OBx, float OBy) {
        Function<Double, Float> getX = angle -> (float) (floatsX + Math.cos(angle) * radius);
        Function<Double, Float> getY = angle -> (float) (floatsY + (Math.signum(OAy) == -1 && Math.signum(OBy) == -1 ? (-1) : 1) * Math.sin(angle) * radius);
        double deltaAngle = Math.PI / (360 * radius / 100f);

        List<Float> thetas = new ArrayList<>();
        if (OAy > 0 && OBy > 0) {
            double alpha = Math.acos(OAx / radius);
            double beta = Math.acos(OBx / radius);
            for (double i = alpha; i < beta; i += deltaAngle) {
                thetas.add(getX.apply(i));
                thetas.add(getY.apply(i));
            }
        } else if (OAy < 0 && OBy > 0) {
            double alpha = -Math.acos(OAx / radius);
            double beta = Math.acos(OBx / radius);
            for (double i = alpha; i < beta; i += deltaAngle) {
                thetas.add(getX.apply(i));
                thetas.add(getY.apply(i));
            }
        } else if (OAy < 0 && OBy < 0) {
            double alpha = Math.acos(OAx / radius);
            double beta = Math.acos(OBx / radius);
            for (double i = alpha; i > beta; i -= deltaAngle) {
                thetas.add(getX.apply(i));
                thetas.add(getY.apply(i));
            }
        } else {
            double alpha = Math.acos(OAx / radius);
            double beta = -Math.acos(OBx / radius);
            for (double i = alpha; i < -beta + 2 * (Math.PI + beta); i += deltaAngle) {
                thetas.add(getX.apply(i));
                thetas.add(getY.apply(i));
            }
        }
        return thetas;
    }

    private void updateCycloidCirclePosition() {
        float ax = ((Segment) shapes.get(0)).getOriginalPoints()[2] - ((Segment) shapes.get(0)).getOriginalPoints()[0];
        float ay = ((Segment) shapes.get(0)).getOriginalPoints()[3] - ((Segment) shapes.get(0)).getOriginalPoints()[1];
        float a = (float) Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2));
        double phi = Math.acos(ax / a) - Math.PI / 2f;

        float x1 = (float) (Math.cos(phi));
        float x2 = (float) (-Math.signum(ay) * Math.sin(phi));
        float y1 = (float) (Math.signum(ay) * Math.sin(phi));
        float y2 = (float) (Math.cos(phi));

        float x = (float) (radius * (x1 * Math.cos(omega * t) + x2 * Math.sin(omega * t)) + shapes.get(numberShape).getPoints()[numberPoint]);
        float y = (float) (radius * (y1 * Math.cos(omega * t) + y2 * Math.sin(omega * t)) + shapes.get(numberShape).getPoints()[numberPoint + 1]);

        cycloidCircle.setX(x);
        cycloidCircle.setY(y);

        coordinatesCycloid.add(x);
        coordinatesCycloid.add(y);
    }

    public void setAngles(int angles) {
        if (this.angles != angles) {
            this.angles = angles;
            generatePolygon();
            updateActor();
        }
    }

    public void setMaxLength(float maxLength) {
        if (this.maxLength != maxLength) {
            this.maxLength = maxLength;
            generatePolygon();
            updateActor();
        }
    }

    public void setRadius(float radius) {
        if (this.radius != radius) {
            this.radius = radius;
            updateActor();
        }
    }

    public void setOmega(float omega) {
        if (this.omega != omega) {
            this.omega = omega;
            updateActor();
        }
    }
}
