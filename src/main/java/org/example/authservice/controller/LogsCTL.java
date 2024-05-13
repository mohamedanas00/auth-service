package org.example.authservice.controller;
import org.example.authservice.BSL.LogsBSL;
import org.example.authservice.model.Logs;
import org.example.authservice.model.response.GeneralResponse;
import org.example.authservice.utils.AuthUtil;
import org.example.authservice.utils.TokenService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/logs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LogsCTL {
	@Inject
	private LogsBSL logsBSL;
	@Inject
	private GeneralResponse generalResponse;
	@Inject
	private AuthUtil authUtil;

	@GET
	@Path("/ViewLogsAccountService")
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
			return logsBSL.GetLogs();
		} catch (Exception e) {
			e.printStackTrace();
			generalResponse = new GeneralResponse("An error occurred while View Accounts");
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
		}
	}

}
