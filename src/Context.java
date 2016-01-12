/**
 * Created by alex on 1/11/2016.
 */
public class Context {
    public static final Context NO_FRICTION = new Context(
            1, 0, 0, 0
    );

    public static final Context SOME_FRICTION = new Context(
            0.95, 0.3, 0.2, 0.04
    );


    private final double speedLossOnCollision;// = 1;//0.95;// fraction of speed remaining
    private final double frictionDecelerationConstantComponent;// = 0;//0.3; // m/s/s
    private final double frictionDecelerationSpeedComponent;// = 0;//0.2; // m/s/s per m/s/s
    private final double minimumVelocity;// = 0;// 0.04;

    public Context(double speedLossOnCollision, double frictionDecelerationConstantComponent, double frictionDecelerationSpeedComponent, double minimumVelocity) {
        this.speedLossOnCollision = speedLossOnCollision;
        this.frictionDecelerationConstantComponent = frictionDecelerationConstantComponent;
        this.frictionDecelerationSpeedComponent = frictionDecelerationSpeedComponent;
        this.minimumVelocity = minimumVelocity;
    }

    public double getSpeedLossOnCollision() {
        return speedLossOnCollision;
    }

    public double getFrictionDecelerationConstantComponent() {
        return frictionDecelerationConstantComponent;
    }

    public double getFrictionDecelerationSpeedComponent() {
        return frictionDecelerationSpeedComponent;
    }

    public double getMinimumVelocity() {
        return minimumVelocity;
    }
}
