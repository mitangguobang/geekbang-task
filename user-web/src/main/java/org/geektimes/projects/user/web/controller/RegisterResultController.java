package org.geektimes.projects.user.web.controller;

import org.geektimes.bo.ReturnValue;
import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserServiceImpl;
import org.geektimes.web.mvc.controller.PageController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author chenyue
 * @date 2021/2/28
 */
@Path("/registerDo")
public class RegisterResultController implements PageController {

    private UserServiceImpl userService = ComponentContext.getInstance().getComponent("bean/UserService");
    @Override
    @GET
    @POST
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        User user = new User();
        user.setName(request.getParameter("inputName"));
        user.setEmail(request.getParameter("inputEmail"));
        user.setPhoneNumber(request.getParameter("inputTel"));
        user.setPassword(request.getParameter("inputPassword"));
        ReturnValue ret = userService.register(user);
        if (ret.getCode()) {
            return "register-success.jsp";
        } else {
            request.setAttribute("errorMsg", ret.getMessage());
            return "register-fail.jsp?";
        }
    }
}
