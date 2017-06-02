package de.fh_bielefeld.geograph.TEST;

import de.fh_bielefeld.geograph.API.OSMApi;
import de.fh_bielefeld.geograph.API.Exception.InvalidAPIRequestException;

import static org.junit.Assert.*;
//import org.junit.After;
//import org.junit.AfterClass;
import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Tests for the OSMApi class
 */
public class OSMApiTest{
	double latitude = 0;
	double longitude = 0;
	double range = 0;
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
	 * @throws InvalidAPIRequestException 
	 */
	@Test (expected = InvalidAPIRequestException.class)
	public void getNodeWithIDGivenAWrongNode() {
		long notValidNode = -120;
		try{
			this.testDocument = OSMApi.getNodeWithID(notValidNode);
		}
		catch(InvalidAPIRequestException ex) {
			assertTrue("The Exception we expected to get", true);
		}
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
		catch(InvalidAPIRequestException ex){
			fail("api failure");
		}
		assertNotNull(this.testDocument);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with a range over 0.25.
	 * The tested method is expected to throw an IOException.
	 */
	@Test (expected = InvalidAPIRequestException.class)
	public void getBoundingBoxOfRangeGivenTooHighRange(){
		latitude = 51.9032375;
		longitude = 8.3857535;
		range = 1;
		
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
		}
		catch(InvalidAPIRequestException ex){
			assertTrue("The Exception we expected to get", true);
		}
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with a range below 0.
	 * The tested method is expected to throw an IOException.
	 */
	@Test (expected = InvalidAPIRequestException.class)
	public void getBoundingBoxOfRangeGivenNegativeRange(){
		latitude = 51.9032375;
		longitude = 8.3857535;
		range = -1;
		
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
		}
		catch(InvalidAPIRequestException ex){
			assertTrue("The Exception we expected to get.", true);
		}
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with the maximum range 0.25.
	 */
	@Test
	public void getBoundingBoxOfRangeGivenMaximumRange(){
		latitude = 51.9032375;
		longitude = 8.3857535;
		range = 0.25;
		
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an IOException!");
		}
		assertNotNull(this.testDocument);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with the minimum range 0.
	 * @deprecated
	 */
	@Test
	public void getBoundingBoxOfRangeGivenZeroRange(){
		latitude = 51.9032375;
		longitude = 8.3857535;
		range = 0;
		
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an InvalidApiRequestException!");
		}
		assertNotNull(this.testDocument);
	}
	
	/**
	 * This test will invoke the getBoundingBoxOfRange method with a valid range and valid latitude and longitude.
	 */
	@Test
	public void getBoundingBoxOfRangeGivenValidArguments(){
		latitude = 51.9032375;
		longitude = 8.3857535;
		range = 0.1;
		
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an InvalidAPIRequestException!");
		}
		assertNotNull(this.testDocument);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with the same value for latitude and longitude.
	 * @deprecated
	 */
	@Test
	public void getBoundingBoxOfRangeGivenSameCoordinates(){
		latitude = 51.9032375;
		longitude = 51.9032375;
		range = 0.1;
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an InvalidAPIRequesException");
		}
		assertNotNull(this.testDocument);
	}
	
	/**
	 * This test will invoke the getBoundingBoxOfRange method with a Latitude over 90.
	 */
	@Test (expected = InvalidAPIRequestException.class)
	public void getBoundingBoxOfRangeGivenWrongLatitude(){
		latitude = 91.9032375;
		longitude = 8.3857535;
		range = 0.1;
		
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
		}
		catch(InvalidAPIRequestException ex){
			assertTrue("The Exception we expected to get.", true);
		}
	}
	
	/**
	 * This test will invoke the getBoundingBoxOfRange method with a Latitude over 180.
	 */
	@Test (expected = InvalidAPIRequestException.class)
	public void getBoundingBoxOfRangeGivenWrongLongitude(){
		latitude = 51.9032375;
		longitude = 181;
		range = 0.1;
		try{
			this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
		}
		catch(InvalidAPIRequestException ex){
			assertTrue("The Exception we expected to get.", true);
		}
	}
	
	/**
	 * This test will invoke the getBoundingBoxLatLong method with two different and valid coordinates.
	 */
	@Test
	public void getBoundinBoxLatLongGivenValidCoordinates(){
		latitude = 52.520007;
		longitude = 13.204953999999975;
		double latitude2 = 52.720007;
		double longitude2 = 13.404953999999975;
		try{
			this.testDocument = OSMApi.getBoundingBoxLatLong(latitude, longitude, latitude2, longitude2);
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an InvalidAPIRequestException!");
		}
		assertNotNull(this.testDocument);
	}
	
	/**
	 * This test will invoke the getBoundingBoxLatLong method with two same coordinates.
	 * @deprecated
	 */
	@Test
	public void getBoundinBoxLatLongGivenTwoSameCoordinates(){
		latitude = 52.520007;
		longitude = 13.204953999999975;
		try{
			this.testDocument = OSMApi.getBoundingBoxLatLong(latitude, longitude, latitude, longitude);
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an InvalidAPIRequestException");
		}
		assertNotNull(this.testDocument);
	}
	
	/**
	 * This test will invoke the getBoundingBoxLatLong method with a wrong minLatitude.
	 */
	@Test (expected = InvalidAPIRequestException.class)
	public void getBoundinBoxLatLongGivenWrongMinLatitude(){
		double minLatitude = 91;
		double minLongitude = 13.204953999999975;
		double maxLatitude = 52.720007;
		double maxLongitude = 13.204953999999975;
		try{
			this.testDocument = OSMApi.getBoundingBoxLatLong(minLatitude, minLongitude,
					maxLatitude, maxLongitude);
		}
		catch(InvalidAPIRequestException ex){
			assertTrue("The Exception we expected to get.", true);
		}
	}
	
	/**
	 * This test will invoke the getBoundingBoxLatLong method with a wrong maxLatitude.
	 */
	@Test
	public void getBoundinBoxLatLongGivenWrongMaxLatitude(){
		double minLatitude = 52.720007;
		double minLongitude = 13.204953999999975;
		double maxLatitude = 91;
		double maxLongitude = 13.204953999999975;
		try{
			this.testDocument = OSMApi.getBoundingBoxLatLong(minLatitude, minLongitude, maxLatitude, maxLongitude);
		}
		catch(InvalidAPIRequestException ex){
			assertTrue("The Exception we expected to get.", true);
		}
	}
	
	/**
	 * This test will invoke the getBoundingBoxLatLong method with a wrong minLongitude.
	 */
	@Test (expected = InvalidAPIRequestException.class)
	public void getBoundinBoxLatLongGivenWrongMinLongitude(){
		double minLatitude = 52.520007;
		double minLongitude = 181;
		double maxLatitude = 52.520007;
		double maxLongitude = 13.204953999999975;
		try{
			this.testDocument = OSMApi.getBoundingBoxLatLong(minLatitude, minLongitude,
					maxLatitude, maxLongitude);
		}
		catch(InvalidAPIRequestException ex){
			assertTrue("The Exception we expected to get.", true);
		}
	}
	
	/**
	 * This test will invoke the getBoundingBoxLatLong method with a wrong maxLongitude.
	 */
	@Test (expected = InvalidAPIRequestException.class)
	public void getBoundinBoxLatLongGivenWrongMaxLongitude(){
		double minLatitude = 52.520007;
		double minLongitude = 13.204953999999975;
		double maxLatitude = 52.520007;
		double maxLongitude = 181;
		try{
			this.testDocument = OSMApi.getBoundingBoxLatLong(minLatitude, minLongitude, maxLatitude, maxLongitude);
		}
		catch(InvalidAPIRequestException ex){
			assertTrue("The Exception we expected to get.", true);
		}
	}
}
