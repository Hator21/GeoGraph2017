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
	private double minLatitude = 50.804776;
	private double minLongitude = 6.171098;
	private double maxLatitude = 50.804781;
	private double maxLongitude = 6.171103;
	
	/**
	 * This test invokes the parser method given an uninitialized ContentHolder.
	 * 
	 * (Error Test)
	 */
	@Test(expected = NullPointerException.class)
	public void parseGivenUninitializedContentHolder(){
		this.testParser = new OSMParser(this.testHolder);
		try{
			ContentHolderInterface tempHolder = this.testParser.parse();
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an InvalidAPIRequestException.");
		}
	}
	
	/**
	 * This test will invoke the parse method with null in OSMParser-Constructor.
	 * 
	 * (Error Test)
	 */
	@Test(expected = NullPointerException.class)
	public void parseGivenContentHolderNull(){
		this.testParser = new OSMParser(null);
		try{
			this.testHolder = testParser.parse();
		}
		catch(InvalidAPIRequestException ex){
			fail("There shouldn't be an InvalidAPIRequestException.");
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
		
		this.testHolder.setMinLatitude(this.minLatitude);
		this.testHolder.setMinLongitude(this.minLongitude);
		this.testHolder.setMaxLatitude(this.maxLatitude);
		this.testHolder.setMaxLongitude(this.maxLongitude);
		
		this.testParser = new OSMParser(this.testHolder);
		
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
		
		
		this.testHolder.setMinLatitude(this.minLatitude);
		this.testHolder.setMinLongitude(this.minLongitude);
		this.testHolder.setMaxLatitude(this.maxLatitude);
		this.testHolder.setMaxLongitude(this.maxLongitude);
		
		this.testParser = new OSMParser(this.testHolder);
		
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
	 * Tests whether the parse method sets the nodes of the returned ContentHolder.
	 * 
	 * (Acceptance Test)
	 */
	@Test
	public void parseTestReturnedContentHolderNodes(){
		this.testHolder = new ContentHolder(null);
		
		this.testHolder.setMinLatitude(this.minLatitude);
		this.testHolder.setMinLongitude(this.minLongitude);
		this.testHolder.setMaxLatitude(this.maxLatitude);
		this.testHolder.setMaxLongitude(this.maxLongitude);
		
		this.testParser = new OSMParser(this.testHolder);
		
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