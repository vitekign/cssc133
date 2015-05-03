package a4.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import a4.model.GameWorld;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Vector;

/**
 * Pylon class.
 *
 * Not allowed to change color once they're created
 */
public class Pylon extends Fixed implements IDrawable, ICollider, ISelectable {

    private float radius;
    final private int sequenceNumber;
    static private int count = 1;

    private GameWorld gw;

    private boolean isSelected;

    public static int zIndex;

    public void initObject(){
        objectsCollidedWith = new Vector<>();

        myRotationMatrix = new AffineTransform();
        myTranslationMatrix = new AffineTransform();
        myScaleMatrix = new AffineTransform();

    }

    public Pylon(Location location, float radius, Color color, GameWorld gw){
            super(color);
            this.gw = gw;



            this.X = location.getX();
            this.Y = location.getY();
            this.radius = radius;

        sequenceNumber = count++;

        initObject();
    }

    public Pylon(Location location, float radius, Color color, GameWorld gw, int seqNumberOfPylon){
        super(color);

        this.gw = gw;


        this.X = location.getX();
        this.Y = location.getY();
        this.radius = radius;

        sequenceNumber = seqNumberOfPylon;
                count++;

        initObject();

    }

    public int getZIndex(){
        return Pylon.zIndex;
    }

    public static int getzIndex() {
        return zIndex;
    }

    public static void setzIndex(int zIndex) {
        Pylon.zIndex = zIndex;
    }

    @Override
    public String toString() {
        return "Pylon " +
                super.toString() +
                " radius " + (int)this.radius +
                " seqNum " + (int)this.sequenceNumber;
    }

    /**
     *
     * @return
     * current index number
     */
    public int getIndexNumber() {
        return sequenceNumber;
    }

    /**
     * Set the first index number for Pylon Class
     * @param numberOfTheFirstPylon
     * index number
     */
    public static void resetSequenceGeneratorTo(int numberOfTheFirstPylon) {
        count = numberOfTheFirstPylon;
    }

    /**
     * Pylons don't have the ability to have their
     * color changed after creation.
     * @param color
     */
    @Override
    public void changeColor(Color color) {

    }

    /**
     *
     * @return
     * number of pylons created + 1
     */
    public static int getCount(){
        return count - 1;
    }


    /****************************************/
    /* Confirms to ISelectable              */
    /****************************************/
    @Override
    public void setSelected(boolean yesNo) {
        this.isSelected = yesNo;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public boolean contains(Point2D p) {
        int px = (int) p.getX();
        int py = (int) p.getY();
        int xLoc = (int)getX();
        int yLoc = (int)getY();

        if((px >= xLoc - (this.getDistanceOfReference()/2)) && (px <= xLoc + (this.getDistanceOfReference()/2))
                && (py >= yLoc - (this.getDistanceOfReference()/2))&& (py <= yLoc + (this.getDistanceOfReference()/2)))
            return true;
        else
            return false;
    }
    /****************************************/




    @Override
    public void draw(Graphics2D g2d) {

        AffineTransform saveAt = g2d.getTransform();
        myTranslationMatrix.translate((int) getX() - (int) radius / 2, (int) getY() - (int) radius / 2);
        g2d.transform(myTranslationMatrix);


    if(isSelected){
        g2d.setColor(Color.gray);
        g2d.fillOval(0, 0, (int) radius, (int) radius);
        g2d.setColor(Color.white);
        g2d.fillOval(0, 0, 1, 1);
        g2d.setColor(Color.white);
        g2d.drawString(String.valueOf(getIndexNumber()), (int) getX(), (int) getY());
        g2d.setColor(Color.black);


    } else {
        g2d.setColor(new Color(14, 40, 3));
        g2d.fillOval(0, 0, (int) radius, (int) radius);
        g2d.setColor(Color.white);
        g2d.fillOval(0, 0, 1, 1);
        g2d.setColor(Color.white);



        AffineTransform addTranslate = new AffineTransform();
        addTranslate.translate(20,20);

       // myTranslationMatrix.concatenate(addTranslate);
        myScaleMatrix.scale(1, -1);
        g2d.transform(addTranslate);
        g2d.transform(myScaleMatrix);

        g2d.drawString(String.valueOf(getIndexNumber()), 0, 0);
        g2d.setColor(Color.black);
    }
        setToIdentity();
        g2d.setTransform(saveAt);
}



    /**
     * The logic which detects the collision
     * with other object in the Game World
     * @param obj the other object
     * @return true if collision has happened
     */
    @Override
    public boolean collidesWith(ICollider obj) {
        float distX = this.getX() - ((GameObject)obj).getX();
        float distY = this.getY() - ((GameObject)obj).getY();
        float distanceBtwnCenters = (float) Math.sqrt(distX * distX + distY * distY);

        if((this.getDistanceOfReference() + obj.getDistanceOfReference() >
                distanceBtwnCenters)){
            return true;
        } else {
            return false;
        }
    }

    /* Handle collision of the Pylon with other Game Objects.
       * @param otherObject
    */
    @Override
    public void handleCollision(ICollider otherObject) {
        if(otherObject instanceof Car && !(otherObject instanceof NPCCar)){
            if(!objectsCollidedWith.contains((GameObject)otherObject)){
                objectsCollidedWith.add((GameObject)otherObject);

            }
        }
    }

    @Override
    public float getDistanceOfReference() {
        return radius;
    }


}
