import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 1/11/2016.
 */
public class M1 {
    public static void main(String[] args) {
        double w = 1.5;
        int h = 3;
        Table table = new Table(w, h);

        List<Circle> cc = new ArrayList<>();

        {
            {
                Circle c = new Circle(1, 0.50, "B");
                c.setLocation(new Point(0.75, 1));
                c.setVelocity(new Vector(0.0, 0));


                cc.add(c);
            }

            for (int i=0; i<100; i++) {
                {
                    Circle c = new Circle(0.0004, 0.01, Integer.toString(i));
                    c.setLocation(new Point(0.1, 0.1 + 0.022 * i));
                    c.setVelocity(new Vector(10.0, 0));


                    cc.add(c);
                }

            }
        }

        final TableWithCircles tableWithCircles = new TableWithCircles(table, cc);

        new DumbDriver(tableWithCircles, Context.NO_FRICTION);
    }
}
