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
