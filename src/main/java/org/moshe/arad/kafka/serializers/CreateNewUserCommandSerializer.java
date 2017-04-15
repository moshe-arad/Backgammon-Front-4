package org.moshe.arad.kafka.serializers;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.moshe.arad.kafka.commands.CreateNewUserCommand;

public class CreateNewUserCommandSerializer implements Serializer<CreateNewUserCommand>{

	private static final String encoding = "UTF8";
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configure(Map<String, ?> paramMap, boolean paramBoolean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] serialize(String paramString, CreateNewUserCommand command) {

		byte[] serializedUserName;
		int sizeOfUserName;		
		byte[] serializedPassword;
		int sizeOfPassword;
		byte[] serializedFirstName;
		int sizeOfFirstName;
		byte[] serializedLastName;
		int sizeOfLastName;
		byte[] serializedEmail;
		int sizeOfEmail;
		
		
		 try {
			 if (command == null)
				 return null;
            
			 serializedUserName = command.getBackgammonUser().getUsername().getBytes(encoding);
			 sizeOfUserName = command.getBackgammonUser().getUsername().length();
			 
			 serializedPassword = command.getBackgammonUser().getPassword().getBytes(encoding);
			 sizeOfPassword = command.getBackgammonUser().getPassword().length();
			 
			 serializedFirstName = command.getBackgammonUser().getFirstName().getBytes(encoding);
			 sizeOfFirstName = command.getBackgammonUser().getFirstName().length();
			 
			 serializedLastName = command.getBackgammonUser().getLastName().getBytes(encoding);
			 sizeOfLastName = command.getBackgammonUser().getLastName().length();
			 
			 serializedEmail = command.getBackgammonUser().getEmail().getBytes(encoding);
			 sizeOfEmail = command.getBackgammonUser().getEmail().length();	
			 
			 ByteBuffer buf = ByteBuffer.allocate(sizeOfUserName+4+sizeOfPassword+4+sizeOfFirstName+4+sizeOfLastName+4+sizeOfEmail+4);
			 buf.putInt(sizeOfUserName);
			 buf.put(serializedUserName);        
            
			 buf.putInt(sizeOfPassword);
             buf.put(serializedPassword);
             
             buf.putInt(sizeOfFirstName);
             buf.put(serializedFirstName);        
             
             buf.putInt(sizeOfLastName);
             buf.put(serializedLastName);
             
             buf.putInt(sizeOfEmail);
             buf.put(serializedEmail);
             
	         return buf.array();

	        } catch (Exception e) {
	            throw new SerializationException("Error when serializing CreateNewUserCommand to byte[]");
	        }
	}

}
