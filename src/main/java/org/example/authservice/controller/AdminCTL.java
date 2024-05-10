package org.example.authservice.controller;

import org.example.authservice.BSL.AdminBSL;
import org.example.authservice.model.User;
import org.example.authservice.model.response.GeneralResponse;
import org.example.authservice.utils.AuthUtil;
import org.example.authservice.utils.RoleProvider;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminCTL {
	@Inject
	GeneralResponse generalResponse;
	@Inject
	AdminBSL adminBSL;
	@Inject
	private RoleProvider roleProvider;
	@Inject
	private AuthUtil authUtil;

	@GET
	@Path("/ViewInstructors/{id}")
	public Response ViewInstructorsAccounts(@PathParam("id") int id) {
		try {
			if(!authUtil.isAdmin(id)){
				generalResponse =new GeneralResponse("Unauthorized!");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(generalResponse).build();
			}
			return adminBSL.ViewInstructorsAccounts();
		} catch (Exception e) {
			e.printStackTrace();
			generalResponse = new GeneralResponse("An error occurred while View Accounts");
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
		}
	}

	@GET
	@Path("/ViewStudents/{id}")
	public Response ViewStudentsAccounts(@PathParam("id") int id) {
		try {
			if(!authUtil.isAdmin(id)){
				generalResponse =new GeneralResponse("Unauthorized!");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(generalResponse).build();
			}
			return adminBSL.ViewStudentAccounts();
		} catch (Exception e) {
			e.printStackTrace();
			generalResponse = new GeneralResponse("An error occurred while View Accounts");
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
		}
	}

	@GET
	@Path("/ViewCenterTest/{id}")
	public Response ViewCenterTestAccounts(@PathParam("id") int id) {
		try {
			if(!authUtil.isAdmin(id)){
				generalResponse =new GeneralResponse("Unauthorized!");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(generalResponse).build();
			}
			return adminBSL.ViewCenterTestAccounts();
		} catch (Exception e) {
			e.printStackTrace();
			generalResponse = new GeneralResponse("An error occurred while View Accounts");
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
		}
	}

	@DELETE
	@Path("/DeleteUser/{id}")
	public Response DeleteUserAccount(@PathParam("id") int id, @QueryParam("userId") int userId) {
		try {
			if(!authUtil.isAdmin(id)){
				generalResponse =new GeneralResponse("Unauthorized!");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(generalResponse).build();
			}
			return adminBSL.DeleteAccount(userId);
		} catch (Exception e) {
			e.printStackTrace();
			generalResponse = new GeneralResponse("An error occurred while Delete Account");
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
		}
	}



}
