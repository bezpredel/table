/**
 * Created by alex on 1/7/2016.
 */
public class TwoCirclesCollision extends Collision {
    private final Circle circleA;
    private final Circle circleB;

    public TwoCirclesCollision(double time, Circle circleA, Circle circleB) {
        super(time);
        this.circleA = circleA;
        this.circleB = circleB;
    }

    public Circle getCircleB() {
        return circleB;
    }

    public Circle getCircleA() {
        return circleA;
    }
}
