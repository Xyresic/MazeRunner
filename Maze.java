import processing.core.PApplet;
import java.util.ArrayList;
import java.util.Collections;
public class Maze implements Renderable{
    private Wall[][] walls;
    private boolean[][] maze;
    private int length;
    private int width;
    private PApplet sketch;
    public Maze(int rows, int cols, PApplet sketch){
        length = rows;
        width = cols;
        this.sketch = sketch;
        walls = new Wall[2*rows-1][];
        maze = new boolean[2*length-1][2*width-1];
        fillWalls(length,width);
        generate(0,0);
        convertToBool();
    }
    public void fillWalls(int rows, int cols){
        float rowWidth = 4f*sketch.height/5/rows;
        float colWidth = 4f*sketch.width/5/cols;
        for(int i = 0; i<walls.length; i++){
            if(i%2==0){
                walls[i] = new Wall[cols-1];

            } else {
                walls[i] = new Wall[cols];
            }
            for(int j = 0; j<cols-(i%2==0? 1:0); j++){
                float startX,startY,endX,endY;
                if(i%2==0){
                    startX = sketch.width/10f+(j+1)*colWidth;
                    startY = sketch.height/10f+(i/2)*rowWidth+1;
                    endX = startX;
                    endY = sketch.height/10f+(i/2+1)*rowWidth-1;
                } else {
                    startX = sketch.width/10f+j*colWidth+1;
                    startY = sketch.height/10f+(i/2+1)*rowWidth;
                    endX = sketch.width/10f+(j+1)*colWidth-1;
                    endY = startY;
                }
                Point start = new Point(startX,startY);
                Point end = new Point(endX,endY);
                walls[i][j] = new Wall(start,end);
            }
        }
    }
    public void generate(int row, int col){
        ArrayList<Integer> adjacentWalls = new ArrayList<Integer>();
        if(col!=0 && walls[2*row][col-1]!=null){
            adjacentWalls.add(0); //left
        }
        if(col!=width-1 && walls[2*row][col]!=null){
            adjacentWalls.add(1); //right
        }
        if(row!=0 && walls[2*row-1][col]!=null){
            adjacentWalls.add(2); //up
        }
        if(row!=length-1 && walls[2*row+1][col]!=null){
            adjacentWalls.add(3); //down
        }
        Collections.shuffle(adjacentWalls);
        for(Integer direction : adjacentWalls){
            if(direction==0 && unvisited(row,col-1)){
                walls[2*row][col-1]=null;
                generate(row,col-1);
            }
            if(direction==1 && unvisited(row,col+1)){
                walls[2*row][col]=null;
                generate(row,col+1);
            }
            if(direction==2 && unvisited(row-1,col)){
                walls[2*row-1][col]=null;
                generate(row-1,col);
            }
            if(direction==3 && unvisited(row+1,col)){
                walls[2*row+1][col]=null;
                generate(row+1,col);
            }
        }
    }
    private boolean unvisited(int row, int col){
        if(col!=0 && walls[2*row][col-1]==null){
            return false;
        }
        if(col!=width-1 && walls[2*row][col]==null){
            return false;
        }
        if(row!=0 && walls[2*row-1][col]==null){
            return false;
        }
        if(row!=length-1 && walls[2*row+1][col]==null){
            return false;
        }
        return true;
    }
    private void convertToBool(){
        for(int i = 0; i < walls.length; i++){
            for(int j = 0; j < walls[i].length; j++){
                if(walls[i][j]!=null){
                    if(i%2==0){
                        maze[i][j*2+1]=true;
                    } else {
                        maze[i][j*2]=true;
                        if(j<length-1 && walls[i-1][j]!=null){
                            maze[i][j*2+1]=true;
                        }
                        if(j>0 && walls[i-1][j-1]!=null){
                            maze[i][j*2-1]=true;
                        }
                    }
                }
            }
        }
    }
    public ArrayList<Wall> convertToList(){
        ArrayList<Wall> tempMaze = new ArrayList<Wall>();
        for(int i = 0; i < walls.length; i++){
            for(int j = 0; j < walls[i].length; j++){
                if(walls[i][j]!=null){
                    Point start = walls[i][j].getStart();
                    Point end = walls[i][j].getEnd();
                    if(i%2==0){
                        int index = i;
                        while(index < walls.length && walls[index][j]!=null){
                            end = walls[index][j].getEnd();
                            walls[index][j]=null;
                            index+=2;
                        }
                    } else {
                        int index = j;
                        while(index < walls[i].length && walls[i][index]!=null){
                            end = walls[i][index].getEnd();
                            walls[i][index]=null;
                            index++;
                        }
                    }
                    tempMaze.add(new Wall(start,end));
                }
            }
        }
        return tempMaze;
    }
    public boolean[][] getBool(){
        return maze;
    }
    public int getLength(){
        return length;
    }
    public int getWidth(){
        return width;
    }
    @Override
    public void render(){
        sketch.line(sketch.width/10,sketch.height/10,9*sketch.width/10,sketch.height/10);
        sketch.line(sketch.width/10,sketch.height/10,sketch.width/10,9*sketch.height/10);
        sketch.line(9*sketch.width/10,sketch.height/10,9*sketch.width/10,9*sketch.height/10);
        sketch.line(sketch.width/10,9*sketch.height/10,9*sketch.width/10,9*sketch.height/10);
        for(Wall[] row : walls){
            for(Wall wall : row){
                if(wall!=null){
                    wall.render();
                }
            }
        }
    }
}
