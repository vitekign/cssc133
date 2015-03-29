package a3.objects;

import java.awt.*;

/**
 * Created by Victor Ignatenkov on 3/26/15.
 */
public interface ISelectable {

    // a way to mark an object as "selected" or not
    public void setSelected(boolean yesNo);

    // a way to test whether an object is selected
    public boolean isSelected();

    //a way to determine if a mouse point is "in" an object
    public boolean contains(Point p);

    //a way to "draw" the object that knows about drawing
    //different ways depending on "isSelected"
    public void draw(Graphics g);

}