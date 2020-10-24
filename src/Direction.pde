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
