package ro.allevo.fintpws.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ro.allevo.fintpws.model.ValidationsRulesEntity;

public class EnrichmentWithValidationResource {
	
	private UriInfo uriInfo;
	private EntityManager emc;
	private HttpHeaders headers;
	private String projectType;

	public EnrichmentWithValidationResource(UriInfo uriInfo, EntityManager emc, String projectType) {
		this.uriInfo = uriInfo;
		this.emc = emc;
		this.projectType = projectType;
	}

	@PUT
	@RequestMapping(consumes = MediaType.APPLICATION_XML)
	@ResponseBody
	public Document addAnvelopeToXml(@QueryParam("message_type") String messageType, @RequestBody String xmlString,
			@Context HttpHeaders headers) {
		this.headers = headers;

		Document xmlDoc = convertStringToXMLDocument(xmlString);
		if (xmlDoc == null) {
			xmlDoc = getDocumentBuilder().newDocument();
			Element document = xmlDoc.createElement("Document");
			Text newContent = xmlDoc.createTextNode("Document from xml is null");
			document.appendChild(newContent);
			xmlDoc.appendChild(document);
			return xmlDoc;
		}
		xmlDoc.getDocumentElement().normalize();
		xmlDoc = addEnvelopeToDocument(xmlDoc);
		try {
			updateDocFromEnrichRules(xmlDoc, messageType);
		} catch (StackOverflowError e) {

			xmlDoc = getStackOverflowErrorDocument(e.getMessage());
		}

		Document newDoc = addEnvelopeToDocument((formatingXMLDocument(xmlDoc, messageType)));
		addElemenetToXMl(newDoc, xmlDoc.getElementsByTagName("Enrich").item(0));
		addElemenetToXMl(newDoc, xmlDoc.getElementsByTagName("wsrm:SequenceFault").item(0));
		setXmlAttributes((Element) newDoc.getFirstChild(), getXmlAttributes(xmlDoc, "EnrichDocument"));

		updateDocFromValidations(newDoc, messageType);
		newDoc.normalize();

		return (newDoc);
	}

	private void addElemenetToXMl(Document doc, Node node) {
		if (null != node) {
			Node copyNode = doc.importNode(node, true);
			doc.getDocumentElement().appendChild(copyNode);
		}
	}

	private Element setXmlAttributes(Element element, Map<String, String> mapAttributes) {
		for (Map.Entry<String, String> attribute : mapAttributes.entrySet()) {
			element.setAttribute(attribute.getKey(), attribute.getValue());
		}
		return element;
	}

	private Map<String, String> getXmlAttributes(Document document, String tagName) {
		NodeList nodes = document.getElementsByTagName("*");
		Map<String, String> mapAttributes = new HashMap<>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			if (currentNode.getNodeName().equalsIgnoreCase(tagName)) {
				NamedNodeMap attributes = currentNode.getAttributes();
				for (int ind = 0; ind < attributes.getLength(); ind++) {
					mapAttributes.put(attributes.item(ind).getNodeName(), attributes.item(ind).getNodeValue());
				}
			}
		}
		return mapAttributes;
	}

	private Document formatingXMLDocument(Document envelopeDoc, String messageType) {
		String xmlFile = "xsd/xsd.cast.to.xml.xml";
		Map<String, String> attributeDocument = getXmlAttributes(envelopeDoc, "Document");
		if (this.projectType.equalsIgnoreCase("adpharma")) {
			xmlFile = "xsd/adpharma_xsd.cast.to.xml.xml";
			if (messageType.equalsIgnoreCase("BkToCstmrDbtCdtNtfctn")) {
				xmlFile = "xsd/xsd.cast.to.xml_adpharma.xml";
			} 
		}
		Document templateDoc = parseXMLFromFile(xmlFile);
		Document newDoc = getDocumentBuilder().newDocument();
		Element document = setXmlAttributes(newDoc.createElement("Document"), attributeDocument);
		newDoc.appendChild(document);
		if (null == templateDoc) {
			return newDoc;
		}
		NodeList nodes = templateDoc.getElementsByTagName("*");

		for (int i = 1; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			String templatexPath = getXPath(currentNode).replace("CstmrCdtTrfInitn", messageType);
			Element element = getDocElementFromXpath(envelopeDoc, "EnrichDocument/" + templatexPath);
			if (null != element) {
				try {
					String value = "";
					for (int ind = 0; ind < element.getChildNodes().getLength(); ind++) {
						if (null != element.getChildNodes().item(0).getNodeValue()) {
							value = element.getChildNodes().item(0).getNodeValue().trim();
						}
						break;
					}
					Pattern pattern = Pattern.compile("\\w");
					Matcher matcher = pattern.matcher(value);
					if (!matcher.find())
						value = "";
					createElement(newDoc, newDoc.getDocumentElement(), ArrayUtils.remove(templatexPath.split("/"), 0),
							1, value);
					for (int ind = 0; ind < element.getAttributes().getLength(); ind++) {
						Node attribute = element.getAttributes().item(ind);
						String valueAttribute = attribute.getNodeValue();
						if (null != valueAttribute && !valueAttribute.trim().isEmpty()) {
							templatexPath += "/@" + attribute.getNodeName();
							createElement(newDoc, newDoc.getDocumentElement(),
									ArrayUtils.remove(templatexPath.split("/"), 0), 1, valueAttribute);
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return newDoc;
	}

	private Document getStackOverflowErrorDocument(String msg) {
		DocumentBuilder db = getDocumentBuilder();
		if (null != db) {
			Document doc = db.newDocument();
			try {
				Element rootElement = doc.createElement("Error");
				Element msgElement = doc.createElement("Msg");
				Node node = doc.createTextNode(msg);
				msgElement.appendChild(node);
				rootElement.appendChild(msgElement);
				doc.appendChild(rootElement);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return doc;
		}
		return null;
	}

	private Document convertStringToXMLDocument(String xmlString) {

		try {
			DocumentBuilder builder = getDocumentBuilder();

			if (builder != null) {
				return builder.parse(new InputSource(new StringReader(xmlString)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Document addEnvelopeToDocument(Document xmlDoc) {
		if (null != getDocElementFromXpath(xmlDoc, "EnrichDocument")) {
			Element element = xmlDoc.getDocumentElement();
			element.setAttribute("xmlns:wsrm", "http://schemas.xmlsoap.org/ws/2005/02/rm");
			element.setAttribute("xmlns:wsu", "http://schemas.xmlsoap.org/ws/2002/07/utility");
			return xmlDoc;
		}
		Document newDoc = getDocumentBuilder().newDocument();

		Element root = newDoc.createElement("EnrichDocument");
		Map<String, String> attributes = getXmlAttributes(xmlDoc, "EnrichDocument");
		attributes.put("xmlns", "urn:fintp:enrich.01");
		attributes.put("xmlns:wsrm", "http://schemas.xmlsoap.org/ws/2005/02/rm");
		attributes.put("xmlns:wsu", "http://schemas.xmlsoap.org/ws/2002/07/utility");
		setXmlAttributes(root, attributes);
//		root.setAttribute("xmlns:wsrm", "http://schemas.xmlsoap.org/ws/2005/02/rm");
//		root.setAttribute("xmlns:wsu", "http://schemas.xmlsoap.org/ws/2002/07/utility");

		// Add a copy of the nodes from existing document
		Node copy = newDoc.importNode(xmlDoc.getDocumentElement(), true);
		root.appendChild(copy);

		newDoc.appendChild(root);
		return newDoc;
	}

	private void updateDocFromValidations(Document envelopeDoc, String messageType) {
		List<ValidationsRulesEntity> validationsRulesList = getValidationRulesByTxType(messageType);
		for (ValidationsRulesEntity validationsRulesEntity : validationsRulesList) {
			String algorithm = validationsRulesEntity.getAlgorithm();
			LinkedHashMap<String, Object> conditions = validationsRulesEntity.getConditions();
			if (!conditions.isEmpty() && (algorithm == null)) {
				JSONObject jsonObject = new JSONObject(conditions);
				if (jsonObject != null && isDataValid(jsonObject, envelopeDoc)) {
					// add to xml
					addInvalidationEnvelope(envelopeDoc, validationsRulesEntity);
				}
			}
			if (algorithm != null) {
				// 'CNP syntax' or 'IBAN syntax' or 'CIF syntax' from DB -> resourcePath needs
				// to be lower case and without 'syntax'
				String resourcePath = algorithm.trim().split("\\s+")[0].toLowerCase();

				String xpath = validationsRulesEntity.getTxField();
				String xpathValueFromXml = getXpathValue(envelopeDoc, xpath);

				// ex: api/validations/iban?value=<iban>
				MultiValueMap<String, String> queryParamMap = new LinkedMultiValueMap<>();
				if (resourcePath.equalsIgnoreCase("roIban")) {
					queryParamMap.add("iban", xpathValueFromXml);
					JSONObject jsonObject = new JSONObject(conditions);
					if (!conditions.isEmpty()) {
						queryParamMap.add("debtor",
								getXpathValue(envelopeDoc, jsonObject.optString("additionalXpath")));
					}
				} else {
					queryParamMap.add("value", xpathValueFromXml);
				}

				// call api from ValidationsResource for cnp, iban, cif
				Object result = getQueriedHttpResponse("/validations/" + resourcePath, queryParamMap, String.class);

				boolean isAlgoValid = false;
				if (result instanceof String && result.toString().equalsIgnoreCase("true")) {
					isAlgoValid = true;
				}
				if (!isAlgoValid) {
					addInvalidationEnvelope(envelopeDoc, validationsRulesEntity);
				}
			}
		}
	}

	private ValidationsRulesResources getValidationRules() {
		return new ValidationsRulesResources(uriInfo, emc);
	}

	private ValidationsRulesEntity getValidationsRulesByName(String name) {
		ValidationsRulesEntity validationsRules = new ValidationsRulesEntity();
		ValidationsRulesResources validationsRulesResources = getValidationRules();
		for (ValidationsRulesEntity validationsRulesEntity : validationsRulesResources.getItems()) {
			if (validationsRulesEntity.getName().equalsIgnoreCase(name)) {
				validationsRules = validationsRulesEntity;
			}
		}
		try {
			validationsRulesResources.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validationsRules;
	}

	private List<ValidationsRulesEntity> getValidationRulesByTxType(String messageType) {
		List<ValidationsRulesEntity> validationsRulesList = new ArrayList<>();
		ValidationsRulesResources validationsRulesResources = getValidationRules();
		for (ValidationsRulesEntity validationsRulesEntity : validationsRulesResources.getItems()) {
			if (validationsRulesEntity.getTxType().equalsIgnoreCase(messageType)) {
				validationsRulesList.add(validationsRulesEntity);
			}
		}
		try {
			validationsRulesResources.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validationsRulesList;
	}

	private Element createWsrmNackElement(Document envelopeDoc, ValidationsRulesEntity validationsRulesEntity) {
		Element wsrmNack = envelopeDoc.createElement("wsrm:Nack");
//		wsrmNack.setAttribute("id", "" + validationsRulesEntity.getId());
		wsrmNack.setTextContent(validationsRulesEntity.getName());
		return wsrmNack;
	}

	private DocumentBuilder getDocumentBuilder() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String convertDocumentToString(Document doc) {
		try {
			doc.normalize();
			StringWriter sw = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.transform(new DOMSource(doc), new StreamResult(sw));

			return sw.toString();
		} catch (Exception ex) {
			throw new RuntimeException("Error converting to String", ex);
		}
	}

	private boolean isDataValid(JSONObject jsonObject, Document xmlDoc) {
		try {
			if (jsonObject.has("condition")) {
				String condition = jsonObject.getString("condition");
				JSONArray jsonRules = jsonObject.getJSONArray("rules");
				MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
				String apiEntityName = "";
				boolean checkRezult = true;
				for (int i = 0; i < jsonRules.length(); i++) {
					JSONObject jsonRule = jsonRules.getJSONObject(i);
					if (jsonRule.has("condition")) {
						Boolean returnIsDataValid;
						if (condition.equalsIgnoreCase("AND")) {
							returnIsDataValid = checkWithResource(map, apiEntityName);
							if (Boolean.TRUE.equals(returnIsDataValid)) {
								return true;
							}
							map.clear();
						}
						returnIsDataValid = isDataValid(jsonRule, xmlDoc);
						if (Boolean.TRUE.equals(returnIsDataValid)) {
							return true;
						}
					} else {
						String[] entityAndField = jsonRule.getString("field").split("-");
						String entityType = entityAndField[0]; // Bank, InternalAccount, ExternalAccount etc
						String entityField = entityAndField[1];
						String typeOfFilter = "";
						String operator = jsonRule.getString("operator");

						if (operator.contains("equal")) {
							if (condition.equalsIgnoreCase("or")) {
								typeOfFilter = "_exact";
								if (operator.contains("not")) {
									checkRezult = false;
								}
							} else {
								typeOfFilter = operator.contains("not") ? "_nexact" : "_exact";
							}
						} else if (operator.equals("<")) {
							typeOfFilter = "_lnum";
						} else if (operator.contentEquals(">")) {
							typeOfFilter = "_unum";
						}
						// -> exact / contains
						String filter = "filter_" + entityField + typeOfFilter;
						apiEntityName = getApiEntityCallingName(entityType);

						String[] typeAndValue = jsonRule.getString("value").split("\\.");
						String typeRule = typeAndValue[0];
						String xPathValue = typeAndValue[1];
						switch (typeRule) {
						case "xpath":
							String xpathValueFromXml = getXpathValue(xmlDoc, xPathValue);
							if (xpathValueFromXml == null) {
								return true;
							}
							map.add(filter, xpathValueFromXml);
							break;
						case "rule":
							ValidationsRulesEntity validationsRulesEntity = getValidationsRulesByName(xPathValue);
							LinkedHashMap<String, Object> conditions = validationsRulesEntity.getConditions();
							if (!conditions.isEmpty()) {
								JSONObject conditionJsonObject = new JSONObject(conditions);
								if (conditionJsonObject != null && !isDataValid(conditionJsonObject, xmlDoc)) {
									return false;
								}
							}
							break;
						case "text":
							map.add(filter, xPathValue);
							break;
						default:
							throw new IllegalArgumentException();
						}
					}
					if (condition.equalsIgnoreCase("OR") && !map.isEmpty()) {
						Boolean returnCheckWithResource = checkWithResource(map, apiEntityName);
						map.clear();
						if (!returnCheckWithResource == checkRezult) {
							return false;
						}
					}
				}
				if (!map.isEmpty()) {
					return checkWithResource(map, apiEntityName);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean checkWithResource(MultiValueMap<String, String> map, String apiEntityName) {
		ArrayList<?> responseItemsList = getFilteredHttpResponseItemsList(uriInfo, HttpMethod.GET, headers, map,
				apiEntityName);
		if (responseItemsList == null) {
			return false;
		}
		return responseItemsList.isEmpty();
	}

	private String getXpathValue(Document doc, String xpathExpression) {
		String nodeValue = null;
		try {
			Element element = getDocElementFromXpath(doc, xpathExpression);
			if (element != null) {
				nodeValue = element.getNodeValue();
				if (element.getFirstChild() != null) {
					nodeValue = element.getFirstChild().getNodeValue();
				}
			}
		} catch (DOMException e) {
			e.printStackTrace();
		}
		return nodeValue;
	}

	private Node getDocumentFromXML(Document doc, String nodeName) {
		return getDocElementFromXpath(doc, nodeName);
	}

	private Element getDocElementFromXpath(Document doc, String xpathExpression) {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			Element element = (Element) xpath.evaluate("/" + xpathExpression, doc, XPathConstants.NODE);
			return element;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setXpathValue(Document doc, String xpathExpression, String valuetoInsertInXpath) throws Exception {
		try {
			if (null != getDocumentFromXML(doc, "EnrichDocument")) {
				xpathExpression = "/EnrichDocument" + xpathExpression;
			}
			Element element = getDocElementFromXpath(doc, xpathExpression);

			if (element != null) {
				element.setNodeValue(valuetoInsertInXpath);
				if (element.getFirstChild() != null) {
					element.getFirstChild().setNodeValue(valuetoInsertInXpath);
				}
			} else {
				addNodeByDoc(doc, /* "/EnrichDocument" + */ xpathExpression, valuetoInsertInXpath);
			}
		} catch (DOMException e) {
			e.printStackTrace();
		}
	}

	private Document addNodeByDoc(Document doc, String fieldXPath, String fieldValue) throws Exception {
		String[] segments = fieldXPath.split("/");
		segments = org.apache.commons.lang3.ArrayUtils.remove(segments, 0);
		createElement(doc, doc.getDocumentElement(), segments, 1, fieldValue);
		return doc;
	}

	private void createElement(Document doc, Node parent, String[] segments, int index, String value) throws Exception {
		String xpath = String.join("/", Arrays.copyOfRange(segments, 0, index));
		Node node = getDocElementFromXpath(doc, xpath);
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
			if (index == segments.length && !parent.hasChildNodes()) {
				parent.setTextContent(value);
			}
		}
		if (index < segments.length)
			createElement(doc, parent, segments, ++index, value);
	}

	private boolean checkWithResource(String xpathValueFromXml, String operator, String... fields) {
		if (fields.length == 2) {
			String entityType = fields[0]; // Bank, InternalAccount, ExternalAccount etc
			String entityField = fields[1];
			String typeOfFilter = "";

			if (operator.contains("equal")) {
//				typeOfFilter = operator.contains("not") ? "_nexact" : "_exact";
				typeOfFilter = "_exact";
			}
			// -> exact / contains
			String filter = "filter_" + entityField + typeOfFilter;
			String apiEntityName = getApiEntityCallingName(entityType);

			boolean checkReturn = true;
			if (operator.toLowerCase().contains("not")) {
				checkReturn = false;
			}

			MultiValueMap<String, String> map = new LinkedMultiValueMap();
			map.add(filter, xpathValueFromXml);
			ArrayList<?> responseItemsList = getFilteredHttpResponseItemsList(uriInfo, HttpMethod.GET, headers, map,
					apiEntityName);
			if (responseItemsList == null) {
				return false;
			}
			return (!responseItemsList.isEmpty()) == checkReturn;
		}
		return false;
	}

	private String getApiEntityCallingName(String entityName) {
		String[] entityNameSplitByUpperCaseLetter = entityName.split("(?<=.)(?=\\p{Lu})"); // split after upper case
																							// letter
		int nameSplitLength = entityNameSplitByUpperCaseLetter.length;
		StringBuilder entityNameBuilder = new StringBuilder();

		for (int i = 0; i < nameSplitLength; i++) {
			if (entityNameSplitByUpperCaseLetter[i].substring(entityNameSplitByUpperCaseLetter[i].length() - 1)
					.equalsIgnoreCase("y") && i == nameSplitLength - 1) {
				entityNameBuilder.append(entityNameSplitByUpperCaseLetter[i]
						.substring(0, entityNameSplitByUpperCaseLetter[i].length() - 1).toLowerCase());
				entityNameBuilder.append("ie");
			} else {
				entityNameBuilder.append(entityNameSplitByUpperCaseLetter[i].toLowerCase());
			}

			if (i < nameSplitLength - 1) {
				entityNameBuilder.append('-');
			}
			if (i == nameSplitLength - 1) {
				entityNameBuilder.append('s');
			}
		}
		return entityNameBuilder.toString();
	}

//	@JsonIgnore
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<?> getFilteredHttpResponseItemsList(UriInfo uriInfo, HttpMethod method, HttpHeaders headers,
			MultiValueMap<String, String> filters, String apiEntityName) {

		RestTemplate restTemplate = new RestTemplate();

		org.springframework.http.HttpHeaders headersSpring = new org.springframework.http.HttpHeaders();
		headers.getRequestHeaders().forEach((key, value) -> headersSpring.put(key.toLowerCase(), value));

		HttpEntity<Object> httpEntity = new HttpEntity<>(headersSpring);
		try {
			List<PathSegment> pathSegments = uriInfo.getPathSegments();

//		    uriInfo.getBaseUri() = http://localhost:8086/  -----   pathSegments.get(0) = api     
			String urlString = uriInfo.getBaseUri().toString() + pathSegments.get(0).toString() + "/" + apiEntityName; // ex:
																														// +
																														// banks
																														// +
																														// "?filter_bic_exact=CECEROBU"
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlString);
//			builder.queryParam("filter_bic_exact", "CECEROBU");
			builder.queryParams((MultiValueMap<String, String>) filters);

			ResponseEntity<Object> responseEntity = restTemplate.exchange(builder.toUriString(), method, httpEntity,
					Object.class);
			Object result = responseEntity.getBody();
			if (result == null) {
				return null;
			}

			if (result instanceof LinkedHashMap<?, ?>) {
				LinkedHashMap<?, ?> jsObj = (LinkedHashMap<?, ?>) result;
				Object itemsObj = jsObj.get("items");

				if (itemsObj instanceof ArrayList<?>) {
					return (ArrayList<?>) itemsObj;
				}
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			System.out.println(e.getResponseBodyAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void updateDocFromEnrichRules(Document envelopeDoc, String messageType) {
		// <Enrich><entity name="Business field list" id="rule
		// name">value</entity></Enrich>
		// when call api with 1 or 2 filters - should return 1 element

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("filter_txType_exact", messageType);
		List<?> enrichList = getFilteredHttpResponseItemsList(uriInfo, HttpMethod.GET, headers, map, "enrich");

		Iterator<?> iterator = enrichList.iterator();
		while (iterator.hasNext()) {
			LinkedHashMap<String, Object> enrich = (LinkedHashMap<String, Object>) iterator.next();
			JSONObject enrichJSON = new JSONObject(enrich);
			try {
				if (enrichJSON.has("txtemplatesconfigoption")) {
					// add predefined field
					addPredefinedFieldToEnrich(enrichJSON, envelopeDoc);
				} else {
					getReadRuleConditions(enrichJSON, envelopeDoc, enrichList, 0, null);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//			for (Object enrich : enrichList) {
//				// ex:
//				// conditions:"{"condition":"AND","rules":[{"id":"Bank1","field":"Bank-bic","type":"string","input":"text","operator":"equal","value":"text.CECEROBU"},
//				// {...}],"valid":true}"
//				// enrich has 1 "condition" and max 2 rules
//				// only 1 api call with max 2 filters
//				JSONObject entity =new JSONObject(enrich);
//				entity.get("txField")
//				getReadRuleConditions((LinkedHashMap<String, Object>) entity, envelopeDoc, enrichList, 0, null);
//			}

	}

	private void addPredefinedFieldToEnrich(JSONObject enrichJSON, Document envelopeDoc) throws Exception {
		String pattern = enrichJSON.getString("pattern");
		JSONObject txtemplatesconfigoption = enrichJSON.getJSONObject("txtemplatesconfigoption");
		String dataSource = txtemplatesconfigoption.getString("datasource").trim();
		String name = enrichJSON.getString("name");
		String xmlNode = enrichJSON.getString("txField");
		String[] segment = xmlNode.split("/");
		String field = segment[segment.length - 1];
		String xPathValue = getXpathValue(envelopeDoc, xmlNode);

		if (!pattern.isEmpty() && (null == xPathValue || xPathValue.isEmpty())) {
			switch (dataSource) {
			case "sequency":
			case "date":
				Date date = Calendar.getInstance().getTime();
				DateFormat dateFormat = new SimpleDateFormat(pattern);
				try {
					xPathValue = dateFormat.format(date);
				} catch (Exception e) {
					addEnrichElement(envelopeDoc, field, name, e.getMessage());
				}
				break;
			case "timestamp":
				TimeStamp ts = new TimeStamp(Calendar.getInstance().getTime());
				xPathValue = ts.toString();
				break;
			default:
				addEnrichElement(envelopeDoc, field, name, "Patter undefined");
				return;
			}
			setXpathValue(envelopeDoc, xmlNode, xPathValue);
		}
//		else {
//			addEnrichElement(envelopeDoc, field, name, "Patter is empty");
//		}

	}

	private Document parseXMLFromFile(String fileName) {
		if (null == fileName) {
			return null;
		}
		InputStream is = getFileFromResourceAsStream(fileName);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			return db.parse(is);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private InputStream getFileFromResourceAsStream(String fileName) {

		// The class loader that loaded the class
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);

		// the stream holding the file content
		if (inputStream == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return inputStream;
		}

	}

	public static String getXPath(Node node) {
		Node parent = node.getParentNode();
		if (parent == null) {
			return "";
		}
		return getXPath(parent) + "/" + node.getNodeName();
	}

	private String getReadRuleConditions(JSONObject jsonObjectEnrich, Document envelopeDoc, List<?> enrichList,
			int iter, Set<String> oldSetRules) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		String entityFieldValue = "";
		Set<String> setRules = new HashSet<>();
		if (null != oldSetRules)
			setRules.addAll(oldSetRules);
		iter++;
		try {
			String ruleName = jsonObjectEnrich.getString("name");
			String bussListField = jsonObjectEnrich.getString("bussListField");
			String xpathFromTxField = jsonObjectEnrich.getString("txField");
			boolean isMandatory = jsonObjectEnrich.getBoolean("mandatory");
			JSONObject jsonObjectConditions = jsonObjectEnrich.getJSONObject("conditions");

			entityFieldValue = getXpathValue(envelopeDoc, xpathFromTxField);
			setRules.add(ruleName);
			if (null == entityFieldValue && iter != setRules.size()) {
				throw new StackOverflowError(ruleName);
			}
			if (null == entityFieldValue && jsonObjectConditions.has("condition")) {
				JSONArray jsonRules = jsonObjectConditions.getJSONArray("rules");
				ArrayList<?> responseItemsList = null;
				String apiEntityCallingName = null;
				for (int i = 0; i < jsonRules.length(); i++) {
					JSONObject jsonRule = jsonRules.getJSONObject(i);

					String[] fields = jsonRule.getString("field").split("-"); // ex: "field":"Bank-bic"
					String operator = jsonRule.getString("operator");

					if (fields[0].equals("xpath")) {
						String nodeValue = getXpathValue(envelopeDoc,
								jsonRule.getJSONObject("data").getString("filterXpath"));
						switch (operator) {
						case "is_not_null":
							if (nodeValue.length() == 0) {
								i = 100;
								map.clear();
							}
							break;
						case "is_null":
							if (nodeValue.length() != 0) {
								i = 100;
								map.clear();
							}
							break;
						}
					} else {
						apiEntityCallingName = getApiEntityCallingName(fields[0]);

						String typeOfFilter = "";
						if (operator.contains("equal")) {
							typeOfFilter = operator.contains("not") ? "_nexact" : "_exact";
						}
						// "operator":"(not_)equal/(not_)contains"
						// -> exact / contains
						String filter = "filter_" + fields[1] + typeOfFilter;

						String[] valueFieldRule = jsonRule.getString("value").split("\\.");
						String jsonRuleValueType = valueFieldRule[0];
						String jsonRuleValue = valueFieldRule[1];

						switch (jsonRuleValueType) {
						case "xpath":
							String filterValue = getXpathValue(envelopeDoc, jsonRuleValue);
							map.add(filter, filterValue);
							break;
						case "text":
							map.add(filter, jsonRuleValue.trim());
							break;
						case "rule":
							for (int item = 0; item < enrichList.size(); item++) {
								LinkedHashMap<String, Object> enrich = (LinkedHashMap<String, Object>) enrichList
										.get(item);
								JSONObject enrichJSON = new JSONObject(enrich);
								if (jsonRuleValue.trim().equals(enrichJSON.getString("name").trim())) {
									try {
										String value = getReadRuleConditions(enrichJSON, envelopeDoc, enrichList, iter,
												setRules);
										map.add(filter, value.trim());
									} catch (StackOverflowError e) {
										throw new StackOverflowError(ruleName + ", " + e.getMessage());
									}
									break;
								}
							}
							break;
						default:
							throw new IllegalArgumentException();
						}
					}
					if (jsonObjectConditions.get("condition").toString().equalsIgnoreCase("OR")) {
						entityFieldValue = executeMap(envelopeDoc, map, jsonObjectEnrich, i);
						if (null != entityFieldValue) {
							return entityFieldValue;
						}
						map.clear();
					}
				}

				entityFieldValue = executeMap(envelopeDoc, map, jsonObjectEnrich, jsonRules.length() - 1);
//				if (!map.isEmpty()) {
//					// when call api with 1 or 2 filters - should return 1 element
//					responseItemsList = getFilteredHttpResponseItemsList(uriInfo, HttpMethod.GET, headers, map,
//							apiEntityCallingName);
//
//					if (Boolean.FALSE.equals(isMandatory) && responseItemsList.isEmpty()) {
//						// don't add node
//					} else {
//						String msgAddEnrich = "Succes";
//						try {
//							for (int ind = 0; ind < responseItemsList.size(); ind++) {
//								// bussListField = entityField
//								JSONObject responseJson = new JSONObject(
//										(LinkedHashMap<String, Object>) responseItemsList.get(ind));
//								if (responseJson.has("defaultAccount")
//										&& responseJson.getString("defaultAccount").equalsIgnoreCase("Y")) {
//									// entityFieldValue insert in xpath from txField
//									entityFieldValue = responseJson.getString(bussListField);
//									break;
//								} else
//									entityFieldValue = responseJson.getString(bussListField);
//							}
//							if (null == entityFieldValue || (!isMandatory && 0 == entityFieldValue.trim().length())) {
//								msgAddEnrich = "Error:  entity field value is empty";
//							} else {
//								setXpathValue(envelopeDoc, xpathFromTxField, entityFieldValue);
//							}
//						} catch (Exception ex) {
//							msgAddEnrich = "Error: " + ex.getMessage();
//						}
//						addEnrichElement(envelopeDoc, bussListField, ruleName, msgAddEnrich);
//					}
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entityFieldValue;
	}

	private String executeMap(Document envelopeDoc, MultiValueMap<String, String> map, JSONObject jsonObjectEnrich,
			int index) throws JSONException {
		String entityFieldValue = null;
		ArrayList<?> responseItemsList = null;
		String ruleName = jsonObjectEnrich.getString("name");
		String bussListField = jsonObjectEnrich.getString("bussListField");
		String xpathFromTxField = jsonObjectEnrich.getString("txField");
		boolean isMandatory = jsonObjectEnrich.getBoolean("mandatory");
		JSONObject jsonObjectConditions = jsonObjectEnrich.getJSONObject("conditions");
		JSONArray jsonRules = jsonObjectConditions.getJSONArray("rules");
		JSONObject jsonRule = jsonRules.getJSONObject(index);
		String[] fields = jsonRule.getString("field").split("-");
		String apiEntityCallingName = getApiEntityCallingName(fields[0]);
		if (!map.isEmpty()) {
			// when call api with 1 or 2 filters - should return 1 element
			responseItemsList = getFilteredHttpResponseItemsList(uriInfo, HttpMethod.GET, headers, map,
					apiEntityCallingName);
//			String msgAddEnrich = "Succes";
			if (!isMandatory && responseItemsList.isEmpty()) {
				// don't add node
			} else {
				String msgAddEnrich = "Succes";
				try {
					for (int ind = 0; ind < responseItemsList.size(); ind++) {
						// bussListField = entityField
						JSONObject responseJson = new JSONObject(
								(LinkedHashMap<String, Object>) responseItemsList.get(ind));
						if (responseJson.has("defaultAccount")
								&& responseJson.getString("defaultAccount").equalsIgnoreCase("Y")) {
							// entityFieldValue insert in xpath from txField
							entityFieldValue = getResponseFromJson(responseJson,bussListField);
							break;
						} else {
								entityFieldValue = getResponseFromJson(responseJson,bussListField);
						}
							
					}
					if (null == entityFieldValue || (!isMandatory && 0 == entityFieldValue.trim().length())) {
						msgAddEnrich = "Error:  entity field value is empty";
					} else {
						setXpathValue(envelopeDoc, xpathFromTxField, entityFieldValue);
					}
				} catch (Exception ex) {
					msgAddEnrich = "Error: " + ex.getMessage();
				}
				addEnrichElement(envelopeDoc, bussListField, ruleName, msgAddEnrich);
			}
		}
		return entityFieldValue;
	}
	
	private String getResponseFromJson(JSONObject responseJson, String bussListField) throws JSONException {
		if (responseJson.has(bussListField)) {
			return responseJson.getString(bussListField);
		}
		return null;
	}

	private void addEnrichElement(Document envelopeDoc, String bussField, String ruleName, String msgType) {
		// root EnrichDocument <Document/> <Enrich> <entity/> <entity/> </Enrich>
		// /EnrichDocument ... /root

		Node enrichDocumentNode = envelopeDoc.getFirstChild();
		NodeList enrichElementsNodeList = enrichDocumentNode.getChildNodes();
		for (int i = 0; i < enrichElementsNodeList.getLength(); i++) {
			Node element = enrichElementsNodeList.item(i);

			if (element.getNodeName().equals("Enrich")) {
				// add <entity/> element
				element.appendChild(createEnrichEntityElement(envelopeDoc, bussField, ruleName, msgType));
				break;
			} else if (i == enrichElementsNodeList.getLength() - 1) {
				// no enrich element found and at the end of node list -> create Enrich element
				// from scratch
				Element enrichElement = envelopeDoc.createElement("Enrich");
				enrichElement.appendChild(createEnrichEntityElement(envelopeDoc, bussField, ruleName, msgType));
				enrichDocumentNode.appendChild(enrichElement);
				break;
			}
		}
	}

	private Element createEnrichEntityElement(Document envelopeDoc, String bussField, String ruleName, String msgType) {
		Element entityElement = envelopeDoc.createElement("entity");
		entityElement.setAttribute("name", bussField);
		entityElement.setAttribute("id", ruleName);
		entityElement.setAttribute("msg_type", msgType);
		return entityElement;
	}

	private void addInvalidationEnvelope(Document envelopeDoc, ValidationsRulesEntity validationsRulesEntity) {
		NodeList nodeList = envelopeDoc.getFirstChild().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			String nodeName = element.getTagName();
			if (nodeName.equals("wsrm:SequenceFault")) {
				element.appendChild(createWsrmNackElement(envelopeDoc, validationsRulesEntity));
			} else if (i == nodeList.getLength() - 1) {
				// if nodename not found and is at last elem in nodelist - create WSRM anvelope
				// otherwise just append child
				Element wsuIdentifier = envelopeDoc.createElement("wsu:Identifier");
				Element wsrmFaultCode = envelopeDoc.createElement("wsrm:FaultCode");
				Element wsrmSequenceFault = envelopeDoc.createElement("wsrm:SequenceFault");

				wsrmFaultCode.setTextContent("WS Invalid Data");
				wsuIdentifier.setTextContent(validationsRulesEntity.getId().toString());

				Element wsrmNack = createWsrmNackElement(envelopeDoc, validationsRulesEntity);

				wsrmSequenceFault.appendChild(wsuIdentifier);
				wsrmSequenceFault.appendChild(wsrmFaultCode);
				wsrmSequenceFault.appendChild(wsrmNack);

				envelopeDoc.getFirstChild().appendChild(wsrmSequenceFault);
				// if not 'break' - envelopeDoc will update itself after .appendchild and this
				// loop will be entered again
				break;
			}
		}
	}

	private Object getQueriedHttpResponse(String resourcePath, MultiValueMap<String, String> queryParamMap,
			Class<?> cls) {
		RestTemplate restTemplate = new RestTemplate();

		org.springframework.http.HttpHeaders headersSpring = new org.springframework.http.HttpHeaders();
		headers.getRequestHeaders().forEach((key, value) -> headersSpring.put(key.toLowerCase(), value));
		HttpEntity<Object> httpEntity = new HttpEntity<>(headersSpring);

		List<PathSegment> pathSegments = uriInfo.getPathSegments();
		String urlString = uriInfo.getBaseUri().toString() + pathSegments.get(0).toString() + resourcePath;
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlString);
		builder.queryParams((MultiValueMap<String, String>) queryParamMap);
		try {
			ResponseEntity<?> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity,
					cls);
			Object result = responseEntity.getBody();
			return result;
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		return null;
	}

}
