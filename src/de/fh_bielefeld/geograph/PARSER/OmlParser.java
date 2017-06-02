package de.fh_bielefeld.geograph.PARSER;


import de.fh_bielefeld.geograph.API.Exception.InvalidAPIRequestException;
import de.fh_bielefeld.geograph.GUI.MapNode;
import de.fh_bielefeld.geograph.GUI_INTERFACE.ContentHolderInterface;
import de.fh_bielefeld.geograph.GUI.MapWay;
import de.fh_bielefeld.geograph.GUI.AVLTree;
import de.fh_bielefeld.geograph.GUI.MapTag;
import de.fh_bielefeld.geograph.API.OSMApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;



/**
 * The Parser Class for an osm type Document
 * with Methods to parse the given Document, and to return the Streets.
 * @author  Stefan Schuck
 * @version 0.1
 * @since   2017-05-25 
*/
public class OmlParser {
    private double positiveDifference, negativeDifference;
    private AVLTree<MapNode> parsedNodeTree;
    private AVLTree<MapWay> parsedWayTree;
    private ArrayList<MapNode> nodesToTransfer;
    private ArrayList<MapWay> waysToTransfer;
    private ContentHolderInterface usedHolder;
    private Map<String,String> changedIDS;
    private Map<String,String> includeConditions;
    private Document givenDocument;

    /**
     * Method to get the data of a single node by id
     * 
     * @param givenHolder The ID of the Node you want to get additional information about
 */
    public OmlParser(ContentHolderInterface givenHolder){
        usedHolder = givenHolder;
        positiveDifference = 0.0000005;//magicNumber how close the Nodes must be to be considered as one
        negativeDifference = positiveDifference*(-1);
        parsedNodeTree = new AVLTree<MapNode>(usedHolder);
        parsedWayTree = new AVLTree<MapWay>(usedHolder);
        nodesToTransfer = new ArrayList<MapNode>();
        waysToTransfer = new ArrayList<MapWay>();
        changedIDS = new HashMap<String,String>();
        includeConditions = new HashMap<String,String>();
        
    }
    
    private void setIncludeConditions(){
        includeConditions.put("route", "road");
    }
    private void clearEverythingUnimportant(){
        waysToTransfer.clear();
        nodesToTransfer.clear();
        changedIDS.clear();
        includeConditions.clear();
        
    }
    public ContentHolderInterface parse() throws NullPointerException{
        OSMApi ApiCaller = new OSMApi();
        try{
            givenDocument=ApiCaller.getBoundingBoxLatLong(usedHolder.getMinLatitude(),usedHolder.getMinLongitude(),usedHolder.getMaxLatitude(),usedHolder.getMaxLongitude());
        }catch(InvalidAPIRequestException e){

            //was zu tuen ist
        }
        for(int i=0;i<givenDocument.getDocumentElement().getAttributes().getLength();i++){
            System.out.println(givenDocument.getDocumentElement().getAttributes().item(i).getNodeValue());
        }
        
        givenDocument.getDocumentElement().setIdAttribute("id", true);
        setIncludeConditions();
        NodeList relationsFromGivenDocument = givenDocument.getElementsByTagName("relation");
        
        for(int i=0;i<relationsFromGivenDocument.getLength();i++){
            boolean isImportant = false;
            if(relationsFromGivenDocument.item(i).hasChildNodes()){
                NodeList childsOfRelation = relationsFromGivenDocument.item(i).getChildNodes();
                for(int x=0;x<childsOfRelation.getLength();x++){
                    if((childsOfRelation.item(x).getAttributes()!=null)&&(childsOfRelation.item(x).getAttributes().getNamedItem("k")!= null)){
                        if(includeConditions.containsKey(childsOfRelation.item(x).getAttributes().getNamedItem("k").getNodeValue())){
                            if((childsOfRelation.item(x).getAttributes().getNamedItem("v").getNodeValue()).equals(includeConditions.get(childsOfRelation.item(x).getAttributes().getNamedItem("k").getNodeValue()))){
                                isImportant=true;
                            }
                        }
                    }
                }
                if(isImportant){
                    for(int x=0;x<childsOfRelation.getLength();x++){
                        if((childsOfRelation.item(x).getAttributes()!=null)&&(childsOfRelation.item(x).getAttributes().getNamedItem("type")!=null)){
                            if((childsOfRelation.item(x).getAttributes().getNamedItem("type").getNodeValue().toString()).equals("way")){
                                if(givenDocument.getElementById(childsOfRelation.item(x).getAttributes().getNamedItem("ref").getNodeValue())!=null){
                                    System.out.println("parse way begonnen");
                                    parseWay(givenDocument.getElementById(childsOfRelation.item(x).getAttributes().getNamedItem("ref").getNodeValue()));
                                    System.out.println("parse way beendet");
                                }
                            }
                       }
                    }
                }
            }
        }
        usedHolder.setNodes(parsedNodeTree);
        usedHolder.setWays(parsedWayTree);
        clearEverythingUnimportant();
        System.out.println("returned");
        return usedHolder;
    }
    private void parseWay(Element givenWay){
        String parsedWayID = givenWay.getAttributes().getNamedItem("id").toString();
        ArrayList<String> refsFromGivenWay= new ArrayList<String>();
        ArrayList<MapTag> tagsFromGivenWay= new ArrayList<MapTag>();
        if(givenWay.hasChildNodes()){
            NodeList childsFromGivenWays = givenWay.getChildNodes();
            
            for(int j=0;j<childsFromGivenWays.getLength();j++){
                if(childsFromGivenWays.item(j).getNodeName()=="nd"){
                    parseNode(givenDocument.getElementById(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue()));
                    if(changedIDS.containsKey(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue())){
                        refsFromGivenWay.add(changedIDS.get(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue()));
                    }else{
                        refsFromGivenWay.add(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue());
                    }
                }
            }
        }
        MapWay parsedWay=new MapWay(parsedWayID,refsFromGivenWay,tagsFromGivenWay);
        parsedWayTree.insert(parsedWay);
        for(int i = 0;i<nodesToTransfer.size();i++){
        parsedNodeTree.insert(nodesToTransfer.get(i));
    }
        
    }
    
    private void parseNode(Element givenNode){
        String parsedNodeID = givenNode.getAttributes().getNamedItem("id").toString();
        Double parsedNodeLongitude = Double.parseDouble(givenNode.getAttributes().getNamedItem("lon").getNodeValue());
        Double parsedNodeLatitude = Double.parseDouble(givenNode.getAttributes().getNamedItem("lat").getNodeValue());

        MapNode parsedNode = new MapNode(parsedNodeID,parsedNodeLongitude,parsedNodeLatitude);
            
        if(givenNode.hasChildNodes()==true){
            ArrayList<MapTag> tagsFromGivenNode = new ArrayList<MapTag>();
            NodeList childsNodesFromGivenNode = givenNode.getChildNodes();
            for(int j = 0;j<childsNodesFromGivenNode.getLength();j++){
                if(childsNodesFromGivenNode.item(j).hasAttributes()){
                    String kString = givenNode.getChildNodes().item(j).getAttributes().getNamedItem("k").toString();
                    String vString = givenNode.getChildNodes().item(j).getAttributes().getNamedItem("v").toString();
                    MapTag tagFromGivenNode = new MapTag(kString,vString);
                    tagsFromGivenNode.add(tagFromGivenNode);
                }
            }
            if(!tagsFromGivenNode.isEmpty()){
                parsedNode.setTagList(tagsFromGivenNode);
            }
        }
        for(int z = 0;z<nodesToTransfer.size();z++){
            if((positiveDifference<=(parsedNodeLongitude-nodesToTransfer.get(z).getLongitude())||
                negativeDifference>=(parsedNodeLongitude-nodesToTransfer.get(z).getLongitude()))&&
               (positiveDifference<=(parsedNodeLatitude-nodesToTransfer.get(z).getLatitude())||
                negativeDifference>=(parsedNodeLatitude-nodesToTransfer.get(z).getLatitude()))
               ){
                changedIDS.put(parsedNodeID, nodesToTransfer.get(z).getId());
                nodesToTransfer.get(z).getTagList().addAll(parsedNode.getTagList());
                break;
            }
        }
        if(!changedIDS.containsKey(parsedNode.getId())){
            nodesToTransfer.add(parsedNode);
        }
    } 
}
