package org.example.authservice.utils;

import org.example.authservice.model.TestCenterService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ProcessingException;

public class TestCenterUpdateService {

	private static final String BASE_URL = "http://localhost:5000/branch";

	public Boolean updateTestCenter(int testCenterId, TestCenterService testCenter) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(BASE_URL)
				.path("updateTestCenterBranch")
				.path(String.valueOf(testCenterId));

		try {
			Response response = target.request().put(Entity.entity(testCenter, MediaType.APPLICATION_JSON));

			boolean result = false;
			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				result = true;
			}
			response.close();
			return result;
		} catch (ProcessingException e) {
			// Log the exception if necessary
			System.err.println("Error invoking REST request: " + e.getMessage());
			return false;
		} finally {
			client.close();
		}
	}
}
