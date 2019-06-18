package Controllers

import MainSystem.MainApp
import util.control._
import scala.collection.mutable.ListBuffer
import scala.collection.Iterator

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

import util.control.Breaks._
import scala.collection.mutable.Map

@sfxml
class GameController(
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

	var holes = new ListBuffer[TextField]()
	holes += (hole0,hole1,hole2,hole3,hole4,hole5,hole6,hole7,hole8,hole9,hole10,hole11,hole12,hole13)



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
	def chooseHole(holeIndex: Int, player: Int): Unit = {		
		
		//run in thread so that it will not hang the User Interface
		val myThread = new Thread {
		    override def run {

		    	//set canChooseHole to false so that we can block input before Algorithm finish
				canChooseHole = false

				//this is the hole that is chosen						

				
				//amount of seed in the Hole
				var amountInHole: Int = holes(holeIndex).getText().toInt
				var nextHole = 0				
				if (holeIndex >= holes.size-1) {
					nextHole = 0
				}
				else {
					nextHole = holeIndex + 1
				}
				
				

				grabFromHoleAndAddToHandAnimation(holeIndex, amountInHole) //grab all stones from chosen hole
				Platform.runLater(new Runnable() {
		       		override def run {
		       			holes(holeIndex).text = "0" // set hole value to 0	
						
		       		}
		       	});
				Thread.sleep(1500)
							
				var amountInHand = amountInHole
				hand.text = amountInHand.toString
				amountInHole = 0

				while (amountInHand > 0){								

					var holeNewAmount = (holes(nextHole).getText().toInt) + 1
					holes(nextHole).setText(holeNewAmount.toString)
 
					//setHoleBackgroundToYellow(holeIndex) HoleIndex is which hole to be grab from
		        	setHoleBackgroundToYellow(nextHole)

		        	//minusOneAtHoleAnimation(holeIndex) HoleIndex is which hole to be grab from
		        	addOneAtHoleAnimation(nextHole)
		        	hand.text = ((hand.getText().toInt) - 1).toString
		        	//minusOneAtHoleAnimation need 500 miliseconds to do animation we wait for it to done
		       		Thread.sleep(200)

		       		//setHoleBackgroundToNormal(holeIndex) HoleIndex is which hole to be grab from
		       		setHoleBackgroundToNormal(nextHole)		       		
					
					if (nextHole >= holes.size-1) {
						nextHole = 0
					}
					else {
						nextHole += 1
					}	
		       		amountInHand = amountInHand - 1
		        }
		       
		    	
		        val loop = new Breaks
		        var scoreUpdated = false
		        //if next hole still got stones, take and distribute
		        if (holes(nextHole).getText().toInt > 0) {
		        	chooseHole (nextHole, player)       	
		        }
		        else {
		        	loop.breakable {		        		
		        		var checkHolePointer = nextHole+1
		        		if (checkHolePointer > holes.size-1) {
		        			checkHolePointer = 0
		        		}
		        		//check current hole to hole holes.size-1, if any hole got stone, take
		        		for (x <- checkHolePointer to holes.size-1) {
		        			if (holes(x).getText().toInt != 0) {
			        			if (player == 1) {
			        				player1Score.text = (player1Score.getText().toInt + 
			        					(holes(x).getText().toInt)).toString
			        				holes(x).text = "0"
			        				scoreUpdated = true
		        					loop.break
		        				}
		        				else {
			        				player2Score.text = (player2Score.getText().toInt + 
			        					(holes(x).getText().toInt)).toString
			        				holes(x).text = "0"
			        				scoreUpdated = true		        					
		        					loop.break
	        					}		        				
		        			}
		        		}
	        			//if next hole to hole holes.size-1 all empty, check from 0 to current hole
	        			if (!scoreUpdated) {
	        				for (x <- 0 to checkHolePointer-1) {
	        					if (holes(x).getText().toInt != 0) {
				        			if (player == 1) {
				        				player1Score.text = (player1Score.getText().toInt + 
				        					(holes(x).getText().toInt)).toString
				        				holes(x).text = "0"
				        				scoreUpdated = true									
			        					loop.break
			        				}
			        				else {
				        				player2Score.text = (player2Score.getText().toInt + 
				        					(holes(x).getText().toInt)).toString
				        				holes(x).text = "0"
				        				scoreUpdated = true	        					
			        					loop.break
		        					}		        				 
		        				}
        					}
	        			}	        			        		        		
		        	}
		        	 if (scoreUpdated) {
			        	changePlayer()
			        }
			        
			        var player1NoMoves = true
			        var player2NoMoves = true
			        //check if player 2 have no more moves
			        for (x <- 0 to 6) {
			        	if (holes(x).getText().toInt > 0) {
			        		player1NoMoves = false
			        	}
			        }
			        for (x <- 7 to holes.size-1) {
			        	if (holes(x).getText().toInt > 0) {
			        		player2NoMoves = false
			        	}
			        }

			       
			        if (player1NoMoves || player2NoMoves) {
			        	if (player1Score.getText().toInt > player2Score.getText().toInt) {
			        		setWinner(1)
			        	}
			        	else if (player2Score.getText().toInt > player1Score.getText().toInt) {
			        		setWinner(2)
			        	}
			        }
			        else {
			        	canChooseHole = true					
			        }	        		        	
		        }
		       
		    }
		}

		//thread will be close when main program terminate
		myThread.setDaemon(true);
		
		myThread.start()
		
	}



	def AICalculateBestMove() = {
		
		var bestScore:Int = -1;
		var holeShouldBeChosen:Int = 0;
		for (predict <- 0 to 6){
			//println("The Hole Chosen is "+predict+" is predicted to get Score: "+PredictHoleCanGetHowMuchMark(predict))
			
			if(PredictHoleCanGetHowMuchMark(predict) > bestScore){
				bestScore = PredictHoleCanGetHowMuchMark(predict);
				holeShouldBeChosen = predict;
			}
		}
		

		//bestPredictMove = bestPredictMove - 1;

		chooseHole(holeShouldBeChosen,2);
	}

	def PredictHoleCanGetHowMuchMark(holeToPredict: Int):Int = {
		var holeIndex = holeToPredict;
		var scoreFound = false;
		var predictedScore:Int = 0;

		var duplicatedBoardHole = Array(holes(0).getText().toInt,holes(1).getText().toInt,holes(2).getText().toInt,holes(3).getText().toInt,holes(4).getText().toInt,
				holes(5).getText().toInt,holes(6).getText().toInt,holes(7).getText().toInt,holes(8).getText().toInt,holes(9).getText().toInt,holes(10).getText().toInt,
				holes(11).getText().toInt,holes(12).getText().toInt,holes(13).getText().toInt);

		var keeplooping = true
		while(keeplooping == true){
			var duplicateHand = 0;

			//amount of seed in the Hole
			var amountInHole: Int = duplicatedBoardHole(holeIndex)
			var holePointer = holeIndex
			var nextHole = holePointer+1
			

	       	duplicatedBoardHole(holeIndex) = 0 // set hole value to 0	
					
			var amountInHand = amountInHole
			duplicateHand = amountInHand
			amountInHole = 0

			while ( amountInHand > 0){

				if (holePointer >= duplicatedBoardHole.size-1) {
					nextHole = 0
				}

				for (hole <- 0 to duplicatedBoardHole.size-1) {
					if (hole == holePointer) {
						var holeNewAmount = duplicatedBoardHole(nextHole) + 1
						duplicatedBoardHole(nextHole) = holeNewAmount
					}
				}


	        	duplicateHand = duplicateHand - 1

				if (holePointer == duplicatedBoardHole.size-1) {
					holePointer = 0
				}
				else {
					holePointer += 1
				}					
				nextHole = holePointer + 1
	       		amountInHand = amountInHand - 1
	        }

	       
	        if (holePointer >= duplicatedBoardHole.size-1) {
					nextHole = 0
			}

	        val loop = new Breaks
	        var scoreUpdated = false
	        //if next hole still got stones, take and distribute
	        if (duplicatedBoardHole(nextHole) > 0) {
	        	holeIndex= nextHole       	
	        }
	        else {
	        	loop.breakable {		        		
	        		var checkHolePointer = nextHole+1
	        		if (checkHolePointer > duplicatedBoardHole.size-1) {
	        			checkHolePointer = 0
	        		}

	        		//check current hole to hole holes.size-1, if any hole got stone, take
	        		for (x <- checkHolePointer to duplicatedBoardHole.size-1) {
	        			if (duplicatedBoardHole(x) != 0) {
	        				predictedScore = duplicatedBoardHole(x)	        			
	        				scoreUpdated = true 
	        				scoreFound = true
	        				loop.break  				
	        			}
	        		}
	    			//if next hole to hole holes.size-1 all empty, check from 0 to current hole
	    			if (!scoreUpdated) {
	    				for (x <- 0 to checkHolePointer-1) {
	    					if (duplicatedBoardHole(x) != 0) {
	    						predictedScore = duplicatedBoardHole(x)
	    						scoreUpdated = true									
		        				scoreFound= true
		        				loop.break
	        				 
	        				}
						}
	    			}	        			        		        		
	        	}		        	
	        }

	        if(scoreFound == true){
	        	keeplooping =false;
	        }

		}
    	return predictedScore
	}

	//call this method when game is end
	//parameter:
	//player value to 1 if player 1 wins || player value set to 2 if player 2 wins || player value set to 0 if no one wins(Tie)
	//player1Score is the score of player1
	//player2Score is the score of player2
	def setWinner(player:Int): Unit = {

		//redirect to Winner Screen to display the winner
		MainApp.goToWinnerScreen(player,player1Score.getText().toInt,player2Score.getText().toInt)
	}

	
	

	def setHoleHoverEffect() = {	

		//hole 0 to 6 is for player 2 to choose
		for(index <- 0 to 6){
			var currentHoleObject = holes(index);
			//set hole hover in 
			currentHoleObject.onMouseEntered = (event: MouseEvent) =>  {
			
				if(canChooseHole == true && currentPlayer == "Player2" && currentHoleObject.getText != "0"){
					//if it is Player 1 means hole 0 to hole 6 can be choose
					//set cursor hand
				  	currentHoleObject.setCursor(Cursor.HAND)
				  	//set background green
				  	currentHoleObject.setStyle(greenBackgroundHoleCSS)

				}else{
					//if it is not Player 1 means hole cannot be choose
					//show a Cursor that indicate cannot choose
				  	currentHoleObject.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
				  	//change background red
				  	currentHoleObject.setStyle(redBackgroundHoleCSS)
				}
			}

			//set hole hover out
			currentHoleObject.onMouseExited = (event: MouseEvent) =>  {
				//change hole background color to normal
				currentHoleObject.setStyle(normalBackgroundHoleCSS)
			}
		}

		//hole 7 to 13 is for player 1 to choose
		for(index <- 7 to 13){
			var currentHoleObject = holes(index);
			//set hole hover in 
			currentHoleObject.onMouseEntered = (event: MouseEvent) =>  {
			
				if(canChooseHole == true && currentPlayer == "Player1" && currentHoleObject.getText != "0"){
					//if it is Player 1 means hole 0 to hole 6 can be choose
					//set cursor hand
				  	currentHoleObject.setCursor(Cursor.HAND)
				  	//set background green
				  	currentHoleObject.setStyle(greenBackgroundHoleCSS)

				}else{
					//if it is not Player 1 means hole cannot be choose
					//show a Cursor that indicate cannot choose
				  	currentHoleObject.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
				  	//change background red
				  	currentHoleObject.setStyle(redBackgroundHoleCSS)
				}
			}

			//set hole hover out
			currentHoleObject.onMouseExited = (event: MouseEvent) =>  {
				//change hole background color to normal
				currentHoleObject.setStyle(normalBackgroundHoleCSS)
			}
		}

	}

	def initializeHoleOnAction() = {

		//hole 0 to 6 is for player 2 to choose
		for(index <- 0 to 6){
			var currentHoleObject = holes(index);

			currentHoleObject.onMouseClicked = (event: MouseEvent) =>  {  
				if(canChooseHole == true && currentPlayer == "Player2" && currentHoleObject.getText != "0"){ 
					chooseHole(index, 2)
					//show a Cursor that indicate cannot choose
				  	currentHoleObject.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
				  	//change background red
				  	currentHoleObject.setStyle(redBackgroundHoleCSS) 
				} 
			}

		}

		//hole 7 to 13 is for player 1 to choose
		for(index <- 7 to 13){
			var currentHoleObject = holes(index);

			currentHoleObject.onMouseClicked = (event: MouseEvent) =>  {  
				if(canChooseHole == true && currentPlayer == "Player1" && currentHoleObject.getText != "0"){ 
					chooseHole(index, 1)
					//show a Cursor that indicate cannot choose
				  	currentHoleObject.setCursor(new ImageCursor(new Image(getClass.getResourceAsStream("/Images/Game/CancelCursor.png"))))
				  	//change background red
				  	currentHoleObject.setStyle(redBackgroundHoleCSS) 
				} 
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
				}


				//create the font object
				var textMinus1 = new Text(holeReferenceObject.getLayoutX+30,holeReferenceObject.getLayoutY+30,"+1");

				//set font
				textMinus1.setFont(Font.font("Arial", FontWeight.BOLD, 20))
				textMinus1.setFill(Paint.valueOf("red"))

				//add the textMinus1 to screen
				myGameBoard.getChildren().add(textMinus1);

				//do animation
				var animation = new TranslateTransition(new Duration(200),textMinus1)

				
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

	def grabFromHoleAndAddToHandAnimation(holeIndex: Int, handAmount:Int) = {
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
				}

				//duplicate the hole
				var newHole:TextField = new TextField();
				newHole.setLayoutX(holeReferenceObject.getLayoutX())
				newHole.setLayoutY(holeReferenceObject.getLayoutY())
				newHole.setText(handAmount.toString)
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
					addAmountToHandAnimation(handAmount.toString)					
					
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
