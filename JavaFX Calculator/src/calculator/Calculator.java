package calculator;

import java.util.ArrayList;
/* 
 * Calculator.java
 * Made by: Joshua Ragusin
 * Last Edit: August 3, 2019
 * 
 * Contains the necessary methods and elements to be used
 * by a calculator. Is mainly used to hold and process the 
 * calculator's value and its display.
*/
public class Calculator {
	public static double value;
	public static String display;
	public static ArrayList<Double> inputs = new ArrayList<Double>();		// Used to store previous inputs

	/**
	 * reset:
	 * Used to perform a hard reset of the calculator. 
	 * Will clear the display and delete the calculator's
	 * memory of previous inputs
	 */
	public static void reset() {
		value = 0;
		inputs.clear();
	}
	
	/**
	 * getLastInput:
	 * @return the last numeric input into the calculator
	 * Called whenever the = Button is selected and an 
	 * operation has been set. Used to determine which other 
	 * number is to be used in the selected operation.
	 */
	public static double getLastInput() throws IndexOutOfBoundsException {
		if(inputs.size() > 0) {
			double last = inputs.get(0);
			inputs.remove(0);									// Remove the last input from history
			return last;
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	/**
	 * storeInput:
	 * @param double d
	 * Used to store user input into the inputs ArrayList.
	 */
	public static void storeInput(double d) {
		inputs.add(d);
	}
	
	/**
	 * setDisplay:
	 * @param double d
	 * will change the value of the calculator's
	 * display value. Will not change the screen's textField data
	 * as that is done in updateScreen() in Main.java.
	 */
	public static void setDisplay(double d) {
		display = Double.toString(d);
	}
	
	/**
	 * setValue:
	 * @param double d
	 * Used the change the value of calculator's value field.
	 */
	public static void setValue(double d) {
		value = d;
	}
	
	/**
	 * 
	 * @return String of the calculator's current value.
	 * Used in updateScreen() to change the screen's text value.
	 */
	public static String getDisplay() {
		return Double.toString(value);
	}
	
	/**
	 * 
	 * @return the current numeric value of the calculator's
	 * value field.
	 */
	public static double getValue() {
		return value;
	}
	
	/**
	 * changeSign:
	 * Changes the sign of the current value from positive to negative
	 * or vice versa.
	 */
	public static void changeSign() {
		value = value * (-1);
	}
	
	/**
	 * clear():
	 * performs a soft reset by clearing the display back to "0".
	 */
	public static void clear() {
		value = 0;
	}
	
	/**
	 * percent:
	 * @param double p
	 * @param double value
	 * @return a double corresponding to the percentage of value
	 * as specified by p.
	 */
	public static double percent(double p, double value) {
		return (value) * (p/100);
	}
}
