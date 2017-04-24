package de.fh_bielefeld.geograph.API;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class OSMApi {

    public static final String api_uri = "http://www.openstreetmap.org/";
    public static final String api_test_uri = "http://www.openstreetmap.org";

    public static enum CALLS {
        Capabilities("/api/capabilities"),
        BoundingBoxMap("/api/0.6/map?bbox="), //GET /api/0.6/map?bbox=left,down,right,up
        Permissions("/api/0.6/permissions"),
        Node("/api/0.6/node/") //GET /api/0.6/node/nodeid
        ;

        private final String call_uri;

        private CALLS(final String call_uri) {
            this.call_uri = call_uri;
        }

        @Override
        public String toString() {
            return call_uri;
        }
    }
    
    public static Document getNodeWithID(long nodeId) throws ParserConfigurationException, IOException, SAXException{
        
        String connectionString = api_uri + CALLS.Node + nodeId;
        
        return requestXML(connectionString);
    }

    public static Document getBoundingBoxOfRange(double latitude, double longitude, double range) throws IOException, ParserConfigurationException, SAXException{
        return getBoundingBoxLatLong(latitude-range, longitude-range, latitude+range, latitude+range);
    }

    public static Document getBoundingBoxLatLong(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) throws IOException, ParserConfigurationException, SAXException{

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

    private static Document requestXML(final String url) throws ParserConfigurationException, IOException, SAXException{
        URL apiURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();

        return docBuilder.parse(connection.getInputStream());
    }

    //For testing purposes only! Remove when merged into Master!
    public static void main(String[] args){
        //For testing purposes only! Remove when merged into Master!
        Document d = null;
        try {
            //d = OSMApi.getBoundingBoxLatLong(0.0000000,10.0000000,0.1000000,10.1000000);
            d = OSMApi.getNodeWithID(3785338095L);
            System.out.println(getStringFromDocument(d));
           
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
    
    public static String getStringFromDocument(Document doc)
    {
        try
        {
           DOMSource domSource = new DOMSource(doc);
           StringWriter writer = new StringWriter();
           StreamResult result = new StreamResult(writer);
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.setOutputProperty(OutputKeys.INDENT, "yes");
           transformer.transform(domSource, result);
           return writer.toString();
        }
        catch(TransformerException ex)
        {
           ex.printStackTrace();
           return null;
        }
    } 
}
