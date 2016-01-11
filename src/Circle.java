public class Circle {
    private double mass;
    private double radius;
    private Point location;
    private Vector velocity;
    private String name;

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getDistanceTravelTime(double distance) {
        double v = velocity.magnitude();
        if (v > 0) {
            return distance/v;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    public Point getLocationAfterTime(double dt) {
        return location.shiftedBy(velocity.getX() * dt, velocity.getY() * dt);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
