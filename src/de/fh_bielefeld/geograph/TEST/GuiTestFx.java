package de.fh_bielefeld.geograph.TEST;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.experimental.categories.Category;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;

import com.sun.webkit.dom.KeyboardEventImpl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.MoveTo;

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

//	@Test
	@Ignore
	public void checkSearchButton() {
		Button btn = find("#searchButtonArea");
		assertTrue(btn.getText().equals("Nach Koordinaten suchen"));
		sleep(1000);
	}
	
//	@Test
	@Ignore
	public void checkSearchButtonFunction() {
		Button btn = find("#searchButtonArea");
		click(btn);
		sleep(1000);
	}
	
//	@Test
	@Ignore
	//negativ Test
	public void checkLeftLatitudeValue() {
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
		
	}
	
//	@Test
	@Ignore
	public void checkLeftLongitudeValue() {
		TextField tField =find("#longitudeTextFieldL");
		sleep(500);
		click(tField);
		tField.setText("8.");
		sleep(500);
		tField.setText("8");
		sleep(500);
		tField.setText("");
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
		sleep(500);
	}
	
//	@Test
	@Ignore
	public void checkRightLatitudeValue() {
		TextField tField =find("#latitudeTextFieldR");

	}
	
	
//	@Test
	@Ignore
	public void checkRightLongitudeValue() {
		TextField tField =find("#longitudeTextFieldR");
	}
	
//	@Test
	@Ignore
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
		sleep(1000);
	}
	
//	@Ignore
	@Test 
	public void checkSlideControl() {
		Button btn = find("#searchButtonArea");
		Slider sliderX = find("#zoomSlider");

		double max = sliderX.getMax();
		double min = sliderX.getMin();
		
		click(btn);
		sleep(1000);
		
		for(double i = min; i <= max - 1; i += 0.01){
			sliderX.setValue(i);
			sleep(100);
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
		sleep(1000);
		
		for(double i = max - 1; i <= max; i += 0.01){
			sliderX.setValue(i);
			sleep(100);
		}
		
		sleep(1000);
		press(MouseButton.PRIMARY);
		moveBy( 0, 200 );
		moveBy( 200, 0);
		moveBy( 0, -200);
		moveBy(-200, 0);
		moveBy(0, 200);
		release(MouseButton.PRIMARY);
		sleep(1000);

	}
	
	@Test
	public void checkFileTab(){
		checkSearchButtonFunction();
		Node tab = find("#FileTab");
		click(tab);
		
	}
	
	@Test
	public void checkMiddlePointTab(){
		checkSearchButtonFunction();
		Node tab = find("#TabRadius");
		click(tab);
	}
	
	@Ignore
//	@Test
	public void checkBreitengradLabel() {
		TextField lbl = find("#longitudeTextFieldL");
		assertTrue(lbl.getText().equals("52.294"));	
	}
}
