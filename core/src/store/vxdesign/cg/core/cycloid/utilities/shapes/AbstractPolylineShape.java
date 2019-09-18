package store.vxdesign.cg.core.cycloid.utilities.shapes;

abstract class AbstractPolylineShape implements PolylineShape {
    final float[] points;

    AbstractPolylineShape(float[] points) {
        this.points = points;
    }

    @Override
    public float[] getPoints() {
        return points;
    }
}
