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


public class OmlParser {
    private double positiveDifference, negativeDifference;
    
    OmlParser(){
        positiveDifference = 0.0000005;
        negativeDifference = positiveDifference*(-1);
    }
    
     

    private void parse(ContentHolder givenHolder, Document givenDocument) throws NullPointerException{
        AVLTree<MapNode> parsedNodeTree = new AVLTree<MapNode>(givenHolder);
        AVLTree<MapWay> parsedWayTree = new AVLTree<MapWay>(givenHolder);
        
        ArrayList<MapNode> nodesToTransfer = new ArrayList<MapNode>();
        Map<String,String> changedIDS = new HashMap<String,String>();
        
        NodeList nodesFromGivenDocument = givenDocument.getElementsByTagName("node");
        NodeList waysFromGivenDocument = givenDocument.getElementsByTagName("ways");
        
        for(int i = 0;i<nodesFromGivenDocument.getLength();i++){
            String parsedNodeID = nodesFromGivenDocument.item(i).getAttributes().getNamedItem("id").toString();
            Double parsedNodeLongitude = Double.parseDouble(nodesFromGivenDocument.item(i).getAttributes().getNamedItem("lon").getNodeValue());
            Double parsedNodeLatitude = Double.parseDouble(nodesFromGivenDocument.item(i).getAttributes().getNamedItem("lat").getNodeValue());

            MapNode parsedNode = new MapNode(parsedNodeID,parsedNodeLongitude,parsedNodeLatitude);
            
            if(nodesFromGivenDocument.item(i).hasChildNodes()==true){
                ArrayList<MapTag> tagsFromGivenNode = new ArrayList<MapTag>();
                NodeList childsNodesFromGivenNode = nodesFromGivenDocument.item(i).getChildNodes();
                for(int j = 0;j<childsNodesFromGivenNode.getLength();j++){
                    if(childsNodesFromGivenNode.item(j).hasAttributes()){
                        String kString = nodesFromGivenDocument.item(i).getChildNodes().item(j).getAttributes().getNamedItem("k").toString();
                        String vString = nodesFromGivenDocument.item(i).getChildNodes().item(j).getAttributes().getNamedItem("v").toString();
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
        
        for(int i = 0;i<waysFromGivenDocument.getLength();i++){
            String parsedWayID = waysFromGivenDocument.item(i).getAttributes().getNamedItem("id").toString();
            ArrayList<String> refsFromGivenWay= new ArrayList<String>();
            ArrayList<MapTag> tagsFromGivenWay= new ArrayList<MapTag>();
            if(waysFromGivenDocument.item(i).hasChildNodes()){
                NodeList childsFromGivenWays = waysFromGivenDocument.item(i).getChildNodes();
                
                for(int j=0;j<childsFromGivenWays.getLength();j++){
                    if(childsFromGivenWays.item(j).getNodeName()=="nd"){
                        if(changedIDS.containsKey(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue())){
                            refsFromGivenWay.add(changedIDS.get(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue()));
                        }else{
                            refsFromGivenWay.add(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue());
                        }
                    }else if (childsFromGivenWays.item(j).getNodeName()=="tag"){
                        String kString = childsFromGivenWays.item(j).getAttributes().getNamedItem("k").getNodeValue();
                        String vString = childsFromGivenWays.item(j).getAttributes().getNamedItem("v").getNodeValue();
                        tagsFromGivenWay.add(new MapTag(kString,vString));
                    }
                }        
            }
            MapWay parsedWay=new MapWay(parsedWayID,refsFromGivenWay,tagsFromGivenWay);
            parsedWayTree.insert(parsedWay);
        }
        for(int i = 0;i<nodesToTransfer.size();i++){
        parsedNodeTree.insert(nodesToTransfer.get(i));
    }
        givenHolder.setNodes(parsedNodeTree);
        givenHolder.setWays(parsedWayTree);
    }
}
