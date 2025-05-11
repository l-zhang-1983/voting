package org.cpsbeijing.voting.controller;

import org.cpsbeijing.voting.common.Response;
import org.cpsbeijing.voting.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/collect")
public class CollectController {

    private CommonService commonService;

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/example")
    @ResponseBody
    public Response<String> example(@RequestParam(name = "param", required = false) String param) {
        Response<String> response = new Response<>();
        response.setData("Example Result: [" + param + "]");
        return response;
    }



}
