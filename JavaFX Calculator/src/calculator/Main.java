package calculator;
/*
 * Calculator Main.java
 * Made by: Joshua Ragusin
 * Date of Last Edit: August 3, 2019
 * 
 * This program is a basic 4-function calculator made with a JavaFX GUI.
 * As of this time, the program can only handle addition, subtraction, 
 * multiplication, and division, however there are plans to add more 
 * functionality such as trigonometric functions, user specified display
 * settings, angle settings, and history.
 * 
 * Known Issues:
 * Resizing, Decimal point display
*/
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import calculator.Calculator;

public class Main extends Application {
	
	private final String[][] buttonTemplate = {
			{ "exp", "+/-", "%", "CE", "C" },
			{ "ln", "7", "8", "9", "/" },
			{ "sin", "4", "5", "6", "*" },
			{ "cos", "1", "2", "3", "-" },
			{ "tan", "0", ".", "=", "+" }
	};
	
	private TextField screen;
	
	private enum TrigOps {SIN, COS, TAN, EXP, LN };								// Contains all trig operations 
	
	private enum Operations { ADD, SUBTRACT, MULTIPLY, DIVIDE, NONE };			// Contains all operations this   
																				// calculator can perform
	
	private static Operations op = Operations.NONE;								// The current operation being processes
																				// Default is NONE
	private boolean radians = true;												// Used to determine the angle measurement 
																				// for the trig functions. Default is in radians
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("JavaFX Calculator");				
		primaryStage.centerOnScreen();
		
		this.screen = createScreen();												// Initializing class' display screen
		TilePane buttons = createButtons();
		screen.prefWidthProperty().bind(buttons.widthProperty());
		VBox layout = createLayout(this.screen, buttons, primaryStage);				// Initializing calculator layout
		Scene s = new Scene(layout);
		primaryStage.setScene(s);
		primaryStage.show();
		
	}
	/** 
	 * createLayout:
	 * @param TextField screen
	 * @param TilePane buttons
	 * @param Stage primaryStage
	 * @return VBox containing the screen, and calculator buttons
	 * A VBox is used as the layout for the calculator's components.
	 */
	public VBox createLayout(TextField screen, TilePane buttons, Stage primaryStage) {
		VBox layout = new VBox(20);
		layout.setAlignment(Pos.CENTER);
		layout.setFillWidth(false);													// Reverting this to true messes with button positioning
		
		MenuBar menuBar = new MenuBar();
		Menu angle = new Menu("Angle");
		
		RadioMenuItem deg = new RadioMenuItem("Degrees");
		deg.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				radians = false;
			}
		});
		
		RadioMenuItem rad = new RadioMenuItem("Radians");
		rad.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				radians = true;
			}
		});
		
		ToggleGroup toggleGroup = new ToggleGroup();
		toggleGroup.getToggles().addAll(deg, rad);
		
		angle.getItems().addAll(deg, rad);
		menuBar.getMenus().add(angle);
		
		layout.getChildren().addAll(menuBar, screen, buttons);
		return layout;
	}
	
	/** 
	 * @return a TextField that will be used to display calculations and 
	 * the current value in the calculator to the user.
	 */
	public TextField createScreen() {
		TextField screen = new TextField();
		screen.setStyle("-fx-background-color: white; -fx-font-size: 20");
		screen.setAlignment(Pos.CENTER_RIGHT);
		screen.setEditable(false);
		screen.setText("0");
		return screen;
	}
	
	
	/**
	 * createButtons:
	 * @return a TilePane consisting of all the buttons needed on the GUI.
	 * The TilePane will have a button for each number 0-9 and command.
	 */
	public TilePane createButtons() {
		TilePane ret = new TilePane();
		ret.setVgap(7);
		ret.setHgap(7);
		ret.setPadding(new Insets(20,20,20,20));
		String[][] temp = this.buttonTemplate;
		ret.setPrefColumns(temp[0].length);
		ArrayList<Button> bList = new ArrayList<Button>();				// ArrayList that will contain all buttons on the GUI
		for(String[] t : temp) {
			for(String s : t) {
				 Button b = makeButton(s);								// Sends each button to be made depending on whether 
				 bList.add(b);											// it is a numeric button or a command button
			}
		}
		ret.getChildren().addAll(bList);
		return ret;
	}
	
	/**
	 * makeButton:
	 * @param String s
	 * @return Button with a set specific ActionEvent depending on its 
	 * numeric value or command.
	 */
	public Button makeButton(String s) {
		Button b = new Button();
		b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);				
		if(isNumeric(s)) {												// Button is numeric (0-9)
			b.setText(s);											
			setActionNumeric(b, s);
			
		}
		else {
			b.setText(s);												// Button is a command (+,-, ect)
			setActionSymbol(b, s);
		}
		
		return b;														// Each button returned should be fully 
																		// initialized with its specific ActionEvent
		
	}
	
	/**
	 * setActionSymbol:
	 * @param Button b
	 * @param String s
	 * All buttons that are passed into this method are to be used as 
	 * commands and are not numeric. In this method, each button is routed
	 * to another method to implement its ActionEvent accordingly.
	 */
	public void setActionSymbol(Button b, String s) {
		switch(s) {
			case "+":
				setActionAddition(b);
				break;
			case "-":
				setActionSubtraction(b);
				break;
			case "*":
				setActionMultiply(b);
				break;
			case "/":
				setActionDivide(b);
				break;
			case "C":
				setActionClear(b);
				break;
			case "=":
				setActionEquals(b);
				break;
			case "+/-":
				setActionPlusMinus(b);
				break;
			case "%":
				setActionPercent(b);
				break;
			case "CE":
				setActionHardClear(b);
				break;
			case "exp":
				setActionTrig(b, TrigOps.EXP);
				break;
			case "ln":
				setActionTrig(b, TrigOps.LN);
				break;
			case "sin":
				setActionTrig(b, TrigOps.SIN);
				break;
			case "cos":
				setActionTrig(b, TrigOps.COS);
				break;
			case "tan":
				setActionTrig(b, TrigOps.TAN);
				break;
		}
	}
	
	/**
	 * setActionAddition:
	 * @param Button b
	 * Utilized by the Addition button. Sets the class' operation to ADD
	 * , stores the current calculator's displayed value, and clears the screen
	 * to its default setting (0).
	 */
	private void setActionAddition(Button b) {
		b.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.op = Operations.ADD;
				Calculator.storeInput(Calculator.value);
				clearScreen();
			}
		});
	}
	
	/**
	 * setActionSubtraction:
	 * @param Button b
	 * Utilized by the Subtraction button. Sets the class' operation, stores the
	 * current displayed value, and clears screen to the default value.
	 */
	private void setActionSubtraction(Button b) {
		b.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.op = Operations.SUBTRACT;
				Calculator.storeInput(Calculator.value);
				clearScreen();
			}
		});
	}
	
	/**
	 * setActionMultiply:
	 * @param Button b
	 * Utilized by the Multiplication button. Sets the class' operation, stores the 
	 * current displayed value, and resets the screen to its default value.
	 */
	private void setActionMultiply(Button b) {
		b.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.op = Operations.MULTIPLY;
				Calculator.storeInput(Calculator.value);
				clearScreen();
			}
		});
	}
	
	/**
	 * setActionDivide:
	 * @param Button b
	 * Utilized by the Divide button. Sets the class' operation, stores the current
	 * displayed value, and resets the screen to the default value.
	 */
	private void setActionDivide(Button b) {
		b.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.op = Operations.DIVIDE;
				Calculator.storeInput(Calculator.value);
				clearScreen();
			}
		});
	}
	
	/**
	 * setActionClear
	 * @param Button b
	 * Utilized by the "C" Button. Performs a soft reset on the calculator.
	 * The current display is set to "0" but the current expression will continue to 
	 * be processed. This method does not delete previous inputs from storage.
	 */
	private void setActionClear(Button b) {
		b.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Calculator.clear();
				updateDisplay();
			}
		});
	}
	
	/**
	 * setActionEquals
	 * @param Button b
	 * Utilized by the = Button. Performs the appropriate write to the 
	 * calculator's display depending on the class' current operation.
	 * Also updates the display to show the answer to the operation.
	 */
	private void setActionEquals(Button b) {
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				double ans;
				switch(Main.op) {
					case ADD: 
						ans = Calculator.getLastInput() + Calculator.value;
						Calculator.setValue(ans);
						Calculator.storeInput(ans);
						updateDisplay();
						break;
					case SUBTRACT:
						ans = Calculator.getLastInput() - Calculator.value;
						Calculator.setValue(ans);
						Calculator.storeInput(ans);
						updateDisplay();
						break;
					case MULTIPLY:
						ans = Calculator.getLastInput() * Calculator.value;
						Calculator.setValue(ans);
						Calculator.storeInput(ans);;
						updateDisplay();
						break;
					case DIVIDE:
						ans = Calculator.getLastInput() / Calculator.value;
						Calculator.setValue(ans);
						updateDisplay();
						break;
					case NONE:														// If no operation is selected prior to selecting the = button,	exit
						break;
					default:														// Default only here for compiler's happiness
						break;
				}
			}
		});
	}
	
	/**
	 * setActionPlusMinus:
	 * @param Buton b
	 * Sets the action for the "+/-" button. Used to change the sign of the current display 
	 * either from positive to negative or vice verse. Calls a sign change method in 
	 * Calculator.java.
	 */
	private void setActionPlusMinus(Button b) {
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Calculator.changeSign();
				updateDisplay();
			}
		});
	}
	
	/**
	 * setActionPercent:
	 * @param Button b
	 * Sets the action for the "%" button. Is used to perform the 4 basic functions
	 * with percentages of the previous input. For example, the input
	 * "4 + 50%" will simplify to "4 + 2" which will produce "6" as a result.
	 * If "%" is pressed when there is no previous input, then "0" will display.
	 */
	private void setActionPercent(Button b) {
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					double amount = Calculator.getLastInput();
					double result = Calculator.percent(Calculator.value, amount);		// Current percentage value is in calculator's display
					Calculator.storeInput(amount);										// Puts the previous input back into storage for use in the operation
					Calculator.setValue(result);
				}
				catch(IndexOutOfBoundsException e) {
					Calculator.setValue(0);												// Either caused by no previous input or no number 
				}																		// entered before pressing the '%' button. Should continue current expression line
				updateDisplay();
			}
		});
	}
	
	/**
	 * setActionHardClear:
	 * @param Button b
	 * Sets the action for the "CE" button. When pressed will perform
	 * a hard clear and erase all stored inputs.
	 */
	private void setActionHardClear(Button b) {
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Calculator.reset();
				updateDisplay();
			}
		});
	}
	
	/**
	 * setActionNumeric:
	 * @param Button b
	 * @param String s
	 * Sets the ActionEvent for all numeric buttons. Updates the display accordingly
	 * depending on which numeric button was selected.
	 */
	private void setActionNumeric(Button b, String s) {
		b.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				double buttonValue = Double.parseDouble(s);
				double currentValue = Calculator.getValue();
				if(currentValue == 0) {												// Either the display was reset, or the previous input 
					Calculator.setDisplay(buttonValue);								// has already been stored. Regardless, only display this
					Calculator.setValue(buttonValue);								// button's value.
				}
				else {
					double newValue = (currentValue*10) + buttonValue;				// A previous button has been pressed. So add this button's 
					Calculator.setValue(newValue);									// value to the end of the string of numbers. This is done 
					Calculator.setDisplay(newValue);								// by multiplying its current value by 10 and adding the 
				}																	// buttons value.
				updateDisplay();												
			}
		});
	}
	
	/**
	 * setActionTrig:
	 * @param Button b
	 * @param TrigOps o
	 * Initializes an ActionEvent for all of the buttons associated with trigonometry.
	 * The specific functionality of each Action Event is determined by the TrigOps 
	 * that was passed into the method. Based on the TrigOps o, each button is initialized. 
	 */
	private void setActionTrig(Button b, TrigOps o) {
		switch(o) {
			case SIN:
				b.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						double sin = 0;
						if(radians) {
							sin = Math.sin(Calculator.value);
						}
						else {
							sin = Math.sin(Math.toDegrees(Calculator.value));
						}
						Calculator.setValue(sin);					// Each case sets the Calculator's display
						updateDisplay();							// and updates the display to reflect the new calculation
					}
				});
				break;
				
			case COS:
				b.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						double cos = 0;
						if(radians) {
							cos = Math.cos(Calculator.value);
						}
						else {
							cos = Math.acos(Math.toDegrees(Calculator.value));
						}
						Calculator.setValue(cos);
						updateDisplay();
					}
				});
				break;
				
			case TAN:
				b.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						double tan = 0;
						if(radians) {
							tan = Math.tan(Calculator.value);
						}
						else {
							tan = Math.tan(Math.toDegrees(Calculator.value));
						}
						Calculator.setValue(tan);
						updateDisplay();
					}
				});
				break;
				
			case EXP:
				b.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						double exp = Math.exp(Calculator.value);
						Calculator.setValue(exp);
						updateDisplay();
					}
				});
				break;
				
			case LN:
				b.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
							double ln = Math.log(Calculator.value);
							Calculator.setValue(ln);
							updateDisplay();
					}
				});
				break;
		}
	}
	
	/**
	 * updateDisplay:
	 * Used only to force the calculator to update its
	 * display according to the calculator's display value.
	 * Only used when the display has changed due to new inputs
	 * or a calculation.
	 */
	private void updateDisplay() {
		this.screen.setText(Calculator.getDisplay());
	}
	
	/**
	 * clearScreen:
	 * Used to set the screen to its default value 
	 * of zero.
	 */
	private void clearScreen() {
		Calculator.setValue(0);
		updateDisplay();
	}
	
	/**
	 * isNUmeric:
	 * @param String s
	 * @return true if the String is a numeric value, false
	 * otherwise. Used to determine if a button is numeric
	 * or has a command.
	 */
	private boolean isNumeric(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
}
