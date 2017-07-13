package de.fh_bielefeld.geograph.TEST;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;


/**
 * Tests for the GUI
 */
@Category(TestFX.class)
public class GuiTestFx extends GuiTest {

	/**
	 * This method will open the GUI for every test.
	 */
	@Override
	protected Parent getRootNode() {
		Parent parent = null;
		try {
			parent = FXMLLoader.load(getClass().getResource("../GUI/OSMStreetGUI.fxml"));
			stage.setScene(new Scene(parent, 1300, 700));
			stage.setFullScreen(true);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return parent;
	}
	
	/**
	 * A method to delay the tests for the given amount of milliseconds.
	 * 
	 * @param time milliseconds that the test will be delayed
	 */
	private void sleep(int time){
		try {
			TimeUnit.MILLISECONDS.sleep(time);	
		} catch (Exception e) {
		}
	}
	
	/**
	 * A method to click the search button which starts the OSM-Request. 
	 */
	private void clickSearchButton(){
		Button btn = find("#searchButtonArea");
		click(btn);
	}
	
	/**
	 * Returns a psuedo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimim value
	 * @param max Maximim value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

	    // Usually this can be a field rather than a method variable
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	/**
	 * This test will check for the correct text on the search button.
	 */
	@Test
	public void checkSearchButton() {
		Button btn = find("#searchButtonArea");
		assertTrue(btn.getText().equals("Nach Koordinaten suchen"));
		sleep(2000);
	}
	
	/**
	 * This test will try to make an OSM-Request with a wrong left latitude.
	 */
	@Test
	public void checkLeftLatitudeValueNegativ() {
		TextField tField =find("#latitudeTextFieldL");
		sleep(500);
		click(tField);
		tField.setText("52.29");
		sleep(500);
		tField.setText("52.2");
		sleep(500);
		tField.setText("52.");
		sleep(500);
		tField.setText("52");
		sleep(500);
		tField.setText("5");
		sleep(500);
		tField.setText("54");
		sleep(500);
		tField.setText("54.");
		sleep(500);
		tField.setText("54.2");
		sleep(500);
		tField.setText("54.23");
		sleep(500);
		Button buttonlbl = find("#searchButtonArea");
		click(buttonlbl);
		sleep(500);
		type(KeyCode.ENTER);
		sleep(2000);		
	}
	
	/**
	 * This test will try to make an OSM-Request with a wrong left longitude.
	 */
	@Test
	public void checkLeftLongitudeValueNegativ() {
		TextField tField =find("#longitudeTextFieldL");
		sleep(500);
		click(tField);
		tField.setText("8.");
		sleep(500);
		tField.setText("8");
		sleep(500);
		tField.setText("");
		sleep(500);
		tField.setText("9");
		sleep(500);
		tField.setText("9.");
		sleep(500);
		tField.setText("9.1");
		sleep(500);
		tField.setText("9.12");
		sleep(500);
		Button buttonlbl = find("#searchButtonArea");
		click(buttonlbl);
		sleep(1000);
		type(KeyCode.ENTER);
		sleep(2000);
	}
	
	/**
	 * This test will try to make an OSM-Request with a wrong right longitude.
	 */
	@Test
	public void checkRightLongitudeValueNegativ() {
		TextField tField =find("#longitudeTextFieldR");
		sleep(500);
		click(tField);
		tField.setText("8.");
		sleep(500);
		tField.setText("8");
		sleep(500);
		tField.setText("");
		sleep(500);
		tField.setText("9");
		sleep(500);
		tField.setText("9.");
		sleep(500);
		tField.setText("9.3");
		sleep(500);
		tField.setText("9.32");
		sleep(500);
		Button buttonlbl = find("#searchButtonArea");
		click(buttonlbl);
		sleep(1000);
		type(KeyCode.ENTER);
		sleep(2000);
	}
	
	/**
	 * This test will try to make an OSM-Request with a wrong right latitude.
	 */
	@Test
	public void checkRightLatitudeValueNegativ() {
		TextField tField =find("#latitudeTextFieldR");
		sleep(500);
		click(tField);
		tField.setText("52.29");
		sleep(500);
		tField.setText("52.2");
		sleep(500);
		tField.setText("52.");
		sleep(500);
		tField.setText("52");
		sleep(500);
		tField.setText("5");
		sleep(500);
		tField.setText("53");
		sleep(500);
		tField.setText("53.");
		sleep(500);
		tField.setText("53.1");
		sleep(500);		
		tField.setText("53.12");
		sleep(500);
		Button buttonlbl = find("#searchButtonArea");
		click(buttonlbl);
		sleep(1000);
		type(KeyCode.ENTER);
		sleep(2000);
	}
	
	/**
	 * This test will try to move the map after the map is loaded.
	 */
	@Test
	public void checkMapMove() {
		clickSearchButton();
		moveBy( 500, 0 );
		press(MouseButton.PRIMARY);
		moveBy( 0, 200 );
		moveBy( 200, 0);
		moveBy( 0, -200);
		moveBy(-200, 0);
		moveBy(0, 200);
		release(MouseButton.PRIMARY);
		sleep(2000);
	}

	/**
	 * This test will move the slider to zoom in on the map.
	 * It will also check if the map is still moveable after zooming in.
	 */
	@Test 
	public void checkSlideControl() {
		Button btn = find("#searchButtonArea");
		Slider sliderX = find("#zoomSlider");

		double max = sliderX.getMax();
		double min = sliderX.getMin();
		click(btn);
		sleep(1000);
		for(double i = min; i <= max - 1; i += 0.02){
			sliderX.setValue(i);
			sleep(50);
		}
		
		sleep(500);
		moveBy( 500, 0 );
		press(MouseButton.PRIMARY);
		moveBy( 0, 200 );
		moveBy( 200, 0);
		moveBy( 0, -200);
		moveBy(-200, 0);
		moveBy(0, 200);
		release(MouseButton.PRIMARY);
		sleep(2000);
		
		for(double i = max - 1; i <= max; i += 0.02){
			sliderX.setValue(i);
			sleep(50);
		}
		
		sleep(1000);
		press(MouseButton.PRIMARY);
		moveBy( 0, 200 );
		moveBy( 200, 0);
		moveBy( 0, -200);
		moveBy(-200, 0);
		moveBy(0, 200);
		release(MouseButton.PRIMARY);
		sleep(2000);
	}

	/**
	 * This test will open the file tab and then load a saved map.
	 */
	@Test
	public void checkFileTabMapLoad(){	
		
		click("#FileTab");
		Button btn = find("#fileChooserButton");
		sleep(1000);
		click(btn);
		sleep(1000);
		type("Beispiel.osm");
		sleep(500);
		type(KeyCode.ENTER);
		sleep(2000);
	}			

	/**
	 * This test will save the currently loaded map.
	 */
	@Test
	public void checkFileTabMapSave(){
		int min = 0;
		int max = 99;
		clickSearchButton();
		
		click("#FileTab");
		Button btn = find("#fileSaveButton");
		sleep(1000);
		click(btn);
		sleep(1000);
		type("newOsm" + randInt(min, max) + ".osm");
		sleep(500);
		type(KeyCode.ENTER);
		sleep(2000);	
	}

	/**
	 * This test will go to the middlepoint tab and click the center node button.
	 */
	@Test
	public void checkMiddlePointTabCenterNode(){	
		clickSearchButton();
		
		click("#TabRadius");
		Button btn = find("#searchButtonRadius");
		sleep(1000);
		click(btn);
		sleep(2000);	
	}

	/**
	 * This test will try to center on a node with a wrong latitude.
	 */
	@Test
	public void checkMiddlePointTabCenterNodeLatitudeNegativ(){	
		clickSearchButton();
		
		click("#TabRadius");
		Button btn = find("#searchButtonRadius");
		TextField tField = find("#radiusLatitude");
		click(tField);
		sleep(1000);
		tField.clear();
		sleep(1000);
		tField.setText("52.300");
		sleep(1000);
		click(btn);
		sleep(1000);
		type(KeyCode.ENTER);
		sleep(2000);
		
	}

	/**
	 * This test will try to center on a node with a wrong longitude.
	 */
	@Test
	public void checkMiddlePointTabCenterNodeLongitudeNegativ(){
		clickSearchButton();
		
		click("#TabRadius");
		Button btn = find("#searchButtonRadius");
		TextField tField = find("#radiusLongitude");
		click(tField);
		sleep(1000);
		tField.clear();
		sleep(1000);
		tField.setText("9.25");
		sleep(1000);
		click(btn);
		sleep(1000);
		type(KeyCode.ENTER);
		sleep(2000);	
	}
}
