package org.moshe.arad.kafka.deserializers;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.moshe.arad.kafka.commands.CheckUserNameAvailabilityCommand;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;

public class UserNameAvailabilityCheckedEventDeserializer implements Deserializer<UserNameAvailabilityCheckedEvent>{

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
	public UserNameAvailabilityCheckedEvent deserialize(String paramString, byte[] data) {
		try {
	        if (data == null){
	            System.out.println("Null recieved at deserialize");
	            return null;
	        }
	        
	        ByteBuffer buf = ByteBuffer.wrap(data);         
	     
	        boolean isAvailable = buf.get()==1 ? true:false;
	        UUID uuid = new UUID(buf.getLong(), buf.getLong());
	        
	        UserNameAvailabilityCheckedEvent userNameAvailabilityCheckedEvent = new UserNameAvailabilityCheckedEvent();
	        userNameAvailabilityCheckedEvent.setAvailable(isAvailable);
	        userNameAvailabilityCheckedEvent.setUuid(uuid);	    	      	     
	        return userNameAvailabilityCheckedEvent;
	        	            		                      
	    } catch (Exception e) {
	        throw new SerializationException("Error when deserializing byte[] to CreateNewUserCommand");
	    }
	}
	
	private String deserializeString(ByteBuffer buf) throws UnsupportedEncodingException {
		int sizeOfStringDeserialize = buf.getInt();
		byte[] nameBytes = new byte[sizeOfStringDeserialize];
		buf.get(nameBytes);
		String deserializedString = new String(nameBytes, encoding);
		return deserializedString;
	}

}
