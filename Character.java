import java.util.*;
import processing.core.PApplet;
/**
* The Character class stores information about the playable character on screen.
*/
public class Character implements Renderable{
    private Point location;
    private Set<Wall> wallsAhead;
    private List<Point> intersectionPoints;
    private PApplet sketch;
    public Character(Point location, PApplet sketch){
        this.location=location;
        this.sketch=sketch;
        wallsAhead=new HashSet<Wall>();
        intersectionPoints=new ArrayList<Point>();
    }
    public void move(Point newPos){
        location=newPos;
    }
    @Override
    public void render(){
        sketch.fill(0);
        sketch.ellipse(location.getX(),location.getY(),10,10);
    }
}
