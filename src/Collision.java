/**
 * Created by alex on 1/7/2016.
 */
public abstract class Collision {
    private final double time;

    public Collision(double time) {
        this.time = time;
    }

    public double getCollisionTimeDelta() {
        return time;
    }

}
