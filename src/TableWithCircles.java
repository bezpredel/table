import java.util.Arrays;
import java.util.List;

public class TableWithCircles {
    private final Table table;
    private final List<Circle> circles;

    public TableWithCircles(Table table, List<Circle> circles) {
        this.table = table;
        this.circles = circles;
    }

    public Table getTable() {
        return table;
    }

    public List<Circle> getCircles() {
        return circles;
    }

    public TableWithCirclesShadow makeShadow() {
        CircleShadow[] arr = new CircleShadow[circles.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = circles.get(i).makeShadow();

        }
        return new TableWithCirclesShadow(table, Arrays.asList(arr));
    }

    public void updateToShadow(TableWithCirclesShadow shadow) {
        List<CircleShadow> circles = shadow.getCircles();
        if (circles.size() != this.circles.size()) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < circles.size(); i++){
            this.circles.get(i).updateToShadow(circles.get(i));

        }
    }
}
