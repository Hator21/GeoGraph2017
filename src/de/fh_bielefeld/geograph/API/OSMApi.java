package de.fh_bielefeld.geograph.API;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import de.fh_bielefeld.geograph.API.APIRequest.RequestType;
import de.fh_bielefeld.geograph.API.APIRequest.ResponseType;
import de.fh_bielefeld.geograph.API.Exception.InvalidAPIRequestException;

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
     * Method to get the data of a single node by id
     * 
     * @param nodeId The ID of the Node you want to get additional information about
     * @return Document A XML Document with the data the API request returned for the given node
     * @throws ParserConfigurationException If the answer is not a valid XML document or the parser factory cannot be initialized correctly
     * @throws InvalidAPIRequestException If the request failed or the given Arguments do not match the API documentation
     */
    public static Document getNodeWithID(int nodeId) throws InvalidAPIRequestException {
        
        if(nodeId <= 0){
            throw new InvalidAPIRequestException("Node ID is out of range(must be greater than 0)");
        }
        
        
        APIRequestBuilder builder = new APIRequestBuilder();
        
        APIRequest request = builder.server("http://www.openstreetmap.org")
                .call("/api/0.6/node/")
                .callType(RequestType.NODE_BY_ID)
                .argument(String.valueOf(nodeId))
                .build();
        
        if(request.ready()){
            request = sendRequest(request);
            if(request.getResponseType() == ResponseType.SUCCESS){
                return request.getResponse();
            }else{//If response type is error switch to other api
                request = builder.server("http://overpass-api.de")
                    .call("/api/node/")
                    .build();
                if(request.ready()){
                    request = sendRequest(request);
                    if(request.getResponseType() == ResponseType.SUCCESS){
                        return request.getResponse();
                    }//If response type is again error throw exception(let the user handle it)
                    throw new InvalidAPIRequestException("Invalid Request(Maybe the ID is wrong ?)");
                }
                
            }
        }
        throw new InvalidAPIRequestException("Invalid Request(Maybe the ID is wrong ?)");
    }
    

    /**
     * Method to get the BoundingBox with the size of the given range 
     * while the given Point(latitude & longitude) remains as the center
     * 
     * @param latitude A geographic coordinate that specifies the north–south position of a point on the Earth's surface.
     * @param longitude A geographic coordinate that specifies the east-west position of a point on the Earth's surface.
     * @param range The Range(eg. size of the bounding box) within 0 and 0.25
     * @return The XML Document returned by the API with information within the calculated bounding box
     * @throws InvalidAPIRequestException If the request failed or the given Arguments do not match the API documentation
     */
    public static Document getBoundingBoxOfRange(double latitude, double longitude, double range) 
            throws InvalidAPIRequestException{
        
        if(!(range <= 0.25 && range > 0))
            throw new InvalidAPIRequestException("Range is out of Range ( not between 0 and 0.25");
        
        return getBoundingBoxLatLong(latitude-(range/2), longitude-(range/2), latitude+(range/2), longitude+(range/2));
    }

    /**
     * Method to get the BoudingBox built with the two given Points(latitude & longitude)
     * 
     * @param minLatitude A geographic coordinate that specifies the north–south position of a point on the Earth's surface for the lower Point
     * @param minLongitude A geographic coordinate that specifies the east-west position of a point on the Earth's surface for the lower Point
     * @param maxLatitude A geographic coordinate that specifies the north–south position of a point on the Earth's surface for the upper point
     * @param maxLongitude A geographic coordinate that specifies the east-west position of a point on the Earth's surface for the upper Point
     * @return The XML Document returned by the API with information within the bounding box
     * @throws InvalidAPIRequestException If the request failed or the given Arguments do not match the API documentation
     */
    public static Document getBoundingBoxLatLong(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) 
            throws InvalidAPIRequestException{
        
        if(!((minLatitude <= 90 && minLatitude >= -90) && (maxLatitude <= 90 && maxLatitude >= -90)))
            throw new InvalidAPIRequestException("Latitude is out of Range ( not between -90 and 90)");
        if(!((minLongitude <= 180 && minLongitude >= -180) && (maxLongitude <= 180 && maxLongitude >= -180)))
            throw new InvalidAPIRequestException("Longitude is out of Range ( not between -180 and 180)");
        if((minLatitude == maxLatitude)||(minLongitude == maxLongitude))
            throw new InvalidAPIRequestException("BoundingBox size would be zero");
        if((minLatitude > maxLatitude) || (minLongitude > maxLongitude))
            throw new InvalidAPIRequestException("The min Values must be lower than the max Values");
        
        APIRequestBuilder builder = new APIRequestBuilder();
        
        APIRequest request = builder.server("http://www.openstreetmap.org")
                .call("/api/0.6/map?bbox=")
                .callType(RequestType.BOUNDING_BOX_LATLANG)
                .argument(String.valueOf(minLongitude))
                .argument(String.valueOf(minLatitude))
                .argument(String.valueOf(maxLongitude))
                .argument(String.valueOf(maxLatitude))
                .build();
        
        if(request.ready()){
            request = sendRequest(request);
            if(request.getResponseType() == ResponseType.SUCCESS){
                return request.getResponse();
            }else{//If response type is error switch to other api
                request = builder.server("http://overpass-api.de")
                    .call("/api/xapi?map?bbox=")
                    .build();
                if(request.ready()){
                    request = sendRequest(request);
                    if(request.getResponseType() == ResponseType.SUCCESS){
                        return request.getResponse();
                    }//If response type is again error throw exception(let the user handle it)
                    throw new InvalidAPIRequestException("Invalid Request(Maybe the Range is to big?)");
                }
                
            }
        }
        
        throw new InvalidAPIRequestException("Invalid Request(Maybe the Range is to big?)");
    }

    /**
     * The Method to send a request and parse the returned OSM File
     * 
     * @param request The APIRequest Object representation
     * @return The APIRequest filled with ResponseType and Response
     */
    private static APIRequest sendRequest(APIRequest request) {
        URL apiURL;
        try {
            apiURL = new URL(request.toURL());
        
            HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();

            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();

            request.setResponse(docBuilder.parse(connection.getInputStream()));
            request.setResponseType(ResponseType.SUCCESS);
            
        } catch (Exception e) {
            request.setResponseType(ResponseType.ERROR);
        }
        
        return request;
    }
}
