package de.fh_bielefeld.geograph.TEST;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

@Category(TestFX.class)
public class GuiTestFx extends GuiTest {

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
	
	public void sleep(int time){
		try {
			TimeUnit.MILLISECONDS.sleep(time);	
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void clickSearchButton(){
		Button btn = find("#searchButtonArea");
		click(btn);
	}
	
	
	@Test
	public void checkSearchButton() {
		Button btn = find("#searchButtonArea");
		assertTrue(btn.getText().equals("Nach Koordinaten suchen"));
		sleep(2000);
	}
	
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
	
	@Test
	public void checkMapMove() {
		Button btn = find("#searchButtonArea");
		click(btn);
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
		
		sleep(1000);
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

	@Test
	public void checkFileTabMapLoad(){
		
		clickSearchButton();
		
		click("#FileTab");
		Button btn = find("#fileChooserButton");
		sleep(1000);
		click(btn);
		sleep(1000);
		type("Dateiname.osm");
		sleep(500);
//		type(KeyCode.ENTER);
		type(KeyCode.CANCEL);
		sleep(2000);
		
		}			

	@Test
	public void checkFileTabMapSave(){
		
		clickSearchButton();
		
		click("#FileTab");
		Button btn = find("#fileSaveButton");
		sleep(1000);
		click(btn);
		sleep(1000);
		type("Dateiname.osm");
		sleep(500);
//		type(KeyCode.ENTER);
		type(KeyCode.CANCEL);
		sleep(2000);	
	}

	@Test
	public void checkMiddlePointTabCenterNode(){
		
		clickSearchButton();
		
		click("#TabRadius");
		Button btn = find("#searchButtonRadius");
		sleep(1000);
		click(btn);
		sleep(2000);
		
	}

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
