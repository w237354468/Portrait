package com.wzq.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("test")
public class Test {
  @RequestMapping(value = "helloworld", method = RequestMethod.GET)
  public String hw(HttpServletRequest req) {
    String addr = req.getRemoteAddr();
    return "hello " + addr + " \nreq success!";
  }
}
