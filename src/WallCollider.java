/**
 * Created by alex on 1/7/2016.
 */
public class WallCollider {
    public WallCollision calculateCollisionTime(Circle c, Table t) {
        Point location = c.getLocation();
        Vector velocity = c.getVelocity();
        double radius = c.getRadius();

        double timeToVerticalBoundaryCollision = calculateTimeToCollision1D(radius, location.getX(), velocity.getX(), 0, t.getWidth());
        double timeToHorizontalBoundaryCollision = calculateTimeToCollision1D(radius, location.getY(), velocity.getY(), 0, t.getHeight());

        if (timeToHorizontalBoundaryCollision < timeToVerticalBoundaryCollision) {
            return new WallCollision(timeToHorizontalBoundaryCollision, false, c);
        } else {
            // includes positive infinity, sowhat
            return new WallCollision(timeToVerticalBoundaryCollision, true, c);
        }
    }

    private double calculateTimeToCollision1D(double radius, double position, double speed, double min, double max) {
        if (speed == 0) {
            return Double.POSITIVE_INFINITY;
        } else if (speed > 0) {
            final double distanceLeftToTravel = (max - radius - position);
            if (distanceLeftToTravel <= 0 ) {
                return 0.0;
            } else {
                return distanceLeftToTravel / speed;
            }
        } else /*if (speed < 0)*/ {
            final double distanceLeftToTravel = (position - radius - min);
            if (distanceLeftToTravel <= 0 ) {
                return 0.0;
            } else {
                return distanceLeftToTravel / -speed;
            }
        }
    }
}
