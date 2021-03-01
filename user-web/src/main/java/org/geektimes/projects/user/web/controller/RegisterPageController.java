package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.service.UserServiceImpl;
import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author chenyue
 * @date 2021/2/28
 */
@Path("/register")
public class RegisterPageController implements PageController {
    private UserServiceImpl userService = new UserServiceImpl();
    @Override
    @GET
    @POST
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        return "register-form.jsp";
    }

}
