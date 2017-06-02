package de.fh_bielefeld.geograph.API;

import de.fh_bielefeld.geograph.API.APIRequest.RequestType;

/**
 * The APIRequestBuilder Class
 * with Method to build a API Request with the Builder-Pattern
 * 
 * @author Philipp Clausing
 * @version 0.1
 * @since 2017-05-31
 */
public class APIRequestBuilder {
    private APIRequest request;
    
    /**
     * Constructor of the APIRequestBuilder
     * Initializes the APIRequest Object
     */
    public APIRequestBuilder(){
        request = new APIRequest();
    }
    
    /**
     * Adds a new argument to the APIRequest
     * 
     * @param argument The argument which should be added to the argument list of the APIRequest
     * @return The current APIRequestBuilder(BuilderPattern)
     */
    public APIRequestBuilder argument(final String argument){
        request.addArgument(argument);
        return this;
    }
    
    /**
     * Sets the Server Url of the APIRequest Object
     * 
     * @param server_url The Server request URL the request should be sent to
     * @return The current APIRequestBuilder(BuilderPattern)
     */
    public APIRequestBuilder server(final String server_url){
        request.setServer_Url(server_url);
        return this;
    }
    
    /**
     * Sets the Call Url of the APIRequest Object
     * 
     * @param call_url The call url where the request is sent to (eg. serverurl+callurl)
     * @return The current APIRequestBuilder(BuilderPattern)
     */
    public APIRequestBuilder call(final String call_url){
        request.setCall_Url(call_url);
        return this;
    }
    
    /**
     * Sets the RequestType of the APIRequest Object
     * 
     * @param type The RequestType of the request
     * @return The current APIRequestBuilder(BuilderPattern)
     */
    public APIRequestBuilder callType(RequestType type){
        request.setRequestType(type);
        return this;
    }
    
    /**
     * "builds" the APIRequest Object
     * 
     * @return the APIRequest Object
     */
    public APIRequest build(){
        return this.request;
    }
}
