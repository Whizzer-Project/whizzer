package ro.allevo.fintpui.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.MessageTypesDao;
import ro.allevo.fintpui.model.MessageType;

@Service
public class MessageTypeService {

	@Autowired
	MessageTypesDao messageTypeDao;
	
	@Autowired
	Config config;
	
	public MessageType[] getMessageTypes() {
		return messageTypeDao.getMessageTypes();
	}
	
	public MessageType[] getMessageTypes(LinkedHashMap<String, List<String>> param) {
		return messageTypeDao.getMessageTypes(param);
	}
	
	public String[] getBusinessAreas() {
		return messageTypeDao.getBusinessAreas();
	}
	
	public MessageType[] getMessageTypes(String businessArea) {
		return messageTypeDao.getMessageTypes(businessArea);
	}
	
	public MessageType getMessageType(String messageType) {
		return messageTypeDao.getMessageType(messageType);
	}
	
	
	public Map<String, String> getAllMessageTypeByArea() {
		HashMap<String, String> messageTypes = new LinkedHashMap<>();
		for (String flow : config.getFlow().split(",")) {
			MessageType[] msgTypes = getMessageTypes(flow.trim());
			for (MessageType msgType : msgTypes) {
				messageTypes.put(msgType.getMessageType(), msgType.getFriendlyName());
			}
		}
		return (HashMap<String, String>) messageTypes;
	}
	
}
