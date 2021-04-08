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
public class UserResource {

    //private Set<User> Users = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private List<User> Users = new ArrayList<User>();
    //All device in the system
    private List<Device> BLEDevices = new ArrayList<Device>();    
    //binding between the user and a list of devices
	private HashMap<User,List<Device>> userPermissions= new HashMap<User,List<Device>>();
	//binding between the device and the symmetric key that used as a secret between the server and device
	private HashMap<Device,String> deviceKey = new HashMap<Device,String>();
	private byte[] key = "1111222233334444".getBytes();
	
	private HashMap<UUID, Entry<String, LocalDateTime>> userSessionMap = new HashMap<UUID, Entry<String, LocalDateTime>>();
	private HashMap<UUID,String> userSession= new HashMap<UUID,String>();
	private static final int TIMEOUT = 100; // Timeout in seconds

	
    public UserResource() {
    	User test = new User("mohamad", "hlal");
        Users.add(test);
        
        Device frontdoor1 = new Device("frontdoorlock","19:11:CA:5A:8B:44");
        Device backdoor1 = new Device("backdoolockr","19:44:C6:9B:95:44");
        Device frontdoor2 = new Device("temperture","19:11:CA:5A:8B:44");
        Device backdoor2 = new Device("labsdoor","19:44:C6:9B:95:44");
        Device frontdoor3 = new Device("kitchendoor","19:11:CA:5A:8B:44");
        Device backdoor3 = new Device("lightbulb","19:44:C6:9B:95:44");
        
        BLEDevices.add(frontdoor1);
        BLEDevices.add(backdoor1);
        BLEDevices.add(frontdoor2);
        BLEDevices.add(backdoor2);
        BLEDevices.add(frontdoor3);
        BLEDevices.add(backdoor3);
        
         List<Device> testUserDevices = new ArrayList<Device>(); 
         testUserDevices.add(frontdoor1);
         testUserDevices.add(backdoor1);
         testUserDevices.add(frontdoor2);
         testUserDevices.add(backdoor2);
         testUserDevices.add(frontdoor3);
         testUserDevices.add(backdoor3);
         userPermissions.put(test, testUserDevices);
     
    }
    @Path("/users")
    @GET
	@Produces(MediaType.APPLICATION_JSON)
    public List<User> list() {
        return Users;
    }
/*
    @GET
    @Path("/username/{username}")
    public Set<Country> name(@PathParam String name) {
        return countriesService.getByName(name);
    }
    */
    //@Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void add(User user) {
        Users.add(user);
    }

    //need change to send a User object json instead of sending the password in the header
    
	@GET
	@Path("/checkauthorizatation/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response request(@PathParam("username") String username, @QueryParam("password") String password) {
		String login = "failed";
		List<Device> UserBLEDevices = new ArrayList<Device>();
		//User user = new User();
		for(final User user1 : Users) {
			if(user1.username.equals(username)) {
				if(user1.password.equals(password)) {
					login = "success";
					UserBLEDevices = userPermissions.get(user1);
					UUID session = UUID.randomUUID();
					userSessionMap.put(session, new SimpleEntry<String, LocalDateTime>(username, LocalDateTime.now()));
					//sessionRole.put(session,role);
					userSession.put(session,username);
				return Response.ok(UserBLEDevices, MediaType.APPLICATION_JSON).build();	
				}
			}
		}

		return Response.ok(UserBLEDevices, MediaType.APPLICATION_JSON).build();
	}
	
    //authorized devices
    //need change to send a User object json instead of sending the password in the header
    
	@POST
	@Path("/authorization/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authorizeUser(Authorization auth ) {
		List<Device> UserBLEDevices = new ArrayList<Device>();
		//User user = new User();
		for(final User user1 : Users) {
			if(user1.username.equals(auth.username)) { 
				UserBLEDevices = userPermissions.get(user1);
				UserBLEDevices.add(auth.device);
				userPermissions.remove(user1);
				userPermissions.put(user1, UserBLEDevices);
				break;	
				}
		}
		return  Response.ok(userPermissions, MediaType.APPLICATION_JSON).build();
	}
	
	
	
	@GET
	@Path("/check/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response requestauthorize(@PathParam("username") String username, @QueryParam("password") String password) {
		String login = "failed";
		for(final User user1 : Users) {
			if(user1.username.equals(username)) {
				if(user1.password.equals(password))
					login = "success";
					break;
			}
		}
		return Response.ok(login, MediaType.TEXT_PLAIN).build();

	}
	
	
    @DELETE
    public List<User> delete(User user) {
        Users.removeIf(existingUser -> existingUser.username.contentEquals(user.username));
        return Users;
    }
    

    
    @Path("/keys")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Collection<String> getKeys() {
    	return deviceKey.values();
    }
    
    @Path("/keys")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response add(Device device) {
       	BLEDevices.add(device);
    	deviceKey.put(device, device.getKey());
		List<Token> UserBLENonces = new ArrayList<Token>();

		//Nonc non = new Nonc("hi","hi");
		//UserBLENonces.add(non);
    	return  Response.ok(null, MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response decryptNonces(Token data) {
    	System.out.print(data.deviceID+data.CNonce+data.SNonce+data.username);
    	//define a class that does a decryption
    	//String key = "";
    	//Nonces decryptedNonces=new Nonces() ;
		/*for(Device device : BLEDevices) {
			//System.out.print("MAC" + data.getMAC());

			if(device.deviceID.equals(data.deviceID)){
				key = deviceKey.get(device).getBytes();
				System.out.print("keyyyyyyyyyyyyyyyyyyyyyyyyyy"+key.toString());
					break;
			}
		}*/
		//List<Nonc> UserBLENonces = new ArrayList<Nonc>();
		//List<Device> UserBLEDevices = new ArrayList<Device>();

		//if(!key.equals("")) {
    	if(isLoggedIn(data.username)) {
        	AES aesinstance = new AES();
        	byte[] CNonce = aesinstance.decrypt(data.CNonce.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1),key);
        	byte[] SNonce = aesinstance.decrypt(data.SNonce.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1),key);
    	//	}		

    		Token token = new Token(new String(CNonce,java.nio.charset.StandardCharsets.ISO_8859_1),new String(SNonce,java.nio.charset.StandardCharsets.ISO_8859_1));
    		//UserBLENonces.add(non);
            //generate session key and encrypt it with the shared symmetric key and send it back to client
            byte[] sessionkey = generateNonce();
            byte[] serverNonce = generateNonce();
            byte[] encryptedSessionKey = aesinstance.encrypt(sessionkey,key);
            byte[] encryptedServerNonce = aesinstance.encrypt(serverNonce,key);
            token.setSessionKey(new String(sessionkey,java.nio.charset.StandardCharsets.ISO_8859_1));
            token.setEncryptedSessionKey(new String(encryptedSessionKey,java.nio.charset.StandardCharsets.ISO_8859_1));
            token.setServerNonce(new String(serverNonce,java.nio.charset.StandardCharsets.ISO_8859_1));
            token.setEncryptedServerNonce(new String(encryptedServerNonce,java.nio.charset.StandardCharsets.ISO_8859_1));
            //send the key to the gatt client
        	return  Response.ok(token, MediaType.APPLICATION_JSON).build();
    	}else {
        	//System.out.print("ella");
    		return  Response.ok(null, MediaType.APPLICATION_JSON).build();}

    			
    }
    
    public byte[] generateNonce(){
        byte[] Snonce = new byte[16];
        new SecureRandom().nextBytes(Snonce);
        return  Snonce;
    }
    
	private boolean authenticate(UUID session) {
		SimpleEntry<String, LocalDateTime> value = (SimpleEntry<String, LocalDateTime>) userSessionMap.get(session);
		 if ( value.getValue().isBefore(LocalDateTime.now().minusSeconds(TIMEOUT))) {
			 //sessionRole.remove(session);
		 	userSession.remove(session);
		 	userSessionMap.remove(session);
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
				return authenticate(token.getKey());
			}
		}
		return false;
	}
}