package de.fh_bielefeld.geograph.PARSER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.fh_bielefeld.geograph.API.OSMApi;
import de.fh_bielefeld.geograph.API.Exception.InvalidAPIRequestException;
import de.fh_bielefeld.geograph.GUI.AVLTree;
import de.fh_bielefeld.geograph.GUI.MapNode;
import de.fh_bielefeld.geograph.GUI.MapTag;
import de.fh_bielefeld.geograph.GUI.MapWay;
import de.fh_bielefeld.geograph.GUI_INTERFACE.ContentHolderInterface;

/**
 * The Parser Class for an osm type Document
 * with Methods to parse the given Document, and to return the Streets.
 * 
 * @author Stefan Schuck
 * @version 0.1
 * @since 2017-05-25
 */
public class OmlParser {
	private double				positiveDifference, negativeDifference;
	private ArrayList<MapNode>		parsedNodes;
	private ArrayList<MapWay>		parsedWays;
	private ArrayList<MapNode>		nodesToTransfer;
	private ArrayList<MapWay>		waysToTransfer;
	private ContentHolderInterface          usedHolder;
	private Map<String, String>		changedIDS;
	private Map<String, String>		includeConditions;
	private Document			givenDocument;

	/**
	 * Method to get the data of a single node by id
	 * 
	 * @param givenHolder
	 *            The ID of the Node you want to get additional information about
	 */
	public OmlParser(ContentHolderInterface givenHolder) {
		usedHolder = givenHolder;
		positiveDifference = 0.00015;// magicNumber how close the Nodes must be to be considered as one
		negativeDifference = positiveDifference * (-1);
		parsedNodes = new ArrayList<MapNode>();
		parsedWays = new ArrayList<MapWay>();
		nodesToTransfer = new ArrayList<MapNode>();
		waysToTransfer = new ArrayList<MapWay>();
		changedIDS = new HashMap<String, String>();
		includeConditions = new HashMap<String, String>();
		includeConditions.put("route", "road");

	}
        /**
	 * Method to get the data of a single node by id
	 * 
	 */
	private void clearEverythingUnimportant() {
		waysToTransfer.clear();
		nodesToTransfer.clear();
                parsedNodes.clear();
                parsedWays.clear();
		changedIDS.clear();

	}

	public ContentHolderInterface parse() throws NullPointerException, InvalidAPIRequestException {
		clearEverythingUnimportant();
		OSMApi ApiCaller = new OSMApi();
		givenDocument = ApiCaller.getBoundingBoxLatLong(usedHolder.getMinLatitude(), usedHolder.getMinLongitude(), usedHolder.getMaxLatitude(), usedHolder.getMaxLongitude());

		givenDocument.getDocumentElement().normalize();
		
		NodeList relationsFromGivenDocument = givenDocument.getElementsByTagName("relation");

		for (int i = 0; i < relationsFromGivenDocument.getLength(); i++) {
			boolean isImportant = false;
			if (relationsFromGivenDocument.item(i).hasChildNodes()) {
				NodeList childsOfRelation = relationsFromGivenDocument.item(i).getChildNodes();
				for (int x = 0; x < childsOfRelation.getLength(); x++) {
					if ((childsOfRelation.item(x).getAttributes() != null) && (childsOfRelation.item(x).getAttributes().getNamedItem("k") != null)) {
						if (includeConditions.containsKey(childsOfRelation.item(x).getAttributes().getNamedItem("k").getNodeValue())) {
							if ((childsOfRelation.item(x).getAttributes().getNamedItem("v").getNodeValue()).equals(includeConditions.get(childsOfRelation.item(x).getAttributes().getNamedItem("k").getNodeValue()))) {
								isImportant = true;
							}
						}
					}
				}
				if (isImportant) {
					for (int x = 0; x < childsOfRelation.getLength(); x++) {
						if ((childsOfRelation.item(x).getAttributes() != null) && (childsOfRelation.item(x).getAttributes().getNamedItem("type") != null)) {
							if ((childsOfRelation.item(x).getAttributes().getNamedItem("type").getNodeValue()).equals("way")) {
								XPathFactory factory = XPathFactory.newInstance();
								XPath xpath = factory.newXPath();
								try {
                                                                    String anfrageString = "/osm/way[@id='" + childsOfRelation.item(x).getAttributes().getNamedItem("ref").getNodeValue() + "']";
                                                                    Node uebergabeNode = (Node) xpath.evaluate(anfrageString, givenDocument, XPathConstants.NODE);
                                                                    if (uebergabeNode != null) {
									parseWay(uebergabeNode);
                                                                    }
								} catch (XPathExpressionException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
		usedHolder.setNodes(parsedNodes);
		usedHolder.setWays(parsedWays);
		return usedHolder;
	}

	private void parseWay(Node givenWay) {
            nodesToTransfer.clear();
		String parsedWayID = givenWay.getAttributes().getNamedItem("id").getNodeValue();
		ArrayList<String> refsFromGivenWay = new ArrayList<String>();
		ArrayList<MapTag> tagsFromGivenWay = new ArrayList<MapTag>();
		if (givenWay.hasChildNodes()) {
			NodeList childsFromGivenWays = givenWay.getChildNodes();
			for (int j = 0; j < childsFromGivenWays.getLength(); j++) {
				if (childsFromGivenWays.item(j).getNodeName() == "nd") {
                                    boolean childExists=false;
                                    for(MapNode nodeToCheck:parsedNodes){
                                        if(nodeToCheck.getId().equals(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue())){
                                            childExists = true;
                                            break;
                                        }
                                    }
                                    for(MapNode nodeToCheck:nodesToTransfer){
                                        if(nodeToCheck.getId().equals(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue())){
                                            childExists = true;
                                            break;
                                        }
                                    }
                                    if(!childExists){
					XPathFactory factory = XPathFactory.newInstance();
					XPath xpath = factory.newXPath();
					try {
						String anfrageString = "/osm/node[@id='" + childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue() + "']";
						Node uebergabeNode = (Node) xpath.evaluate(anfrageString, givenDocument, XPathConstants.NODE);
						if (uebergabeNode != null) {
							parseNode(uebergabeNode);
						}
					} catch (XPathExpressionException e) {
						e.printStackTrace();
					}

					if (changedIDS.containsKey(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue())) {
						refsFromGivenWay.add(changedIDS.get(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue()));
					} else {
						refsFromGivenWay.add(childsFromGivenWays.item(j).getAttributes().getNamedItem("ref").getNodeValue());
					}
                                    }
				}
			}
		}
		MapWay parsedWay = new MapWay(parsedWayID, refsFromGivenWay, tagsFromGivenWay);
		parsedWays.add(parsedWay);
                for(MapNode node:nodesToTransfer){
                    parsedNodes.add(node);
                }
	}

	private void parseNode(Node givenNode) {
		String parsedNodeID = givenNode.getAttributes().getNamedItem("id").getNodeValue();
		Double parsedNodeLongitude = Double.parseDouble(givenNode.getAttributes().getNamedItem("lon").getNodeValue());
		Double parsedNodeLatitude = Double.parseDouble(givenNode.getAttributes().getNamedItem("lat").getNodeValue());

		MapNode parsedNode = new MapNode(parsedNodeID, parsedNodeLatitude, parsedNodeLongitude);

		if (givenNode.hasChildNodes() == true) {
			ArrayList<MapTag> tagsFromGivenNode = new ArrayList<MapTag>();
			NodeList childsNodesFromGivenNode = givenNode.getChildNodes();
			for (int j = 0; j < childsNodesFromGivenNode.getLength(); j++) {
				if (childsNodesFromGivenNode.item(j).hasAttributes()) {
					String kString = givenNode.getChildNodes().item(j).getAttributes().getNamedItem("k").toString();
					String vString = givenNode.getChildNodes().item(j).getAttributes().getNamedItem("v").toString();
					MapTag tagFromGivenNode = new MapTag(kString, vString);
					tagsFromGivenNode.add(tagFromGivenNode);
				}
			}
			if (!tagsFromGivenNode.isEmpty()) {
				parsedNode.setTagList(tagsFromGivenNode);
			}
		}
		for (int z = 0; z < parsedNodes.size(); z++) {
			if ((positiveDifference >= (parsedNodeLongitude - parsedNodes.get(z).getLongitude()) && (parsedNodeLongitude - parsedNodes.get(z).getLongitude() >= negativeDifference)) && (positiveDifference >= (parsedNodeLatitude - parsedNodes.get(z).getLatitude()) && (parsedNodeLatitude - parsedNodes.get(z).getLatitude()) >= negativeDifference)) {
				changedIDS.put(parsedNodeID, parsedNodes.get(z).getId());
				if ((parsedNode.getTagList() != null)) {
                                    if(parsedNodes.get(z).getTagList()!=null){
                                       ArrayList<MapTag> newList=parsedNodes.get(z).getTagList();
                                       newList.addAll(parsedNode.getTagList());
                                       parsedNodes.get(z).setTagList(newList);
                                    }else{
                                        parsedNodes.get(z).setTagList(parsedNode.getTagList());
                                    }
				}
				break;
			}
		}
		if (!changedIDS.containsKey(parsedNode.getId())) {
                    nodesToTransfer.add(parsedNode);
		}
	}
}
