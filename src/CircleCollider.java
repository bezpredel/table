/**
 * Created by alex on 1/7/2016.
 */
public class CircleCollider {

    public TwoAdjustments calculateCollisionCourseAdjustment(Circle a, Circle b) {
        final Point pos_a = a.getLocation();
        final Point pos_b = b.getLocation();
        final double distance = pos_a.distanceTo(pos_b);
        final double targetDistance = a.getRadius() + b.getRadius();
        final double distanceEpsilon = targetDistance*1e-10;

        assert lessThanOrEqual(distance, targetDistance, distanceEpsilon);

        final Vector collisionDirection = pos_a.vectorTo(pos_b);

        final Vector collisionComponentOfVelocityA = a.getVelocity().projectOnto(collisionDirection);
        final Vector collisionComponentOfVelocityB = b.getVelocity().projectOnto(collisionDirection);
//
//        final double collisionSpeedA = collisionComponentOfVelocityA.magnitude();
//        final double collisionSpeedB = collisionComponentOfVelocityB.magnitude();

        final double massA = a.getMass();
        final double massB = b.getMass();


//        final double newSpeedA = (collisionSpeedA * (massA - massB) + collisionSpeedB * 2 * massB) / (massA + massB);
//        final double newSpeedB = (collisionSpeedB * (massB - massA) + collisionSpeedA * 2 * massA) / (massA + massB);


        final Vector newCollisionComponentOfVelocityA = (collisionComponentOfVelocityA.multiply((massA - massB) / (massA + massB)).add(collisionComponentOfVelocityB.multiply((2 * massB) / (massA + massB))));
        final Vector newCollisionComponentOfVelocityB = (collisionComponentOfVelocityB.multiply((massB - massA) / (massB + massA)).add(collisionComponentOfVelocityA.multiply((2 * massA) / (massB + massA))));

        final Vector velocityAdjustmentA = newCollisionComponentOfVelocityA.subtract(collisionComponentOfVelocityA);
        final Vector velocityAdjustmentB = newCollisionComponentOfVelocityB.subtract(collisionComponentOfVelocityB);

        final Vector orthogonalComponentOfVelocityA = a.getVelocity().subtract(collisionComponentOfVelocityA);
        final Vector orthogonalComponentOfVelocityB = b.getVelocity().subtract(collisionComponentOfVelocityB);

        return new TwoAdjustments(
                velocityAdjustmentA,
                velocityAdjustmentB,
                collisionComponentOfVelocityA,
                collisionComponentOfVelocityB,
                orthogonalComponentOfVelocityA,
                orthogonalComponentOfVelocityB,
                newCollisionComponentOfVelocityA,
                newCollisionComponentOfVelocityB
        );
    }


    public double calculateCollisionTime(Circle a, Circle b) {
        if (a.getVelocity().equals(b.getVelocity())) {
            // parallel courses (or stationary), will never collide
            return Double.POSITIVE_INFINITY;
        }

        final double targetDistance = a.getRadius() + b.getRadius();

        final double current_distance = a.getLocation().distanceTo(b.getLocation());

        final double distanceEpsilon = targetDistance*1e-10;

        if (lessThanOrEqual(current_distance, targetDistance, distanceEpsilon)) {
            // we are already colliding

            if (current_distance == 0) {
                // centers are at the same point, so the objects are not moving closer to each other. Hence, no collision
                return  Double.POSITIVE_INFINITY;
            }


            // todo: calculate distance function derivative!!!!!
            // derivative of distance function:
            //            final double coef_a = sqr(delta_vx) + sqr(delta_vy);
            //            final double coef_b =  2 * delta_x * delta_vx + 2 * delta_y * delta_vy;
            //            final double coef_c = sqr(delta_x) + sqr(delta_y) - sqr(targetDistance)
            // derivative = 2 * t * coef_a + coef_b
            // So at t0, derivative is equal to 2*coef_b

            final double x_a = a.getLocation().getX();
            final double y_a = a.getLocation().getY();
            final double vx_a = a.getVelocity().getX();
            final double vy_a = a.getVelocity().getY();

            final double x_b = b.getLocation().getX();
            final double y_b = b.getLocation().getY();
            final double vx_b = b.getVelocity().getX();
            final double vy_b = b.getVelocity().getY();


            final double delta_vx = vx_a - vx_b;
            final double delta_vy = vy_a - vy_b;
            final double delta_x = x_a - x_b;
            final double delta_y = y_a - y_b;

            final double derivative_of_distance_between_balls_at_time_zero =  2 * delta_x * delta_vx + 2 * delta_y * delta_vy;

            if (derivative_of_distance_between_balls_at_time_zero <= 0) {
                // with the current velocities, the objects are getting closer. So they have already collided
                return 0;
            } else {
                // while the objects have already collided, they seem to be moving away from each other
                return Double.POSITIVE_INFINITY;
            }
        } else {
            // we are more than targetDistance away from each other. Let's see if we are on a collision course

            final double x_a = a.getLocation().getX();
            final double y_a = a.getLocation().getY();
            final double vx_a = a.getVelocity().getX();
            final double vy_a = a.getVelocity().getY();

            final double x_b = b.getLocation().getX();
            final double y_b = b.getLocation().getY();
            final double vx_b = b.getVelocity().getX();
            final double vy_b = b.getVelocity().getY();


            final double delta_vx = vx_a - vx_b;
            final double delta_vy = vy_a - vy_b;
            final double delta_x = x_a - x_b;
            final double delta_y = y_a - y_b;

            final double coef_a = sqr(delta_vx) + sqr(delta_vy);
            final double coef_b =  2 * delta_x * delta_vx + 2 * delta_y * delta_vy;
            final double coef_c = sqr(delta_x) + sqr(delta_y) - sqr(targetDistance);

            final double discriminant = discriminant(coef_a, coef_b, coef_c);

            if (discriminant < 0) {
                // never collided or will collide
                return Double.POSITIVE_INFINITY;
            } else {
                final double root1 = (-coef_b - Math.sqrt(discriminant)) / 2 / coef_a;
                final double root2 = (-coef_b + Math.sqrt(discriminant)) / 2 / coef_a;

                assert coef_a > 0 : "Its a sum or squares, so must be non-negative; velocities are not equal (see above check) so must be above 0";

                assert root1 <= root2 : "If coef_a > 0, root1 must be <= root2";

                if (root1 >= 0) {
                    // will collide

//
//                    final double timeEpsilon = targetDistance/ Math.max(a.getVelocity().magnitude(), b.getVelocity().magnitude()) * 1e-8;

//                    if (root1 < timeEpsilon) {
//                        return 0; // make sure we dont keep returning too small numbers
//                    } else {
                        return root1;
//                    }
                } else {
                    // if root1 < 0, and root2 is >0, this means that they were getting closer in the past, and now are getting further.
                    // This case is probably not possible due to (current_distance <= targetDistance) check above
                    //
                    // so we must be moving away
                    return Double.POSITIVE_INFINITY;
                }
            }


            // double new_x_a = x_a + vx_a*t;
            // double new_y_a = y_a + vy_a*t;
            // double new_x_b = x_b + vx_b*t;
            // double new_y_b = y_b + vy_b*t;


            // double foo = sqr((x_a - x_b) + t * (vx_a - vx_b)) + sqr((y_a - y_b) + t * (vy_a - vy_b)) - sqr(targetDistance);
            // double foo = sqr(x_a - x_b) + t * 2 * (x_a - x_b) * (vx_a - vx_b) + sqr(t) * sqr(vx_a - vx_b) +  sqr(y_a - y_b) + t * 2 * (y_a - y_b) * (vy_a - vy_b) + sqr(t) * sqr(vy_a - vy_b) - sqr(targetDistance);
        }
    }

    private double sqr(double a) {
        return a*a;
    }

    private double discriminant(double a, double b, double c) {
        return sqr(b) - 4 * a * c;
    }

    private boolean lessThanOrEqual(double a, double b, double epsilon) {
        return a <= b + epsilon;
    }

    public static class TwoAdjustments {
        public final Vector velocityAdjustmentA;
        public final Vector velocityAdjustmentB;

        public final Vector originalCollisionComponentA;
        public final Vector originalCollisionComponentB;

        public final Vector orthogonalComponentA;
        public final Vector orthogonalComponentB;

        public final Vector newCollisionComponentA;
        public final Vector newCollisionComponentB;


        public TwoAdjustments(Vector velocityAdjustmentA, Vector velocityAdjustmentB, Vector originalCollisionComponentA, Vector originalCollisionComponentB, Vector orthogonalComponentA, Vector orthogonalComponentB, Vector newCollisionComponentA, Vector newCollisionComponentB) {
            this.velocityAdjustmentA = velocityAdjustmentA;
            this.velocityAdjustmentB = velocityAdjustmentB;
            this.originalCollisionComponentA = originalCollisionComponentA;
            this.originalCollisionComponentB = originalCollisionComponentB;
            this.orthogonalComponentA = orthogonalComponentA;
            this.orthogonalComponentB = orthogonalComponentB;
            this.newCollisionComponentA = newCollisionComponentA;
            this.newCollisionComponentB = newCollisionComponentB;
        }
    }
}
