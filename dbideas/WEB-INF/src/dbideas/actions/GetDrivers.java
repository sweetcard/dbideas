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

package dbideas.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.JSONAction;
import dbideas.dao.DriversDAO;
import dbideas.entities.Driver;




public class GetDrivers implements JSONAction {

	

	 
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		JSONArray arr=new JSONArray();
		for(Driver drv: DriversDAO.getDrivers(em,"")){//sessions.getUser())){
			JSONObject obj=new JSONObject();
			obj.put("id",drv.getId());
			obj.put("drvname", drv.getDriverName());
			obj.put("drvclassname",drv.getDriverClassName());
			obj.put("exampleurl",drv.getExampleUrl());
			obj.put("valid",drv.isValid());
			obj.put("icon",drv.getIconurl());
			arr.put(obj);
		}
		
		
	
		
		JSONObject ret = new JSONObject();
		ret.put("drivers", arr);
		return ret;
	}

}
