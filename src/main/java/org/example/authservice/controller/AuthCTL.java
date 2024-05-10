package org.example.authservice.controller;

import org.example.authservice.BSL.AuthBSL;
import org.example.authservice.model.UserCredentials;
import org.example.authservice.model.response.GeneralResponse;
import org.example.authservice.utils.TokenService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthCTL {
	@Inject
	private AuthBSL authBSL;

	@Inject
	private  GeneralResponse generalResponse;

	@POST
	@Path("/signUp")
	public Response SignUP(JsonObject jsonObject){
		try {
			return authBSL.RegisterUser(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			generalResponse = new GeneralResponse("An error occurred during SignUP!");
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
		}
	}


	@POST
	@Path("/sign_in")
	public Response SignIn(UserCredentials credentials) {
		try {
			return authBSL.signIn(credentials.getEmail(), credentials.getPassword());
		} catch (IOException e) {
			e.printStackTrace();
			generalResponse = new GeneralResponse("An error occurred during SignIn!");
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
		}
	}



}
