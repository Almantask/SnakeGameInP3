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
