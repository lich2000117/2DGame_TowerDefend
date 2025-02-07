import bagel.Input;
import bagel.util.Point;
import java.util.List;

/**
 * A Mega slicer.
 */
public class MegaSlicer extends Enemy {

    private static final String IMAGE_FILE = Sprite.getCurPath() + "res/images/megaslicer.png";
    private static final double SPEED = 0.75;
    private static final int DAMAGE = 4;
    private static final int REWARD = 5;
    private static final int HEALTH = 30;
    private static final int KIDSNUM = 2;
    private List<Point> polyline;


    /**
     * Creates a new Mega Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public MegaSlicer(List<Point> polyline, Point spawnPoint) {
        super(polyline, spawnPoint, IMAGE_FILE, DAMAGE, SPEED, REWARD, HEALTH);
        this.polyline = polyline;
    }

    /**
     * Updates the current state of the slicer. The slicer moves towards its next target point in
     * the polyline at its specified movement rate.
     */
    @Override
    public void update(Input input) {
        super.update(input);
    }

    /**
     * Spawn enemy if died
     * @param enemies whole enemy list
     */
    @Override
    public void spawnKids(List<Enemy> enemies){
        for (int i = 0; i < KIDSNUM; i++){
            enemies.add(new SuperSlicer(polyline.subList(this.getCurrentIndex(), polyline.size()),this.getCenter()));
        }

    }
}
