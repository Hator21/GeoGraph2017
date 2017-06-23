package de.fh_bielefeld.geograph.TEST;

import de.fh_bielefeld.geograph.PARSER.OSMParser;
import de.fh_bielefeld.geograph.GUI.ContentHolder;
import de.fh_bielefeld.geograph.GUI_INTERFACE.ContentHolderInterface;
import de.fh_bielefeld.geograph.API.Exception.InvalidAPIRequestException;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Tests for the OmlParser class
 */
public class OSMParserTest{
	private ContentHolderInterface testHolder;
	private OSMParser testParser;
	
	/**
	 * This test invokes the parser method given an uninitialized ContentHolder.
	 * 
	 * (Error Test)
	 */
	@Test
	public void parseGivenUninitializedContentHolder(){
		this.testParser = new OSMParser(testHolder);
		try{
		ContentHolderInterface tempHolder = this.testParser.parse();
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an InvalidAPIRequestException.");
		}
		catch(NullPointerException ex){
			fail("Unexpected NullPointerException");
		}
	}
	
	/**
	 * This test will invoke the parse method with null in OSMParser-Constructor.
	 * 
	 * (Error Test)
	 */
	@Test
	public void parseGivenContentHolderNull(){
		this.testParser = new OSMParser(null);
		try{
		this.testHolder = testParser.parse();
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an InvalidAPIRequestException.");
		}
		catch(NullPointerException ex){
			fail("Unexpected NullPointerException");
		}
	}
	
	/**
	 * This test invokes the parser method given an empty ContentHolder.
	 * The tested method is expected to throw an InvalidAPIRequestException.
	 * 
	 * (Error Test)
	 * @throws InvalidAPIRequestException
	 */
	@Test(expected = InvalidAPIRequestException.class)
	public void parseGivenEmptyContentHolder() throws InvalidAPIRequestException{
		this.testHolder = new ContentHolder(null);
		this.testParser = new OSMParser(testHolder);
		
		ContentHolderInterface tempHolder = this.testParser.parse();
	}
	
	/**
	 * This test invokes the parser method given a valid ContentHolder.
	 * Tests whether the parse method returns a ContentHolder that is not null.
	 * 
	 * (Acceptance Test)
	 */
	@Test
	public void parseGivenValidContentHolder(){
		testHolder = new ContentHolder(null);
		double minLatitude = 52.510;
		double minLongitude = 13.20;
		double maxLatitude = 52.510005;
		double maxLongitude = 13.20005;
		
		this.testHolder.setMinLatitude(minLatitude);
		this.testHolder.setMinLongitude(minLongitude);
		this.testHolder.setMaxLatitude(maxLatitude);
		this.testHolder.setMaxLongitude(maxLongitude);
		
		this.testParser = new OSMParser(testHolder);
		
		ContentHolderInterface tempHolder = new ContentHolder(null);
		try{
			tempHolder = this.testParser.parse();
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an InvalidAPIRequestException.");
		}
		assertNotNull(tempHolder);
	}
	
	/**
	 * This test invokes the parser method given a valid ContentHolder.
	 * Tests whether the parse method sets the ways of the returned ContentHolder.
	 * 
	 * (Acceptance Test)
	 */
	@Test
	public void parseTestReturnedContentHolderWays(){
		this.testHolder = new ContentHolder(null);
		double minLatitude = 52.510;
		double minLongitude = 13.20;
		double maxLatitude = 52.510005;
		double maxLongitude = 13.20005;
		
		this.testHolder.setMinLatitude(minLatitude);
		this.testHolder.setMinLongitude(minLongitude);
		this.testHolder.setMaxLatitude(maxLatitude);
		this.testHolder.setMaxLongitude(maxLongitude);
		
		this.testParser = new OSMParser(testHolder);
		
		ContentHolderInterface tempHolder = new ContentHolder(null);
		try{
			tempHolder = this.testParser.parse();
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an InvalidAPIRequestException.");
		}
		assertFalse(tempHolder.getWays().isEmpty());
	}
	
	/**
	 * This test invokes the parser method given a valid ContentHolder.
	 * Tests whether the parse method sets the ways of the returned ContentHolder.
	 * 
	 * (Acceptance Test)
	 */
	@Test
	public void parseTestReturnedContentHolderNodes(){
		testHolder = new ContentHolder(null);
		double minLatitude = 52.510;
		double minLongitude = 13.20;
		double maxLatitude = 52.510005;
		double maxLongitude = 13.20005;
		
		this.testHolder.setMinLatitude(minLatitude);
		this.testHolder.setMinLongitude(minLongitude);
		this.testHolder.setMaxLatitude(maxLatitude);
		this.testHolder.setMaxLongitude(maxLongitude);
		
		this.testParser = new OSMParser(testHolder);
		
		ContentHolderInterface tempHolder = new ContentHolder(null);
		try{
			tempHolder = this.testParser.parse();
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an InvalidAPIRequestException.");
		}
		assertFalse(tempHolder.getNodes().isEmpty());
	}
}