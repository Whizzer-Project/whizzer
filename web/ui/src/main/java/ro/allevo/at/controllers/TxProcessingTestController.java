package ro.allevo.at.controllers;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.ws.rs.Produces;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.at.model.ExpectedOutputDataset;
import ro.allevo.at.model.InputDataset;
import ro.allevo.at.model.TxProcessingTest;
import ro.allevo.at.model.TxProcessingTestLog;
import ro.allevo.at.service.ExpectedOutputDatasetService;
import ro.allevo.at.service.InputDatasetService;
import ro.allevo.at.service.TxProcessingTestLogService;
import ro.allevo.at.service.TxProcessingTestService;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.service.MessageService;
import ro.allevo.fintpui.service.ParamService;
import ro.allevo.fintpui.utils.Roles;

@Controller
@RequestMapping("tests")
public class TxProcessingTestController {

	@Autowired
	Config config;

	@Autowired
	private TxProcessingTestService txProcessingTestService;

	@Autowired
	private InputDatasetService inputDatasetService;

	@Autowired
	private TxProcessingTestLogService txProcessingTestLogService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private ExpectedOutputDatasetService expectedOutputDatasetService;

	@Autowired
	private ParamService paramService;

	private TxProcessingTest[] txProcessingTests;
	
	private String code = "ATR";

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String displayTests(OAuth2Authentication auth, ModelMap model) {

		if (!Roles.hasRoles(auth, Roles.AUTOMATED_TESTING_VIEW))
			throw new NotAuthorizedException();

		txProcessingTests = txProcessingTestService.getAllProcessingTests();
		model.addAttribute("txProcessingTests", txProcessingTests);

		Map<String, Integer> uniqueNameMap = new HashMap<>();
		for (TxProcessingTest txProcessingTest : txProcessingTests) {
			uniqueNameMap.put(txProcessingTest.getName(), txProcessingTest.getId().intValue());
		}

		model.addAttribute("uniqueNameMap", uniqueNameMap);

		return "at/processingTests";
	}

	@GetMapping(path = "{id}/transaction-type")
	@ResponseBody
	public Map<Integer, String> getTxProcessingTestType(@PathVariable int id) {

		// search in txProcessingTests by id, for each same name get txtype and display
		// it
		String txProcessingTestName = null;
		for (TxProcessingTest txProcessingTest : txProcessingTests) {
			if (txProcessingTest.getId() == id) {
				txProcessingTestName = txProcessingTest.getName();
			}
		}

		// when find "txProcessingTestName" -- can find out all Txtypes
		Map<Integer, String> txTypes = new HashMap<Integer, String>();
		for (TxProcessingTest txProcessingTest : txProcessingTests) {
			if (txProcessingTest.getName().equals(txProcessingTestName)) {
				txTypes.put(txProcessingTest.getInterfaceconfig().getId(), txProcessingTest.getTxtype());
			}
		}

//		return map (key = id (which is interfaceId), value = txtype)
		return txTypes;
	}

//	url : './tests/' + interfaceConfigId + "/input-dataset-type/" + processingTestId,
	@GetMapping(path = "{commonid}/input-dataset-type/{processingtestid}")
	@ResponseBody
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> getinputDataSetType(@PathVariable("commonid") int commonId,
			@PathVariable("processingtestid") int processingtestid) {

		Map<String, String> inputDataSetTypes = new HashMap<String, String>();

		// search all datasettype from table inputdatasets by interfaceId and display
		// them in select
		InputDataset[] inputDataSets = inputDatasetService.getAllInputDatasetsById((long) commonId,
				(long) processingtestid);
		for (InputDataset inputDataset : inputDataSets) {
			// pass datasettype and id with separator : as value
			inputDataSetTypes.put(inputDataset.getDataset(),
					inputDataset.getDatasettype() + ':' + inputDataset.getId());
		}

		// return map with (key = name of file = dataset) and value = datasettype
		return inputDataSetTypes;
	}

	@GetMapping(path = "download-file/{filename}")
	@ResponseBody
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String fileName) throws IOException {

		Path path = Paths.get(paramService.getParam(code).getValue()
				+ System.getProperty("file.separator") + fileName);

		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(resource);
	}

	@GetMapping(path = "{inputType}/{inputDataSetFileName}/{txProcessingTestId}/{inputDatasetId}")
	@ResponseBody
	public String copyFileToAnotherLocation(@PathVariable("inputType") String inputType,
			@PathVariable("inputDataSetFileName") String fileName,
			@PathVariable("txProcessingTestId") long txProcessingTestId,
			@PathVariable("inputDatasetId") long inputDatasetId) throws Exception {

		StringBuilder strBuilder = new StringBuilder();
		TxProcessingTest txProcessingTest = txProcessingTestService.getProcessingTest(txProcessingTestId);
		String outputType = txProcessingTest.getInterfaceconfigOutput() != null
				? txProcessingTest.getInterfaceconfigOutput().getInputtype()
				: null;

		String outputFileLocation = txProcessingTest.getInterfaceconfigOutput() != null
				? txProcessingTest.getInterfaceconfigOutput().getLocation()
				: null; // /fintpc/PaymentsExternal/ISO/Destination generated file after copy in
						// inputFileLocation
//		String outputFileLocation = txProcessingTest.getInterfaceconfigOutput()!=null? "C:\\AT Repository Destination" : null; for windows testing

		if (inputType.equalsIgnoreCase("file")) {

			String atFileRepo = paramService.getParam(code).getValue(); // /fintpc/AT_Repository
//			String atFileRepo = "C:\\AT Repository";  for windows testing
			String inputFileLocation = txProcessingTest.getInterfaceconfig().getLocation(); // /fintpc/StatementExternal/SourcePath
																							// copy from atFileRepo to
																							// here
//			String inputFileLocation = "C:\\AT Repository Source";  for windows testing			

			try {
				// need to delete files in directory before copy to input location
				if ("file".equals(outputType) && outputFileLocation != null) {
					deleteFilesInLocation(outputFileLocation);
				}

				// folder for input location is always empty - no need to delete any files there
				Path originalPath = Paths.get(atFileRepo + System.getProperty("file.separator") + fileName);
				Path copiedFilePath = Paths.get(inputFileLocation + System.getProperty("file.separator") + fileName);

				Files.copy(originalPath, copiedFilePath, StandardCopyOption.REPLACE_EXISTING);

				strBuilder.append("File copied successfully");
				strBuilder.append(System.lineSeparator());

				TimeUnit.SECONDS.sleep(15);
			} catch (IOException e) {
				strBuilder.append("Error copy file");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if ("file".equals(outputType)) {

				if (outputFileLocation == null) {
					return "Verify result";
				}
				File generatedFileRE = getGeneratedFile(outputFileLocation);
				;

				Map<String, String> nameOfExpectedOutputFile = getFileNamesFromExpectedOutputDataset(txProcessingTestId,
						inputDatasetId);
				boolean areFilesEqual = checkExpectedWithGeneratedPayloads(nameOfExpectedOutputFile, outputFileLocation,
						generatedFileRE.getName(), atFileRepo);

				strBuilder.append(getMsgAboutFileEquality(areFilesEqual));

			} else if ("database".equals(outputType)) {
				strBuilder.append(getMsgAboutFileEquality(
						checkPayloadWithExpectedOutputFile(txProcessingTestId, inputDatasetId, atFileRepo)));
			} else {
				strBuilder.append("Verify result");
				strBuilder.append(System.lineSeparator());
			}

			insertTestLog(txProcessingTestId, inputDatasetId);

			strBuilder.append(System.lineSeparator());
			strBuilder.append(trimStoredProcedureResult(callStoredProcedure(txProcessingTestId)));

		} else {
			// need to delete files in directory before copy to input location
			if ("file".equals(outputType) && outputFileLocation != null) {
				deleteFilesInLocation(outputFileLocation);
			}
			// pass this to ajax
			strBuilder.append("DB");
		}
		return strBuilder.toString();
	}

	@GetMapping(path = "{txProcessingTestId}/{inputDatasetId}")
	@ResponseBody
	public String insertInLogAfterPressedButton(@PathVariable("txProcessingTestId") long txProcessingTestId,
			@PathVariable("inputDatasetId") long inputDatasetId) {

		TxProcessingTest txProcessingTest = txProcessingTestService.getProcessingTest(txProcessingTestId);
		String outputType = txProcessingTest.getInterfaceconfigOutput() != null
				? txProcessingTest.getInterfaceconfigOutput().getInputtype()
				: null;
		String atFileRepo = paramService.getParam(code).getValue(); // /fintpc/AT_Repository
//		String atFileRepo = "C:\\AT Repository";   for windows testing

		insertTestLog(txProcessingTestId, inputDatasetId);
		String storedProcedureResult = callStoredProcedure(txProcessingTestId);

		if ("file".equals(outputType)) {

			String outputFileLocation = txProcessingTest.getInterfaceconfigOutput() != null
					? txProcessingTest.getInterfaceconfigOutput().getLocation()
					: null; // /fintpc/PaymentsExternal/ISO/Destination generated file after copy in
							// inputFileLocation
//			String outputFileLocation = txProcessingTest.getInterfaceconfigOutput()!=null? "C:\\AT Repository Destination" : null;   for windows testing

			if (outputFileLocation == null) {
				return "Verify result";
			}

			try {
				TimeUnit.SECONDS.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			File generatedFileRE = getGeneratedFile(outputFileLocation);

			if (generatedFileRE == null) {
				return "" + false;
			}

			Map<String, String> nameOfExpectedOutputFile = getFileNamesFromExpectedOutputDataset(txProcessingTestId,
					inputDatasetId);
			boolean areFilesEqual = checkExpectedWithGeneratedPayloads(nameOfExpectedOutputFile, outputFileLocation,
					generatedFileRE.getName(), atFileRepo);

			return getMsgAboutFileEquality(areFilesEqual) + System.lineSeparator()
					+ trimStoredProcedureResult(storedProcedureResult);
		} else if ("database".equals(outputType)) {
			return "" + checkPayloadWithExpectedOutputFile(txProcessingTestId, inputDatasetId, atFileRepo);
		}
		// the return is used in ajax succes
		return "" + false;
	}

	private String getMsgAboutFileEquality(boolean areFilesEqual) {
		if (areFilesEqual) {
			return "Files are equal";
		} else {
			return "Files are not equal";
		}
	}

	private boolean checkPayloadWithExpectedOutputFile(long txProcessingTestId, long inputDatasetId,
			String fileLocation) {

		Map<String, String> expectedFileNames = getFileNamesFromExpectedOutputDataset(txProcessingTestId,
				inputDatasetId); // key = file name , value = file xpath

		List<String> messagesPayloads = getMessagesPayloads();

		if (messagesPayloads.isEmpty() || expectedFileNames.isEmpty()) {
			return false;
		}

		Map<String, String> fileNameMsgPayload = matchExpectedFilesOutputWithMsgPayloadS(expectedFileNames,
				messagesPayloads, fileLocation);

		boolean arePayloadsEqual = false;
		Iterator<Entry<String, String>> iterator = fileNameMsgPayload.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();

			if (entry.getValue() == null) {
				return false;
			}

			Document docFromFile = getDocumentFromFile(fileLocation, entry.getKey());
			Document docFromPayload = convertStringToXMLDocument(entry.getValue());
			docFromPayload = modifyLinesInDocument(docFromPayload, getTagsWithValuesFromXMLDocument(docFromFile));
			docFromPayload.getDocumentElement().normalize();

			String filePayloadString = getDocumentPayloadAsString(docFromFile.getElementsByTagName("*"));
			String msgPayloadString = getDocumentPayloadAsString(docFromPayload.getElementsByTagName("*"));
			arePayloadsEqual = filePayloadString.equals(msgPayloadString);
			if (!arePayloadsEqual) {
				return false;
			}
		}
		return arePayloadsEqual;
	}

	private void insertTestLog(long txProcessingTestId, long inputDatasetId) {

		InputDataset inputDataset = inputDatasetService.getInputDataset(inputDatasetId, txProcessingTestId);

		TxProcessingTest txProcessingTest = txProcessingTestService.getProcessingTest(txProcessingTestId);

		TxProcessingTestLog txProcessingTestLog = new TxProcessingTestLog();
		txProcessingTestLog.setInputdataset(inputDataset);
		txProcessingTestLog.setInsertdate(getCurrentTime());
		txProcessingTestLog.setStatus(0);

		txProcessingTestLog.setTxprocessingtest(txProcessingTest);
		txProcessingTestLog.setTxtype(txProcessingTest.getTxtype());
		txProcessingTestLogService.insertTxProcessingTestLog(txProcessingTestLog);
	}

	private Timestamp getCurrentTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return java.sql.Timestamp
				.from(localDateTime.toInstant(ZoneId.systemDefault().getRules().getOffset(localDateTime)));
	}

	private String callStoredProcedure(long txProcessingTestId) {

		List<Object> procedureResult = txProcessingTestService.callProcedure(txProcessingTestId)
				.getCallProcedureResult();
		StringBuilder strBuilder = new StringBuilder();
		if (procedureResult != null && procedureResult.size() > 0) {
			for (Object obj : procedureResult) {

				@SuppressWarnings("unchecked")
				ArrayList<Object> arObj = (ArrayList<Object>) obj;
				String v_outquelocation = (String) arObj.get(0);
				strBuilder.append(v_outquelocation);
			}
		}
		return strBuilder.toString();
	}

	private Map<String, String> getFileNamesFromExpectedOutputDataset(long txProcessingTestId, long inputDatasetId) {

		ExpectedOutputDataset[] expectedOutputDatasetArr = expectedOutputDatasetService
				.getAllInputDatasets(txProcessingTestId);
		Map<String, String> expectedOutputFileNames = new HashMap<String, String>();

		for (ExpectedOutputDataset expectedOutputDataset : expectedOutputDatasetArr) {
			InputDataset expectedInputDataset = expectedOutputDataset.getInputdataset();
			TxProcessingTest expectedTxProcessingTest = expectedOutputDataset.getProcessingTest();

			if (expectedInputDataset != null && expectedInputDataset.getId() == inputDatasetId
					&& expectedTxProcessingTest != null && expectedTxProcessingTest.getId() == txProcessingTestId) {

				expectedOutputFileNames.put(expectedOutputDataset.getDataset(),
						expectedOutputDataset.getUniquekeypath());
			}
		}
		return expectedOutputFileNames;
	}

	private List<String> getMessagesPayloads() {
		ObjectNode[] objNodeArr = messageService.getAllMessages();
		// String result = null;
		List<String> messagesPayloads = new ArrayList<String>();

		for (ObjectNode obj : objNodeArr) {
			String msgId = obj.get("id").asText();
			try {
				String result = messageService.getEntryQueuePayload(msgId);
				if (result != null) {
					messagesPayloads.add(result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return messagesPayloads;
	}

	private Document convertStringToXMLDocument(String xmlString) {
		// Parser that produces DOM object trees from XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// API to obtain DOM Document instance
		// DocumentBuilder builder = null;
		try {
			// Create DocumentBuilder with default configuration
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Parse the content to Document object
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Document modifyLinesInDocument(Document document, Map<String, String> generalTagsMap) {

		Document result = document;
		NodeList nodeList = result.getElementsByTagName("*");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);

			generalTagsMap.forEach((key, value) -> {
				if (key.equals(element.getNodeName())) {
					element.getFirstChild().setNodeValue(value);
				}
			});
		}
		return result;
	}

	private String trimStoredProcedureResult(String storedProcedureResult) {

		String[] splitWords = storedProcedureResult.split(" ");

		if (splitWords.length > 0) {
			String firstWord = splitWords[0];

			int secondAppearanceOfFirstWordIndex = storedProcedureResult.indexOf(firstWord,
					storedProcedureResult.indexOf(firstWord) + 1);

			if (secondAppearanceOfFirstWordIndex > 1) {
				return storedProcedureResult.substring(0, secondAppearanceOfFirstWordIndex);
			}
		}
		return storedProcedureResult;
	}

	private Document getDocumentFromFile(String fileLocation, String fileName) {

		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document doc = null;

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			doc = db.parse(fileLocation + System.getProperty("file.separator") + fileName);
			doc.getDocumentElement().normalize();

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	private String getXpathValue(Document doc, String xpathValue) {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		String nodeValue = null;
		try {
			Element element = (Element) xpath.evaluate("//" + xpathValue, doc, XPathConstants.NODE);
			if (element != null) {
				nodeValue = element.getNodeValue();
				if (element.getFirstChild() != null) {
					nodeValue = element.getFirstChild().getNodeValue();
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return nodeValue;
	}

	private Map<String, String> getTagsWithValuesFromXMLDocument(Document document) {

		Map<String, String> tagsWithGeneralValues = new HashMap<String, String>();
		// this general tag values ar found in the output file which is verified with
		// msg payload which doesn't have general values
		List<String> generalValues = new ArrayList<String>(
				Arrays.asList("batch01", "YYYYMMDDHHmmSSff", "YYYY-MM-DDTHH:MM:SS", "YYYY-MM-DD HH:MM", "YYYY-MM-DD"));

		Document doc = document;
		doc.getDocumentElement().normalize();

		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {

			Element element = (Element) nodeList.item(i);
			String nodeValue = element.getFirstChild().getNodeValue();

			for (String value : generalValues) {
				if (value.equals(nodeValue)) {
					tagsWithGeneralValues.put(element.getNodeName(), value);
				}
			}
		}

		// key = tag name, value = tag value
		return tagsWithGeneralValues;
	}

	private Map<String, String> matchExpectedFilesOutputWithMsgPayloadS(Map<String, String> expectedFileNames,
			List<String> messagesPayloads, String fileLocation) {

		Map<String, String> fileXpathValue = new HashMap<String, String>(); // key = file name , value = xpathValue
		Map<String, String> xpathValueAsKeyTagAsValue = new HashMap<String, String>(); // key = xpathValue , value =
																						// xpath

		expectedFileNames.forEach((fileName, xpath) -> {
			Document documentFromFile = getDocumentFromFile(fileLocation, fileName);
			String xpathValue = getXpathValue(documentFromFile, xpath);
			fileXpathValue.put(fileName, xpathValue);
			xpathValueAsKeyTagAsValue.put(xpathValue, xpath);
		});

		Map<String, String> xpathValueMsgPayload = new HashMap<String, String>(); // key = xpathValue from msgpayload ,
																					// value = msgpayload
		messagesPayloads.forEach(item -> {
			Document payloadDoc = convertStringToXMLDocument(item);
			xpathValueAsKeyTagAsValue.forEach((xpathValue, xpath) -> {
				String xpathValueFromPayloadDoc = getXpathValue(payloadDoc, xpath);
				xpathValueMsgPayload.put(xpathValueFromPayloadDoc, item);
			});

		});
		// fileXpathValue - key = file name , value = xpath value
		// xpathValueMsgPayload - key = xpath value, value = msgpayload
		// fileNameMsgPayload - map with matched file name(from expectedoutput) with msg
		// payload
		Map<String, String> fileNameMsgPayload = new HashMap<String, String>();
		fileXpathValue.forEach((fileName, xpathValue) -> {
			fileNameMsgPayload.put(fileName, xpathValueMsgPayload.get(xpathValue));
		});
		return fileNameMsgPayload;
	}

	private String getDocumentPayloadAsString(NodeList nodeList) {
		StringBuilder strBuilder = new StringBuilder();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			String nodeValue = element.getFirstChild().getNodeValue();

			strBuilder.append("<" + element.getNodeName().trim() + ">");

			if (element.hasAttributes() && !element.getNodeName().equals("Document")) {

				int numAttrs = element.getAttributes().getLength();
				for (int j = 0; j < numAttrs; j++) {
					Attr attr = (Attr) element.getAttributes().item(j);
					strBuilder.append(attr.getNodeName().trim());
					strBuilder.append(attr.getNodeValue().trim());
				}
			}
			strBuilder.append(nodeValue.trim());
		}
		return strBuilder.toString();
	}

	private Map<String, Object> getTagsValues(Document doc, String repeatingTag) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		String docPayloadWithoutRepeatingTag = null;
		List<String> repeatingPayloadsValues = new ArrayList<String>();

		try {
			docPayloadWithoutRepeatingTag = getDocumentPayloadAsString((NodeList) xpath
					.evaluate("//*[not(ancestor-or-self::" + repeatingTag + ")]", doc, XPathConstants.NODESET));

			String docPayloadRepeatingTag = getDocumentPayloadAsString((NodeList) xpath
					.evaluate("//*[ancestor-or-self::" + repeatingTag + "]", doc, XPathConstants.NODESET));
			String[] splitByRepeatTag = docPayloadRepeatingTag.split("<" + repeatingTag + ">");
			for (String result : splitByRepeatTag) {
				if (result.length() > 0) {
					repeatingPayloadsValues.add(result);
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		resultMap.put("nonRepeatingTag", docPayloadWithoutRepeatingTag);
		resultMap.put("repeatingTagValues", repeatingPayloadsValues);
		return resultMap;
	}

	private boolean checkExpectedWithGeneratedPayloads(Map<String, String> nameOfExpectedOutputFile,
			String outputFileLocation, String fileName, String atFileRepo) {

		Map<String, Object> expectedFilePayload = new HashMap<String, Object>();
		Map<String, Object> generatedFilePayload = new HashMap<String, Object>();

		for (Map.Entry<String, String> entry : nameOfExpectedOutputFile.entrySet()) {
			String repeatingTag = entry.getValue().split("/")[0];

			Document docFromExpectedFile = getDocumentFromFile(atFileRepo, entry.getKey());
			expectedFilePayload = getTagsValues(docFromExpectedFile, repeatingTag);

			Document docFromGeneratedFile = getDocumentFromFile(outputFileLocation, fileName);
			docFromGeneratedFile = modifyLinesInDocument(docFromGeneratedFile,
					getTagsWithValuesFromXMLDocument(docFromExpectedFile));
			generatedFilePayload = getTagsValues(docFromGeneratedFile, repeatingTag);
		}

		String nonRepeatingTagsValuesFromExpectedFile = (String) expectedFilePayload.get("nonRepeatingTag");
		String nonRepeatingTagsValuesFromGeneratedFile = (String) generatedFilePayload.get("nonRepeatingTag");
		@SuppressWarnings("unchecked")
		List<String> repeatingTagsValuesFromExpectedFileList = (List<String>) expectedFilePayload
				.get("repeatingTagValues");
		@SuppressWarnings("unchecked")
		List<String> repeatingTagsValuesFromGeneratedFileList = (List<String>) generatedFilePayload
				.get("repeatingTagValues");

		// boolean areFilesEqual = false;
		boolean areFilesEqual = nonRepeatingTagsValuesFromExpectedFile.equals(nonRepeatingTagsValuesFromGeneratedFile);
		if (areFilesEqual) {
			for (String payloadExpected : repeatingTagsValuesFromExpectedFileList) {
				if (areFilesEqual)
					for (String payloadGenerated : repeatingTagsValuesFromGeneratedFileList) {
						if (payloadExpected.equals(payloadGenerated)) {
							areFilesEqual = true;
							break;
						}
						areFilesEqual = false;
					}
			}
		}
		return areFilesEqual;
	}

	private File getGeneratedFile(String outputFileLocation) {
		File generatedFileRE = null;
		File[] listFiles = new java.io.File(outputFileLocation).listFiles();
		if (null != listFiles) {
			for (File file : listFiles) {
				if (!file.isDirectory()) {
					generatedFileRE = file;
					// here should be only 1 file
					break;
				}
			}
		}
		return generatedFileRE;
	}

	private void deleteFilesInLocation(String outputFileLocation) throws Exception {
		File[] listFiles = new java.io.File(outputFileLocation).listFiles();
		if (null != listFiles) {
			for (File file : listFiles) {
				if (file != null && !file.isDirectory()) {
					file.delete();
				}
			}
		}
	}
}
