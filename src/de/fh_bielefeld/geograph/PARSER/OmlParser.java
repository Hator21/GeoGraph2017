package de.fh_bielefeld.geograph.PARSER;


import de.fh_bielefeld.geograph.GUI.MapNode;
import de.fh_bielefeld.geograph.GUI.ContentHolder;
import de.fh_bielefeld.geograph.GUI.MapWay;
import de.fh_bielefeld.geograph.GUI.AVLTree;
import de.fh_bielefeld.geograph.GUI.MapTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;


public class OmlParser {
    private double positiveDifference, negativeDifference;
    private AVLTree<MapNode> parsedNodeTree;
    private AVLTree<MapWay> parsedWayTree;
    private ArrayList<MapNode> nodesToTransfer;
    private ArrayList<MapWay> waysToTransfer;
    private ContentHolder usedHolder;
    private Map<String,String> changedIDS;
    private Map<String,String> includeConditions;
    private Document givenDocument;
    
    OmlParser(ContentHolder givenHolder){
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
    public void parse(Document documentToParse) throws NullPointerException{
        givenDocument=documentToParse;
        setIncludeConditions();
        NodeList relationsFromGivenDocument = givenDocument.getElementsByTagName("relation");
        
        for(int i=0;i<relationsFromGivenDocument.getLength();i++){
            boolean isImportant = false;
            if(relationsFromGivenDocument.item(i).hasChildNodes()){
                NodeList childsOfRelation = relationsFromGivenDocument.item(i).getChildNodes();
                for(int x=0;x<childsOfRelation.getLength();i++){
                    if(includeConditions.containsKey(childsOfRelation.item(x).getAttributes().getNamedItem("k").getNodeValue())){
                        if((childsOfRelation.item(x).getAttributes().getNamedItem("v").getNodeValue()).equals(includeConditions.get(childsOfRelation.item(x).getAttributes().getNamedItem("k")))){
                            isImportant=true;
                        }
                    }
                }
                if(isImportant){
                    for(int x=0;x<childsOfRelation.getLength();x++){
                       if((childsOfRelation.item(x).getAttributes().getNamedItem("type").getNodeValue()).equals("way")){
                           try{
                               parseWay(givenDocument.getElementById(childsOfRelation.item(x).getAttributes().getNamedItem("ref").getNodeValue()));
                           }catch(NullPointerException e){
                               //do Nothing, because there is no way!
                           }
                       }
                    }
                }
            }
        }
        usedHolder.setNodes(parsedNodeTree);
        usedHolder.setWays(parsedWayTree);
        clearEverythingUnimportant();
    }
    private void parseWay(Element givenWay)throws NullPointerException{
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
