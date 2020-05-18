package com.iThingsFoundation.IThingsFramework.common;


import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class SessionService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public String SessionData(String sessionid)
	{
		long lastaccesstime=0;
		int interval=0;
		long expiry=0;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String sql = "SELECT  * from SPRING_SESSION  where SESSION_ID=?"; 
		String statement=null;
		System.out.println(sql);
		try {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,sessionid);
		
		if(rows.size()>0)
		{
			for (Map<String, Object> row : rows) 
	        {
	
			lastaccesstime=(long)row.get("LAST_ACCESS_TIME");
			interval=(int)row.get("MAX_INACTIVE_INTERVAL");	
			expiry=(long)row.get("EXPIRY_TIME");	
				  
	        }
			
			
			if((timestamp.getTime()-lastaccesstime)>(interval*1000))
			{
				
				statement="Session Expired";
			}
			else
			{
				long exp=timestamp.getTime()+(interval*1000);
				String sqlupdate="Update SPRING_SESSION set LAST_ACCESS_TIME=?,EXPIRY_TIME=? where SESSION_ID=?";
				int update=jdbcTemplate.update(sqlupdate,timestamp.getTime(),exp,sessionid);
				if(update>0)
				{
					statement= "Session Updated";
				}
				
				
			}
				
			
			
		}
		else {
			statement= "Session Expired";
		}
		}catch (Exception e) {
			// TODO: handle exception
			statement= "Session Expired";
		}
		
		
		return statement;
	}

}
