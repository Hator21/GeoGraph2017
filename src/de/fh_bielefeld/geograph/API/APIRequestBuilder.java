package de.fh_bielefeld.geograph.API;

import de.fh_bielefeld.geograph.API.APIRequest.RequestType;

public class APIRequestBuilder {
    private APIRequest request;
    
    public APIRequestBuilder(){
        request = new APIRequest();
    }
    
    public APIRequestBuilder argument(final String argument){
        request.addArgument(argument);
        return this;
    }
    
    public APIRequestBuilder server(final String server_url){
        request.setServer_Url(server_url);
        return this;
    }
    
    public APIRequestBuilder call(final String call_url){
        request.setCall_Url(call_url);
        return this;
    }
    
    public APIRequestBuilder callType(RequestType type){
        request.setRequestType(type);
        return this;
    }
    
    public APIRequest build(){
        return this.request;
    }
}
