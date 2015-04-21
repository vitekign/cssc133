package a3.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import a3.app.utilities.Services;
import a3.model.GameWorld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.Vector;

/**
 * All birds have certain properties:
 * Fixed color
 * Always fly in straight line
 * Damage caused by colliding with a bird equals to
 * half the damage of colliding with another car
 */


public class Bird extends Moveable implements IDrawable, ICollider {

    private float size;
    private GameWorld gw;
    Image imageRes;
    public static int zIndex;

    public Bird(Location location, float size, float heading, float speed, Color color, GameWorld gw){
        super(color);

        this.gw = gw;

        objectsCollidedWith = new Vector<>();

        this.X = location.getX();
        this.Y = location.getY();
        this.heading = heading;
        this.speed = speed;
        this.size = size;



        String pathToResources = Services.getPathToImgResources();
        File file = new File(pathToResources + "bird.png");

        try {
            imageRes = ImageIO.read(file);
        } catch (Exception e){
            System.out.println("The picture for Bird wasn't found");
        }

    }


    public int getZIndex(){
        return Bird.zIndex;
    }



    @Override
    public void changeColor(Color color) {

    }



    @Override
    public String toString() {
        return "Bird: " + super.toString() + ", " +
                "heading=" + (int)this.heading + "," +
                " size=" + (int)this.size;
    }




    @Override
    public void draw(Graphics g) {

        int width = (int)size;
        int length = (int)size;

        g.setColor(this.getColor());



            g.drawImage( imageRes,  (int) getX() - (int) (width / 2), (int) getY() - (int) (length / 2), 20, 20, null);

       // g.drawOval((int)getX()-(int)width/2, (int)getY()-(int)length/2,width, length);
        g.setColor(Color.black);
    }



    @Override
    public void move(int framesPerSecond){
        framesPerSecond *=3;
      float angle =  (90 - heading);
        float deltaY = (float) (Math.sin(Math.toRadians(angle))*speed * framesPerSecond);
        float deltaX = (float) (Math.cos(Math.toRadians(angle))*speed * framesPerSecond);
        Location temp = new Location(this.getLocation().getX() + deltaX,
                this.getLocation().getY() + deltaY);

        this.X = temp.getX();
        this.Y = temp.getY();

        //TODO find out how to make the change of the angle better
        if((getX() >= 1000) || (getX() < 0)){
            speed = -speed;
            this.setHeading(this.getHeading() + 90);

        }

        if((getY() >= 1000) || (getY() < 0)){
            speed = -speed;
            this.setHeading(this.getHeading() + 90);
        }
    }


    /*
     * ICollider Implementation
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


    @Override
    public void handleCollision(ICollider otherObject) {
        if(otherObject instanceof Car && !(otherObject instanceof NPCCar)){
            System.out.println("Just collided with bird");
            //gw.gameObjectsToDelete.add((GameObject)otherObject);
            if(!objectsCollidedWith.contains((GameObject)otherObject)){
                objectsCollidedWith.add((GameObject)otherObject);
                ((GameObject)otherObject).objectsCollidedWith.add(this);
            }

            gw.birdFlyOver();
        }
    }




    @Override
    public float getDistanceOfReference() {
        return size;
    }
}


















