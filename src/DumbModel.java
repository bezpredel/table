import java.util.List;

/**
 * Created by alex on 1/7/2016.
 */
public class DumbModel {
    private final Table table;
    private final List<Circle> circles;

    private final WallCollider wallCollider = new WallCollider();
    private final CircleCollider circleCollider = new CircleCollider();

    public DumbModel(Table table, List<Circle> circles) {
        this.table = table;
        this.circles = circles;
    }


    public void advanceTime(double timeIncrement) {
        while(true) {

            WallCollision nextWallCollision = null;

            for (Circle circle : circles) {
                WallCollision wallCollision = wallCollider.calculateCollisionTime(circle, table);

                if (nextWallCollision == null || nextWallCollision.getCollisionTimeDelta() > wallCollision.getCollisionTimeDelta()) {
                    nextWallCollision = wallCollision;
                }
            }

            TwoCirclesCollision nextTwoCirclesCollision = null;

            for (int i = 0, c = circles.size(); i < c; i++) {
                Circle circleA = circles.get(i);
                for (int j = i + 1; j < c; j++) {
                    Circle circleB = circles.get(j);

                    double collisionTime = circleCollider.calculateCollisionTime(circleA, circleB);

                    if (collisionTime < 0.00206) {
                        System.out.println();
                    }

                    if (collisionTime == Double.POSITIVE_INFINITY) {
                        continue;
                    }

                    if (nextTwoCirclesCollision == null || nextTwoCirclesCollision.getCollisionTimeDelta() > collisionTime) {
                        nextTwoCirclesCollision = new TwoCirclesCollision(collisionTime, circleA, circleB);
                    }
                }
            }

            double nextWallCollisionTime = nextWallCollision != null ? nextWallCollision.getCollisionTimeDelta() : Double.POSITIVE_INFINITY;
            double nextBallCollisionTime = nextTwoCirclesCollision != null ? nextTwoCirclesCollision.getCollisionTimeDelta() : Double.POSITIVE_INFINITY;

            if (nextWallCollisionTime == 0) {
                doWallCollision(nextWallCollision);
                continue;
            } else if (nextBallCollisionTime == 0) {
                doBallCollision(nextTwoCirclesCollision);
                continue;
            } else {
                double dt = Math.min(timeIncrement, Math.min(nextWallCollisionTime, nextBallCollisionTime));

                if (dt > 0) {
                    advanceAll(dt);
                    timeIncrement = Math.max(timeIncrement - dt, 0);
                    continue;
                } else {
                    break;
                }
            }
        }
    }

    private void advanceAll(double dt) {
        for (Circle circle : circles) {
            circle.setLocation(circle.getLocationAfterTime(dt));
        }
    }

    private void doBallCollision(TwoCirclesCollision collision) {
        circleCollider.calculateCollisionCourseAdjustment(collision.getCircleA(), collision.getCircleB());
    }

    private void doWallCollision(WallCollision wallCollision) {
        Circle circle = wallCollision.getCircle();
        Vector currentVelocity = circle.getVelocity();
        if (wallCollision.isVerticalWall()) {
            circle.setVelocity(new Vector(-currentVelocity.getX(), currentVelocity.getY()));
        } else {
            circle.setVelocity(new Vector(currentVelocity.getX(), -currentVelocity.getY()));
        }
    }
}
