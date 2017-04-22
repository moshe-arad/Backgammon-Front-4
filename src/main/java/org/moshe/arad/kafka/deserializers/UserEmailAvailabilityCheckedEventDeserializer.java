package org.moshe.arad.kafka.deserializers;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.moshe.arad.kafka.events.UserEmailAvailabilityCheckedEvent;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;

public class UserEmailAvailabilityCheckedEventDeserializer implements Deserializer<UserEmailAvailabilityCheckedEvent>{

	private String encoding = "UTF8";
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configure(Map<String, ?> paramMap, boolean paramBoolean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserEmailAvailabilityCheckedEvent deserialize(String paramString, byte[] data) {
		try {
	        if (data == null){
	            System.out.println("Null recieved at deserialize");
	            return null;
	        }
	        
	        ByteBuffer buf = ByteBuffer.wrap(data);         
	     
	        boolean isAvailable = buf.get()==1 ? true:false;
	        UUID uuid = new UUID(buf.getLong(), buf.getLong());
	        
	        UserEmailAvailabilityCheckedEvent userEmailAvailabilityCheckedEvent = new UserEmailAvailabilityCheckedEvent();
	        userEmailAvailabilityCheckedEvent.setAvailable(isAvailable);
	        userEmailAvailabilityCheckedEvent.setUuid(uuid);	    	      	     
	        return userEmailAvailabilityCheckedEvent;
	        	            		                      
	    } catch (Exception e) {
	        throw new SerializationException("Error when deserializing byte[] to userEmailAvailabilityCheckedEvent");
	    }
	}
}
