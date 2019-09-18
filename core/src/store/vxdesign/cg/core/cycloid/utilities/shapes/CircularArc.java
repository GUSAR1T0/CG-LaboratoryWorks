package store.vxdesign.cg.core.cycloid.utilities.shapes;

import java.util.List;

public class CircularArc extends AbstractPolylineShape {
    public CircularArc(List<Float> points) {
        super(PolylineShape.transform(points));
    }
}
