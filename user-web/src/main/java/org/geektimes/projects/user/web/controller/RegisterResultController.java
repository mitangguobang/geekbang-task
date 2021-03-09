package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
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
@Path("/registerDo")
public class RegisterResultController implements PageController {
    private UserServiceImpl userService = new UserServiceImpl();
    @Override
    @GET
    @POST
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setName(request.getParameter("inputName"));
        user.setEmail(request.getParameter("inputEmail"));
        user.setPhoneNumber(request.getParameter("inputTel"));
        user.setPassword(request.getParameter("inputPassword"));

        if (true == userService.register(user)) {
            return "register-success.jsp";
        } else {
            return "register-fail.jsp";
        }
    }
}
