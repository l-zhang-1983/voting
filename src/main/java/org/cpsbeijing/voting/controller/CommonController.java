package org.cpsbeijing.voting.controller;

import org.cpsbeijing.voting.common.PagingRequest;
import org.cpsbeijing.voting.common.Request;
import org.cpsbeijing.voting.common.Response;
import org.cpsbeijing.voting.entity.ConfigProvince;
import org.cpsbeijing.voting.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/common")
public class CommonController {

    private CommonService commonService;

    @Autowired
    public void setDeputyService(CommonService commonService) {
        this.commonService = commonService;
    }

    @PostMapping("/getProvinceForPage")
    @ResponseBody
    public Response<Page<ConfigProvince>> getProvinceForPage(@RequestBody PagingRequest<Map<String, String>> pagingRequest) {
        Response<Page<ConfigProvince>> response = new Response<>();
        Page<ConfigProvince> deputyProvincePage = this.commonService.getConfigProvinceForPage(pagingRequest.getPagingConfig());
        response.setData(deputyProvincePage);
        return response;
    }

    @PostMapping("/getProvince")
    @ResponseBody
    public Response<Iterable<ConfigProvince>> getProvince(@RequestBody Request<Map<String, String>> request) {
        Response<Iterable<ConfigProvince>> response = new Response<>();
        String rawProvinceCode = request.getParam().getOrDefault("provinceCode", null);
        Integer provinceCode = Optional.ofNullable(rawProvinceCode).map(Integer::valueOf).orElse(null);
        Iterable<ConfigProvince> deputyProvincePage = this.commonService.getConfigProvinces(provinceCode);
        response.setData(deputyProvincePage);
        return response;
    }
}
