package com.wzq.search.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class Test {
  @RequestMapping(value = "helloworld", method = RequestMethod.GET)
  public String hw(HttpServletRequest req) {
    String addr = req.getRemoteAddr();
    return "hello " + addr + " \nreq success!";
  }
}
