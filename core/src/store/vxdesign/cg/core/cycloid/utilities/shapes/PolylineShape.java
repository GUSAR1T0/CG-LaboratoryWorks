package store.vxdesign.cg.core.cycloid.utilities.shapes;

import java.util.List;

public interface PolylineShape {
    float[] getPoints();

    static float[] transform(List<Float> points) {
        float[] transformedPoints = new float[points.size()];
        for (int i = 0; i < points.size(); i++) {
            transformedPoints[i] = points.get(i);
        }
        return transformedPoints;
    }
}
