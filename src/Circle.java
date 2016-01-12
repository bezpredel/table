public class Circle implements Cloneable{
    private final double mass;
    private final double radius;
    private final String name;

    private Point location;
    private Vector velocity;

    private CircleShadow validShadow;

    public Circle(double mass, double radius, String name) {
        this.mass = mass;
        this.radius = radius;
        this.name = name;
    }

    public double getMass() {
        return mass;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
        validShadow = null;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public double getKineticEnergy() {
        double speed = velocity.magnitude();
        return mass * speed * speed / 2;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
        validShadow = null;
    }

    public double getRadius() {
        return radius;
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

    public String getName() {
        return name;
    }

    public Circle clone() {
        try {
            return (Circle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public CircleShadow makeShadow() {
        if (validShadow == null) {
            validShadow = new CircleShadow(getMass(), getRadius(), getName(), getLocation(), getVelocity());
        }
        return validShadow;
    }

    public void updateToShadow(CircleShadow shadow) {
        if (validShadow != shadow) {
            validShadow = shadow;
            setVelocity(shadow.getVelocity());
            setLocation(shadow.getLocation());
        }
    }
}
