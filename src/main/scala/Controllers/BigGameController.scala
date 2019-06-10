package Controllers

import MainSystem.MainApp

import scalafxml.core.macros.sfxml
import scalafx.scene.control._

import javafx.scene.layout.BackgroundImage
import javafx.scene.layout.BackgroundRepeat
import javafx.scene.layout.BackgroundPosition
import javafx.scene.layout.BackgroundSize
import javafx.scene.layout.Background
import scalafx.scene.image.Image

import scalafx.scene.control.Button
import scalafx.scene.image.ImageView
import scalafx.scene.text.Text
import scalafx.scene.layout.AnchorPane
import scalafx.scene.control.TextField

import javafx.event.EventHandler
import javafx.event.ActionEvent
import javafx.scene.input.MouseEvent
import scalafx.scene.Cursor
import scalafx.scene.ImageCursor
import scalafx.scene.text.Font
import scalafx.scene.text.FontWeight

import javafx.scene.paint.Paint

import scalafx.application.Platform

//animation class
import scalafx.animation.TranslateTransition
import scalafx.util.Duration


@sfxml
class BigGameController(
	val title: ImageView,
	val player1Icon: ImageView,
	val player2Icon: ImageView,
	val gameStatus: Text,
	val myGameBoard: AnchorPane,
	val instruction: ImageView,
	val quitGameButton: Button,
	val hole0: TextField,
	val hole1: TextField,
	val hole2: TextField,
	val hole3: TextField,
	val hole4: TextField,
	val hole5: TextField,
	val hole6: TextField,
	val hole7: TextField,
	val hole8: TextField,
	val hole9: TextField,
	val hole10: TextField,
	val hole11: TextField,
	val hole12: TextField,
	val hole13: TextField,
	val hole14: TextField,
	val hole15: TextField,
	val hole16: TextField,
	val hole17: TextField,
	val player1Score: TextField,
	val player2Score: TextField,
	val hand: TextField
)
{
	val normalBackgroundHoleCSS: String = "-fx-background-color: #E3AF7D;-fx-border-radius: 25;-fx-border-width:5;-fx-background-radius:25;-fx-border-color: black;-fx-font-family: Arial;-fx-font-weight: bold;-fx-font-size: 15;-fx-alignment: center"
	val greenBackgroundHoleCSS: String = "-fx-background-color: #00FF48;-fx-border-radius: 25;-fx-border-width:5;-fx-background-radius:25;-fx-border-color: black;-fx-font-family: Arial;-fx-font-weight: bold;-fx-font-size: 15;-fx-alignment: center"
	val redBackgroundHoleCSS: String = "-fx-background-color: #FC4522;-fx-border-radius: 25;-fx-border-width:5;-fx-background-radius:25;-fx-border-color: black;-fx-font-family: Arial;-fx-font-weight: bold;-fx-font-size: 15;-fx-alignment: center"
	val yellowBackgroundHoleCSS: String = "-fx-background-color: yellow;-fx-border-radius: 25;-fx-border-width:5;-fx-background-radius:25;-fx-border-color: black;-fx-font-family: Arial;-fx-font-weight: bold;-fx-font-size: 15;-fx-alignment: center"

	//set value to 'Player1' if it is player 1 turn
	//set value to 'Player2' if it is player 2 turn
	var currentPlayer: String = "Player1"

	var canChooseHole: Boolean= true



	//set title image
	title.setImage(new Image(getClass.getResourceAsStream("/Images/Game/MancalaGameTitle.png")));

	//set Player1 Icon
	player1Icon.setImage(new Image(getClass.getResourceAsStream("/Images/Game/PlayerIcon.png")));

	//set Player 2 Icon
	if(MainApp.selectedEnemyMode == "AI"){
		//if it is AI then put robot icon
		player2Icon.setImage(new Image(getClass.getResourceAsStream("/Images/Game/BotIcon.png")));
		
	}else{
		//if it is human then put human icon
		player2Icon.setImage(new Image(getClass.getResourceAsStream("/Images/Game/PlayerIcon.png")));
	}

	gameStatus.setText("Player 1 Turns!");

	//set Game board Anchor Pane background Image
	var myBackgroundImage= new BackgroundImage(new Image(getClass.getResourceAsStream("/Images/Game/EmptyMancalaBoard.png")),
        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
          new BackgroundSize(100, 100, true, true, true, true));


	//set background image to anchorpane
	myGameBoard.setBackground(new Background(myBackgroundImage));

	//set instruction image
	instruction.setImage(new Image(getClass.getResourceAsStream("/Images/Game/Instruction.png")));

	//set BackButton image
	quitGameButton.setGraphic(new ImageView(new Image(getClass.getResourceAsStream("/Images/Game/QuitGameButton.png"))));

	//set Game board Anchor Pane background Image
	var handBackground= new BackgroundImage(new Image(getClass.getResourceAsStream("/Images/Game/hand.png")),
        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
          new BackgroundSize(100, 100, true, true, true, true));

	//set background image to hand
	hand.setBackground(new Background(handBackground));


	//initialize all hole hover effect
	setHoleHoverEffect()
	
	initializeHoleOnAction()



	def changePlayer() = {
		if(currentPlayer == "Player1"){
			currentPlayer = "Player2"
			gameStatus.setText("Player 2 Turn!")

			//if player 2 is AI call AI to make the move
			if(MainApp.selectedEnemyMode == "AI"){
				AICalculateBestMove()
			}



		}else{
			currentPlayer = "Player1"
			gameStatus.setText("Player 1 Turn!")

		}
	}

	//chooseHole() will be called when player press hole
	def chooseHole(holeIndex: Int): Unit = {		
		
		//run in thread so that it will not hang the User Interface
		val myThread = new Thread {
		    override def run {
		    	//set canChooseHole to false so that we can block input before Algorithm finish
				canChooseHole = false

				//this is the hole that is chosen
				var holeChosenObject: TextField = null

				// get object reference according to holeIndex
				holeIndex match {
				    case 0  => holeChosenObject = hole0
				    case 1  => holeChosenObject = hole1
				    case 2  => holeChosenObject = hole2
				    case 3  => holeChosenObject = hole3
				    case 4  => holeChosenObject = hole4
				    case 5  => holeChosenObject = hole5
				    case 6  => holeChosenObject = hole6
				    case 7  => holeChosenObject = hole7
				    case 8  => holeChosenObject = hole8
				    case 9  => holeChosenObject = hole9
				    case 10  => holeChosenObject = hole10
				    case 11  => holeChosenObject = hole11
				    case 12  => holeChosenObject = hole12
				    case 13  => holeChosenObject = hole13
				    case 14  => holeChosenObject = hole14
				    case 15  => holeChosenObject = hole15
				    case 16  => holeChosenObject = hole16
				    case 17  => holeChosenObject = hole17
				}

				//amount of seed in the Hole
				var amountInHole: Int = holeChosenObject.getText().toInt 

				
				//***testing Animation***
				for( x <- 0 to 17){
					//setHoleBackgroundToYellow(holeIndex) HoleIndex is which hole to be grab from
		        	setHoleBackgroundToYellow(x)

		        	//minusOneAtHoleAnimation(holeIndex) HoleIndex is which hole to be grab from
		        	minusOneAtHoleAnimation(x)
		        	//minusOneAtHoleAnimation need 500 miliseconds to do animation we wait for it to done
		       		Thread.sleep(500)

		       		//setHoleBackgroundToNormal(holeIndex) HoleIndex is which hole to be grab from
		       		setHoleBackgroundToNormal(x)
		        }		 

		        for( x <- 0 to 17){
		        	setHoleBackgroundToYellow(x)
		        	addOneAtHoleAnimation(x)
		       		Thread.sleep(500)
		       		setHoleBackgroundToNormal(x)
		        }		     

		        //grabFromHoleAndAddScoreToCurrentPlayerAnimation(HoleIndex) HoleIndex is which hole to be grab from
		        grabFromHoleAndAddScoreToCurrentPlayerAnimation(1)
		        //grabFromHoleAndAddScoreToCurrentPlayerAnimation need 1500 miliseconds to do animation we wait for animation to finish
		        Thread.sleep(1500)

		        //grabFromHoleAndAddToHandAnimation(HoleIndex) HoleIndex is which hole to be grab from
		        grabFromHoleAndAddToHandAnimation(1)
		        //grabFromHoleAndAddToHandAnimation need 1500 miliseconds to do animation we wait for animation to finish
		        Thread.sleep(1500)

		        //***end testing animation***

		    

		        /*Do some Algorithm*/
		        

		        
		        
		       	
		    
		        
		        /*Finish Algorithm*/

				/*Set canChooseHole to true so that next player can start playing */
				canChooseHole = true

				//change to another player
				changePlayer()
		    }
		}

		//thread will be close when main program terminate
		myThread.setDaemon(true);
		
		myThread.start()
		
	}

	def AICalculateBestMove() = {
		/*calculate best move*/



		/*end calculate*/

		//choose bestmove 
		//i randomly put to choose hole 7 
		chooseHole(7)

	}

	//call this method when game is end
	//parameter:
	//player value to 1 if player 1 wins || player value set to 2 if player 2 wins || player value set to 0 if no one wins(Tie)
	//player1Score is the score of player1
	//player2Score is the score of player2
	def setWinner(player:Int, player1Score: Int, player2Score: Int): Unit = {

		//redirect to Winner Screen to display the winner
		MainApp.goToWinnerScreen(player,player1Score,player2Score)
	}

	
	

	def setHoleHoverEffect() = {
		//set hole hover in 
		hole0.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player1"){
				//if it is Player 1 means hole 0 to hole 6 can be choose
				//set cursor hand
			  	hole0.setCursor(Cursor.HAND)
			  	//set background green
			  	hole0.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 1 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole0.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole0.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole0.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole0.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole1.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player1"){
				//if it is Player 1 means hole 0 to hole 6 can be choose
				//set cursor hand
			  	hole1.setCursor(Cursor.HAND)
			  	//set background green
			  	hole1.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 1 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole1.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole1.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole1.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole1.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole2.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player1"){
				//if it is Player 1 means hole 0 to hole 6 can be choose
				//set cursor hand
			  	hole2.setCursor(Cursor.HAND)
			  	//set background green
			  	hole2.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 1 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole2.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole2.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole2.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole2.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole3.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player1"){
				//if it is Player 1 means hole 0 to hole 6 can be choose
				//set cursor hand
			  	hole3.setCursor(Cursor.HAND)
			  	//set background green
			  	hole3.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 1 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole3.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole3.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole3.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole3.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole3.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player1"){
				//if it is Player 1 means hole 0 to hole 6 can be choose
				//set cursor hand
			  	hole3.setCursor(Cursor.HAND)
			  	//set background green
			  	hole3.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 1 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole3.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole3.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole3.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole3.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole4.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player1"){
				//if it is Player 1 means hole 0 to hole 6 can be choose
				//set cursor hand
			  	hole4.setCursor(Cursor.HAND)
			  	//set background green
			  	hole4.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 1 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole4.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole4.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole4.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole4.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole5.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player1"){
				//if it is Player 1 means hole 0 to hole 6 can be choose
				//set cursor hand
			  	hole5.setCursor(Cursor.HAND)
			  	//set background green
			  	hole5.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 1 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole5.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole5.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole5.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole5.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole6.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player1"){
				//if it is Player 1 means hole 0 to hole 6 can be choose
				//set cursor hand
			  	hole6.setCursor(Cursor.HAND)
			  	//set background green
			  	hole6.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 1 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole6.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole6.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole6.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole6.setStyle(normalBackgroundHoleCSS)
		}

		//-----------------------------Hole 7 to 13--------------------------------------------------------------------
		//set hole hover in 
		hole7.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player1"){
				//if it is Player 2 means hole 7 to hole 13 can be choose
				//set cursor hand
			  	hole7.setCursor(Cursor.HAND)
			  	//set background green
			  	hole7.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 2 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole7.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole7.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole7.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole7.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole8.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player1"){
				//if it is Player 2 means hole 7 to hole 13 can be choose
				//set cursor hand
			  	hole8.setCursor(Cursor.HAND)
			  	//set background green
			  	hole8.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 2 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole8.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole8.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole8.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole8.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole9.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player2"){
				//if it is Player 2 means hole 7 to hole 13 can be choose
				//set cursor hand
			  	hole9.setCursor(Cursor.HAND)
			  	//set background green
			  	hole9.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 2 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole9.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole9.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole9.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole9.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole10.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player2"){
				//if it is Player 2 means hole 7 to hole 13 can be choose
				//set cursor hand
			  	hole10.setCursor(Cursor.HAND)
			  	//set background green
			  	hole10.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 2 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole10.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole10.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole10.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole10.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole11.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player2"){
				//if it is Player 2 means hole 7 to hole 13 can be choose
				//set cursor hand
			  	hole11.setCursor(Cursor.HAND)
			  	//set background green
			  	hole11.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 2 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole11.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole11.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole11.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole11.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole12.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player2"){
				//if it is Player 2 means hole 7 to hole 13 can be choose
				//set cursor hand
			  	hole12.setCursor(Cursor.HAND)
			  	//set background green
			  	hole12.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 2 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole12.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole12.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole12.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole12.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole13.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player2"){
				//if it is Player 2 means hole 7 to hole 13 can be choose
				//set cursor hand
			  	hole13.setCursor(Cursor.HAND)
			  	//set background green
			  	hole13.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 2 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole13.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole13.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole13.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole13.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole14.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player2"){
				//if it is Player 2 means hole 7 to hole 13 can be choose
				//set cursor hand
			  	hole14.setCursor(Cursor.HAND)
			  	//set background green
			  	hole14.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 2 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole14.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole14.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole14.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole14.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole15.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player2"){
				//if it is Player 2 means hole 7 to hole 13 can be choose
				//set cursor hand
			  	hole15.setCursor(Cursor.HAND)
			  	//set background green
			  	hole15.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 2 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole15.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole15.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole15.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole15.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole16.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player2"){
				//if it is Player 2 means hole 7 to hole 13 can be choose
				//set cursor hand
			  	hole16.setCursor(Cursor.HAND)
			  	//set background green
			  	hole16.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 2 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole16.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole16.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole16.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole16.setStyle(normalBackgroundHoleCSS)
		}

		//set hole hover in 
		hole17.onMouseEntered = (event: MouseEvent) =>  {
		
			if(canChooseHole == true && currentPlayer == "Player2"){
				//if it is Player 2 means hole 7 to hole 13 can be choose
				//set cursor hand
			  	hole17.setCursor(Cursor.HAND)
			  	//set background green
			  	hole17.setStyle(greenBackgroundHoleCSS)

			}else{
				//if it is not Player 2 means hole cannot be choose
				//show a Cursor that indicate cannot choose
			  	hole17.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole17.setStyle(redBackgroundHoleCSS)
			}
		}

		//set hole hover out
		hole17.onMouseExited = (event: MouseEvent) =>  {
			//change hole background color to normal
			hole17.setStyle(normalBackgroundHoleCSS)
		}
	}

	def initializeHoleOnAction() = {

		hole0.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player1"){ 
				chooseHole(0)
				//show a Cursor that indicate cannot choose
			  	hole0.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole0.setStyle(redBackgroundHoleCSS) 
			} 
		}
		hole1.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player1"){ 
				chooseHole(1)
				//show a Cursor that indicate cannot choose
			  	hole1.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole1.setStyle(redBackgroundHoleCSS) 
			} 
		}
		hole2.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player1"){ 
				chooseHole(2)
				//show a Cursor that indicate cannot choose
			  	hole2.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole2.setStyle(redBackgroundHoleCSS) 
			} 
		}
		hole3.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player1"){ 
				chooseHole(3)
				//show a Cursor that indicate cannot choose
			  	hole3.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole3.setStyle(redBackgroundHoleCSS) 
			} 
		}
		hole4.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player1"){ 
				chooseHole(4)
				//show a Cursor that indicate cannot choose
			  	hole4.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole4.setStyle(redBackgroundHoleCSS) 
			} 
		}
		hole5.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player1"){ 
				chooseHole(5)
				//show a Cursor that indicate cannot choose
			  	hole5.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole5.setStyle(redBackgroundHoleCSS) 
			} 
		}
		hole6.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player1"){ 
				chooseHole(6)
				//show a Cursor that indicate cannot choose
			  	hole6.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole6.setStyle(redBackgroundHoleCSS) 
			} 
		}

		hole7.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player1"){ 
				chooseHole(7)
				//show a Cursor that indicate cannot choose
			  	hole7.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole7.setStyle(redBackgroundHoleCSS) 
			} 
		}
		hole8.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player1"){ 
				chooseHole(8)
				//show a Cursor that indicate cannot choose
			  	hole8.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole8.setStyle(redBackgroundHoleCSS) 
			} 
		}

		/*----------------------------Hole 9 to Hole 17----------------------------------------------------*/
		
		hole9.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player2"){ 
				chooseHole(9)
				//show a Cursor that indicate cannot choose
			  	hole9.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole9.setStyle(redBackgroundHoleCSS) 
			} 
		}
		hole10.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player2"){ 
				chooseHole(10)
				//show a Cursor that indicate cannot choose
			  	hole10.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole10.setStyle(redBackgroundHoleCSS) 
			} 
		}
		hole11.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player2"){ 
				chooseHole(11)
				//show a Cursor that indicate cannot choose
			  	hole11.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole11.setStyle(redBackgroundHoleCSS) 
			} 
		}
		hole12.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player2"){ 
				chooseHole(12)
				//show a Cursor that indicate cannot choose
			  	hole12.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole12.setStyle(redBackgroundHoleCSS) 
			} 
		}
		hole13.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player2"){ 
				chooseHole(13)
				//show a Cursor that indicate cannot choose
			  	hole13.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole13.setStyle(redBackgroundHoleCSS) 
			} 
		}

		hole14.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player2"){ 
				chooseHole(14)
				//show a Cursor that indicate cannot choose
			  	hole14.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole14.setStyle(redBackgroundHoleCSS) 
			} 
		}

		hole15.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player2"){ 
				chooseHole(15)
				//show a Cursor that indicate cannot choose
			  	hole15.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole15.setStyle(redBackgroundHoleCSS) 
			} 
		}

		hole16.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player2"){ 
				chooseHole(16)
				//show a Cursor that indicate cannot choose
			  	hole16.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole16.setStyle(redBackgroundHoleCSS) 
			} 
		}

		hole17.onMouseClicked = (event: MouseEvent) =>  {  
			if(canChooseHole == true && currentPlayer == "Player2"){ 
				chooseHole(17)
				//show a Cursor that indicate cannot choose
			  	hole17.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
			  	//change background red
			  	hole17.setStyle(redBackgroundHoleCSS) 
			} 
		}
	}

	
	/*-------------------------Animation Function----------------------------------------------------------------------------------------*/
	def setHoleBackgroundToYellow(holeIndex: Int) = {
		//use Platform runlater because if you call UI object in thread you need to use this function
		Platform.runLater(new Runnable() {
       		override def run {
       			holeIndex match {
				    case 0  => hole0.setStyle(yellowBackgroundHoleCSS) 
				    case 1  => hole1.setStyle(yellowBackgroundHoleCSS) 
				    case 2  => hole2.setStyle(yellowBackgroundHoleCSS) 
				    case 3  => hole3.setStyle(yellowBackgroundHoleCSS) 
				    case 4  => hole4.setStyle(yellowBackgroundHoleCSS) 
				    case 5  => hole5.setStyle(yellowBackgroundHoleCSS) 
				    case 6  => hole6.setStyle(yellowBackgroundHoleCSS) 
				    case 7  => hole7.setStyle(yellowBackgroundHoleCSS) 
				    case 8  => hole8.setStyle(yellowBackgroundHoleCSS) 
				    case 9  => hole9.setStyle(yellowBackgroundHoleCSS) 
				    case 10 => hole10.setStyle(yellowBackgroundHoleCSS) 
				    case 11 => hole11.setStyle(yellowBackgroundHoleCSS) 
				    case 12 => hole12.setStyle(yellowBackgroundHoleCSS) 
				    case 13 => hole13.setStyle(yellowBackgroundHoleCSS)  
				    case 14 => hole14.setStyle(yellowBackgroundHoleCSS) 
				    case 15 => hole15.setStyle(yellowBackgroundHoleCSS) 
				    case 16 => hole16.setStyle(yellowBackgroundHoleCSS) 
				    case 17 => hole17.setStyle(yellowBackgroundHoleCSS)  
				}
       		}
       	});
		
	}

	def setHoleBackgroundToNormal(holeIndex: Int) = {
		//use Platform runlater because if you call UI object in thread you need to use this function
		Platform.runLater(new Runnable() {
       		override def run {
       			holeIndex match {
				    case 0  => hole0.setStyle(normalBackgroundHoleCSS) 
				    case 1  => hole1.setStyle(normalBackgroundHoleCSS) 
				    case 2  => hole2.setStyle(normalBackgroundHoleCSS) 
				    case 3  => hole3.setStyle(normalBackgroundHoleCSS) 
				    case 4  => hole4.setStyle(normalBackgroundHoleCSS) 
				    case 5  => hole5.setStyle(normalBackgroundHoleCSS) 
				    case 6  => hole6.setStyle(normalBackgroundHoleCSS) 
				    case 7  => hole7.setStyle(normalBackgroundHoleCSS) 
				    case 8  => hole8.setStyle(normalBackgroundHoleCSS) 
				    case 9  => hole9.setStyle(normalBackgroundHoleCSS) 
				    case 10 => hole10.setStyle(normalBackgroundHoleCSS) 
				    case 11 => hole11.setStyle(normalBackgroundHoleCSS) 
				    case 12 => hole12.setStyle(normalBackgroundHoleCSS) 
				    case 13 => hole13.setStyle(normalBackgroundHoleCSS)  
				    case 14 => hole14.setStyle(normalBackgroundHoleCSS) 
				    case 15 => hole15.setStyle(normalBackgroundHoleCSS) 
				    case 16 => hole16.setStyle(normalBackgroundHoleCSS) 
				    case 17 => hole17.setStyle(normalBackgroundHoleCSS)  
				}
       		}
       	});
		
	}

	def minusOneAtHoleAnimation(holeIndex: Int) = {

		//use Platform runlater because if you call UI object in thread you need to use this function
		Platform.runLater(new Runnable() {
       		override def run {
       			var holeReferenceObject: TextField = null

				// get object reference according to holeIndex
				holeIndex match {
				    case 0  => holeReferenceObject = hole0
				    case 1  => holeReferenceObject = hole1
				    case 2  => holeReferenceObject = hole2
				    case 3  => holeReferenceObject = hole3
				    case 4  => holeReferenceObject = hole4
				    case 5  => holeReferenceObject = hole5
				    case 6  => holeReferenceObject = hole6
				    case 7  => holeReferenceObject = hole7
				    case 8  => holeReferenceObject = hole8
				    case 9  => holeReferenceObject = hole9
				    case 10  => holeReferenceObject = hole10
				    case 11  => holeReferenceObject = hole11
				    case 12  => holeReferenceObject = hole12
				    case 13  => holeReferenceObject = hole13
				    case 14  => holeReferenceObject = hole14
				    case 15  => holeReferenceObject = hole15
				    case 16  => holeReferenceObject = hole16
				    case 17  => holeReferenceObject = hole17
				}


				//create the font object
				var textMinus1 = new Text(holeReferenceObject.getLayoutX+30,holeReferenceObject.getLayoutY+30,"-1");

				//set font
				textMinus1.setFont(Font.font("Arial", FontWeight.BOLD, 20))
				textMinus1.setFill(Paint.valueOf("red"))

				//add the textMinus1 to screen
				myGameBoard.getChildren().add(textMinus1);

				//do animation
				var animation = new TranslateTransition(new Duration(1000),textMinus1)

				//from location
				//animation.fromX = textMinus1.getLayoutX
				//animation.fromY = textMinus1.getLayoutY

				//to destination location
				animation.toY = -30 // move up

				//play only one time
				animation.cycleCount = 1

				animation.onFinished = (event: ActionEvent) =>  {  
					//remove after animation done
					myGameBoard.getChildren().remove(textMinus1)
				}


				animation.play()
       		}
       	});
	}

	def addOneAtHoleAnimation(holeIndex: Int) = {

		//use Platform runlater because if you call UI object in thread you need to use this function
		Platform.runLater(new Runnable() {
       		override def run {
       			var holeReferenceObject: TextField = null

				// get object reference according to holeIndex
				holeIndex match {
				    case 0  => holeReferenceObject = hole0
				    case 1  => holeReferenceObject = hole1
				    case 2  => holeReferenceObject = hole2
				    case 3  => holeReferenceObject = hole3
				    case 4  => holeReferenceObject = hole4
				    case 5  => holeReferenceObject = hole5
				    case 6  => holeReferenceObject = hole6
				    case 7  => holeReferenceObject = hole7
				    case 8  => holeReferenceObject = hole8
				    case 9  => holeReferenceObject = hole9
				    case 10  => holeReferenceObject = hole10
				    case 11  => holeReferenceObject = hole11
				    case 12  => holeReferenceObject = hole12
				    case 13  => holeReferenceObject = hole13
				    case 14  => holeReferenceObject = hole14
				    case 15  => holeReferenceObject = hole15
				    case 16  => holeReferenceObject = hole16
				    case 17  => holeReferenceObject = hole17
				}


				//create the font object
				var textMinus1 = new Text(holeReferenceObject.getLayoutX+30,holeReferenceObject.getLayoutY+30,"+1");

				//set font
				textMinus1.setFont(Font.font("Arial", FontWeight.BOLD, 20))
				textMinus1.setFill(Paint.valueOf("red"))

				//add the textMinus1 to screen
				myGameBoard.getChildren().add(textMinus1);

				//do animation
				var animation = new TranslateTransition(new Duration(500),textMinus1)

				
				//to destination location
				animation.toY = - 30 // move up

				//play only one time
				animation.cycleCount = 1

				animation.onFinished = (event: ActionEvent) =>  {  
					//remove after animation done
					myGameBoard.getChildren().remove(textMinus1)
				}


				animation.play()
       		}
       	});
		
	}

	def grabFromHoleAndAddScoreToCurrentPlayerAnimation(holeIndex: Int) = {
		//use Platform runlater because if you call UI object in thread you need to use this function
		Platform.runLater(new Runnable() {
       		override def run {

       			var currentPlayerScoreHole: TextField = null

       			if(currentPlayer == "Player1"){
       				currentPlayerScoreHole = player1Score
       			}else{
       				currentPlayerScoreHole = player2Score
       			}

       			var holeReferenceObject: TextField = null

				// get object reference according to holeIndex
				holeIndex match {
				    case 0  => holeReferenceObject = hole0
				    case 1  => holeReferenceObject = hole1
				    case 2  => holeReferenceObject = hole2
				    case 3  => holeReferenceObject = hole3
				    case 4  => holeReferenceObject = hole4
				    case 5  => holeReferenceObject = hole5
				    case 6  => holeReferenceObject = hole6
				    case 7  => holeReferenceObject = hole7
				    case 8  => holeReferenceObject = hole8
				    case 9  => holeReferenceObject = hole9
				    case 10  => holeReferenceObject = hole10
				    case 11  => holeReferenceObject = hole11
				    case 12  => holeReferenceObject = hole12
				    case 13  => holeReferenceObject = hole13
				    case 14  => holeReferenceObject = hole14
				    case 15  => holeReferenceObject = hole15
				    case 16  => holeReferenceObject = hole16
				    case 17  => holeReferenceObject = hole17
				}

				//duplicate the hole
				var newHole:TextField = new TextField();
				newHole.setLayoutX(holeReferenceObject.getLayoutX())
				newHole.setLayoutY(holeReferenceObject.getLayoutY())
				newHole.setText(holeReferenceObject.getText)
				newHole.setStyle(normalBackgroundHoleCSS)
				newHole.prefWidth = 50
				newHole.prefHeight = 50
				newHole.opacity = 1
				newHole.disable = true

				//add new Hole to Display
				myGameBoard.getChildren().add(newHole);

				
				//do animation
				var animationMoveHoleToScore = new TranslateTransition(new Duration(1000),newHole)

				//from location
				//animationMoveHoleToScore.fromX = newHole.getLayoutX
				//animationMoveHoleToScore.fromY = newHole.getLayoutY

				//to destination location
				//set destination to player score hole
				//toX will specify value to be add to X axis and toY will specify value to be add to Y axis
				//we want to move newHole toward the PlayerScoreHole location
				//so we will do Destination minus current location to get distance between X axis and Y axis
				animationMoveHoleToScore.toX = currentPlayerScoreHole.getLayoutX() - newHole.getLayoutX()
				animationMoveHoleToScore.toY = currentPlayerScoreHole.getLayoutY() - newHole.getLayoutY()

				//play only one time
				animationMoveHoleToScore.cycleCount = 1

				animationMoveHoleToScore.onFinished = (event: ActionEvent) =>  {  
					//remove after hole after done
					myGameBoard.getChildren().remove(newHole)

					//play add Score animation
					if(currentPlayer == "Player1"){
						addAmountToPlayerScoreHoleAnimation(1,holeReferenceObject.getText)
					}else{
						addAmountToPlayerScoreHoleAnimation(2,holeReferenceObject.getText)
					}
					
				}


				animationMoveHoleToScore.play()
				
       		}
       	});
	}

	def addAmountToPlayerScoreHoleAnimation(player: Int, amount: String) = {

		//use Platform runlater because if you call UI object in thread you need to use this function
		Platform.runLater(new Runnable() {
       		override def run {
       			var scoreHoleReferenceObject: TextField = null

				// get object reference according to holeIndex
				player match {
				    case 1  => scoreHoleReferenceObject = player1Score
				    case 2  => scoreHoleReferenceObject = player2Score
				}


				//create the font object
				var textAddScore = new Text(scoreHoleReferenceObject.getLayoutX+30,scoreHoleReferenceObject.getLayoutY+50,"+"+amount);

				//set font
				textAddScore.setFont(Font.font("Arial", FontWeight.BOLD, 20))
				textAddScore.setFill(Paint.valueOf("red"))

				//add the textMinus1 to screen
				myGameBoard.getChildren().add(textAddScore);

				//do animation
				var animation = new TranslateTransition(new Duration(500),textAddScore)


				//to destination location
				animation.toY = -50 // move up

				//play only one time
				animation.cycleCount = 1

				animation.onFinished = (event: ActionEvent) =>  {  
					//remove after animation done
					myGameBoard.getChildren().remove(textAddScore)
				}

				animation.play()
       		}
       	});
		
	}

	def grabFromHoleAndAddToHandAnimation(holeIndex: Int) = {
		//use Platform runlater because if you call UI object in thread you need to use this function
		Platform.runLater(new Runnable() {
       		override def run {

       			var holeReferenceObject: TextField = null

				// get object reference according to holeIndex
				holeIndex match {
				    case 0  => holeReferenceObject = hole0
				    case 1  => holeReferenceObject = hole1
				    case 2  => holeReferenceObject = hole2
				    case 3  => holeReferenceObject = hole3
				    case 4  => holeReferenceObject = hole4
				    case 5  => holeReferenceObject = hole5
				    case 6  => holeReferenceObject = hole6
				    case 7  => holeReferenceObject = hole7
				    case 8  => holeReferenceObject = hole8
				    case 9  => holeReferenceObject = hole9
				    case 10  => holeReferenceObject = hole10
				    case 11  => holeReferenceObject = hole11
				    case 12  => holeReferenceObject = hole12
				    case 13  => holeReferenceObject = hole13
				    case 14  => holeReferenceObject = hole14
				    case 15  => holeReferenceObject = hole15
				    case 16  => holeReferenceObject = hole16
				    case 17  => holeReferenceObject = hole17
				}

				//duplicate the hole
				var newHole:TextField = new TextField();
				newHole.setLayoutX(holeReferenceObject.getLayoutX())
				newHole.setLayoutY(holeReferenceObject.getLayoutY())
				newHole.setText(holeReferenceObject.getText)
				newHole.setStyle(normalBackgroundHoleCSS)
				newHole.prefWidth = 50
				newHole.prefHeight = 50
				newHole.opacity = 1
				newHole.disable = true

				//add new Hole to Display
				myGameBoard.getChildren().add(newHole);

				
				//do animation
				var animationMoveHoleToScore = new TranslateTransition(new Duration(1000),newHole)

				//from location
				//animationMoveHoleToScore.fromX = newHole.getLayoutX
				//animationMoveHoleToScore.fromY = newHole.getLayoutY

				//to destination location
				//set destination to player score hole
				//toX will specify value to be add to X axis and toY will specify value to be add to Y axis
				//we want to move newHole toward the PlayerScoreHole location
				//so we will do Destination minus current location to get distance between X axis and Y axis
				var centerX = myGameBoard.getWidth / 2

				animationMoveHoleToScore.toX = centerX - newHole.getLayoutX()
				animationMoveHoleToScore.toY = - newHole.getLayoutY() -50

				//play only one time
				animationMoveHoleToScore.cycleCount = 1

				animationMoveHoleToScore.onFinished = (event: ActionEvent) =>  { 
					
					//remove after hole after done
					myGameBoard.getChildren().remove(newHole)

					//play add Score animation
					addAmountToHandAnimation(holeReferenceObject.getText)					
					
				}


				animationMoveHoleToScore.play()
				
       		}
       	});
	}

	def addAmountToHandAnimation(amount: String) = {

		//use Platform runlater because if you call UI object in thread you need to use this function
		Platform.runLater(new Runnable() {
       		override def run {

				//create the font object
				var textAddScore = new Text(hand.getLayoutX+80,hand.getLayoutY+80,"+"+amount);

				//set font
				textAddScore.setFont(Font.font("Arial", FontWeight.BOLD, 20))
				textAddScore.setFill(Paint.valueOf("red"))

				var handAnchorPane = new AnchorPane(hand.getParent().asInstanceOf[javafx.scene.layout.AnchorPane])
				//add the textMinus1 to screen
				handAnchorPane.getChildren().add(textAddScore);

				//do animation
				var animation = new TranslateTransition(new Duration(500),textAddScore)


				//to destination location
				animation.toY = -50 // move up

				//play only one time
				animation.cycleCount = 1

				animation.onFinished = (event: ActionEvent) =>  {  
					//remove after animation done
					handAnchorPane.getChildren().remove(textAddScore)
				}

				animation.play()
       		}
       	});
		
	}


	def quitGame() = {
		MainApp.goToMainScreen()
	}	


	
	
}
