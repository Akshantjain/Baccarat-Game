import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class BaccaratGame extends Application {

	private static DecimalFormat df = new DecimalFormat("0.00");

	private ArrayList<Card> playerHand, bankerHand;		// data member to store the cards for the player and banker

	private BaccaratDealer theDealer;		// data member to create a new dealer for the game
	private BaccaratGameLogic gameLogic;	// data member to get the game logic for the game

	double currentPlayerBet = 0;				// data member to store the bet made by the player
	private double currentBankBet = 0;
	private double currentTieBet = 0;
	private double totalWinnings = 0;			// data member to store the winning according to the rules

	private double nowPlayerBet = 0, nowBankBet = 0, nowTieBet = 0;

	private MenuBar menuBar;		// data member for menu bar
	private BorderPane paneCenter;		// data member for the main box in the scene
	private Menu options;			// data member for options in the menu bar
	private Text welcomeMessage;
	private VBox startBox;;

	private TextField player, bank;			// data member for the Player and Bank Section label

	// data member for the input for the player bet, banker bet, tie bet and show the progress of the game
	private TextField playerBet, bankerBet, tieBet, progress, playerTotal, bankTotal;

	// data member to display player card 1, card 2, card 3
	private Rectangle playerCard1, playerCard2, playerCard3;
	// data member to display Banker card 1, card 2, card 3
	private Rectangle bankCard1, bankCard2, bankCard3;

	int playerHandTotal = 0, bankHandTotal = 0;
	double roundWinnings = 0;

	HashMap<String, Scene> SceneMap;	// data member to store all different scenes to be used in the game

	private Button startButton, dealButton, clearBets, reBet, doubleBets;

	private PauseTransition pauseStart = new PauseTransition(Duration.seconds(4));
	private PauseTransition pauseGameStart1 = new PauseTransition(Duration.seconds(1));
	private PauseTransition pauseGameStart2 = new PauseTransition(Duration.seconds(2));
	private PauseTransition pauseGameStart3 = new PauseTransition(Duration.seconds(3));


	private Scene scene;

	public BaccaratGame() {
	}

	public static void main(String[] args) {
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Let's Play Baccarat!!!");

		menuBar = new MenuBar();
		options = new Menu("Options");
		paneCenter = initialisation();
		scene = new Scene(paneCenter, 1200, 750);
		welcomeMessage = new Text("Baccarat Game");
		MenuItem FreshStart = new MenuItem("Fresh Start");
		MenuItem Exit = new MenuItem("Exit");
		startButton = new Button("Start Game");
		startBox = new VBox(welcomeMessage, startButton);

		theDealer = new BaccaratDealer();
		gameLogic = new BaccaratGameLogic();

		playerHand = new ArrayList <>();
		bankerHand = new ArrayList <>();

		totalWinnings = 0;

		InnerShadow welcomeShadow = new InnerShadow();

		welcomeMessage.setStyle(
				"-fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, orange 0%, red 50%);"
		);
		welcomeMessage.setFont(Font.font("Verdana", FontWeight.BOLD, 108));
		welcomeShadow.setOffsetX(4.0f);
		welcomeShadow.setOffsetY(4.0f);
		welcomeMessage.setEffect(welcomeShadow);

		startButton.setStyle(
				"-fx-font-size: 48px;" +
				"-fx-background-radius: 5em;" +
				"-fx-background-color: black;" +
				"-fx-border-color: rgba(148,192,0,0.78);" +
				"-fx-border-radius: 5em;" +
				"-fx-text-fill: #ffffff;"
		);

		options.getItems().addAll(FreshStart, Exit);
		menuBar.getMenus().addAll(options);
		menuBar.setPadding(new Insets(0, 0, 0, 0));
		paneCenter.setTop(menuBar);
		paneCenter.setCenter(startBox);
		startBox.setAlignment(Pos.CENTER);

		welcomeMessage.setTextOrigin(VPos.CENTER);

		pauseStart.setOnFinished(e->primaryStage.getScene().setRoot(createGameBox()));
		pauseGameStart1.setOnFinished(e->{
			startButton.setVisible(false);
			welcomeMessage.setText("Starting.");
		});
		pauseGameStart2.setOnFinished(e->welcomeMessage.setText("Starting.."));
		pauseGameStart3.setOnFinished(e->welcomeMessage.setText("Starting..."));

		startButton.setOnAction(e->
		{
			totalWinnings = 0;
			currentBankBet = 0;
			currentPlayerBet = 0;
			currentTieBet = 0;
			pauseGameStart1.play();
			pauseGameStart2.play();
			pauseGameStart3.play();
			pauseStart.play();
		});

		startButton.setOnMouseEntered(e-> startButton.setStyle(
				"-fx-font-size: 48px;" +
				"-fx-background-radius: 5em;" +
				"-fx-background-color: #171717;" +
				"-fx-border-color: rgba(148,192,0,0.78);" +
				"-fx-border-radius: 5em;" +
				"-fx-text-fill: #ffffff;"
		));

		startButton.setOnMouseExited(e-> startButton.setStyle(
				"-fx-font-size: 48px;" +
				"-fx-background-radius: 5em;" +
				"-fx-background-color: black;" +
				"-fx-border-color: rgba(148,192,0,0.78);" +
				"-fx-border-radius: 5em;" +
				"-fx-text-fill: #ffffff;"
		));

		FreshStart.setOnAction(e->
		{
			deleteData();
			primaryStage.getScene().setRoot(createGameBox());
		});

		Exit.setOnAction(e-> primaryStage.close());

		primaryStage.setFullScreenExitHint("");
		primaryStage.setFullScreen(true);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private double evaluateWinnings()	{
		return gameLogic.evaluateEachRoundWinning(nowPlayerBet, nowBankBet, nowTieBet);
	}

	private void playTheGame()	{
		clearImagesAndArrays();

		PauseTransition betweenGamePause4 = new PauseTransition(Duration.seconds(0.5));
		PauseTransition betweenGamePause1 = new PauseTransition((Duration.seconds(1)));
		PauseTransition betweenGamePause2 = new PauseTransition(Duration.seconds(2));
		PauseTransition betweenGamePause3 = new PauseTransition(Duration.seconds(3));
		PauseTransition betweenGamePause5 = new PauseTransition(Duration.seconds(4));

		betweenGamePause4.setOnFinished(e->{
			clearBets.setDisable(true);
			reBet.setDisable(true);
			doubleBets.setDisable(true);
		});
		betweenGamePause5.setOnFinished(e->{
			clearBets.setDisable(false);
			reBet.setDisable(false);
			doubleBets.setDisable(false);
		});

		betweenGamePause4.play();

		playerHand = theDealer.dealHand();
		playerCard1.setFill(new ImagePattern(new Image(playerHand.get(0).getImageString())));
		playerCard1.setOpacity(0.9);
		playerCard2.setFill(new ImagePattern(new Image(playerHand.get(1).getImageString())));
		playerCard2.setOpacity(0.9);

		bankerHand = theDealer.dealHand();
		bankCard1.setFill(new ImagePattern(new Image(bankerHand.get(0).getImageString())));
		bankCard1.setOpacity(0.9);
		bankCard2.setFill(new ImagePattern(new Image(bankerHand.get(1).getImageString())));
		bankCard2.setOpacity(0.9);

		if (gameLogic.evaluatePlayerDraw(playerHand))	{
			playerHand.add(theDealer.drawOne());
			//playerCard3.setFill(new ImagePattern(new Image(playerHand.get(2).getImageString())));
			//playerCard3.setOpacity(0.9);
			betweenGamePause1.play();
		}

		betweenGamePause1.setOnFinished(e->{
			playerCard3.setFill(new ImagePattern(new Image(playerHand.get(2).getImageString())));
			playerCard3.setOpacity(0.9);
		});

		if (playerHand.size() == 3)	{
			if (gameLogic.evaluateBankerDraw(bankerHand, playerHand.get(playerHand.size()-1))){
				bankerHand.add(theDealer.drawOne());
				betweenGamePause2.play();
			}
		}
		else {
			Card nullCard = new Card("None", -1);
			if (gameLogic.evaluateBankerDraw(bankerHand, nullCard))	{
				bankerHand.add(theDealer.drawOne());
				betweenGamePause2.play();
			}
		}

		betweenGamePause2.setOnFinished(e->{
			bankCard3.setFill(new ImagePattern(new Image(bankerHand.get(2).getImageString())));
			bankCard3.setOpacity(0.9);
		});

		String won= gameLogic.whoWon(playerHand, bankerHand);

		switch (won) {
			case "Player":
				nowPlayerBet = currentPlayerBet;
				nowBankBet = (-nowBankBet);
				nowTieBet = (-nowTieBet);
				roundWinnings = evaluateWinnings();
				won = "Player Wins";
				break;
			case "Banker":
				nowBankBet = currentBankBet;
				nowTieBet = (-nowTieBet);
				nowPlayerBet = (-nowPlayerBet);
				roundWinnings = evaluateWinnings();
				won = "Bank Wins";
				break;
			case "Draw":
				nowTieBet = currentTieBet;
				nowPlayerBet = (-nowPlayerBet);
				nowBankBet = (-nowBankBet);
				roundWinnings = evaluateWinnings();
				won = "Game Tie";
				break;
		}
		totalWinnings = totalWinnings + roundWinnings;

		String finalWon = won;

		betweenGamePause3.setOnFinished(e->{
			progress.setText(finalWon + "   |   Round Winnings:  $" + df.format(roundWinnings) + "   |   Your total winnings:  $" + df.format(totalWinnings));
			playerHandTotal = gameLogic.handTotal(playerHand);
			bankHandTotal = gameLogic.handTotal(bankerHand);

			player.setText("Player Cards    |    Player Total:  " + playerHandTotal);
			bank.setText("Banker Cards    |    Bank Total:  " + bankHandTotal);
		});

		betweenGamePause3.play();
		betweenGamePause5.play();

		if(totalWinnings > 10000000)	{
			dealButton.setDisable(true);
			reBet.setDisable(true);
			clearBets.setDisable(true);
			doubleBets.setDisable(true);

			playerBet.setDisable(true);
			bankerBet.setDisable(true);
			tieBet.setDisable(true);
			//progress.setText("Maximum Total Winnings Reached: $" + df.format(totalWinnings));
			player.setText("Maximum Total Winnings Reached  |  Start a Fresh Game");
			bank.setText("For Fresh Game, go to Options in Menu Bar");
		}
	}

	private BorderPane createGameBox	()	{
		pauseStart.stop();
		pauseGameStart1.stop();
		pauseGameStart2.stop();
		pauseGameStart3.stop();

		BorderPane borderPane;
		borderPane = new BorderPane();
		borderPane.setTop(menuBar);
		borderPane.setStyle(
				"-fx-background-image: url(baccarat_game1.jpeg);" +
						"-fx-background-repeat: no-repeat;" +
						"-fx-background-size: 100%;" +
						"-fx-opacity: 0.8"
		);

		playerCard1 = createCards();
		playerCard2 = createCards();
		playerCard3 = createCards();

		bankCard1 = createCards();
		bankCard2 = createCards();
		bankCard3 = createCards();

		playerBet =  createTextField(new Text("Player Bet"));
		bankerBet = createTextField(new Text("Bank Bet"));
		tieBet = createTextField(new Text("Tie Bet"));

		playerBet.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("([$]?)\\d{0,4}([.]\\d{0,2})?")) {
				playerBet.setText(oldValue);
			}
		});
		bankerBet.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("([$]?)\\d{0,4}([.]\\d{0,2})?")) {
				bankerBet.setText(oldValue);
			}
		});
		tieBet.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("([$]?)\\d{0,4}([.]\\d{0,2})?")) {
				tieBet.setText(oldValue);
			}
		});

		progress = createTextField(new Text("Progress"));
		progress.setDisable(true);
		progress.setOpacity(1);
		progress.setStyle("-fx-font-size: 20px;" +
				"-fx-background-radius: 1em;" +
				"-fx-border-radius: 1em;" +
				"-fx-alignment: center;" +
				"-fx-display-caret: false;" +
				"-fx-text-fill: white;" +
				"-fx-border-color: white;" +
				"-fx-background-color: rgba(48,49,47,1);" +
				"-fx-wrap-text: false;"
		);
		progress.setText("(:  Welcome to the Game :)");

		dealButton = createControlButtons("DEAL CARDS");
		clearBets = createControlButtons("NEW BET");
		doubleBets = createControlButtons("REBET x 2");
		reBet = createControlButtons("REBET CHIPS");

		clearBets.setDisable(true);
		doubleBets.setDisable(true);
		reBet.setDisable(true);

		player = createTextField(new Text("Player Cards    |    Player Total:  " + playerHandTotal));
		player.setDisable(true);
		player.setOpacity(1);
		player.setStyle("-fx-font-size: 30px;" +
				"-fx-background-radius: 1em;" +
				"-fx-border-radius: 1em;" +
				"-fx-alignment: center;" +
				"-fx-display-caret: false;" +
				"-fx-text-fill: white;" +
				"-fx-border-color: white;" +
				"-fx-background-color: rgba(48,49,47,1);" +
				"-fx-wrap-text: false;"
		);

		bank = createTextField(new Text("Banker Cards    |    Bank Total:  " + bankHandTotal));
		bank.setDisable(true);
		bank.setOpacity(1);
		bank.setStyle("-fx-font-size: 30px;" +
				"-fx-background-radius: 1em;" +
				"-fx-border-radius: 1em;" +
				"-fx-alignment: center;" +
				"-fx-display-caret: false;" +
				"-fx-text-fill: white;" +
				"-fx-border-color: white;" +
				"-fx-background-color: rgba(48,49,47,1);" +
				"-fx-wrap-text: false;"
		);

		setButtonsActions();

		borderPane.setLeft(createBettingArea(playerBet, bankerBet, tieBet));
		borderPane.setRight(createButtonsArea(dealButton, clearBets, reBet, doubleBets));
		borderPane.setCenter(createGamePlayArea(player, bank, playerCard1, playerCard2, playerCard3, bankCard1, bankCard2, bankCard3, progress));
		return borderPane;
	}

	private void setButtonsActions()	{
		dealButton.setOnAction(e->{
			try {
				currentPlayerBet = Double.parseDouble(playerBet.getText());
			} catch (NumberFormatException ignore) {}

			try {
				currentBankBet = Double.parseDouble(bankerBet.getText());
			} catch (NumberFormatException ignore) {}

			try {
				currentTieBet = Double.parseDouble(tieBet.getText());
			} catch (NumberFormatException ignore) {}

			playerBet.setText("$" + (currentPlayerBet));
			bankerBet.setText("$" + (currentBankBet));
			tieBet.setText("$" + (currentTieBet));

			playerBet.setDisable(true);
			bankerBet.setDisable(true);
			tieBet.setDisable(true);

			dealButton.setDisable(true);
			clearBets.setDisable(true);
			reBet.setDisable(true);
			doubleBets.setDisable(true);

			progress.setText("Dealing...");
			player.setText("Player Cards");
			bank.setText("Bank Cards");

			playTheGame();
		});

		clearBets.setOnAction(e->{
			currentPlayerBet = 0;
			currentBankBet = 0;
			currentTieBet = 0;

			playerBet.setText("");
			bankerBet.setText("");
			tieBet.setText("");

			playerBet.setDisable(false);
			bankerBet.setDisable(false);
			tieBet.setDisable(false);

			dealButton.setDisable(false);
			clearBets.setDisable(false);
			reBet.setDisable(true);
			doubleBets.setDisable(true);

			progress.setText("Cleared All Bets!!");
		});

		reBet.setOnAction(e->{
			playerBet.setDisable(true);
			bankerBet.setDisable(true);
			tieBet.setDisable(true);

			dealButton.setDisable(true);

			dealButton.setDisable(true);
			reBet.setDisable(true);
			clearBets.setDisable(true);
			doubleBets.setDisable(true);

			progress.setText("Dealing Re-Bet...");
			player.setText("Player Cards");
			bank.setText("Bank Cards");

			playTheGame();
		});

		doubleBets.setOnAction(e->{
			playerBet.setDisable(false);
			bankerBet.setDisable(false);
			tieBet.setDisable(false);

			currentPlayerBet = 2 * currentPlayerBet;
			playerBet.setText("$" + (currentPlayerBet));

			currentBankBet = 2 * currentBankBet;
			bankerBet.setText("$" + (currentBankBet));

			currentTieBet = 2 * currentTieBet;
			tieBet.setText("$" + currentTieBet);

			playerBet.setDisable(true);
			bankerBet.setDisable(true);
			tieBet.setDisable(true);

			dealButton.setDisable(true);
			reBet.setDisable(true);
			clearBets.setDisable(true);
			doubleBets.setDisable(true);

			progress.setText("Dealing Double Re-Bet...");
			player.setText("Player Cards");
			bank.setText("Bank Cards");

			playTheGame();
		});
	}

	private VBox createBettingArea(TextField player, TextField bank, TextField tie)	{
		VBox vBox = new VBox(25);
		vBox.getChildren().addAll(player, bank, tie);
		vBox.setPadding(new Insets(10, 35, 10, 35));
		vBox.setAlignment(Pos.CENTER);
		return vBox;
	}

	private VBox createGamePlayArea
			(TextField player, TextField bank,
			Rectangle playerCard1, Rectangle playerCard2, Rectangle playerCard3,
			Rectangle bankCard1, Rectangle bankCard2, Rectangle bankCard3,
			TextField progress)	{

		VBox vBox = new VBox(30);
		vBox.getChildren().addAll(player, createCardsArea(playerCard1, playerCard2, playerCard3), bank, createCardsArea(bankCard1, bankCard2, bankCard3), progress);
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(0, 0, 0, 0));
		return vBox;
	}

	private HBox createCardsArea(Rectangle card1, Rectangle card2, Rectangle card3)	{
		HBox hBox = new HBox(10);
		hBox.getChildren().addAll(card1, card2, card3);
		return hBox;
	}

	private VBox createButtonsArea(Button deal, Button clear, Button reBet, Button doubleBet)	{
		VBox vBox = new VBox(15);
		vBox.getChildren().addAll(deal,clear, reBet, doubleBet);
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(0, 70, 0, 30));
		return vBox;
	}

	private Button createControlButtons(String string)	{
		Button button = new Button(string);
		button.setPrefWidth(200);
		button.setPrefHeight(50);

		button.setStyle("-fx-font-size: 20px;" +
				"-fx-background-radius: 1em;" +
				"-fx-border-radius: 1em;" +
				"-fx-alignment: center;" +
				"-fx-display-caret: false;" +
				"-fx-text-fill: white;" +
				"-fx-border-color: white;" +
				"-fx-background-color: rgba(48,49,47,0.8);" +
				"-fx-label-padding: 0px;");
		button.setOnMouseEntered(e->button.setStyle(
				"-fx-font-size: 20px;" +
						"-fx-background-radius: 1em;" +
						"-fx-border-radius: 1em;" +
						"-fx-alignment: center;" +
						"-fx-display-caret: false;" +
						"-fx-text-fill: white;" +
						"-fx-border-color: white;" +
						"-fx-background-color: rgba(32,33,31,0.8);" +
						"-fx-label-padding: 0px;"
		));
		button.setOnMouseExited(e->button.setStyle(
				"-fx-font-size: 20px;" +
						"-fx-background-radius: 1em;" +
						"-fx-border-radius: 1em;" +
						"-fx-alignment: center;" +
						"-fx-display-caret: false;" +
						"-fx-text-fill: white;" +
						"-fx-border-color: white;" +
						"-fx-background-color: rgba(48,49,47,0.8);" +
						"-fx-label-padding: 0px;"
		));

		return button;
	}

	private BorderPane initialisation()	{
		BorderPane borderPane = new BorderPane();
		borderPane.setStyle(
				"-fx-background-image: url(baccarat_game1.jpeg);" +
				"-fx-background-repeat: no-repeat;" +
				"-fx-background-size: 100%;" +
				"-fx-opacity: 0.8"
		);
		return borderPane;
	}

	private TextField createTextField(Text string)	{
		TextField textField = new TextField();
		textField.setPromptText(string.getText());
		textField.setFocusTraversable(false);
		textField.setPrefWidth(300);
		textField.setPrefHeight(70);
		textField.setStyle("-fx-font-size: 30px;" +
				"-fx-background-radius: 2em;" +
				"-fx-border-radius: 2em;" +
				"-fx-alignment: center;" +
				"-fx-display-caret: false;" +
				"-fx-text-fill: white;" +
				"-fx-border-color: white;" +
				"-fx-background-color: rgba(45,48,50,0.8)");
		textField.setOnMouseEntered(event -> textField.setStyle(
				"-fx-font-size: 30px;" +
						"-fx-background-radius: 2em;" +
						"-fx-border-radius: 2em;" +
						"-fx-alignment: center;" +
						"-fx-display-caret: false;" +
						"-fx-text-fill: white;" +
						"-fx-border-color: white;" +
						"-fx-background-color: rgba(32,33,31,0.8);"
		));
		textField.setOnMouseExited(event -> textField.setStyle(
				"-fx-font-size: 30px;" +
						"-fx-background-radius: 2em;" +
						"-fx-border-radius: 2em;" +
						"-fx-alignment: center;" +
						"-fx-display-caret: false;" +
						"-fx-text-fill: white;" +
						"-fx-border-color: white;" +
						"-fx-background-color: rgba(45,48,50,0.8)"
		));
		return textField;
	}

	private Rectangle createCards()	{
		Rectangle card = new Rectangle();
		card.setX(50);
		card.setY(50);
		card.setWidth(180);
		card.setHeight(1.432 * 180);
		card.setArcWidth(20);
		card.setArcHeight(20);
		card.setStyle(
				"-fx-opacity: 0.5;" +
				"-fx-padding: 20;" +
				"-fx-background-color: rgba(87,87,87,0.78);" +
				"-fx-border-color: black;" +
				"-fx-border-width: 1em;" +
				"-fx-border-radius: 1em;");
		return card;
	}

	private void deleteData()	{
		playerBet.clear();
		bankerBet.clear();
		tieBet.clear();
		progress.clear();
		currentTieBet = 0;
		currentBankBet = 0;
		currentPlayerBet = 0;
		totalWinnings = 0;
		playerHandTotal = 0;
		bankHandTotal = 0;
		try {
			playerHand.clear();
		} catch (Exception ignored){ }
		try {
			bankerHand.clear();
		} catch (Exception ignored){ }
	}

	private void clearImagesAndArrays()	{
		playerCard1.setFill(Color.rgb(87,87,87,0.78));
		playerCard2.setFill(Color.rgb(87,87,87,0.78));
		playerCard3.setFill(Color.rgb(87,87,87,0.78));
		bankCard3.setFill(Color.rgb(87,87,87,0.78));
		bankCard2.setFill(Color.rgb(87,87,87,0.78));
		bankCard1.setFill(Color.rgb(87,87,87,0.78));

		playerHand.clear();
		bankerHand.clear();
		roundWinnings = 0;

		nowBankBet = 0;
		nowPlayerBet = 0;
		nowTieBet = 0;
	}
}
