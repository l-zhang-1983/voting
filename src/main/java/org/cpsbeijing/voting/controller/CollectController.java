package org.cpsbeijing.voting.controller;

import org.cpsbeijing.voting.common.Request;
import org.cpsbeijing.voting.common.Response;
import org.cpsbeijing.voting.pojo.BallotContents;
import org.cpsbeijing.voting.service.CollectService;
import org.cpsbeijing.voting.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/getBallotContents")
    @ResponseBody
    public Response<BallotContents> getBallotContents(@RequestBody Request<Integer> request) {
        Integer ballotId = request.getParam();
        Response<BallotContents> response = new Response<>();
        response.setData(this.collectService.getBallotContents(ballotId));
        return response;
    }

    @PostMapping("/saveBallotContents")
    @ResponseBody
    public Response<BallotContents> saveBallotContents(@RequestBody Request<BallotContents> request) {
        Response<BallotContents> response = new Response<>();
        BallotContents ballotContents = request.getParam();
        Integer ballotId = ballotContents.getBallotId();
        if (ballotId == null) {
            // 不存在 ballotId 即为页面首次录入的新选票内容
            // 直接保存完整选票内容
            Integer serialNo = ballotContents.getSerialNo();
            if (this.collectService.hasDuplicatedBallotSerialNo(serialNo)) {
                response.setSuccess(Boolean.FALSE);
                response.setResponseMessage("Duplicated Ballot Serial No: " + serialNo);
                return response;
            } else {
                ballotContents = this.collectService.saveBallotContents(ballotContents, Boolean.FALSE);
            }
        } else {
            // 存在 ballotId 即为页面修改现有的选票内容
            ballotContents = this.collectService.updateBallotContents(ballotContents);
        }

        response.setData(ballotContents);
        return response;
    }

    @PostMapping("/deleteBallotContents")
    @ResponseBody
    public Response<Integer> deleteBallotContents(@RequestBody Request<Integer> request) {
        Response<Integer> response = new Response<>();
        Integer serialNo = request.getParam();
        serialNo = this.collectService.deleteBallotContents(serialNo);
        response.setData(serialNo);
        return response;
    }
}
