package store.vxdesign.cg.core.move_polygon.utilities;

public class Coordinates2D {
    private double x;
    private double y;

    public Coordinates2D() {
        this.x = 0;
        this.y = 0;
    }

    public Coordinates2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setCoordinates(Coordinates2D coordinates) {
        this.x = coordinates.getX();
        this.y = coordinates.getY();
    }

    @Override
    public String toString() {
        return "Coordinates2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}