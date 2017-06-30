package de.fh_bielefeld.geograph.TEST;

import de.fh_bielefeld.geograph.API.OSMApi;
import de.fh_bielefeld.geograph.API.Exception.InvalidAPIRequestException;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.w3c.dom.Document;

/**
 * Tests for the OSMApi class
 */
public class OSMApiTest {

	double latitude = 0;
	double longitude = 0;

	double latitudeMin = 0;
	double latitudeMax = 0;
	double longitudeMin = 0;
	double longitudeMax = 0;

	double range = 0;
	Document testDocument;

	/**
	 * This method will initialize the testDocument before every testcase.
	 */
	@Before
	public void initializeDocument() {
		this.testDocument = null;
	}

	/**
	 * This test will invoke the getNodeWithID method with an invalid node.
	 * The tested method is expected to throw an InvalidAPIRequestException.
	 * (Error Test)
	 * 
	 * @throws InvalidAPIRequestException
	 */
	@Test(expected = InvalidAPIRequestException.class)
	public void getNodeWithIDGivenAWrongNode() throws InvalidAPIRequestException {
		int invalidNode = -120;
		this.testDocument = OSMApi.getNodeWithID(invalidNode);
	}

	/**
	 * This test will invoke the getNodeWithID method with a valid node.
	 * (Acceptance Test)
	 */
	@Test
	public void getNodeWithIDGivenValidNode() {
		int validNode = 132611634;
		try {
			System.out.println("wait 10 secs");
			TimeUnit.SECONDS.sleep(10);
			this.testDocument = OSMApi.getNodeWithID(validNode);
		} catch (InvalidAPIRequestException ex) {
			ex.printStackTrace();
			fail("There shouldn't be an InvalidAPIRequestException !");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			fail("fail!");
		}
		assertNotNull(this.testDocument);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with a range over 0.25.
	 * The tested method is expected to throw an InvalidAPIRequestException.
	 * (Error Test)
	 * 
	 * @throws InvalidAPIRequestException
	 */
	@Test(expected = InvalidAPIRequestException.class)
	public void getBoundingBoxOfRangeGivenTooHighRange() throws InvalidAPIRequestException {
		this.latitude = 51.9032375;
		this.longitude = 8.3857535;
		this.range = 1;

		this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with a range below 0.
	 * The tested method is expected to throw an InvalidAPIRequestException.
	 * (Error Test)
	 * 
	 * @throws InvalidAPIRequestException
	 */
	@Test(expected = InvalidAPIRequestException.class)
	public void getBoundingBoxOfRangeGivenNegativeRange() throws InvalidAPIRequestException {
		this.latitude = 51.9032375;
		this.longitude = 8.3857535;
		this.range = -1;
		this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with the minimum
	 * range 0.
	 * (Error Test)
	 * 
	 * @throws InvalidAPIRequestException
	 */
	@Test(expected = InvalidAPIRequestException.class)
	public void getBoundingBoxOfRangeGivenZeroRange() throws InvalidAPIRequestException {
		this.latitude = 51.9032375;
		this.longitude = 8.3857535;
		this.range = 0;
		this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with a valid range
	 * and valid latitude and longitude.
	 * (Acceptance Test)
	 */
	@Test
	public void getBoundingBoxOfRangeGivenValidArguments() {
		this.latitude = 51.9032375;
		this.longitude = 8.3857535;
		this.range = 0.003;
		try {
			System.out.println("wait 10 secs");
			TimeUnit.SECONDS.sleep(10);
			this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
		} catch (InvalidAPIRequestException ex) {
			fail("There shouldn't be an InvalidAPIRequestException!");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			fail("fail!");
		}
		assertNotNull(this.testDocument);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with the same
	 * value for latitude and longitude.
	 * (Acceptance Test)
	 */
	@Test
	public void getBoundingBoxOfRangeGivenSameCoordinates() {
		this.latitude = 51.9032375;
		this.longitude = 51.9032375;
		this.range = 0.1;
		try {
			System.out.println("wait 10 secs");
			TimeUnit.SECONDS.sleep(10);
			this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
		} catch (InvalidAPIRequestException ex) {
			fail("There shouldn't be an InvalidAPIRequesException");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			fail("fail!");
		}
		assertNotNull(this.testDocument);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with a Latitude over 90.
	 * The tested method is expected to throw an InvalidAPIRequestException.
	 * (Error Test)
	 * 
	 * @throws InvalidAPIRequestException
	 */
	@Test(expected = InvalidAPIRequestException.class)
	public void getBoundingBoxOfRangeGivenWrongLatitude() throws InvalidAPIRequestException {
		this.latitude = 91.9032375;
		this.longitude = 8.3857535;
		this.range = 0.1;
		this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with a Latitude over 180.
	 * The tested method is expected to throw an InvalidAPIRequestException.
	 * (Negative Test)
	 * 
	 * @throws InvalidAPIRequestException
	 */
	@Test(expected = InvalidAPIRequestException.class)
	public void getBoundingBoxOfRangeGivenWrongLongitude() throws InvalidAPIRequestException {
		this.latitude = 51.9032375;
		this.longitude = 181;
		this.range = 0.1;
		this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
	}

	/**
	 * This test will invoke the getBoundingBoxLatLong method with two different
	 * and valid coordinates. The tested method is expected to throw an InvalidAPIRequestException.
	 * (Acceptance Test)
	 */
	@Test
	public void getBoundinBoxLatLongGivenValidCoordinates() {
		this.latitudeMin = 52.510;
		this.longitudeMin = 13.20;
		this.latitudeMax = 52.510005;
		this.longitudeMax = 13.20005;

		try {
			System.out.println("wait 10 secs");
			TimeUnit.SECONDS.sleep(10);
			this.testDocument = OSMApi.getBoundingBoxLatLong(latitudeMin, longitudeMin, latitudeMax, longitudeMax);
		} catch (InvalidAPIRequestException ex) {
			fail("There shouldn't be an InvalidAPIRequestException!");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			fail("fail!");
		}
		assertNotNull(this.testDocument);
	}

	/**
	 * This test will invoke the getBoundingBoxLatLong method with two same coordinates.
	 * The tested method is expected to throw an InvalidAPIRequestException.
	 * (Error Test)
	 * 
	 * @throws InvalidAPIRequestException
	 */
	@Test(expected = InvalidAPIRequestException.class)
	public void getBoundinBoxLatLongGivenTwoSameCoordinates() throws InvalidAPIRequestException {
		this.latitude = 52.520007;
		this.longitude = 13.204953999999975;
		this.testDocument = OSMApi.getBoundingBoxLatLong(latitude, longitude, latitude, longitude);
	}

	/**
	 * This test will invoke the getBoundingBoxLatLong method with a wrong minLatitude.
	 * The tested method is expected to throw an InvalidAPIRequestException.
	 * (Error Test)
	 * 
	 * @throws InvalidAPIRequestException
	 */
	@Test(expected = InvalidAPIRequestException.class)
	public void getBoundinBoxLatLongGivenWrongMinLatitude() throws InvalidAPIRequestException {
		double minLatitude = 91;
		double minLongitude = 13.204953999999975;
		double maxLatitude = 52.720007;
		double maxLongitude = 13.204953999999975;

		this.testDocument = OSMApi.getBoundingBoxLatLong(minLatitude, minLongitude, maxLatitude, maxLongitude);
	}

	/**
	 * This test will invoke the getBoundingBoxLatLong method with a wrong maxLatitude.
	 * The tested method is expected to throw an InvalidAPIRequestException.
	 * (Error Test)
	 * 
	 * @throws InvalidAPIRequestException
	 */
	@Test(expected = InvalidAPIRequestException.class)
	public void getBoundinBoxLatLongGivenWrongMaxLatitude() throws InvalidAPIRequestException {
		double minLatitude = 52.720007;
		double minLongitude = 13.204953999999975;
		double maxLatitude = 91;
		double maxLongitude = 13.204953999999975;

		this.testDocument = OSMApi.getBoundingBoxLatLong(minLatitude, minLongitude, maxLatitude, maxLongitude);
	}

	/**
	 * This test will invoke the getBoundingBoxLatLong method with a wrong minLongitude.
	 * The tested method is expected to throw an InvalidAPIRequestException.
	 * (Error Test)
	 * 
	 * @throws InvalidAPIRequestException
	 */
	@Test(expected = InvalidAPIRequestException.class)
	public void getBoundinBoxLatLongGivenWrongMinLongitude() throws InvalidAPIRequestException {
		double minLatitude = 52.520007;
		double minLongitude = 181;
		double maxLatitude = 52.520007;
		double maxLongitude = 13.204953999999975;

		this.testDocument = OSMApi.getBoundingBoxLatLong(minLatitude, minLongitude, maxLatitude, maxLongitude);
	}

	/**
	 * This test will invoke the getBoundingBoxLatLong method with a wrong maxLongitude.
	 * The tested method is expected to throw an InvalidAPIRequestException.
	 * (Error Test)
	 * 
	 * @throws InvalidAPIRequestException
	 */
	@Test(expected = InvalidAPIRequestException.class)
	public void getBoundinBoxLatLongGivenWrongMaxLongitude() throws InvalidAPIRequestException {
		double minLatitude = 52.520007;
		double minLongitude = 13.204953999999975;
		double maxLatitude = 52.520007;
		double maxLongitude = 181;

		this.testDocument = OSMApi.getBoundingBoxLatLong(minLatitude, minLongitude, maxLatitude, maxLongitude);
	}

	/**
	 * This test will invoke the getBoundingBoxOfRange method with the maximum
	 * range 0.25. Has a long runtime due to the big range.
	 * (Extreme Value Test)
	 */
	@Ignore
//	@Test
	public void getBoundingBoxOfRangeGivenMaximumRange() {
		this.latitude = 51.9032375;
		this.longitude = 8.3857535;
		this.range = 0.25;

		try {
			System.out.println("wait 10 secs");
			TimeUnit.SECONDS.sleep(10);
			this.testDocument = OSMApi.getBoundingBoxOfRange(latitude, longitude, range);
		} catch (InvalidAPIRequestException ex) {
			fail("There shouldn't be an InvalidAPIRequestException!");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			fail("fail!");
		}
		assertNotNull(this.testDocument);
	}
}