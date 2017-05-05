package de.fh_bielefeld.geograph.Parser;


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
    private ContentHolder usedHolder;
    private Map<String,String> changedIDS;
    private Map<String,String> includeConditions;
    
    OmlParser(ContentHolder givenHolder){
        usedHolder = givenHolder;
        positiveDifference = 0.0000005;
        negativeDifference = positiveDifference*(-1);
        parsedNodeTree = new AVLTree<MapNode>(usedHolder);
        parsedWayTree = new AVLTree<MapWay>(usedHolder);
        nodesToTransfer = new ArrayList<MapNode>();
        changedIDS = new HashMap<String,String>();
        includeConditions = new HashMap<String,String>();
        
    }
    
    private void setIncludeConditions(){
        includeConditions.put("highway", "tertiary");
        includeConditions.put("highway", "primary");
        includeConditions.put("highway", "residential");
    }
    private void clearEverythingUnimportant(){
        nodesToTransfer.clear();
        nodesToTransfer.clear();
        changedIDS.clear();
        includeConditions.clear();
        
    }
    private void parse(Document givenDocument) throws NullPointerException{
        setIncludeConditions();
        NodeList waysFromGivenDocument = givenDocument.getElementsByTagName("ways");
        
        for(int i = 0;i<waysFromGivenDocument.getLength();i++){
            boolean isImportant = false;
            String parsedWayID = waysFromGivenDocument.item(i).getAttributes().getNamedItem("id").toString();
            ArrayList<String> refsFromGivenWay= new ArrayList<String>();
            ArrayList<MapTag> tagsFromGivenWay= new ArrayList<MapTag>();
            if(waysFromGivenDocument.item(i).hasChildNodes()){
                NodeList childsFromGivenWays = waysFromGivenDocument.item(i).getChildNodes();
                
                for(int j=0;j<childsFromGivenWays.getLength();j++){
                    if (childsFromGivenWays.item(j).getNodeName()=="tag"){
                        String kString = childsFromGivenWays.item(j).getAttributes().getNamedItem("k").getNodeValue();
                        String vString = childsFromGivenWays.item(j).getAttributes().getNamedItem("v").getNodeValue();
                        tagsFromGivenWay.add(new MapTag(kString,vString));
                        if(includeConditions.containsKey(kString)){
                            if(vString.equals(includeConditions.get(j))){
                                isImportant=true;
                                break;
                            }
                        } 
                    }
                }
                if(isImportant){
                    for(int j=0;j<childsFromGivenWays.getLength();j++){
                        if(childsFromGivenWays.item(j).getNodeName()=="nd"){
                            parseNode(givenDocument.getElementById(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue));
                            if(changedIDS.containsKey(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue())){
                                refsFromGivenWay.add(changedIDS.get(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue()));
                            }else{
                                refsFromGivenWay.add(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue());
                            }
                        }
                    }
                }
            }
            if(isImportant){
                MapWay parsedWay=new MapWay(parsedWayID,refsFromGivenWay,tagsFromGivenWay);
                parsedWayTree.insert(parsedWay);
            }
        }
        for(int i = 0;i<nodesToTransfer.size();i++){
        parsedNodeTree.insert(nodesToTransfer.get(i));
    }
        usedHolder.setNodes(parsedNodeTree);
        usedHolder.setWays(parsedWayTree);
        clearEverythingUnimportant();
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
