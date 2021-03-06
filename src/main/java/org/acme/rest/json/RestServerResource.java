package org.acme.rest.json;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;


@Path("/")
public class RestServerResource {

    private List<User> Users = new ArrayList<User>();

    //All device in the system
    private List<Device> BLEDevices = new ArrayList<Device>();    
    
    //binding between the user and a list of devices
	private HashMap<User,List<Device>> userDevices= new HashMap<User,List<Device>>();
	
	private byte[] key = null;
	
	private HashMap<UUID, Entry<String, LocalDateTime>> userSessionMap = new HashMap<UUID, Entry<String, LocalDateTime>>();

	private static final int TIMEOUT = 500; // Timeout in seconds

	
    public RestServerResource() {
    	User testUser = new User("mohamad", "hlal");
    	User testUser1 = new User("1", "hlal");
    	Users.add(testUser);
    	Users.add(testUser1);
        Device frontdoor1 = new Device("0000000000","0000000000000000");
        Device backdoor1 = new Device("0000000000","0000000000000000");
        Device frontdoor2 = new Device("0000000000","0000000000000000");
        Device backdoor2 = new Device("0000000000","0000000000000000");
        Device frontdoor3 = new Device("0000000000","0000000000000000");
        Device backdoor3 = new Device("0000000000","0000000000000000");       
        Device huawei = new Device("0000000000","0000000000000000");
        
        BLEDevices.add(frontdoor1);
        BLEDevices.add(backdoor1);
        BLEDevices.add(frontdoor2);
        BLEDevices.add(backdoor2);
        BLEDevices.add(frontdoor3);
        BLEDevices.add(backdoor3);
        BLEDevices.add(huawei);
        
         List<Device> AssignededUserDevices = new ArrayList<Device>(); 
         AssignededUserDevices.add(frontdoor1);
         AssignededUserDevices.add(backdoor1);
         AssignededUserDevices.add(frontdoor2);
         AssignededUserDevices.add(backdoor2);
         AssignededUserDevices.add(frontdoor3);
         AssignededUserDevices.add(backdoor3);
         AssignededUserDevices.add(huawei);
         userDevices.put(testUser, AssignededUserDevices);
         userDevices.put(testUser1, AssignededUserDevices);
    }



    
    @Path("/users")
    @GET
	@Produces(MediaType.APPLICATION_JSON)
    public List<User> listUsers() {
        return Users;
    }


    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void addUser(User user) {
        Users.add(user);
    }
    
    @Path("/users")
    @DELETE
    public List<User> deleteUser(User user) {
        Users.removeIf(existingUser -> existingUser.username.contentEquals(user.username));
        return Users;
    }
        
   
	@POST
	@Path("/authentication/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response checkUser(User receivedUser) {
		Session userSession = new Session();
		for(final User user : Users) {
			if(user.username.equals(receivedUser.username)) {
				if(user.password.equals(receivedUser.password)) {
					UUID session = UUID.randomUUID();
					userSession.setUUID(session);
					if(!isLoggedIn(receivedUser.username))
						userSessionMap.put(session, new SimpleEntry<String, LocalDateTime>(receivedUser.username, LocalDateTime.now()));
					return Response.ok(userSession, MediaType.APPLICATION_JSON).build();	
				}
			}
		}
					return Response.ok(userSession, MediaType.APPLICATION_JSON).build();
	}

    
	@POST
	@Path("/authorization/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authorizeUserForDevice(Authorization auth) {
		List<Device> UserBLEDevices = new ArrayList<Device>();
		//User user = new User();
		for(final User user : Users) {
			if(user.username.equals(auth.username)) { 
				UserBLEDevices = userDevices.get(user);
				UserBLEDevices.add(auth.device);
				userDevices.remove(user);
				userDevices.put(user, UserBLEDevices);
				break;	
				}
		}
		return  Response.ok(userDevices, MediaType.APPLICATION_JSON).build();
	}
	
    @Path("/device")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void addDevice(Device device) {
       	BLEDevices.add(device);
    }

    @Path("/deviceID")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response updateDeviceID(Device ReveivedDevice) {
    	System.out.print("deviceID"+ReveivedDevice.deviceID+ReveivedDevice.username+"oldedeviceID"+ReveivedDevice.oldDeviceID);
    	Device dev = new Device();
    	boolean existingDevice = false;
		for(Device device1 : BLEDevices) {
				//device1.deviceID = device.deviceID;    				
				//key = device1.getKey().getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
				System.out.print("correct deviceID"+device1.deviceID);

		}
    	if(isLoggedIn(ReveivedDevice.username)) {
    		for(Device bledevice : BLEDevices) {
    			if(bledevice.deviceID.equals(ReveivedDevice.oldDeviceID)){
    				bledevice.deviceID = ReveivedDevice.deviceID;    				
    				existingDevice = true;
    				key = bledevice.getKey().getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
    				System.out.print("new value deviceID"+bledevice.deviceID);
    				break;
    			}
    		}
    		if(!existingDevice) {
    			Device newDevice = new Device(ReveivedDevice.deviceID,"0000000000000000"); 
    			BLEDevices.add(newDevice);
    		}
        	return  Response.ok(ReveivedDevice, MediaType.APPLICATION_JSON).build();
    	}else
        	return  Response.ok(dev, MediaType.APPLICATION_JSON).build();
    }

    @Path("/key")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response updateKeyForDevice(Device device) {
    	System.out.print("keys"+device.deviceID+device.username+device.key);    
    	boolean existingDevice = false;
    	if(isLoggedIn(device.username)) {
    		for(Device device1 : BLEDevices) {
    			if(device1.deviceID.equals(device.deviceID)){
    				//String k = new String(device.key,java.nio.charset.StandardCharsets.ISO_8859_1);
    				
    				device1.setKey(device.key);     		
    				existingDevice = true;
    				System.out.print("new value key"+device1.key);
    				break;
    			}
    		}    	
    		if(!existingDevice) {
    			Device newDevice = new Device("0000000000",device.key); 
    			BLEDevices.add(newDevice);
    		}
        	return  Response.ok(device, MediaType.APPLICATION_JSON).build();
    	}else {
        	Device dev = new Device();
        	return  Response.ok(dev, MediaType.APPLICATION_JSON).build();
    	}
    }
    
    
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response getToken(Token data) {
    	System.out.print("Tokens:deviceID"+data.deviceID+"username"+data.username+"CNonce"+data.CNonce+"SNonce"+data.SNonce);
		for(Device device : BLEDevices) {
			if(device.deviceID.equals(data.deviceID)){
				key = device.getKey().getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
				System.out.print("key for deviceID"+device.getKey());
					break;
			}
		}
    	if(isLoggedIn(data.username)) {
    		System.out.print("Token for authenticated users"+userSessionMap.size());
        	AES aesinstance = new AES();
        	byte[] CNonce = aesinstance.decrypt(data.CNonce.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1),key);
        	byte[] SNonce = aesinstance.decrypt(data.SNonce.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1),key);
    	//	}		

    		Token token = new Token(new String(CNonce,java.nio.charset.StandardCharsets.ISO_8859_1),new String(SNonce,java.nio.charset.StandardCharsets.ISO_8859_1));
    		//UserBLENonces.add(non);
            //generate session key and encrypt it with the shared symmetric key and send it back to client
            byte[] sessionkey = generateNonce();
            //byte[] serverNonce = generateNonce();
            byte[] encryptedSessionKey = aesinstance.encrypt(sessionkey,key);
            //byte[] encryptedServerNonce = aesinstance.encrypt(serverNonce,key);
            token.setSessionKey(new String(sessionkey,java.nio.charset.StandardCharsets.ISO_8859_1));
            token.setEncryptedSessionKey(new String(encryptedSessionKey,java.nio.charset.StandardCharsets.ISO_8859_1));
            //token.setServerNonce(new String(serverNonce,java.nio.charset.StandardCharsets.ISO_8859_1));
            //token.setEncryptedServerNonce(new String(encryptedServerNonce,java.nio.charset.StandardCharsets.ISO_8859_1));
            //send the key to the gatt client            
        	return  Response.ok(token, MediaType.APPLICATION_JSON).build();
    	}else {
    		return  Response.ok(null, MediaType.APPLICATION_JSON).build();
    		}
 		}
    
    public byte[] generateNonce(){
        byte[] Snonce = new byte[16];
        new SecureRandom().nextBytes(Snonce);
        return  Snonce;
    }
    
	private boolean authenticate(UUID session) {
		SimpleEntry<String, LocalDateTime> value = (SimpleEntry<String, LocalDateTime>) userSessionMap.get(session);
		 if ( value.getValue().isBefore(LocalDateTime.now().minusSeconds(TIMEOUT))) {
		 	userSessionMap.remove(session);
			System.out.println("session is finished"+userSessionMap.size());
		 }
		 return value == null ? false : !value.getValue().isBefore(LocalDateTime.now().minusSeconds(TIMEOUT));
	}

	private String getUsername(UUID session) {
		SimpleEntry<String, LocalDateTime> value = (SimpleEntry<String, LocalDateTime>) userSessionMap.get(session);

		return value == null ? "" : value.getKey();
	}

	public boolean isLoggedIn(String username) {
		System.out.println("isLoggedIn(" + username + ")");

		for (Entry<UUID, Entry<String, LocalDateTime>> token : userSessionMap.entrySet()) {
			if (token.getValue().getKey().equals(username)) {
				System.out.println("session is still valied");
				return authenticate(token.getKey());
			}
		}
		return false;
	}
}