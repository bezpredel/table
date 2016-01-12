
public class CircleShadow {
    private final double mass;
    private final double radius;
    private final String name;

    private final Point location;
    private final Vector velocity;

    public CircleShadow(double mass, double radius, String name, Point location, Vector velocity) {
        this.mass = mass;
        this.radius = radius;
        this.name = name;
        this.location = location;
        this.velocity = velocity;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public Vector getVelocity() {
        return velocity;
    }
}
