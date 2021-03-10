package org.acme.rest.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

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
				return null;	
				}
		}

		return null;
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
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void add(Device device) {
       	BLEDevices.add(device);
    	deviceKey.put(device, device.getKey());
    }
    
    @Path("/keys")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Collection<String> getKeys() {
    	return deviceKey.values();
    }
    
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @POST
    public HashMap<String,String> decryptNonces(Nonces data) {
    	//System.out.print("Hi"+data.getCNonce()+data.getSNonce()+data.getMAC());
    	//define a class that does a decryption
    	String key = "";
    	HashMap<String,String> decryptedNonces=new HashMap<String,String>() ;
		for(Device device : BLEDevices) {
			//System.out.print("MAC" + data.getMAC());

			if(device.MAC.equals(data.getMAC())){
				key = deviceKey.get(device);
				//System.out.print("key"+key);
					break;
			}
		}
		
    	AES aes = new AES();
    	decryptedNonces.put("CNonce", aes.decrypt(data.getCNonce(),key));
    	decryptedNonces.put( "SNonce",aes.decrypt(data.getSNonce(),key));    	
    	return decryptedNonces;
    }
}