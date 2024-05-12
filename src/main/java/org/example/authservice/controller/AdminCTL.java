package org.example.authservice.controller;

import org.example.authservice.BSL.AdminBSL;
import org.example.authservice.model.User;
import org.example.authservice.model.response.GeneralResponse;
import org.example.authservice.utils.AuthUtil;
import org.example.authservice.utils.RoleProvider;
import org.example.authservice.utils.TokenService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

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
	@Path("/ViewInstructors")
	public Response ViewInstructorsAccounts(@HeaderParam("token") String token) {
		try {
			Map<String, Object> payload = TokenService.verifyToken(token);
			if (payload == null) {
				return generalResponse.badRequestResponse("Invalid token");
			}
			int id = (int) payload.get("id");
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
	@Path("/ViewStudents")
	public Response ViewStudentsAccounts(@HeaderParam("token") String token) {
		try {
			Map<String, Object> payload = TokenService.verifyToken(token);
			if (payload == null) {
				return generalResponse.badRequestResponse("Invalid token");
			}
			int id = (int) payload.get("id");
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
	@Path("/ViewCenterTest")
	public Response ViewCenterTestAccounts(@HeaderParam("token") String token) {
		try {
			Map<String, Object> payload = TokenService.verifyToken(token);
			if (payload == null) {
				return generalResponse.badRequestResponse("Invalid token");
			}
			int id = (int) payload.get("id");
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
	public Response DeleteUserAccount(@HeaderParam("token") String token, @PathParam("id") int userId) {
		try {
			Map<String, Object> payload = TokenService.verifyToken(token);
			if (payload == null) {
				return generalResponse.badRequestResponse("Invalid token");
			}
			int id = (int) payload.get("id");
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
