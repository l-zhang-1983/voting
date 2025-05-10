package org.cpsbeijing.voting.controller;

import org.cpsbeijing.voting.common.PagingConfig;
import org.cpsbeijing.voting.common.Request;
import org.cpsbeijing.voting.common.Response;
import org.cpsbeijing.voting.entity.DeputyProvince;
import org.cpsbeijing.voting.service.DeputyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/voting")
public class DeputyController {

    private DeputyService deputyService;

    @Autowired
    public void setDeputyService(DeputyService deputyService) {
        this.deputyService = deputyService;
    }

    @GetMapping("/example")
    @ResponseBody
    public Response<String> example(@RequestParam("param") String param) {
        Response<String> response = new Response<>();
        response.setData("Example Result: [" + param + "]");
        return response;
    }

    @PostMapping("/getProvince")
    @ResponseBody
    public Response<Page<DeputyProvince>> getProvince(@RequestBody Request<Map<String, String>> request) {
        Map<String, String> extraParam = request.getParam();
        Response<Page<DeputyProvince>> response = new Response<>();
        Page<DeputyProvince> deputyProvincePage = this.deputyService.getDeputyProvinceForPage(request.getPagingConfig());
        response.setData(deputyProvincePage);
        return response;
    }

}
