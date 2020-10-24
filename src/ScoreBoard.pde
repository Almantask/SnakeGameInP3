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
