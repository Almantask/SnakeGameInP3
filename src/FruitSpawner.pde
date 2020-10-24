import java.util.*;

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
