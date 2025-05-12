package org.cpsbeijing.voting.controller;

import org.cpsbeijing.voting.common.Request;
import org.cpsbeijing.voting.common.Response;
import org.cpsbeijing.voting.entity.BallotInfo;
import org.cpsbeijing.voting.pojo.BallotContents;
import org.cpsbeijing.voting.pojo.BallotItem;
import org.cpsbeijing.voting.service.CollectService;
import org.cpsbeijing.voting.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/collect")
public class CollectController {

    private CommonService commonService;

    private CollectService collectService;

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @Autowired
    public void setCollectService(CollectService collectService) {
        this.collectService = collectService;
    }

    @GetMapping("/example")
    @ResponseBody
    public Response<String> example(@RequestParam(name = "param", required = false) String param) {
        Response<String> response = new Response<>();
        response.setData("Example Result: [" + param + "]");
        return response;
    }

    @PostMapping("/blankBallot")
    @ResponseBody
    public Response<BallotContents> createBlankBallot() {
        Response<BallotContents> response = new Response<>();
        response.setData(this.collectService.createBlankBallot());
        return response;
    }

    @PostMapping("/saveBallot")
    @ResponseBody
    public Response<String> saveOrUpdateBallot(@RequestBody Request<BallotContents> request) {
        Response<String> response = new Response<>();
        BallotContents ballotContents = request.getParam();
        Integer serialNo = ballotContents.getSerialNo();
        if (this.collectService.hasDuplicatedBallotSerialNo(serialNo)) {
            response.setSuccess(Boolean.FALSE);
            response.setResponseMessage("Duplicated Ballot Serial No: " + serialNo);
        } else {
            BallotContents ballotInfo = this.collectService.saveOrUpdateBallot(ballotContents);
        }
        return response;
    }
}
