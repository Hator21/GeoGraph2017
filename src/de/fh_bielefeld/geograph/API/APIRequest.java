package de.fh_bielefeld.geograph.API;

import org.w3c.dom.Document;

import de.fh_bielefeld.geograph.API.Exception.InvalidAPIRequestException;

import java.util.ArrayList;

public class APIRequest {
    
    public static enum RequestType{
        BOUNDING_BOX_LATLANG,
        NODE_BY_ID
    }
    
    public static enum ResponseType{
        SUCCESS,
        ERROR
    }
    
    private RequestType request_type;
    private String server_url;
    private String call_url;
    private String url = "";
    
    private ResponseType response_type;
    private Document response;
    
    private ArrayList<String> argument_list;
    
    public APIRequest(){
        argument_list = new ArrayList<String>();
    }
    
    public boolean ready() throws InvalidAPIRequestException{
        if(this.request_type == RequestType.BOUNDING_BOX_LATLANG){
            //There are 4 Parameters needed for this request
            if(argument_list.size() != 4){
                throw new InvalidAPIRequestException("Not the right amount of Arguments for this type of request");
            }
            this.url = server_url + call_url;
            this.url += argument_list.get(0) + ",";
            this.url += argument_list.get(1) + ",";
            this.url += argument_list.get(2) + ",";
            this.url += argument_list.get(3);
            
            return true;
        }else if(this.request_type == RequestType.NODE_BY_ID){
            //There is one parameter needed for this request
            if(argument_list.size() != 1){
                throw new InvalidAPIRequestException("Not the right amount of Arguments for this type of request");
            }
            this.url = server_url + call_url + argument_list.get(0);
        }
        return false;
    }
    
    public String toURL(){
        System.out.println("[DEBUG] " + url);
        return url;
    }
    
    public void addArgument(final String argument){
        this.argument_list.add(argument);
    }
    
    public void setRequestType(final RequestType type){this.request_type = type;};
    public void setServer_Url(final String server_url){this.server_url = server_url;}
    public void setCall_Url(final String call_url){this.call_url = call_url;}
    
    public void setResponseType(final ResponseType type){this.response_type = type;};
    public void setResponse(final Document response){this.response = response;};
    
    public RequestType getRequestType(){return request_type;}
    public String getServer_Url(){return server_url;}
    public String getCallUrl(){return call_url;}
    
    public ResponseType getResponseType(){return response_type;}
    public Document getResponse(){return response;}
}
