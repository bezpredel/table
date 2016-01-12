import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 1/11/2016.
 */
public class M2 {
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
                    Circle c1 = new Circle(1, r, Integer.toString(++cnt));
                    c1.setLocation(new Point(x + colStartPos, y + rowStartPos));
                    c1.setVelocity(Vector.ZERO);
                    colStartPos += 2*r + 0.001;
                    cc.add(c1);
                }

            }
        }

        {
            Circle c = new Circle(1, 0.06, "B");
            c.setLocation(new Point(0.84, 2.75));
            c.setVelocity(new Vector(0.0,-9));

            cc.add(c);
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

        final TableWithCircles tableWithCircles = new TableWithCircles(table, cc);

        new DumbDriver(tableWithCircles, Context.SOME_FRICTION);
    }
}
