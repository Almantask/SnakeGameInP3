import java.util.*;

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
