package store.vxdesign.cg.core.cycloid.utilities.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Segment extends AbstractPolylineShape {
    private final float[] extendedPoints;

    private Segment(float[] points) {
        super(points);
        this.extendedPoints = getExtendedPoints(points);
    }

    private static float[] getExtendedPoints(float[] points) {
        float deltaX = points[2] - points[0];
        float deltaY = points[3] - points[1];
        int count = (int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        Function<Float, Float> getX = i -> points[0] + deltaX * i / count;
        Function<Float, Float> getY = i -> points[1] + deltaY * i / count;

        List<Float> extendedPoints = new ArrayList<>();
        for (float i = 0; i <= count; i++) {
            extendedPoints.add(getX.apply(i));
            extendedPoints.add(getY.apply(i));
        }

        return PolylineShape.transform(extendedPoints);
    }

    @Override
    public float[] getPoints() {
        return extendedPoints;
    }

    public float[] getOriginalPoints() {
        return points;
    }

    public static class SegmentCollector {
        List<Float> points;

        public SegmentCollector() {
            this.points = new ArrayList<>();
        }

        public void add(float x, float y) {
            points.add(x);
            points.add(y);
        }

        public Segment create() {
            Segment segment = new Segment(PolylineShape.transform(points));
            points = new ArrayList<>();
            return segment;
        }
    }
}
