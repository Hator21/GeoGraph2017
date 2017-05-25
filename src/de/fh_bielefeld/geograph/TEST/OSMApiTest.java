package de.fh_bielefeld.geograph.TEST;

import de.fh_bielefeld.geograph.API.OSMApi;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Tests for the OSMApi class
 */
public class OSMApiTest{
	Document testDocument;
	
	/**
	 * This method will initialize the testDocument before every testcase.
	 */
	@Before
	public void initializeDocument(){
		this.testDocument = null;
	}
	
	/**
	 * This test will invoke the getNodeWithID method with an invalid node.
	 */
	@Ignore("not ready yet")
	@Test
	public void getNodeWithIDGivenAWrongNode(){
		try{
			this.testDocument = OSMApi.getNodeWithID(-120);
		} 
		catch (SAXException e){
			assertTrue(true);
		}
		catch(IOException e){
			fail();
		}
		catch(ParserConfigurationException e){
			assertTrue(true);
		}
		assertNotNull(testDocument);
	}
	
	/**
	 * This test will invoke the getNodeWithID method with a valid node.
	 */
	@Test
	public void getNodeWithIDGivenValidNode(){
		long validNode = 54;
		try{
			this.testDocument = OSMApi.getNodeWithID(validNode);
		}
		catch(IOException e){
			fail("There's most likely no internet connection!");
		}
		catch(ParserConfigurationException e){
			fail("For some reason the method failed!");
		}
		catch(SAXException e){
			fail("For some reason the method failed!");
		}
		assertNotNull(this.testDocument);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with a range over 0.25.
	 * The tested method is expected to throw an IOException.
	 */
	@Test
	public void getBoundingBoxOfRangeGivenTooHighRange(){
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(51.9032375, 8.3857535, 1);
		}
		catch(IOException e){
			assertTrue("The Exception we expected to get", true);
		}
		catch(ParserConfigurationException e){
			fail("Not the Exception we expected!");
		}
		catch(SAXException e){
			fail("Not the Exception we expected!");
		}
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with a range below 0.
	 * The tested method is expected to throw an IOException.
	 */
	@Test
	public void getBoundingBoxOfRangeGivenNegativeRange(){
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(51.9032375, 8.3857535, -1);
		}
		catch(IOException e){
			assertTrue("The Exception we expected to get.", true);
		}
		catch(ParserConfigurationException e){
			fail("Not the Exception we expected!(ParserConfigurationException)");
		}
		catch(SAXException e){
			fail("Not the Exception we expected!(SAXException)");
		}
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with the maximum range 0.25.
	 */
	@Test
	public void getBoundingBoxOfRangeGivenMaximumRange(){
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(51.9032375, 8.3857535, 0.25);
		}
		catch(IOException e){
			fail("There shouldn't be an IOException!");
		}
		catch(ParserConfigurationException e){
			fail("There shouldn't be a ParserConfigurationException!");
		}
		catch(SAXException e){
			fail("There shouldn't be a SAXException!");
		}
		assertNotNull(this.testDocument);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with the minimum range 0.
	 */
	@Test
	public void getBoundingBoxOfRangeGivenZeroRange(){
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(51.9032375, 8.3857535, 0);
		}
		catch(IOException e){
			fail("There shouldn't be an IOException!");
		}
		catch(ParserConfigurationException e){
			fail("There shouldn't be a ParserConfigurationException!");
		}
		catch(SAXException e){
			fail("There shouldn't be a SAXException!");
		}
		assertNotNull(this.testDocument);
	}
	
	/**
	 * This test will invoke the getBoundingBoxOfRange method with a valid range and valid latitude and longitude.
	 */
	@Test
	public void getBoundingBoxOfRangeGivenValidArguments(){
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(51.9032375, 8.3857535, 0.1);
		}
		catch(IOException e){
			fail("There shouldn't be an IOException!");
		}
		catch(ParserConfigurationException e){
			fail("There shouldn't be a ParserConfigurationException!");
		}
		catch(SAXException e){
			fail("There shouldn't be a SAXException!");
		}
		assertNotNull(this.testDocument);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with the same value for latitude and longitude.
	 */
	@Test
	public void getBoundingBoxOfRangeGivenSameCoordinates(){
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(51.9032375, 51.9032375, 0.1);
		}
		catch(IOException e){
			fail("There shouldn't be an IOException!");
		}
		catch(ParserConfigurationException e){
			fail("There shouldn't be a ParserConfigurationException!");
		}
		catch(SAXException e){
			fail("There shouldn't be a SAXException!");
		}
		assertNotNull(this.testDocument);
	}
	
	/**
	 * This test will invoke the getBoundingBoxOfRange method with a Latitude over 90.
	 */
	@Test
	public void getBoundingBoxOfRangeGivenWrongLatitude(){
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(91, 8.3857535, 0.1);
		}
		catch(IOException e){
			assertTrue("The Exception we expected to get.", true);
		}
		catch(ParserConfigurationException e){
			fail("There shouldn't be a ParserConfigurationException!");
		}
		catch(SAXException e){
			fail("There shouldn't be a SAXException!");
		}
	}
	
	/**
	 * This test will invoke the getBoundingBoxOfRange method with a Latitude over 180.
	 */
	@Test
	public void getBoundingBoxOfRangeGivenWrongLongitude(){
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(51.9032375, 181, 0.1);
		}
		catch(IOException e){
			assertTrue("The Exception we expected to get.", true);
		}
		catch(ParserConfigurationException e){
			fail("There shouldn't be a ParserConfigurationException!");
		}
		catch(SAXException e){
			fail("There shouldn't be a SAXException!");
		}
	}
	
	/**
	 * This test will invoke the getBoundingBoxLatLong method with two different and valid coordinates.
	 */
	@Test
	public void getBoundinBoxLatLongGivenValidCoordinates(){
		try{
			this.testDocument = OSMApi.getBoundingBoxLatLong(52.520007, 13.204953999999975,
					52.720007, 13.404953999999975);
		}
		catch(IOException e){
			fail("There shouldn't be an IOException!");
		}
		catch(ParserConfigurationException e){
			fail("There shouldn't be a ParserConfigurationException!");
		}
		catch(SAXException e){
			fail("There shouldn't be a SAXException!");
		}
		assertNotNull(this.testDocument);
	}
	
	/**
	 * This test will invoke the getBoundingBoxLatLong method with two same coordinates.
	 */
	@Test
	public void getBoundinBoxLatLongGivenTwoSameCoordinates(){
		try{
			this.testDocument = OSMApi.getBoundingBoxLatLong(52.520007, 13.204953999999975,
					52.520007, 13.204953999999975);
		}
		catch(IOException e){
			fail("There shouldn't be an IOException!");
		}
		catch(ParserConfigurationException e){
			fail("There shouldn't be a ParserConfigurationException!");
		}
		catch(SAXException e){
			fail("There shouldn't be a SAXException!");
		}
		assertNotNull(this.testDocument);
	}
	
	/**
	 * This test will invoke the getBoundingBoxLatLong method with a wrong minLatitude.
	 */
	@Test
	public void getBoundinBoxLatLongGivenWrongMinLatitude(){
		try{
			this.testDocument = OSMApi.getBoundingBoxLatLong(91, 13.204953999999975,
					52.720007, 13.204953999999975);
		}
		catch(IOException e){
			assertTrue("The Exception we expected to get.", true);
		}
		catch(ParserConfigurationException e){
			fail("There shouldn't be a ParserConfigurationException!");
		}
		catch(SAXException e){
			fail("There shouldn't be a SAXException!");
		}
	}
	
	/**
	 * This test will invoke the getBoundingBoxLatLong method with a wrong maxLatitude.
	 */
	@Test
	public void getBoundinBoxLatLongGivenWrongMaxLatitude(){
		try{
			this.testDocument = OSMApi.getBoundingBoxLatLong(52.520007, 13.204953999999975,
					91, 13.204953999999975);
		}
		catch(IOException e){
			assertTrue("The Exception we expected to get.", true);
		}
		catch(ParserConfigurationException e){
			fail("There shouldn't be a ParserConfigurationException!");
		}
		catch(SAXException e){
			fail("There shouldn't be a SAXException!");
		}
	}
	
	/**
	 * This test will invoke the getBoundingBoxLatLong method with a wrong minLongitude.
	 */
	@Test
	public void getBoundinBoxLatLongGivenWrongMinLongitude(){
		try{
			this.testDocument = OSMApi.getBoundingBoxLatLong(52.520007, 181,
					52.520007, 13.204953999999975);
		}
		catch(IOException e){
			assertTrue("The Exception we expected to get.", true);
		}
		catch(ParserConfigurationException e){
			fail("There shouldn't be a ParserConfigurationException!");
		}
		catch(SAXException e){
			fail("There shouldn't be a SAXException!");
		}
	}
	
	/**
	 * This test will invoke the getBoundingBoxLatLong method with a wrong maxLongitude.
	 */
	@Test
	public void getBoundinBoxLatLongGivenWrongMaxLongitude(){
		try{
			this.testDocument = OSMApi.getBoundingBoxLatLong(52.520007, 13.204953999999975,
					52.520007, 181);
		}
		catch(IOException e){
			assertTrue("The Exception we expected to get.", true);
		}
		catch(ParserConfigurationException e){
			fail("There shouldn't be a ParserConfigurationException!");
		}
		catch(SAXException e){
			fail("There shouldn't be a SAXException!");
		}
	}
}
