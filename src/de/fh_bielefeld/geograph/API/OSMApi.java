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

public class OSMApi {

    public static final String api_uri = "http://api.openstreetmap.org/";
    public static final String api_test_uri = "http://www.openstreetmap.org";

    public static enum CALLS {
        Capabilities("/api/capabilities"),
        BoundingBoxMap("/api/0.6/map?bbox="), //GET /api/0.6/map?bbox=left,down,right,up
        Permissions("/api/0.6/permissions")
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

    public static Document getBoundingBoxOfRange(double latitude, double longitude, double range) throws IOException, ParserConfigurationException, SAXException{
        return getBoundingBoxLatLong(latitude-range, longitude-range, latitude+range, latitude+range);
    }

    public static Document getBoundingBoxLatLong(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) throws IOException, ParserConfigurationException, SAXException{

        DecimalFormat format = new DecimalFormat("##0.0000000", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

        String left = format.format(minLongitude);
        String down = format.format(minLatitude);
        String right = format.format(maxLongitude);
        String up = format.format(maxLatitude);

        String connectionString = api_test_uri + CALLS.BoundingBoxMap +
                left  + "," +
                down  + "," +
                right + "," +
                up;

        URL apiURL = new URL(connectionString);
        System.out.println(apiURL);
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
            d = OSMApi.getBoundingBoxLatLong(0.0000000,10.0000000,0.1000000,10.1000000);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        System.out.println(d.getDocumentElement());
    }
}
