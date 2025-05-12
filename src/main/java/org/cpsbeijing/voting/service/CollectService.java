package org.cpsbeijing.voting.service;

import org.cpsbeijing.voting.entity.ConfigCandidateInfo;
import org.cpsbeijing.voting.pojo.BallotContents;
import org.cpsbeijing.voting.pojo.BallotItem;
import org.cpsbeijing.voting.repository.ConfigCandidateRepository;
import org.cpsbeijing.voting.repository.CustomizedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CollectService {
    private ConfigCandidateRepository configCandidateRepository;

    private CustomizedRepository customizedRepository;

    @Autowired
    public void setConfigCandidateRepository(ConfigCandidateRepository configCandidateRepository) {
        this.configCandidateRepository = configCandidateRepository;
    }

    @Autowired
    public void setCustomizedRepository(CustomizedRepository customizedRepository) {
        this.customizedRepository = customizedRepository;
    }

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

    public BallotContents getBlankBallot(Integer ballotId) {
        return new BallotContents();
    }

}
