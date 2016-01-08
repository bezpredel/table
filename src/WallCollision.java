/**
 * Created by alex on 1/7/2016.
 */
public class WallCollision extends Collision {
    private final boolean isVerticalWall;
    private final Circle circle;

    public WallCollision(double time, boolean isVerticalWall, Circle circle) {
        super(time);

        this.isVerticalWall = isVerticalWall;
        this.circle = circle;
    }

    public boolean isVerticalWall() {
        return isVerticalWall;
    }

    public Circle getCircle() {
        return circle;
    }
}
