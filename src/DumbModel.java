import java.util.List;

/**
 * Created by alex on 1/7/2016.
 */
public class DumbModel {
    private final TableWithCircles tableWithCircles;
    private final Context context;

    private final WallCollider wallCollider = new WallCollider();
    private final CircleCollider circleCollider = new CircleCollider();

    public DumbModel(TableWithCircles tableWithCircles, Context context) {
        this.tableWithCircles = tableWithCircles;
        this.context = context;
    }

    public void advanceTime(double timeIncrement) {
        while(true) {

            WallCollision nextWallCollision = null;

            List<Circle> circles = tableWithCircles.getCircles();

            for (Circle circle : circles) {
                WallCollision wallCollision = wallCollider.calculateCollisionTime(circle, tableWithCircles.getTable());

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


        System.out.println("Total kinetic energy: " + getTotalKineticEnergy());
    }

    private double getTotalKineticEnergy() {
        double retVal = 0.0;

        for (Circle circle : tableWithCircles.getCircles()) {
            retVal += circle.getKineticEnergy();
        }
        return retVal;
    }


    private void advanceAll(double dt) {
        for (Circle circle : tableWithCircles.getCircles()) {
            circle.setLocation(circle.getLocationAfterTime(dt));
            applyDeceleration(circle, dt);
            applyMinimumSpeed(circle);
        }
    }

    private void doWallCollision(WallCollision wallCollision) {
        Circle circle = wallCollision.getCircle();
        Vector currentVelocity = circle.getVelocity();
        if (wallCollision.isVerticalWall()) {
            circle.setVelocity(new Vector(-currentVelocity.getX() * context.getSpeedLossOnCollision(), currentVelocity.getY()));
        } else {
            circle.setVelocity(new Vector(currentVelocity.getX(), -currentVelocity.getY() * context.getSpeedLossOnCollision()));
        }

        applyMinimumSpeed(circle);

    }


    private void doBallCollision(TwoCirclesCollision collision) {
        System.out.println(collision.getCircleA().getName() + " with " + collision.getCircleB().getName());
        CircleCollider.TwoAdjustments adj = circleCollider.calculateCollisionCourseAdjustment(collision.getCircleA(), collision.getCircleB());

        final Vector collA = makeLessElastic(adj.newCollisionComponentA);
        final Vector collB = makeLessElastic(adj.newCollisionComponentB);


        final Vector newVelocityA = adj.orthogonalComponentA.add(collA);
        final Vector newVelocityB= adj.orthogonalComponentB.add(collB);

        collision.getCircleA().setVelocity(newVelocityA);
        collision.getCircleB().setVelocity(newVelocityB);

        applyMinimumSpeed(collision.getCircleA());
        applyMinimumSpeed(collision.getCircleB());

    }

    private Vector makeLessElastic(Vector speed) {
        return speed.multiply(context.getSpeedLossOnCollision());
    }

    private void applyDeceleration(Circle circle, double dt) {
        Vector velocity = circle.getVelocity();
        double speed = velocity.magnitude();

        double deceleration = computeFrictionSlowdown(circle);
        double speedReduction = dt * deceleration;

        final Vector newVelocity;
        if (speedReduction >= speed) {
            newVelocity = Vector.ZERO;
        } else {
            double newSpeed = speed - speedReduction;
            newVelocity = velocity.unitVector().multiply(newSpeed);

        }

        circle.setVelocity(newVelocity);

    }

    private double computeFrictionSlowdown(Circle circle) {
        double radius = circle.getRadius();
        double mass = circle.getMass();
        double velocity = circle.getVelocity().magnitude();

        double deceleration = context.getFrictionDecelerationConstantComponent() + velocity * context.getFrictionDecelerationSpeedComponent();

        return deceleration;
    }


    private void applyMinimumSpeed(Circle circle) {
        if (circle.getVelocity().magnitude() <= context.getMinimumVelocity() ) {
            circle.setVelocity(Vector.ZERO);
        }
    }
}
