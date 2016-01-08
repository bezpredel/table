/**
 * Created by alex on 1/7/2016.
 */
public final class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceTo(Point other) {
        double dx = other.getX() - this.getX();
        double dy = other.getY() - this.getY();

        return Math.sqrt(dx*dx + dy*dy);
    }

    public Vector vectorTo(Point other) {
        final double dx = other.getX() - this.getX();
        final double dy = other.getY() - this.getY();

        return new Vector(dx, dy);
    }

    public Vector unitVectorTo(Point other) {
        if (equals(other)) {
            throw new IllegalArgumentException("same point");
        }

        final double dx = other.getX() - this.getX();
        final double dy = other.getY() - this.getY();

        final double multiplier = 1/Math.sqrt(dx*dx + dy*dy);

        return new Vector(dx * multiplier, dy * multiplier);
    }

    public Point shiftedBy(double dx, double dy) {
        return new Point(x + dx, y + dy);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.x, x) != 0) return false;
        return Double.compare(point.y, y) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String toString() {
        return x +"x"+y;
    }
}
