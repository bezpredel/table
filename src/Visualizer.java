import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 1/7/2016.
 */
public class Visualizer {
    private final UITable uiTable;
    private final List<UICircle> uiCircles;

    private final JLabel timeLabel = new JLabel();
    private final Canvas canvas = new Canvas();

    private final JFrame frame;

    public Visualizer(Table table, List<Circle> circles) {
        uiTable = new UITable(table);
        uiCircles = new ArrayList<>(circles.size());
        for (Circle circle : circles) {
            uiCircles.add(new UICircle(circle, pickRandomColor()));
        }


        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(900, 800);
        JPanel content = new JPanel(new BorderLayout());
        frame.setContentPane(content);
        content.add(timeLabel, BorderLayout.NORTH);
        content.add(canvas, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void update(double time) {
        NumberFormat f = NumberFormat.getInstance();
        timeLabel.setText("Time: " + f.format(time));

        uiTable.update();
        for (UICircle uiCircle : uiCircles) {
            uiCircle.update();
        }

        frame.repaint();
    }

    private int _ix;
    private Color pickRandomColor() {
        Color[] c = {Color.red, Color.green, Color.blue, Color.orange, Color.black, Color.gray, Color.black, Color.cyan, Color.magenta};

        return c[(_ix++) % c.length];
    }

    private class Canvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {

            Rectangle clipBounds = g.getClipBounds();
            int xOffset = (int)clipBounds.getX();
            int yOffset = (int)clipBounds.getX();

            int margin = 4;

            double height = clipBounds.getHeight() - 2*margin;
            double width = clipBounds.getWidth() - 2*margin;

            if (height < 0 || width < 0) {
                return;
            }

            double multiplier = Math.min(width / uiTable.width, height / uiTable.height);

            int w = scale(uiTable.width, multiplier);
            int h = scale(uiTable.height, multiplier);

            g.setColor(Color.WHITE);
            g.fillRect(xOffset + margin, yOffset + margin, w, h);

            g.setColor(Color.BLACK);
            g.drawRect(xOffset + margin, yOffset + margin, w, h);

            for (UICircle circle : uiCircles) {
                g.setColor(circle.color);
                int radius = scale(circle.r, multiplier);
                int x = scale(circle.x, multiplier);
                int y = scale(circle.y, multiplier);

                g.fillOval(xOffset + margin + x - radius, yOffset + margin + y - radius, radius * 2, radius * 2);
            }

            for (UICircle circle : uiCircles) {
                if (circle.v <= 0) continue;

                g.setColor(Color.black);

                double lenMultiplier = 15/circle.v;

                int x = xOffset + margin + scale(circle.x, multiplier);
                int y = yOffset + margin + scale(circle.y, multiplier);

                g.drawLine(x, y, (int)(x + circle.vx * lenMultiplier), (int)(y + circle.vy * lenMultiplier));
            }
        }
    }

    private int scale(double var, double multiplier) {
        return (int)Math.round(var*multiplier);
    }

    private static class UITable {
        private final Table table;
        private double width;
        private double height;

        public UITable(Table table) {
            this.table = table;
        }

        public void update() {
            width = table.getWidth();
            height = table.getHeight();
        }
    }

    private static class UICircle {
        private final Circle circle;
        private final Color color;

        private double x;
        private double y;
        private double r;

        private double vx;
        private double vy;
        private double v;

        public UICircle(Circle circle, Color color) {
            this.circle = circle;
            this.color = color;
        }

        public void update() {
            x = circle.getLocation().getX();
            y = circle.getLocation().getY();
            vx = circle.getVelocity().getX();
            vy = circle.getVelocity().getY();
            v = circle.getVelocity().magnitude();

            r = circle.getRadius();
        }
    }
}
