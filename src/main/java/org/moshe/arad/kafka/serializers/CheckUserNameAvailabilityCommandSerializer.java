package org.moshe.arad.kafka.serializers;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.moshe.arad.kafka.commands.CheckUserNameAvailabilityCommand;

public class CheckUserNameAvailabilityCommandSerializer implements Serializer<CheckUserNameAvailabilityCommand>{

	private static final String encoding = "UTF8";
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] serialize(String arg0, CheckUserNameAvailabilityCommand command) {
		
		byte[] serializedUserName;
		int sizeOfUserName;		
		
		 try {
			 if (command == null)
				 return null;
            
			 serializedUserName = command.getUserName().getBytes(encoding);
			 sizeOfUserName = command.getUserName().length();
			 
			 ByteBuffer buf = ByteBuffer.allocate(sizeOfUserName+4);
			 buf.putInt(sizeOfUserName);
			 buf.put(serializedUserName);                            
             
	         return buf.array();
	        } catch (Exception e) {
	            throw new SerializationException("Error when serializing CheckUserNameAvailabilityCommand to byte[]");
	        }
	}

}
