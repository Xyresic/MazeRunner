import java.util.*;
import processing.core.PApplet;
public class Monster extends Character{
    private ArrayList<Cell> path;
    private int x,y;
    private boolean chase;
    private PApplet sketch;
    private Maze maze;
    private float matrixX, matrixY;
    private Character player;
    public Monster(int x, int y, Maze maze, Character player){
        super(maze);
        this.x=x;
        this.y=y;
        this.maze=maze;
        sketch=Main.getInstance();
        chase = false;
        path = maze.solve(y,x,(int)(Math.random()*maze.getLength()),(int)(Math.random()*maze.getWidth()));
        matrixX = x*Maze.WALL_SCALE+Maze.WALL_SCALE/2;
        matrixY = y*Maze.WALL_SCALE+Maze.WALL_SCALE/2;
        this.player = player;
    }
    public void move(){
        if(chase){

        } else {
            if(path.size()!=0){
                Cell next = path.get(path.size()-1);
                float nextMatrixX = next.getY()*Maze.WALL_SCALE+Maze.WALL_SCALE/2;
                float nextMatrixY = next.getX()*Maze.WALL_SCALE+Maze.WALL_SCALE/2;
                if(matrixX==nextMatrixX && matrixY==nextMatrixY){
                    path.remove(path.size()-1);
                } else {
                    matrixX += (nextMatrixX-matrixX>0? 1:(nextMatrixX-matrixX==0? 0:-1))*Maze.WALL_SCALE/100;
                    matrixY += (nextMatrixY-matrixY>0? 1:(nextMatrixY-matrixY==0? 0:-1))*Maze.WALL_SCALE/100;
                    int[] matrixPoint = Maze.getMatrixPoint(new Point(matrixX,matrixY));
                    x = matrixPoint[0]/2;
                    y = matrixPoint[1]/2;
                }
            } else {
                path = maze.solve(y,x,(int)(Math.random()*maze.getLength()),(int)(Math.random()*maze.getWidth()));
            }
        }
    }
    public void render(){
        System.out.println(player.canSeeCharacter(player, this));
        sketch.noStroke();
        sketch.fill(160,160,160);
        sketch.ellipse(matrixX+maze.getOffsetX(),matrixY+maze.getOffsetY(),20,20);
    }
    public Point location(){
        return new Point(matrixX+maze.getOffsetX(), matrixY+maze.getOffsetY());
    }
}