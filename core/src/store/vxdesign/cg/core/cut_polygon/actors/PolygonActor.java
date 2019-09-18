package store.vxdesign.cg.core.cut_polygon.actors;

import store.vxdesign.cg.core.cut_polygon.utilities.handlers.PolygonHandler;
import store.vxdesign.cg.core.cut_polygon.utilities.Shape;
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
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ShortArray;

import java.util.ArrayList;
import java.util.List;

public class PolygonActor extends AbstractShapeActor {
    private final PolygonSpriteBatch polygonSpriteBatch = new PolygonSpriteBatch();
    private List<PolygonSprite> polygonSprites = new ArrayList<>();

    private float[] polygonVertices;
    private List<Shape> shapes;

    private int angles = 20;
    private float maxLength = 250;
    private float irregularity = 0.5f;
    private float spikeyness = 0.25f;

    public PolygonActor(Camera camera) {
        super(camera);
        generatePolygon();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            generatePolygon();
        }

        shapeRenderer.setProjectionMatrix(camera.combined);
        polygonSpriteBatch.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        polygonSpriteBatch.begin();
        polygonSprites.forEach(polygonSprite -> polygonSprite.draw(polygonSpriteBatch));
        polygonSpriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        for (int i = 2; i < polygonVertices.length; i += 2) {
            shapeRenderer.rectLine(polygonVertices[i - 2], polygonVertices[i - 1], polygonVertices[i], polygonVertices[i + 1], 3f);
            if (i + 2 == polygonVertices.length) {
                shapeRenderer.rectLine(polygonVertices[i], polygonVertices[i + 1], polygonVertices[0], polygonVertices[1], 3f);
            }
        }

        for (Shape shape : shapes) {
            shapeRenderer.setColor(shape.getColor());
            shapeRenderer.polygon(shape.getVertices());
        }

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
    }

    private void generatePolygon() {
        Polygon polygon = PolygonHandler.generateConcavePolygon(angles, maxLength, irregularity, spikeyness);
        polygonVertices = polygon.getTransformedVertices();

        List<Vector2> vertices = new ArrayList<>();
        for (int i = 0; i < polygonVertices.length; i += 2) {
            vertices.add(new Vector2(polygonVertices[i], polygonVertices[i + 1]));
        }
        vertices.add(new Vector2(polygonVertices[0], polygonVertices[1]));

        shapes = PolygonHandler.cutPolygon(vertices, new ArrayList<>());

        polygonSprites.clear();
        for (Shape shape : shapes) {
            EarClippingTriangulator triangulator = new EarClippingTriangulator();
            ShortArray triangleIndices = triangulator.computeTriangles(shape.getVertices());

            Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pix.setColor(shape.getColor());
            pix.fill();
            Texture texture = new Texture(pix);
            TextureRegion textureRegion = new TextureRegion(texture);
            PolygonRegion polygonRegion = new PolygonRegion(textureRegion, shape.getVertices(), triangleIndices.toArray());
            polygonSprites.add(new PolygonSprite(polygonRegion));
        }
    }

    public void setAngles(int angles) {
        if (this.angles != angles) {
            this.angles = angles;
            generatePolygon();
        }
    }

    public void setMaxLength(float maxLength) {
        if (this.maxLength != maxLength) {
            this.maxLength = maxLength;
            generatePolygon();
        }
    }

    public void setIrregularity(float irregularity) {
        if (this.irregularity != irregularity) {
            this.irregularity = irregularity;
            generatePolygon();
        }
    }

    public void setSpikeyness(float spikeyness) {
        if (this.spikeyness != spikeyness) {
            this.spikeyness = spikeyness;
            generatePolygon();
        }
    }
}
