import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;

/**
 *
 * A Buy panel that generate buying options and
 * rendering selection animation
 *
 **/

public class UpgradePanel extends Observer{

    //resource paths:
    private static final String DOLLAR = "$";
    private static final String PANEL = Sprite.getCurPath() + "res/images/upgradepanel.png";
    private static final String TANK = Sprite.getCurPath() + "res/images/tank.png";
    private static final String SUPERTANK = Sprite.getCurPath() + "res/images/supertank.png";
    private static final String FONT_PATH = Sprite.getCurPath() + "res/fonts/DejaVuSans-Bold.ttf";

    //basic information for rendering:
    private static final int TANK_PRICE = 250;
    private static final int SUPERTANK_PRICE = 600;
    private static final int AIRSUPPORT_PRICE = 500;
    private static final int INI_WIDTH = 64;
    private static final int GAP = 120;
    private static final int WALLET_Y = 65;
    private int wallet;
    private Tower selectedTower;

    //Different Font Size:
    private static final int WALLET_SIZE = 50;
    private static final int LIST_SIZE = 25;
    private static final int KEY_SIZE = 15;

    // objects needed for rendering
    private final Image panelImg;
    private final Image tankImg;
    private final Image superTankImg;

    //font information and colours
    private final Font listFont;
    private final Font moneyFont;
    private final Font keyFont;
    private final DrawOptions RED = new DrawOptions().setBlendColour(Colour.RED);
    private final DrawOptions GREEN = new DrawOptions().setBlendColour(Colour.GREEN);

    //for rendering hovering function
    private Point panelLocation;
    private Level level;
    private static boolean placing = false;



    /**
     * Construct Buy panel.
     *
     * @param player is the player currently connected to, using Observer method here
     * @param level is the current level connected to
     *
     */
    public UpgradePanel(Player player, Level level, Tower selectedTower) {
        this.panelImg = new Image(PANEL);
        this.tankImg = new Image(TANK);
        this.superTankImg = new Image(SUPERTANK);
        this.panelLocation = new Point(panelImg.getWidth()/2, panelImg.getHeight()/2);
        this.level = level;

        this.listFont = new Font(FONT_PATH,LIST_SIZE);
        this.moneyFont = new Font(FONT_PATH, WALLET_SIZE);
        this.keyFont = new Font(FONT_PATH, KEY_SIZE);
        this.wallet = player.getMoney();
        this.player = player;
        this.player.attach(this);
        this.level.attach(this);
        this.selectedTower = selectedTower;
        //this.level.setBuyPanel(this);
    }

    /**
     * Draw list of costs in this function
     * and rendering red if can afford else green.
     *
     */
    private void drawPrice(){
        //draw list of items' price:
        if (wallet >= TANK_PRICE){
            listFont.drawString(DOLLAR+ TANK_PRICE, INI_WIDTH/2, panelImg.getHeight()/2 + 40, GREEN);
        }
        else {
            listFont.drawString(DOLLAR + TANK_PRICE, INI_WIDTH/2, panelImg.getHeight()/2 + 40, RED);
        }

        if (wallet >= SUPERTANK_PRICE){
            listFont.drawString(DOLLAR+SUPERTANK_PRICE, INI_WIDTH/2 + GAP, panelImg.getHeight()/2 + 40, GREEN);
        }
        else {
            listFont.drawString(DOLLAR +SUPERTANK_PRICE, INI_WIDTH/2 + GAP, panelImg.getHeight()/2 + 40, RED);
        }

        if (wallet >= AIRSUPPORT_PRICE){
            listFont.drawString(DOLLAR+AIRSUPPORT_PRICE, INI_WIDTH/2 + GAP*2, panelImg.getHeight()/2 + 40, GREEN);
        }
        else {
            listFont.drawString(DOLLAR + Integer.toString(AIRSUPPORT_PRICE), INI_WIDTH/2 + GAP*2, panelImg.getHeight()/2 + 40, RED);
        }
    }

    /**
     * draw key binding settings
     */
    private void drawKeyBinds() {
        keyFont.drawString("Key binds: \n\nS - Start Wave\nL - Increase TimeScale\nK - Decrease TimeScale",
                panelImg.getWidth() / 2-40, 20);

    }


    /**
     * Observer functionality
     *
     */
    public void updateInfo(){
        wallet = player.getMoney();
        //if already added, do not add in repeat
    }

    /**reset buy panel with connection to new level if new level reached
     *
     * @param level reconnect to another new level class
     */
    public void reset(Level level){
        this.level = level;
        //this.level.setBuyPanel(this);
        this.placing = false;
    }


    /**
     * update method that called every frame
     *
     * @param input keyboard/mouse input
     *
     */
    protected void update(Input input) {
        //draw panel
        panelImg.draw(panelLocation.x, panelLocation.y);

        //draw object images:
        tankImg.draw(panelLocation.x, panelImg.getHeight()/4);
        superTankImg.draw(panelLocation.x, panelImg.getHeight()/2);

        //draw money in Wallet:
        moneyFont.drawString(DOLLAR+wallet, panelImg.getWidth()-200, WALLET_Y);

        //draw Price and Key binding messages
        drawPrice();
        drawKeyBinds();

        //if clicked, get selected item and drawing hovering image:
        selectItem(input);
        Point mousePos = new Point(input.getMouseX(), input.getMouseY());
        //check if current location is valid and can be placed
        if ((placing == true)&&(checkMouse(mousePos)&&(this.level.canPlace(mousePos))))  {
            //notify level class that we are currently placing
            this.level.setPlacing(placing);
        }
    }

    /**
     * a method that get current item selecting and deselecting when right button is clicked.
     *
     **/
    public void selectItem(Input input){
        if(input.wasPressed(MouseButtons.LEFT)){
            Point mousePos = new Point(input.getMouseX(), input.getMouseY());
            //check if currently selecting tank, super tank or air_support by monitoring mouse center
            if (tankImg.getBoundingBoxAt(new Point(INI_WIDTH,
                    panelImg.getHeight()/2-10)).intersects(mousePos)){
                //if have sufficient money ot purchase, then select
                if (wallet>=TANK_PRICE) {
                    this.level.setCurrSelection("tank");
                    placing = true;
                    //set image displays upon hovering
                }
            }
            else if (superTankImg.getBoundingBoxAt(new Point(INI_WIDTH+GAP,
                    panelImg.getHeight()/2-10)).intersects(mousePos)){
                if (wallet>=SUPERTANK_PRICE) {
                    this.level.setCurrSelection("supertank");
                    placing = true;
                }
            }
        }
    }

    /**
     * check if mouse position is in frame.
     *
     * @param point current mouse position
     * @return true if current point is valid.
     */
    private boolean checkMouse(Point point){
        if ((point.x<ShadowDefend.WIDTH)&&(point.y<ShadowDefend.HEIGHT)&&(point.x>=0)&&(point.y>=0)){
            return true;
        }
        return false;
    }

    /**
     * Set current placing status
     *
     * @param bool true if it's placing, false if not.
     */
    public void setPlacing(boolean bool){
        placing = bool;
    }

}
