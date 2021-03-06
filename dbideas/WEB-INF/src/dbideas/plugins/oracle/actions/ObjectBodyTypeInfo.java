/*
 * Copyright (c) 2009 mazzolini at gmail.com
 * This file is part of dbIdeas.
 * 
 * dbIdeas is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * dbIdeas is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with dbIdeas.  If not, see <http://www.gnu.org/licenses/>.
 * 
*/

package dbideas.plugins.oracle.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.IDManager;
import dbideas.plugin.BasePluginType;
import dbideas.plugins.oracle.ObjectBodyNode;


public class ObjectBodyTypeInfo {
	String id;
	public ObjectBodyTypeInfo(String id) {
		this.id=id;
	}

	public JSONObject execute()throws Exception {
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		String []strs={"Property","Value"};
		
		for(int i=0;i<strs.length;i++){
			meta.put(strs[i]);	
		}
		Object obj=IDManager.get().get(id);
		ObjectBodyNode tbn=(ObjectBodyNode)obj;
		final String sql=
		"SELECT  a.created,a.last_ddl_time, a.timestamp, a.status,"+
		" b.predefined, b.incomplete, b.final, b.instantiable, b.typecode "+
		 " FROM sys.all_objects a,sys.all_types b where a.owner=? and a.object_type='TYPE BODY' and object_name=? and b.owner=a.owner "+
		 " and b.type_name=a.object_name ";
		
		ResultSet rs=null;
		PreparedStatement ps=null;
  		try{
			ps=tbn.getConn().prepareStatement(sql);
			BasePluginType tot=(BasePluginType)tbn.getParent();
			String owner=tot.getParent().getName();
			ps.setString(1,owner);
			ps.setString(2,tbn.getName());
			rs=ps.executeQuery();
			
			if(rs.next()){
				JSONArray record=new JSONArray();
				record.put("Created");
				record.put(rs.getString(1));
				data.put(record);
				
				record=new JSONArray();
				record.put("Last DDL Time");
				record.put(rs.getString(2));
				data.put(record);
				
				record=new JSONArray();
				record.put("TimeStamp");
				record.put(rs.getString(3));
				data.put(record);
				
				record=new JSONArray();
				record.put("Status");
				record.put(rs.getString(4));
				data.put(record);
				
				record=new JSONArray();
				record.put("Predefined");
				record.put(rs.getString(5));
				data.put(record);
				
				record=new JSONArray();
				record.put("Incomplete");
				record.put(rs.getString(6));
				data.put(record);
				
				record=new JSONArray();
				record.put("Final");
				record.put(rs.getString(7));
				data.put(record);
				
				
				
				record=new JSONArray();
				record.put("Instantiable");
				record.put(rs.getString(8));
				data.put(record);
				
				record=new JSONArray();
				record.put("Is Collection");
				String typecode=rs.getString(9);
				record.put("COLLECTION".equals(typecode)?true:false);
				data.put(record);
				
			}
  		}catch(Exception e){
  			e.printStackTrace();
  		}finally{
  			try{if(rs!=null)
  				rs.close();
  			}catch(Exception e){
  			}
  			try{if(ps!=null)
  				ps.close();
  			}catch(Exception e){
  			}
  		}
		
  		results.put("meta",meta);
		results.put("data",data);
		return results;
	}
}
