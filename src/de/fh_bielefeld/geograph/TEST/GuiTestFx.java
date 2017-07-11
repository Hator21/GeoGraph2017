package de.fh_bielefeld.geograph.TEST;

import de.fh_bielefeld.geograph.API.OSMApi;
import de.fh_bielefeld.geograph.API.Exception.InvalidAPIRequestException;
import de.fh_bielefeld.geograph.GUI.*;
import static org.junit.Assert.*;

import javafx.application.Application;
import javafx.stage.Stage;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;


/*
  public class GuiTestFX extends Main {
  @Override
  public void start(Stage stage) throws Exception {
      stage.setTitle("Hello World");
      stage.show();
  }
*/
public class GuiTestFx extends Application {

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		arg0.setTitle("Hello");
		arg0.show();
	}
	@Test
	public void TestHello() {
		String a = "Hello";
		assertTrue(true);
	}
	
}

