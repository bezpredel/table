import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 1/7/2016.
 */
public class Visualizer {
    public static final int ARROW_LENGTH = 15;
    private TableWithCirclesShadow state;
    private Map<String, Color> _nameToColor = new HashMap<>();

    private final JLabel timeLabel = new JLabel();
    private final Canvas canvas = new Canvas();

    private final JFrame frame;

    public Visualizer() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(900, 800);
        JPanel content = new JPanel(new BorderLayout());
        frame.setContentPane(content);
        content.add(timeLabel, BorderLayout.NORTH);
        content.add(canvas, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void update(double time, TableWithCirclesShadow state) {
        NumberFormat f = NumberFormat.getInstance();
        timeLabel.setText("Time: " + f.format(time));
        this.state = state;

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
            if (state == null) {
                return;
            }

            Rectangle clipBounds = g.getClipBounds();
            int xOffset = (int)clipBounds.getX();
            int yOffset = (int)clipBounds.getX();

            int margin = 4;

            double height = clipBounds.getHeight() - 2 * margin;
            double width = clipBounds.getWidth() - 2 * margin;

            if (height < 0 || width < 0) {
                return;
            }

            final double multiplier = Math.min(width / state.getTable().getWidth(), height / state.getTable().getHeight());

            final int w = scale(state.getTable().getWidth(), multiplier);
            final int h = scale(state.getTable().getHeight(), multiplier);

            g.setColor(Color.WHITE);
            g.fillRect(xOffset + margin, yOffset + margin, w, h);

            g.setColor(Color.BLACK);
            g.drawRect(xOffset + margin, yOffset + margin, w, h);

            for (CircleShadow circle : state.getCircles()) {
                drawCircle(circle, g, multiplier, xOffset + margin, yOffset + margin);
            }
        }

        private void drawCircle(CircleShadow circle, Graphics g, double multiplier, int xMargin, int yMargin) {
            final String name = circle.getName();
            Color color = _nameToColor.get(name);
            if (color == null) {
                color = pickRandomColor();
                _nameToColor.put(name, color);
            }
            g.setColor(color);
            final int radius = scale(circle.getRadius(), multiplier);
            final int x = scale(circle.getLocation().getX(), multiplier);
            final int y = scale(circle.getLocation().getY(), multiplier);

            final int xCenter = xMargin + x;
            final int yCenter = yMargin + y;

            g.fillOval(xCenter - radius, yCenter - radius, radius * 2, radius * 2);


            final Vector velocity = circle.getVelocity();
            final double v = velocity.magnitude();
            if (v > 0) {
                double lenMultiplier = Math.max(ARROW_LENGTH, radius) / v;


                final int xArrowEnd = (int) (xCenter + velocity.getX() * lenMultiplier);
                final int yArrowEnd = (int) (yCenter + velocity.getY() * lenMultiplier);

                g.setColor(Color.white);
                g.drawLine(xCenter+1, yCenter+1, xArrowEnd+1, yArrowEnd+1);
                g.setColor(Color.black);
                g.drawLine(xCenter, yCenter, xArrowEnd, yArrowEnd);
            }
            if (name != null) {
                g.setFont(new Font("Courier New", Font.BOLD, 18));
                FontMetrics fontMetrics = g.getFontMetrics();
                final int stringWidth = fontMetrics.stringWidth(name);
                final int stringHeight = fontMetrics.getHeight();

                if (stringHeight <= radius * 2) {

                    g.setColor(Color.WHITE);
                    g.drawString(name, xCenter + 1 - stringWidth / 2, yCenter + 1 + stringHeight / 2);
                    g.setColor(Color.BLACK);
                    g.drawString(name, xCenter - stringWidth / 2, yCenter + stringHeight / 2);
                }
            }
        }
    }

    private int scale(double var, double multiplier) {
        return (int)Math.round(var*multiplier);
    }
}
