package Controllers

import scala.collection.mutable.ArrayBuffer

import scalafxml.core.macros.sfxml
import scalafx.scene.control._




import scalafx.scene.image.Image
import scalafx.scene.control.Button
import scalafx.scene.image.ImageView
import scalafx.scene.text.Text
import scalafx.scene.control.TextField


import scalafx.animation.Timeline
import javafx.util.Duration
import javafx.animation.KeyFrame
import javafx.event.ActionEvent
import javafx.event.EventHandler
import scalafx.application.Platform

import MainSystem.MainApp

@sfxml
class WinnerScreenController(
	val winnerTitle: Text,
	val player1Icon: ImageView,
	val player1ScoreBox: TextField,
	val player2Icon: ImageView,
	val player2ScoreBox: TextField,
	val mainMenuButton: Button
	)
{
	//-----------Three of these variable will be pass to this controller At the MainApp There------------------------
	// player1Score will store the player 1 score
	var player1Score: Int =0

	//player2 Score will store the player 2 score
	var player2Score: Int =0

	//winner will be 1 if player 1 win and winner will be 2 if player 2 wins and winner will be zero if tie
	var winner: Int =0

	//----------------------------------------------------------------------------------------------
	//variable that store all animation thread reference so that we can terminate all thread when user go back to mainmenu
	var threadList = ArrayBuffer[Thread]()

	def initializeAllAssets() = {
		player1Icon.setImage(new Image(getClass.getResourceAsStream("/Images/WinnerScreen/SadMan.png")));

		//player 2 happy animation
		if(MainApp.selectedEnemyMode == "AI"){
			player2Icon.setImage(new Image(getClass.getResourceAsStream("/Images/WinnerScreen/SadRobot.png")));
		}else{
			player2Icon.setImage(new Image(getClass.getResourceAsStream("/Images/WinnerScreen/SadMan.png")));
		}
		

		
		if( winner == 1){
			//set title word
			winnerTitle.setText("Player 1 Wins!")

			//player 1 happy animation
			playHappyAnimation(1,"man")


			
		}else if(winner ==2){
			winnerTitle.setText("Player 2 Wins!")


			//player 2 happy animation
			if(MainApp.selectedEnemyMode == "AI"){
				playHappyAnimation(2,"robot")
			}else{
				playHappyAnimation(2,"man")
			}

		}else{
			winnerTitle.setText("Tie! No one wins")

			//player 1 happy animation
			playHappyAnimation(1,"man")

			//player 2 happy animation
			if(MainApp.selectedEnemyMode == "AI"){
				playHappyAnimation(2,"robot")
			}else{
				playHappyAnimation(2,"man")
			}
		}
		

		//display the score
		player1ScoreBox.setText(player1Score.toString)

		//display the score
		player2ScoreBox.setText(player2Score.toString)
	}
	


	

	

	//set button image
	mainMenuButton.setGraphic(new ImageView(new Image(getClass.getResourceAsStream("/Images/WinnerScreen/MainMenuButton.png"))));

	

	def backToMainMenu() = {
		//terminate all animation thread
		for (animThread <- threadList) {
		   animThread.interrupt()
		}


		MainApp.goToMainScreen()
	}	


	
	
	

	//--------------------Animation Function ------------------------------
	def playHappyAnimation(player: Int, icon: String) = {

		var playerImageView: ImageView = null

		if(player == 1){
			playerImageView = player1Icon
		}else{
			playerImageView = player2Icon
		}


		val manAnimationThread = new Thread {
		    override def run {

		    	while(true){

		    		//keep changing image every 100 milliseconds to make animation effect
		    		for (x <- 1 to 8){
			    		var currentImage: Image = null

			    		x match {
						    case 1  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyMan/man1.png"))
						    case 2  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyMan/man2.png"))
						    case 3  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyMan/man3.png"))
						    case 4  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyMan/man4.png"))
						    case 5  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyMan/man5.png"))
						    case 6  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyMan/man6.png"))
						    case 7  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyMan/man7.png"))
						    case 8  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyMan/man8.png"))
						}

			    		Platform.runLater(new Runnable() {
				       		override def run {
								playerImageView.setImage(currentImage);
				       		}
				       	});

				       	Thread.sleep(100)

			    	} 
		    	}	    	
		    }
		}

		val robotnAnimationThread = new Thread {
		    override def run {
		    	
		    	//keep looping
		    	while(true){

		    		//keep changing image every 100 milliseconds to make animation effect
		    		for (x <- 1 to 10){
			    		var currentImage: Image = null

			    		x match {
						    case 1  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyRobot/robot1.png"))
						    case 2  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyRobot/robot2.png"))
						    case 3  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyRobot/robot3.png"))
						    case 4  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyRobot/robot4.png"))
						    case 5  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyRobot/robot5.png"))
						    case 6  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyRobot/robot6.png"))
						    case 7  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyRobot/robot7.png"))
						    case 8  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyRobot/robot8.png"))
						    case 9  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyRobot/robot9.png"))
						    case 10  => currentImage = new Image(getClass.getResourceAsStream("/Images/WinnerScreen/HappyRobot/robot10.png"))
						}

			    		Platform.runLater(new Runnable() {
				       		override def run {
								playerImageView.setImage(currentImage);
				       		}
				       	});
				       	Thread.sleep(100)
			    	} 
		    	}	    	
		    }
		}

		var myThread: Thread = null

		if(icon == "man"){
			myThread = manAnimationThread
		}else{
			myThread = robotnAnimationThread
		}

		//thread will be close when main program terminate
		myThread.setDaemon(true);

		myThread.start()

		//add thread to ThreadList so that we can terminate all thread when user go back to mainmenu
		threadList += myThread
	}


}
