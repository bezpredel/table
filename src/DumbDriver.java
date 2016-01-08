import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Timer;

/**
 * Created by alex on 1/8/2016.
 */
public class DumbDriver {
    public static void main(String[] args) {
        double w = 1.5;
        int h = 3;
        Table table = new Table(w, h);

        List<Circle> cc = new ArrayList<>();
//
//        for (int i=0; i<16; i++) {
//            Circle c1 = new Circle();
//            c1.setLocation(new Point(Math.random()*w, Math.random()*h));
//            c1.setMass(Math.random() * 0.1 + 0.05);
//            c1.setRadius(Math.random() * 0.05);
//            c1.setVelocity(new Vector(2 * Math.random(), 2 * Math.random()));
//
//            cc.add(c1);
//        }

        {
            Circle c = new Circle();
            c.setLocation(new Point(0.75, 0.25));
            c.setMass(1);
            c.setRadius(0.10);
            c.setVelocity(new Vector(0.0, 0.20));

            cc.add(c);
        }

        {
            Circle c = new Circle();
            c.setLocation(new Point(0.93, 2.75));
            c.setMass(1);
            c.setRadius(0.10);
            c.setVelocity(new Vector(0.0, -0.20));

            cc.add(c);
        }


        final DumbModel dumbModel = new DumbModel(table, cc);
        final Visualizer visualizer = new Visualizer(table, cc);

        Timer timer = new Timer();

        timer.schedule(
                new TimerTask() {
                    double currentTime = 0;
                    @Override
                    public void run() {
                        double timeDelta = 0.005;
                        dumbModel.advanceTime(timeDelta);
                        currentTime += timeDelta;
                        try {
                            SwingUtilities.invokeAndWait(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            visualizer.update(currentTime);
                                        }
                                    }
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 5
        );

    }

}
