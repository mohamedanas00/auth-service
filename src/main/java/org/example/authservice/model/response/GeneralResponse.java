package org.example.authservice.model.response;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

@Stateless
public class GeneralResponse {
	private String message;

	public GeneralResponse() {}
	public GeneralResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Response badRequestResponse(String message) {
		return Response.status(Response.Status.BAD_REQUEST).entity(new GeneralResponse(message)).build();
	}

	public Response ServerErrorResponse(String message) {
		return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(new GeneralResponse(message)).build();
	}

	public Response ServerOKResponse(String message) {
		return Response.status(HttpServletResponse.SC_OK).entity(new GeneralResponse(message)).build();
	}


	public Response unAuthorizedResponse(String message) {
		return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(new GeneralResponse(message)).build();
	}
}
