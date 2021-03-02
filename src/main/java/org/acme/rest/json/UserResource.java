package org.acme.rest.json;

import java.util.ArrayList;
import java.util.Collections;
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

@Path("/users")
public class UserResource {

    //private Set<User> Users = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private List<User> Users = new ArrayList<User>();

    public UserResource() {
        Users.add(new User("mohamad", "hlal"));
        //Users.add(new User("ella", "ali"));
    }

    @GET
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
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void add(User user) {
        Users.add(user);
    }

    
	@GET
	@Path("/check/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response request(@PathParam("username") String username, @QueryParam("password") String password) {
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
}