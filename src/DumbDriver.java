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

        {
            final double r = 0.06;
            final double x = 0.6;
            final double y = 0.4;

            int cnt = 0;
            for (int row = 0; row < 5; row++) {
                double colStartPos = row * r;
                final double rowStartPos = row * 2 * r;
                for (int col = row; col < 5; col++) {
                    Circle c1 = new Circle();
                    c1.setName(Integer.toString(++cnt));
                    c1.setLocation(new Point(x + colStartPos, y + rowStartPos));
                    c1.setMass(1);
                    c1.setVelocity(Vector.ZERO);
                    c1.setRadius(r);
                    colStartPos += 2*r + 0.001;

//                    System.out.println(c1.getLocation());
                    cc.add(c1);
                }

            }
        }
//
//        for (int i=0; i<16; i++) {
//            Circle c1 = new Circle();
//            c1.setLocation(new Point(Math.random()*w, Math.random()*h));
//            c1.setMass(Math.random() * 0.1 + 0.05);
//            c1.setRadius(Math.random() * 0.01 + 0.05);
//            c1.setVelocity(new Vector(2 * Math.random(), 2 * Math.random()));
//
//            cc.add(c1);
//        }

//        {
//            Circle c = new Circle();
//            c.setLocation(new Point(0.75, 0.25));
//            c.setMass(1);
//            c.setRadius(0.10);
//            c.setVelocity(new Vector(0.0, 0.00));
//
//            cc.add(c);
//        }

        {
            Circle c = new Circle();
            c.setLocation(new Point(0.84, 2.75));
            c.setMass(1);
            c.setRadius(0.06);
            c.setVelocity(new Vector(0.0,-9));

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
