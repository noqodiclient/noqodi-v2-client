package com.noqodi.client.v2api.controllers;

import com.noqodi.client.v2api.configurations.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class WebController {

    @Value("#{servletContext.contextPath}")
    private String contextPath;

    @Autowired
    private WebAppConfiguration configuration;

    @RequestMapping({"/", "/index", "/home"})
    public String home(ModelMap model) {
        model.addAttribute("contextPath", contextPath);
        model.addAttribute("appBaseUrl", configuration.getAppBaseUrl());
        return "index";
    }

    @RequestMapping("/web/commands")
    public String command(ModelMap  model) {
//        String apiBasePath = contextPath == null ? configuration.getApiBasePath() : contextPath + configuration.getApiBasePath();
        model.addAttribute("contextPath", contextPath);
        model.addAttribute("apiBasePath", configuration.getApiBasePath());
        model.addAttribute("appBaseUrl", configuration.getAppBaseUrl());
        return "commands";
    }

    @RequestMapping("/web/payment")
    public String payment(ModelMap model, HttpServletRequest request) throws UnsupportedEncodingException {
        //        String apiBasePath = contextPath == null ? configuration.getApiBasePath() : contextPath + configuration.getApiBasePath();
        model.addAttribute("noqodiPaymentPage", configuration.getNoqodiPaymentPage());
        model.addAttribute("noqodiPaymentPageOrigin", configuration.getNoqodiPaymentPageOrigin());
        model.addAttribute("callbackUrl", configuration.getCallbackUrl());
        model.addAttribute("noqodiReadyData", getReadyData(request.getParameter("paymentResult")));
        model.addAttribute("contextPath", contextPath);
        model.addAttribute("apiBasePath", configuration.getApiBasePath());
        model.addAttribute("appBaseUrl", configuration.getAppBaseUrl());
        return "payment";
    }

    private String getReadyData(String param) throws UnsupportedEncodingException {
        if (null != param)
            return URLEncoder.encode(param, "UTF-8");
        return configuration.getNoqodiReadyData();
    }

    @RequestMapping("/web/iframe-test")
    public String iframe(ModelMap  model) {
        model.addAttribute("contextPath", contextPath);
        model.addAttribute("appBaseUrl", configuration.getAppBaseUrl());
        return "iframe-test";
    }

}


