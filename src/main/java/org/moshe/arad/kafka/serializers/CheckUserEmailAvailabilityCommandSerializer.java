package org.moshe.arad.kafka.serializers;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.moshe.arad.kafka.commands.CheckUserEmailAvailabilityCommand;
import org.moshe.arad.kafka.commands.CheckUserNameAvailabilityCommand;
import org.moshe.arad.kafka.commands.CreateNewUserCommand;

public class CheckUserEmailAvailabilityCommandSerializer  implements Serializer<CheckUserEmailAvailabilityCommand>{

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
	public byte[] serialize(String arg0, CheckUserEmailAvailabilityCommand command) {
		byte[] serializedEmail;
		int sizeOfUserEmail;		
		
		long highUuid;		
		long lowUuid;
		
		 try {
			 if (command == null)
				 return null;
            
			 serializedEmail = command.getEmail().getBytes(encoding);
			 sizeOfUserEmail = command.getEmail().length();
			 
			 highUuid = command.getUuid().getMostSignificantBits();
			 lowUuid = command.getUuid().getLeastSignificantBits();
			 
			 ByteBuffer buf = ByteBuffer.allocate(sizeOfUserEmail+4+8+8);
			 buf.putInt(sizeOfUserEmail);
			 buf.put(serializedEmail);                            
             
			 buf.putLong(highUuid);
			 buf.putLong(lowUuid);
			 
	         return buf.array();
	        } catch (Exception e) {
	            throw new SerializationException("Error when serializing CheckUserEmailAvailabilityCommand to byte[]");
	        }
	}

}
