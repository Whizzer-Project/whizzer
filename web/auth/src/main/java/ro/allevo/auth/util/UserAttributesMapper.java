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
package ro.allevo.auth.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

public class UserAttributesMapper implements AttributesMapper<Map<String, Object>> {
	   @Override
       public Map<String, Object> mapFromAttributes(Attributes attributes) throws javax.naming.NamingException {
	       Map<String, Object> map = new HashMap<String, Object>();
          
           String email = "";
	       String sAMAccountName = "";
	       String sn = "";
	       String givenName = "";
	       List<String> memberOf = new ArrayList<String>();
	       
	       if( attributes.get("email") != null )
	    	   email = (String) attributes.get("email").get(); 
	       if( attributes.get("sAMAccountName") != null )
	    	   sAMAccountName = (String) attributes.get("sAMAccountName").get(); 
	       if( attributes.get("sn") != null )
	    	   sn = (String) attributes.get("sn").get(); 
	       if( attributes.get("givenName") != null )
	    	   givenName = (String) attributes.get("givenName").get(); 
	       if( attributes.get("memberOf") != null ){
	    	   for (NamingEnumeration<?> vals = attributes.get("memberOf").getAll(); vals.hasMoreElements();) {
	    		   memberOf.add((String)vals.nextElement());
	           }
	       }
	       map.put("sAMAccountName", sAMAccountName);
	       map.put("email", email);
	       map.put("sn", sn);
	       map.put("givenName", givenName);
	       map.put("memberOf", memberOf);
           
           return map;
       }
}
