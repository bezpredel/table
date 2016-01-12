import java.util.List;

/**
 * Created by alex on 1/11/2016.
 */
public class TableWithCirclesShadow {
    private final Table table;
    private final List<CircleShadow> circles;

    public TableWithCirclesShadow(Table table, List<CircleShadow> circles) {
        this.table = table;
        this.circles = circles;
    }

    public Table getTable() {
        return table;
    }

    public List<CircleShadow> getCircles() {
        return circles;
    }
}
