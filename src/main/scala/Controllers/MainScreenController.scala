package Controllers

import scalafxml.core.macros.sfxml
import scalafx.scene.control._
import scalafx.scene.layout.VBox


import javafx.scene.layout.BackgroundImage
import javafx.scene.layout.BackgroundRepeat
import javafx.scene.layout.BackgroundPosition
import javafx.scene.layout.BackgroundSize
import javafx.scene.layout.Background
import scalafx.scene.image.Image

import scalafx.scene.control.Button
import scalafx.scene.image.ImageView

import scalafx.animation.RotateTransition
import scalafx.util.Duration

import MainSystem.MainApp

@sfxml
class MainScreenController(
	val mainVBox: VBox,
	val startGameButton: Button,
	val myTitle: ImageView,
	val mancalaBoardImage: ImageView
	)
{

	//set Anchor Pane background Image
	var myBackgroundImage= new BackgroundImage(new Image(getClass.getResourceAsStream("/Images/MainScreen/Background.png")),
        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
          new BackgroundSize(100, 100, true, true, false, true));

	//set background image to VBox
	mainVBox.setBackground(new Background(myBackgroundImage));

	//set title image
	myTitle.setImage(new Image(getClass.getResourceAsStream("/Images/MainScreen/MancalaGameTitle.png")));

	//set mancalaBoardImage
	mancalaBoardImage.setImage(new Image(getClass.getResourceAsStream("/Images/MainScreen/MancalaBoard.png")));

	//set button image
	startGameButton.setGraphic(new ImageView(new Image(getClass.getResourceAsStream("/Images/MainScreen/StartGameButton.png"))));

	//do animation shake mancalaboard
	var animation = new RotateTransition(new Duration(500),mancalaBoardImage)


	//to destination location
	animation.toAngle = -5 // move up

	//play only one time
	animation.cycleCount = 100

	animation.autoReverse = true
	animation.play()

	def startAction() = {
		MainApp.goToGameModeScreen()
	}	
	


}
