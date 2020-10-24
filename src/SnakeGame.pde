import java.util.*;

public final int SQUARE_SIZE = 50;

private int initialSpeed = 10;
private int gameSpeed = initialSpeed;

private Snake snake;
private FruitSpawner fruitSpawner;
private Fruit fruit;
private ScoreBoard scoreBoard;

private boolean isGameOver;

private Map<Character, Direction> keyMapping;

void setup(){
  background(0);
  fullScreen();
  initGame();
  initControls();
}

void initGame(){
    snake = new Snake(new PVector(width/2, height/2));
    fruitSpawner = new FruitSpawner();
    fruit = fruitSpawner.spawn();
    scoreBoard = new ScoreBoard(width/32, new PVector(width/64, height/15));
}

void initControls(){
  keyMapping = new HashMap<Character, Direction>();
  keyMapping.put('a', Direction.LEFT);
  keyMapping.put('d', Direction.RIGHT);
  keyMapping.put('s', Direction.DOWN);
  keyMapping.put('w', Direction.UP);
}

void draw(){
  if(!isGameOver){
    renderGame();
  }
}

void renderGame(){
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

void determineDirection(){
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

void checkIfFruitEaten(){
  if(CollisionDetector.isColliding(snake, fruit, SQUARE_SIZE)){
    snake.eat(fruit);
    fruit = fruitSpawner.spawn();
    scoreBoard.incrementScore(fruit.getValue());
    gameSpeed++;
  }
}

void checkIfCollidingWithSelf(){
  if(snake.isCollidngWithSelf()){
    isGameOver = true;
    renderGameOver();
  }
}

void renderGameOver(){
  textSize(width/12);
  text("Game Over!", width / 2, height / 2); 
}

void checkIfBooster(){ //<>//
  if(!keyPressed) return;
  
  if(key == TAB){
    gameSpeed = initialSpeed * 10;
  }
  else{
    gameSpeed = initialSpeed;
  }
}
