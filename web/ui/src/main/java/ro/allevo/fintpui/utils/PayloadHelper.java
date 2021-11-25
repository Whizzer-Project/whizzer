package ro.allevo.fintpui.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.codehaus.jettison.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ro.allevo.fintpui.model.EditRules;
import ro.allevo.fintpui.model.TemplateConfigDetailed;

public class PayloadHelper {

	private static final String XML_FRIENDLY_TAGS = "XMLFriendlyTags.xlsx";

	private PayloadHelper() {
		throw new IllegalStateException("PayloadHelper class");
	}

	public static void friendlyPayload(Document payload) {
		// load xlsx
		String path = PayloadHelper.class.getClassLoader().getResource(XML_FRIENDLY_TAGS).getPath();

		Map<String, String> friendlyTags = new HashMap<>();

		try {
			Workbook workbook = WorkbookFactory.create(new File(path));
			Sheet firstSheet = workbook.getSheetAt(0);

			DataFormatter dataFormatter = new DataFormatter();

			for (Row row : firstSheet) {
				String firstCell = dataFormatter.formatCellValue(row.getCell(0));
				String secondCell = dataFormatter.formatCellValue(row.getCell(1));

				friendlyTags.put(secondCell, firstCell);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// rename nodes
		NodeList nodes = payload.getElementsByTagName("*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = node.getNodeName();

				// split by case
				String[] tokens = StringUtils.splitByCharacterTypeCamelCase(nodeName);

				String newNodeName = "";

				for (String token : tokens)
					newNodeName += friendlyTags.getOrDefault(token, token);

				payload.renameNode(node, node.getNamespaceURI(), newNodeName);
			}
		}
	}

	public static void editPayload(Document payload, TemplateConfigDetailed[] configOptions) {

		List<TemplateConfigDetailed> templatesConfig = new ArrayList<>(Arrays.asList(configOptions));

		// rename nodes
		NodeList nodes = payload.getElementsByTagName("*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String xPath = getXPath(node);
				((Element) node).setAttribute("xpath", xPath);
				String id = "-1";
				Optional<TemplateConfigDetailed> templateConfig = templatesConfig.stream()
						.filter(o -> o.getFieldxpath().equals(xPath)).findFirst();
				if (templateConfig.isPresent() && Boolean.TRUE.equals(templateConfig.get().getEditable())) {
					id = templateConfig.get().getTxtemplatesconfigoption().getDatasource();
				}
				((Element) node).setAttribute("optionId", String.valueOf(id));
			}
		}
		friendlyPayload(payload);
	}
	
	public static void applyEditRules(String xPath, Node node, List<EditRules> listEditRules, boolean attribute) {
		String id = "-1";
		String attr ="";
		if (attribute) {
			attr = "Attr";
		}
		Optional<EditRules> editRulesXPath = listEditRules.stream().filter(o -> o.getTxField().equalsIgnoreCase(xPath.toLowerCase())).findFirst();
		if (editRulesXPath.isPresent()) {
			id = editRulesXPath.get().getBussList().equals("")?"editvalue":editRulesXPath.get().getBussList().toLowerCase();
			String pattern = editRulesXPath.get().getPattern();
			Boolean mandatory = editRulesXPath.get().getMandatory();
			String bussList = editRulesXPath.get().getBussListField();
			LinkedHashMap<String, Object> conditions = editRulesXPath.get().getConditions();
			String  name = editRulesXPath.get().getName().replaceAll(" ", "_");
			((Element) node).setAttribute("pattern" + attr, pattern);
			((Element) node).setAttribute("bussList" + attr, bussList);
			((Element) node).setAttribute("conditions" + attr, new JSONObject(conditions).toString());
			((Element) node).setAttribute("name" + attr, name);
			((Element) node).setAttribute("mandatory" + attr, Boolean.toString(mandatory));
		}
		((Element) node).setAttribute("optionId" + attr, String.valueOf(id));
		((Element) node).setAttribute("xpath" + attr, xPath);
	}

	public static void editPayload(Document payload, EditRules[] editRules) {
		
		List<EditRules> listEditRules = new ArrayList<>(Arrays.asList(editRules));
		
		NodeList nodes = payload.getElementsByTagName("*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String xPath = getXPath(node);
				
				NamedNodeMap attributes = node.getAttributes();
				int length = attributes.getLength();
				for(int item = 0; item < length; item++) {
					String nodeAtribute = attributes.item(item).getNodeName();
					applyEditRules(xPath + "/@" + nodeAtribute, node, listEditRules, true);
				}
				applyEditRules(xPath , node, listEditRules, false);
			}
		}
		friendlyPayload(payload);
	}
	
	private static String getMapValue(Map<String, String> xPaths, String xPath) {
		if (xPaths.containsKey(xPath)) {
			return xPaths.get(xPath).trim();
		}
		return null;
	}

	public static String savePayload(Document payload, Map<String, String> xPaths, boolean addNewNode, boolean addNewEmptyNode) throws Exception {

		// rename nodes
		NodeList nodes = payload.getElementsByTagName("*");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String xPath = getXPath(node);
				for (int item = 0; item < node.getAttributes().getLength(); item++) {
					String[] pairAttr = node.getAttributes().item(item).toString().split("=");
					String keyAttr = xPath + "/@" + pairAttr[0];
					String valueAttr = getMapValue(xPaths, keyAttr);
					if (null != valueAttr && !valueAttr.equals("")) {
						node.getAttributes().item(item).setTextContent(valueAttr);
						xPaths.remove(keyAttr);
					}
				}
				if (xPaths.containsKey(xPath)) {
					String value = xPaths.get(xPath).trim();
					node.setTextContent(value);
					xPaths.remove(xPath);
				}
			}
		}
		
		if(Boolean.TRUE.equals(addNewNode)) {
			for(Entry<String, String> map : xPaths.entrySet()) {
				String newxPath = map.getKey();
				String value = map.getValue().trim();
				if (Boolean.TRUE.equals(addNewEmptyNode) || !value.equals("")) {
					String[] segments = newxPath.split("/");
					segments = (String[]) ArrayUtils.remove(segments, 0);
					createElement(payload, payload.getDocumentElement(), segments, 1, value);
				}
			}
		}
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			removeChildNode(node);
		}
		try {
			return getStringFromDoc(payload);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static boolean removeChildNode(Node nodeParent) {
		boolean haveChild = false;
		if (0 == nodeParent.getChildNodes().getLength()) {
			return false;
		}
		for (int item = 0; item < nodeParent.getChildNodes().getLength();) {
			Node nodeChild = nodeParent.getChildNodes().item(item);
			String valueNodeChild = nodeChild.getNodeValue(); 
			if (null == valueNodeChild || valueNodeChild.trim().isEmpty()) {
				haveChild = removeChildNode(nodeChild);
				if (!haveChild) {
					nodeParent.removeChild(nodeChild);
				}else {
					item++;
				}
			}
			else {
				item++;
			}
		}
		return true;
	}

	public static void createElement(Document doc, Node parent, String[] segments, int index, String value)
			throws Exception {
		String xpath = String.join("/", Arrays.copyOfRange(segments, 0, index));
		Node node = getElementByXpath(doc, xpath);
		if ((node == null) || segments[index - 1].startsWith("@")) {
			if (segments[index - 1].startsWith("@")) {
				((Element) parent).setAttribute(StringUtils.substringAfter(segments[index - 1], "@"), value);
			} else {
				Element itemElement = doc.createElement(segments[index - 1]);
				if (index == segments.length) {
					Text newContent = doc.createTextNode(value);
					itemElement.appendChild(newContent);
				}
				parent.appendChild(itemElement);
				parent = itemElement;
			}
		} else {
			parent = node;
			if (index == segments.length) {
				if (!parent.hasChildNodes()) {
					parent.setTextContent(value);
				}
			}
		}
		if (index < segments.length)
			createElement(doc, parent, segments, ++index, value);
	}

	public static Node getElementByXpath(Document document, String xpathExpression) throws Exception {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			XPathExpression expr = xpath.compile(xpathExpression);
			NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			if (nodes != null && nodes.getLength() > 0) {
				return nodes.item(0);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getStringFromDoc(Document doc) throws TransformerException {
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(domSource, result);
		writer.flush();
		return writer.toString();
	}

	public static String getXPath(Node node) {
		Node parent = node.getParentNode();
		if (parent == null) {
			return "";
		}
		return getXPath(parent) + "/" + node.getNodeName();
	}

	public static Document parsePayload(String message) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(message)));

		return doc;
	}
}
