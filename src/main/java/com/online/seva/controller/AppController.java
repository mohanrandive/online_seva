package com.online.seva.controller;

import com.online.seva.domain.Role;
import com.online.seva.domain.User;
import com.online.seva.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AppController {
    private static final String TEMPLATE_PREFIX = "views/";
    private static final String ADMIN_PAGE = "admin";
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);
    @Autowired
    private SessionService sessionService;

    @RequestMapping("/")
    String home(ModelMap modal) {
        modal.addAttribute("title", "online-seva");
        return "index";

    }

    @RequestMapping("/pagerouting/{page}")
    String partialHandler(@PathVariable("page") final String page, HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        logger.error("page::" + page);
        boolean isAdmin = false;
        String view = TEMPLATE_PREFIX + page.trim();
        String forbidden = "/forbidden";
        if (!page.equalsIgnoreCase("login")) {
            User loggedUser = sessionService.getLoggedUser(request);
            if (null == loggedUser) {
                redirectAttributes.addFlashAttribute("routed", true);
                redirectAttributes.addFlashAttribute("logged", false);
                return "redirect:" + forbidden;
            }
            logger.info("logged User ::: " + loggedUser);

            if (!page.equalsIgnoreCase(ADMIN_PAGE))
                return view;

            for (Role role : loggedUser.getRoles()) {
                logger.info("User Role::" + role.getRole());
                if (role.getRole().equalsIgnoreCase("admin"))
                    isAdmin = true;
            }

            if (isAdmin)
                return view;
            logger.info("not admin");
            redirectAttributes.addFlashAttribute("logged", true);
            redirectAttributes.addFlashAttribute("name", loggedUser.getName());
            return "redirect:/" +"accessDenied";
        }
        /*User loggedUser = sessionService.getLoggedUser(request);
        if (null == loggedUser)
            return view;*/

        return view;
    }
}
