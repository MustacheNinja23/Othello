import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class QuickReversi{
 private static final int NUMBER_OF_GAMES = 5;
 private static final int ROWS = 8, COLUMNS = 8;
 private static final Color COLOR_ONE = Color.BLACK;
 private static final Color COLOR_TWO = Color.WHITE;
  
 private Player playerOne;
 private Player playerTwo;
 private boolean player;
 private boolean first_player;
 private int player_one_record;
 private int player_two_record;
 private boolean game_done;
 private boolean playingMultipleGames;
 
 private Board theBoard;

     public QuickReversi(){
  first_player = true;
  game_done = false;
  playingMultipleGames = false;

  this.createGame();

    }

     private void createGame(){
  Color[][] setUp = new Color[ROWS][COLUMNS];
  //Generate starting position in the center
  setUp[ROWS/2 - 1][COLUMNS/2 - 1] = COLOR_ONE;
  setUp[ROWS/2][COLUMNS/2 -1] = COLOR_TWO;
  setUp[ROWS/2 - 1][COLUMNS/2] = COLOR_TWO;
  setUp[ROWS/2][COLUMNS/2] = COLOR_ONE;
  theBoard = new Board(setUp);

  playerOne = new WinningPlayer(COLOR_ONE,"David");
  playerTwo = new RandomPlayerSarahs(COLOR_TWO, "Sarah");
  
   if(first_player) {
   player = true;
  }
  else {
   player = false;
  }
  
  first_player = !first_player;
  
     }

    public void move(){
   Board board_copy = theBoard.getCopy();
   if(player){
    if(theBoard.hasMove(COLOR_ONE) == false){
     if(!playingMultipleGames){
      JOptionPane.showMessageDialog(null, playerOne.getName()+" has no legal move.\nPassing move to "+playerTwo.getName()+".");
     }
    }
    else{
     Point move = playerOne.getMove(theBoard);
     if(!theBoard.equals(board_copy)){
      JOptionPane.showMessageDialog(null, playerOne.getName()+" cheated by changing the board.\nGAME OVER.\n\n"
              +playerTwo.getName()+" WINS!");
      player_two_record++;
      game_done = true;
      this.createGame();
      return;     
     }
    
     boolean m = theBoard.placePiece((int) move.getX(),(int) move.getY(), COLOR_ONE);
     if(!m){
      JOptionPane.showMessageDialog(null, playerOne.getName()+" made an illegal move at "+move+".\nGAME OVER.\n\n"
              +playerTwo.getName()+" WINS!");
      player_two_record++;
      game_done = true;
      this.createGame();
      return;
     }
    }
   }
   else{
    if(theBoard.hasMove(COLOR_TWO) == false){
     if(!playingMultipleGames){
      JOptionPane.showMessageDialog(null, playerTwo.getName()+" has no legal move.\nPassing move to "+playerOne.getName()+".");
     }
    }
    else{
     Point move = playerTwo.getMove(theBoard);
     if(!theBoard.equals(board_copy)){
      JOptionPane.showMessageDialog(null, playerTwo.getName()+" cheated by changing the board.\nGAME OVER.\n\n"
              +playerOne.getName()+" WINS!");
      player_one_record++;
      game_done = true;
      this.createGame();
      return;     
     }
    
     boolean m = theBoard.placePiece((int) move.getX(),(int) move.getY(), COLOR_TWO);
     if(!m){
      JOptionPane.showMessageDialog(null, playerTwo.getName()+" made an illegal move at "+move+".\nGAME OVER.\n\n"+
              playerOne.getName()+" WINS!");
      player_one_record++;
      game_done = true;
      this.createGame();
      return;
     }
    }
   }

   if(!playingMultipleGames){
    printBoard();
   }

   player = !player;
   String first_to_move = "";
   if(first_player){
    first_to_move = playerTwo.getName();
   }
   else{
    first_to_move = playerOne.getName();

   }
   //Check for both Colors out of play
   boolean stuck = false;
   if(theBoard.hasMove(COLOR_ONE)==false && theBoard.hasMove(COLOR_TWO)==false)
    stuck = true;
   if(theBoard.isDone()) //not really needed - hasMove should catch this as well
    stuck = true;
   if(stuck){
    int one = theBoard.colorCount(COLOR_ONE);
    int two = theBoard.colorCount(COLOR_TWO);
    if(one > two){
     //JOptionPane.showMessageDialog(null, first_to_move+" went first.\n\n"+playerOne.getName()+" Wins: "+one+" to "+two);
     player_one_record++;
     game_done = true;
    }
    else if(two > one){
     //JOptionPane.showMessageDialog(null, first_to_move+" went first.\n\n"+playerTwo.getName()+" Wins: "+two+" to "+one);
     player_two_record++;
     game_done = true;
    }
    else{
     //JOptionPane.showMessageDialog(null, first_to_move+" went first.\n\n"+"It is a tie: "+two+" to "+one); 
     game_done = true;
    }
    printBoard();
    this.createGame();
   }
 }
  
 public void printBoard(){
  System.out.println();
  for(int i=0; i<theBoard.getRows(); i++){
     for(int j=0; j<theBoard.getColumns(); j++){
      if(theBoard.getState(i,j) == null)
       System.out.print("*");
      else if(theBoard.getState(i,j).equals(playerOne.getColor()))
       System.out.print("B");
      else if(theBoard.getState(i,j).equals(playerTwo.getColor()))
       System.out.print("W");
      else
       System.out.print("!");
     }  
     System.out.println();
    }
    System.out.println("END BOARD     :     White="+theBoard.colorCount(playerOne.getColor())+"     :     Black="+
     theBoard.colorCount(playerTwo.getColor()));
 }

 public void playGames()
 {
   playingMultipleGames = true;
   player_one_record = 0;
   player_two_record = 0;
   this.createGame();
   for(int i=0; i< NUMBER_OF_GAMES; i++){
    game_done = false;
    while(!game_done){
     move();
    }
    System.out.println("Record ("+playerOne.getName()+" / "+playerTwo.getName()+" / Ties) : "+player_one_record+" , "+player_two_record+" , "
     +(i+1-player_one_record-player_two_record));
   }
   JOptionPane.showMessageDialog(null,playerOne.getName()+" wins "+player_one_record+" games.\n"+
            playerTwo.getName()+" wins "+player_two_record+" games.");
   playingMultipleGames = false;
 }

    
 

 
 public static void main(String[] args){
  QuickReversi game = new QuickReversi();
  if(args[0].equals("M"))
   game.playGames();
  else{
   Scanner input = new Scanner(System.in);
   while(!input.nextLine().equals("Q"))
    game.move();
  }
 }

}
