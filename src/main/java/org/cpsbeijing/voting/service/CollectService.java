package org.cpsbeijing.voting.service;

import org.cpsbeijing.voting.entity.BallotInfo;
import org.cpsbeijing.voting.pojo.BallotContents;
import org.cpsbeijing.voting.pojo.BallotItem;
import org.cpsbeijing.voting.repository.BallotInfoRepository;
import org.cpsbeijing.voting.repository.ConfigCandidateRepository;
import org.cpsbeijing.voting.repository.CustomizedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CollectService {
    private ConfigCandidateRepository configCandidateRepository;

    private CustomizedRepository customizedRepository;

    private BallotInfoRepository ballotInfoRepository;

    @Autowired
    public void setConfigCandidateRepository(ConfigCandidateRepository configCandidateRepository) {
        this.configCandidateRepository = configCandidateRepository;
    }

    @Autowired
    public void setCustomizedRepository(CustomizedRepository customizedRepository) {
        this.customizedRepository = customizedRepository;
    }

    @Autowired
    public void setBallotInfoRepository(BallotInfoRepository ballotInfoRepository) {
        this.ballotInfoRepository = ballotInfoRepository;
    }

    // 创建新的空选票内容
    public BallotContents createBlankBallot() {
        BallotContents ballotContents = new BallotContents();
        List<Object[]> rawBallotItems = this.customizedRepository.getBallotItems();
        List<BallotItem> ballotItems = rawBallotItems.stream().map(i -> {
            BallotItem ballotItem = new BallotItem();
            ballotItem.setCandidateId((Integer) i[0]);
            ballotItem.setCandidateType((Integer) i[1]);
            ballotItem.setProvinceCode((Integer) i[2]);
            ballotItem.setCandidateName((String) i[3]);
            ballotItem.setChecked((Integer) i[4]);
            return ballotItem;
        }).collect(Collectors.toList());

        ballotContents.setBallotItemList(ballotItems);
        return ballotContents;
    }

    // 根据 ballotId 获取已经保存的选票内容
    public BallotContents getBlankBallot(Integer ballotId) {
        return new BallotContents();
    }

    // 根据 serialNo 检查是否已经有重复的选票信息
    public Boolean hasDuplicatedBallotSerialNo(Integer serialNo) {
        BallotInfo ballotInfo = this.getBallotInfo(serialNo);
        return Objects.isNull(ballotInfo) ? Boolean.FALSE : Boolean.TRUE;
    }

    // 根据 serialNo 获取选票信息
    public BallotInfo getBallotInfo(Integer serialNo) {
        return this.ballotInfoRepository.findBySerialNumber(serialNo);
    }

    // 根据页面录入的选票内容 保存或者更新选票内容 [选票信息 + 选票明细]
    public BallotContents saveOrUpdateBallot(BallotContents ballotContents) {
        Integer serialNo = ballotContents.getSerialNo();
        BallotInfo ballotInfo = this.ballotInfoRepository.findBySerialNumber(serialNo);
        return ballotContents;
    }

}
