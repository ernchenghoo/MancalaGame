package MainSystem

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import scalafx.Includes._
import scalafx.scene.Scene
import javafx.{scene => jfxs}
import scalafx.scene.image.Image

import Controllers.WinnerScreenController

object MainApp extends JFXApp {

  var selectedEnemyMode: String = null;
  
 
  val rootResource = getClass.getResource("/Views/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load();
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  
  stage = new PrimaryStage {
    title = "Mancala Game"
    icons += new Image(getClass.getResourceAsStream("/Images/mancalaLogo.png"))
    scene = new Scene {
      root = roots
    }
  }

  goToMainScreen()


  def goToMainScreen() = {
    val resource = getClass.getResource("/Views/MainScreen.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.VBox]
    
    stage.getScene().setRoot(roots)
  } 

  def goToGameModeScreen() = {
    val resource = getClass.getResource("/Views/SelectGameModeScreen.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.BorderPane]
    
    stage.getScene().setRoot(roots)
  }

  def goToGameScreen() = {
    val resource = getClass.getResource("/Views/Game.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.BorderPane]
    
    stage.getScene().setRoot(roots)
  }

  def goToSmallGameScreen() = {
    val resource = getClass.getResource("/Views/SmallGame.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.BorderPane]
    
    stage.getScene().setRoot(roots)
  }

  def goToBigGameScreen() = {
    val resource = getClass.getResource("/Views/BigGame.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.BorderPane]
    
    stage.getScene().setRoot(roots)
  }

  def goToWinnerScreen( winner: Int, player1Score: Int, player2Score: Int) = {
    val resource = getClass.getResource("/Views/WinnerScreen.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.BorderPane]
    val control = loader.getController[WinnerScreenController#Controller]
    control.winner = winner
    control.player1Score = player1Score
    control.player2Score = player2Score
    control.initializeAllAssets()


    
    stage.getScene().setRoot(roots)

    
  }


}