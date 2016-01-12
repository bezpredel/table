import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Timer;

/**
 * Created by alex on 1/8/2016.
 */
public class DumbDriver {
    public DumbDriver(final TableWithCircles tableWithCircles, final Context context) {

        final DumbModel dumbModel = new DumbModel(tableWithCircles, context);
        final Visualizer visualizer = new Visualizer();

        double currentTime = 0;
        double lastUpdateTime = 0;
        double uiUpdateInterval = 1/10;

        double timeDelta = 0.001;

        while (true) {
            dumbModel.advanceTime(timeDelta);
            currentTime += timeDelta;

            if (lastUpdateTime + uiUpdateInterval < currentTime) {
                lastUpdateTime = currentTime;
                updateUI(visualizer, currentTime, tableWithCircles);
            }
        }
    }

    private void updateUI(final Visualizer visualizer, final double currentTime, final TableWithCircles tableWithCircles) {
        try {
            SwingUtilities.invokeAndWait(
                    () -> visualizer.update(currentTime, tableWithCircles.makeShadow())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
