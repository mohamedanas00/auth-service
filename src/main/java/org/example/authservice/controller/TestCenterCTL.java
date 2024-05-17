package org.example.authservice.controller;


import org.example.authservice.BSL.AdminBSL;
import org.example.authservice.BSL.AuthBSL;
import org.example.authservice.BSL.TestCenterBSL;
import org.example.authservice.model.response.GeneralResponse;
import org.example.authservice.utils.AuthUtil;
import org.example.authservice.utils.TokenService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/testCenter")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TestCenterCTL {
    @Inject
    private TestCenterBSL testCenterBSL;

    @Inject
    private GeneralResponse generalResponse;
    @Inject
    private AuthUtil authUtil;

    @PUT
    @Path("/UpdateAccount")
    public Response UpdateTestCenterAccount(@HeaderParam("token") String token, JsonObject jsonObject) {
        try {
            Map<String, Object> payload = TokenService.verifyToken(token);
            if (payload == null) {
                return generalResponse.badRequestResponse("Invalid token");
            }
            int id = (int) payload.get("id");
            if (!authUtil.isTestCenter(id)) {
                generalResponse = new GeneralResponse("Unauthorized!");
                return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(generalResponse).build();
            }
            return testCenterBSL.UpdateUserAccount(jsonObject, id);
        } catch (Exception e) {
            e.printStackTrace();
            generalResponse = new GeneralResponse("An error occurred while Update Account");
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
        }
    }

    //	get test info by token
    @GET
    @Path("/getTestInfo")
    public Response getTestInfo(@HeaderParam("token") String token) {
        try {
            Map<String, Object> payload = TokenService.verifyToken(token);
            if (payload == null) {
                return generalResponse.badRequestResponse("Invalid token");
            }
            int id = (int) payload.get("id");
            if (!authUtil.isTestCenter(id)) {
                generalResponse = new GeneralResponse("Unauthorized!");
                return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(generalResponse).build();
            }
            return testCenterBSL.getTestCenterInfo(id);
        } catch (Exception e) {
            e.printStackTrace();
            generalResponse = new GeneralResponse("An error occurred while get test info");
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
        }
    }


}
