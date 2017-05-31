package de.fh_bielefeld.geograph.API;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
* The API Wrapper Class for the OpenStreetMap API
* with Methods for retrieving Data
*
* @author  Philipp Clausing
* @version 0.1
* @since   2017-05-25 
*/
public class OSMApi {

    /**
     * The URL of the OSM API
     */
    public static final String api_uri = "http://www.openstreetmap.org";
    
    /**
     * URLs for the different Calls to the API
     */
    public static enum CALLS {
        /**
         * The Capability Call URL
         */
        Capabilities("/api/capabilities"),
        /**
         * The BoundingBox Call URL(Example: GET /api/0.6/map?bbox=left,down,right,up)
         */
        BoundingBoxMap("/api/0.6/map?bbox="),
        /**
         * The Permission Call URL
         */
        Permissions("/api/0.6/permissions"),
        /**
         * The Node Call URL(Example: GET /api/0.6/node/nodeid)
         */
        Node("/api/0.6/node/"),
        /**
         * The Way Call URL(Example: GET /api/0.6/[way|relation]/#id/full)
         */
        Way("/api/0.6/way/")
        ;

        private final String call_uri;

        /**
         * Constructor of the Call-URL-Enum
         * @param call_uri String representation of the Call URL
         */

        private CALLS(final String call_uri) {
            this.call_uri = call_uri;
        }

        @Override
        public String toString() {
            return call_uri;
        }
    }
    
    /**
     * Method to get the data of a single node by id
     * 
     * @param nodeId The ID of the Node you want to get additional information about
     * @return Document A XML Document with the data the API request returned for the given node
     * @throws ParserConfigurationException If the answer is not a valid XML document or the parser factory cannot be initialized correctly
     * @throws IOException If there is a problem with the HTTP Connection or with the parser
     * @throws SAXException If there is a problem while parsing the returned OSM file
     */
    public static Document getNodeWithID(long nodeId) 
            throws ParserConfigurationException, IOException, SAXException{

        
        String connectionString = api_uri + CALLS.Node + nodeId;
        
        return requestXML(connectionString);
    }
    

    /**
     * Method to get the BoundingBox with the size of the given range 
     * while the given Point(latitude & longitude) remains as the center
     * 
     * @param latitude A geographic coordinate that specifies the north–south position of a point on the Earth's surface.
     * @param longitude A geographic coordinate that specifies the east-west position of a point on the Earth's surface.
     * @param range The Range(eg. size of the bounding box) within 0 and 0.25
     * @return The XML Document returned by the API with information within the calculated bounding box
     * @throws ParserConfigurationException If the answer is not a valid XML document or the parser factory cannot be initialized correctly
     * @throws IOException If there is a problem with the HTTP Connection or with the parser
     * @throws SAXException If there is a problem while parsing the returned OSM file
     */
    public static Document getBoundingBoxOfRange(double latitude, double longitude, double range) 
            throws IOException, ParserConfigurationException, SAXException{
        return getBoundingBoxLatLong(latitude-range, longitude-range, latitude+range, latitude+range);
    }

    /**
     * Method to get the BoudingBox built with the two given Points(latitude & longitude)
     * 
     * @param minLatitude A geographic coordinate that specifies the north–south position of a point on the Earth's surface for the lower Point
     * @param minLongitude A geographic coordinate that specifies the east-west position of a point on the Earth's surface for the lower Point
     * @param maxLatitude A geographic coordinate that specifies the north–south position of a point on the Earth's surface for the upper point
     * @param maxLongitude A geographic coordinate that specifies the east-west position of a point on the Earth's surface for the upper Point
     * @return The XML Document returned by the API with information within the bounding box
     * @throws ParserConfigurationException If the answer is not a valid XML document or the parser factory cannot be initialized correctly
     * @throws IOException If there is a problem with the HTTP Connection or with the parser
     * @throws SAXException If there is a problem while parsing the returned OSM file
     */
    public static Document getBoundingBoxLatLong(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) 
            throws IOException, ParserConfigurationException, SAXException{


        DecimalFormat format = new DecimalFormat("##0.0000000", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

        String left = format.format(minLongitude);
        String down = format.format(minLatitude);
        String right = format.format(maxLongitude);
        String up = format.format(maxLatitude);

        String connectionString = api_uri + CALLS.BoundingBoxMap +
                left  + "," +
                down  + "," +
                right + "," +
                up;
        return requestXML(connectionString);
    }

    /**
     * The Method to send a request and parse the returned OSM File
     * 
     * @param url The API URL which the Method has to request
     * @return The parsed XML Document
     * @throws ParserConfigurationException If the answer is not a valid XML document or the parser factory cannot be initialized correctly
     * @throws IOException If there is a problem with the HTTP Connection or with the parser
     * @throws SAXException If there is a problem while parsing the returned OSM file
     */
    private static Document requestXML(final String url) 
            throws ParserConfigurationException, IOException, SAXException{
        URL apiURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();

        return docBuilder.parse(connection.getInputStream());
    }
}
