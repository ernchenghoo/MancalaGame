package Controllers

import MainSystem.MainApp

import scalafxml.core.macros.sfxml
import scalafx.scene.control._


import scalafx.scene.image.Image
import scalafx.scene.control.Button
import scalafx.scene.image.ImageView

@sfxml
class SelectGameModeScreenController(
	val GameModeTitle: ImageView,
	val VsTitle: ImageView,
	val player1Icon: ImageView,
	val player2Icon: ImageView,
	val leftButton: Button,
	val rightButton: Button,
	val boardSize: ImageView,
	val smallLeftButton: Button,
	val smallRightButton: Button,
	val PlayButton: Button,
	val BackButton: Button

)
{
	//variable to keep track of Enemy Game Mode selected
	MainApp.selectedEnemyMode= "AI";

	var boardSizeSelected = "7x7";


	//set title image
	GameModeTitle.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/GameModeWord.png")));

	//set Vs Word image
	VsTitle.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/VsWord.png")));

	//set Player1 Icon
	player1Icon.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/PlayerIcon.png")));

	//set Player 2 Icon
	player2Icon.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/BotIcon.png")));

	//set left button image
	leftButton.setGraphic(new ImageView(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/ArrowLeft.png"))));

	//set rightbutton image
	rightButton.setGraphic(new ImageView(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/ArrowRight.png"))));

	//set board size default 7x7
	boardSize.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/Size7x7.png")));

	//set board sizeleft button image
	smallLeftButton.setGraphic(new ImageView(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/SmallArrowLeft.png"))));

	//set BackButton image
	smallRightButton.setGraphic(new ImageView(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/SmallArrowRight.png"))));

	//set PlayButton image
	PlayButton.setGraphic(new ImageView(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/PlayButton.png"))));

	//set BackButton image
	BackButton.setGraphic(new ImageView(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/BackButton.png"))));


	def backAction() = {
		MainApp.goToMainScreen()
	}

	def changePlayer()={

		if(MainApp.selectedEnemyMode == "AI"){
			MainApp.selectedEnemyMode="HUMAN"
			player2Icon.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/PlayerIcon.png")));

		}else{
			MainApp.selectedEnemyMode="AI"
			player2Icon.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/BotIcon.png")));

		}
	}

	def changeLeftBoardSize()={
		if(boardSizeSelected == "5x5"){
			boardSizeSelected = "9x9"
			//set board size image 9x9
			boardSize.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/Size9x9.png")));
		}else if(boardSizeSelected == "7x7"){
			boardSizeSelected = "5x5"
			//set board size image 5x5
			boardSize.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/Size5x5.png")));
		}else if(boardSizeSelected == "9x9"){
			boardSizeSelected = "7x7"
			//set board size image 7x7
			boardSize.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/Size7x7.png")));
		}


	}

	def changeRightBoardSize()={
		if(boardSizeSelected == "5x5"){
			boardSizeSelected = "7x7"
			//set board size image 7x7
			boardSize.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/Size7x7.png")));
		}else if(boardSizeSelected == "7x7"){
			boardSizeSelected = "9x9"
			//set board size image 9x9
			boardSize.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/Size9x9.png")));
		}else if(boardSizeSelected == "9x9"){
			boardSizeSelected = "5x5"
			//set board size image 5x5
			boardSize.setImage(new Image(getClass.getResourceAsStream("/Images/SelectGameMode/Size5x5.png")));
		}
		
	}

	def startGame()={
		if(boardSizeSelected == "5x5"){
			MainApp.goToSmallGameScreen()
		}else if(boardSizeSelected == "7x7"){
			MainApp.goToGameScreen()
		}else if(boardSizeSelected == "9x9"){
			MainApp.goToBigGameScreen()
		}
		
	}
}
