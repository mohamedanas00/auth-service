package org.example.authservice.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class InstructorService {

	private static final String BASE_URL = "http://localhost:3000/course";

	public Boolean deleteInstructorCourses(int instructorId) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(BASE_URL)
				.path("DeleteInstructorCourses")
				.path(String.valueOf(instructorId));

		Response response = target.request().delete();
		boolean result = false;

		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			return  true;
		}
		response.close();

		return result;
	}
}