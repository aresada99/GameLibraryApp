package User;

public class Game{
	protected int game_id;
    protected  String name;
    protected  String description;
    protected  String publisher;
    protected  float price;
    protected  float score;
    protected  boolean is_multi;

    public Game(int game_id,String name,String description,String publisher,float price,float score,boolean is_multi){
        this.game_id = game_id;
        this.name=name;
        this.description=description;
        this.publisher=publisher;
        this.price=price;
        this.score=score;
        this.is_multi=is_multi;
        } 


}