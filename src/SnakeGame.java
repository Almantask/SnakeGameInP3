import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 
import java.util.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SnakeGame extends PApplet {



public final int SQUARE_SIZE = 50;

private int initialSpeed = 10;
private int gameSpeed = initialSpeed;

private Snake snake;
private FruitSpawner fruitSpawner;
private Fruit fruit;
private ScoreBoard scoreBoard;

private boolean isGameOver;

private Map<Character, Direction> keyMapping;

public void setup(){
  background(0);
  
  initGame();
  initControls();
}

public void initGame(){
    snake = new Snake(new PVector(width/2, height/2));
    fruitSpawner = new FruitSpawner();
    fruit = fruitSpawner.spawn();
    scoreBoard = new ScoreBoard(width/32, new PVector(width/64, height/15));
}

public void initControls(){
  keyMapping = new HashMap<Character, Direction>();
  keyMapping.put('a', Direction.LEFT);
  keyMapping.put('d', Direction.RIGHT);
  keyMapping.put('s', Direction.DOWN);
  keyMapping.put('w', Direction.UP);
}

public void draw(){
  if(!isGameOver){
    renderGame();
  }
}

public void renderGame(){
    background(0);
    frameRate(gameSpeed);
    determineDirection();
    snake.move();
    checkIfCollidingWithSelf();
    snake.draw();
    fruit.draw();
    scoreBoard.draw();
    
    checkIfFruitEaten();
    
    checkIfBooster();
}

public void determineDirection(){
  if(keyPressed){
    Character keyValue = Character.toLowerCase(key);
    if(keyMapping.containsKey(keyValue)){
      Direction nextDirection = keyMapping.get(keyValue);
      PVector nextPoint = nextDirection.getPoint();
      PVector oppositePoint = PVector.mult(nextPoint, -1);
      PVector head = snake.getDirection().getPoint();
      boolean isOpposite = oppositePoint.x == head.x && oppositePoint.y == head.y; 
      if(!isOpposite)
      {
        snake.setDirection(nextDirection);
      }
    }
  }
}

public void checkIfFruitEaten(){
  if(CollisionDetector.isColliding(snake, fruit, SQUARE_SIZE)){
    snake.eat(fruit);
    fruit = fruitSpawner.spawn();
    scoreBoard.incrementScore(fruit.getValue());
    gameSpeed++;
  }
}

public void checkIfCollidingWithSelf(){
  if(snake.isCollidngWithSelf()){
    isGameOver = true;
    renderGameOver();
  }
}

public void renderGameOver(){
  textSize(width/12);
  text("Game Over!", width / 2, height / 2); 
}

public void checkIfBooster(){ //<>//
  if(!keyPressed) return;
  
  if(key == TAB){
    gameSpeed = initialSpeed * 10;
  }
  else{
    gameSpeed = initialSpeed;
  }
}
public interface Locatable{
  public PVector getPosition();
}

public static class CollisionDetector{
  public static boolean isColliding(Locatable l1, Locatable l2, int boundSize){
    PVector point1 = l1.getPosition();
    PVector point2 = l2.getPosition();
    PVector difference = PVector.sub(point1, point2);
    
    return Math.abs(difference.x) < boundSize &&
           Math.abs(difference.y) < boundSize;
  }
}
enum Direction{
  LEFT(new PVector(-1,0)),
  RIGHT(new PVector(1,0)),
  UP(new PVector(0, -1)),
  DOWN(new PVector(0, 1));
  
  private final PVector point;
  
  private Direction(PVector point){
    this.point = point;
  }
  
  public PVector getPoint(){
    return point;
  }
}
public class Fruit implements Locatable{
  private final PVector position;
  private final int value;
  
  public Fruit(PVector position, int value){
    this.position = position;
    this.value = value;
  }
  
  public PVector getPosition(){
    return position;
  }
  
  public int getValue(){
    return value;
  }
  
  public void draw(){
    square(position.x, position.y, SQUARE_SIZE);
  }
}


public class FruitSpawner{
  private final int POINTS = 50;
  private final Random random;
  
  public FruitSpawner(){
    random = new Random();
  }
  
  public Fruit spawn(){
    int x = random.nextInt((width-SQUARE_SIZE/2) + SQUARE_SIZE / 2) / SQUARE_SIZE * SQUARE_SIZE;
    int y = random.nextInt((height-SQUARE_SIZE/2) + SQUARE_SIZE / 2) / SQUARE_SIZE * SQUARE_SIZE;
    PVector position = new PVector(x, y);
    
    return new Fruit(position, POINTS);
  }
}
public static class Modulo{
  public static float mod(float a, float b)
  {
      float c = a % b;
      if ((c < 0 && b > 0) || (c > 0 && b < 0)) {
          c += b;
      }
      return c;
  }
}
public class ScoreBoard{
  private int score;
  private final int font;
  private final PVector position;
  
  public ScoreBoard(int font, PVector position){
    this.font = font;
    this.position = position;
  }
  
  public void incrementScore(int score){
    this.score += score;
  }
  
  public void draw(){
    textSize(font);   
    text(String.valueOf(score), position.x, position.y);
  }
}


class Snake implements Locatable {
  private final LinkedList<PVector> body;
  private Direction direction = Direction.RIGHT;

  public Snake(PVector position) {
    body = new LinkedList<PVector>();
    body.add(position);
  }
  
  public PVector getPosition() {
    return body.getLast();
  }

  public void eat(Fruit fruit) {
    body.addLast(fruit.getPosition());
  }

  public void move() {
    PVector head;
    if(body.size() > 1){
      body.removeFirst();
      head = getPosition();
    }
    else{
      head = body.removeFirst();
    }
    
    PVector shift = PVector.mult(direction.getPoint(), SQUARE_SIZE);
    // Check for out of bounds as well
    PVector nextHead = nextBoundedPosition(head, shift);
    body.addLast(nextHead);
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }
  
  public Direction getDirection(){
    return direction;
  }

  public void draw() {
    for (PVector p : body) {
      square(p.x, p.y, SQUARE_SIZE);
    }
  }
  
  public boolean isCollidngWithSelf(){
    for(int i = 0; i < body.size() - 1; i++){
      PVector p = body.get(i);
      PVector head = getPosition();
      if(p.x == head.x && p.y == head.y){
        return true;
      }
    }
    
    return false;
  }

  private PVector nextBoundedPosition(PVector original, PVector shift){
    return new PVector(Modulo.mod(original.x + shift.x, width), Modulo.mod((original.y + shift.y), height));
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SnakeGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
