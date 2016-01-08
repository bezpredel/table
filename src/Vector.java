/**
 * Created by alex on 1/7/2016.
 */
public class Vector {
    private final double x;
    private final double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public boolean isZero() {
        return x==0 && y==0;
    }

    public Vector unitVector() {
        final double magnitude = magnitude();
        if (magnitude == 0) {
            throw new IllegalArgumentException("zero vector");
        }

        final double multiplier = 1 / magnitude;

        return multiply(multiplier);
    }

    public Vector projectOnto(Vector direction) {
        final double magnitude = direction.magnitude();
        if (magnitude == 0) {
            throw new IllegalArgumentException("zero vector");
        }

        double multiplier = (x * direction.getX() + y * direction.getY()) / (direction.getX()*direction.getX() + direction.getY()*direction.getY());




        return newInstance(multiplier * direction.getX(), multiplier*direction.getY());
    }

    public Vector multiply(double scale) {
        return newInstance(x * scale, y * scale);
    }

    public Vector subtract(Vector vector) {
        return newInstance(x - vector.getX(), y - vector.getY());
    }

    public Vector add(Vector vector) {
        return newInstance(x + vector.getX(), y + vector.getY());
    }

    protected Vector newInstance(double newX, double newY) {
        return new Vector(newX, newY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector velocity = (Vector) o;

        if (Double.compare(velocity.x, x) != 0) return false;
        return Double.compare(velocity.y, y) == 0;
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
}
