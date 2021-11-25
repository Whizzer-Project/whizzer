/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

package ro.allevo.fintpws.util;

public class Invariants {


	 public enum messageClassEvents {
	       
	    BATCHAUTH("Message.BatchAuth"),
	    BATCH("Message.Batch"),
	    REJECT("Message.Reject"),
	    MOVE("Message.Move"),
	    CREATE("Message.Create"),
	    EDIT("Message.Edit");
	   
	    

	    private String value;
	    private messageClassEvents(String value) {
	       this.value = value;
	    }
	    public String getValue() {
	       return value;
	    }
	    
	    public String toString(){
	    	return value;
	    }
	 }
	 
//	 public enum listClassEvents {
//		 
//		 MANAGE("List.Manage");
//		 
//		 private String value;
//		 
//	     private listClassEvents(String value) {
//	       this.value = value;
//	     }
//	     
//	     public String getValue() {
//	       return value;
//	     }
//	     
//	     public String toString(){
//	    	return value;
//	     }	   
//	 }
	 
	 public enum transactionClassEvents {
		 
		 OPERATE("Transaction.Operate");
		 
		 private String value;
		 
	     private transactionClassEvents(String value) {
	       this.value = value;
	     }
	     
	     public String getValue() {
	       return value;
	     }
	     
	     public String toString(){
	    	return value;
	     }	   
	 }
		 
	 public enum configClassEvents {

		 MANAGE("Config.Manage");
		 private String value;
		    private configClassEvents(String value) {
		       this.value = value;
		    }
		    public String getValue() {
		       return value;
		    }
		    
		    public String toString(){
		    	return value;
		    }
	 }
	 
	 public enum routingClassEvents {

		 MANAGE(" Routing.Manage");
		   private String value;
		    private routingClassEvents(String value) {
		       this.value = value;
		    }
		    public String getValue() {
		       return value;
		    }
		    
		    public String toString(){
		    	return value;
		    }
	 }
	
	 
	 public enum userClassEvents {

		 MANAGE("User.Manage"),
		 AUTHENTICATION("User.Authentication");
		  private String value;
		    private userClassEvents(String value) {
		       this.value = value;
		    }
		    public String getValue() {
		       return value;
		    }
		    
		    public String toString(){
		    	return value;
		    }
	 }
	 
	 public enum  notificationsClassEvents {

		 NOTIFICATIONS("Notifications");
		 
		 private String value;
		    private notificationsClassEvents(String value) {
		       this.value = value;
		    }
		    public String getValue() {
		       return value;
		    }
		    
		    public String toString(){
		    	return value;
		    }
		   
	 }

	 public enum ATClassEvents {
		 
		 MANAGE("Autotest.Manage");
		 
		 private String value;
		 
	     private ATClassEvents(String value) {
	       this.value = value;
	     }
	     
	     public String getValue() {
	       return value;
	     }
	     
	     public String toString(){
	    	return value;
	     }	   
	 }
	 
	 public enum queueClassEvents {
		 
		 MANAGE("Queue.Manage");
		 
		 private String value;
		 
	     private queueClassEvents(String value) {
	       this.value = value;
	     }
	     
	     public String getValue() {
	       return value;
	     }
	     
	     public String toString(){
	    	return value;
	     }	   
	 }
	 
	 public enum connectClassEvents {
		 
		 MANAGE("Connect.Manage");
		 
		 private String value;
		 
	     private connectClassEvents(String value) {
	       this.value = value;
	     }
	     
	     public String getValue() {
	       return value;
	     }
	     
	     public String toString(){
	    	return value;
	     }	   
	 }
	 
	 public enum whizzerClassEvents {
		 
		 LOAD("Whizzer.LoadHistory"),
		 BSMODULE("BalanceSheet.Manage");
		 
		 private String value;
		 
	     private whizzerClassEvents(String value) {
	       this.value = value;
	     }
	     
	     public String getValue() {
	       return value;
	     }
	     
	     public String toString(){
	    	return value;
	     }	   
	 }
	   
	 
}
