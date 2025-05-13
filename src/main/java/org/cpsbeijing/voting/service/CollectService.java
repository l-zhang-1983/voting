package org.cpsbeijing.voting.service;

import org.cpsbeijing.voting.common.Constants;
import org.cpsbeijing.voting.entity.BallotDetails;
import org.cpsbeijing.voting.entity.BallotInfo;
import org.cpsbeijing.voting.entity.ConfigCandidateInfo;
import org.cpsbeijing.voting.pojo.BallotContents;
import org.cpsbeijing.voting.pojo.BallotItem;
import org.cpsbeijing.voting.repository.BallotDetailsRepository;
import org.cpsbeijing.voting.repository.BallotInfoRepository;
import org.cpsbeijing.voting.repository.ConfigCandidateRepository;
import org.cpsbeijing.voting.repository.CustomizedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CollectService {
    private ConfigCandidateRepository configCandidateRepository;

    private CustomizedRepository customizedRepository;

    private BallotInfoRepository ballotInfoRepository;

    private BallotDetailsRepository ballotDetailsRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss0SSS");

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

    @Autowired
    public void setBallotDetailsRepository(BallotDetailsRepository ballotDetailsRepository) {
        this.ballotDetailsRepository = ballotDetailsRepository;
    }

    // 创建新的空选票内容
    public BallotContents createBlankBallot() {
        BallotContents ballotContents = new BallotContents();
        List<Object[]> rawBallotItems = this.customizedRepository.getBallotItems(null);
        return processBallotContents(ballotContents, rawBallotItems);
    }

    // 根据 ballotId 获取已经保存的选票内容
    public BallotContents getBallotContents(Integer ballotId) {
        List<Object[]> rawBallotItems = this.customizedRepository.getBallotItems(ballotId);
        BallotContents ballotContents = new BallotContents();
        ballotContents = processBallotContents(ballotContents, rawBallotItems);

        BallotInfo ballotInfo = this.ballotInfoRepository.findByBallotId(ballotId);
        ballotContents.setBallotId(ballotId);
        ballotContents.setSerialNo(ballotInfo.getSerialNo());
        ballotContents.setSupervisorCount(ballotInfo.getSupervisorCount());
        ballotContents.setSupervisorExceeded(ballotInfo.getSupervisorExceeded());
        ballotContents.setDirectorCount(ballotInfo.getDirectorCount());
        ballotContents.setDirectorExceed(ballotInfo.getDirectorExceed());
        ballotContents.setCreatedAt(ballotInfo.getCreatedAt());

        return ballotContents;
    }

    private BallotContents processBallotContents(BallotContents ballotContents, List<Object[]> rawBallotItems) {
        List<BallotItem> ballotItems = rawBallotItems.stream().map(i -> {
            BallotItem ballotItem = new BallotItem();
            ballotItem.setCandidateId((Integer) i[0]);
            ballotItem.setCandidateType((Integer) i[1]);
            ballotItem.setProvinceCode((Integer) i[2]);
            ballotItem.setCandidateName((String) i[3]);
            ballotItem.setChecked((Integer) i[4]);
            return ballotItem;
        }).collect(Collectors.toList());

        List<BallotItem> supervisorList
                = ballotItems.stream()
                .filter(item -> item.getCandidateType().compareTo(Constants.CANDIDATE_TYPE_SUPERVISOR) == 0)
                .collect(Collectors.toList());
        ballotContents.setSupervisorList(supervisorList);

        List<BallotItem> directorList
                = ballotItems.stream()
                .filter(item -> item.getCandidateType().compareTo(Constants.CANDIDATE_TYPE_DIRECTOR) == 0)
                .collect(Collectors.toList());
        ballotContents.setDirectorList(directorList);

        return ballotContents;
    }

    // 根据 serialNo 检查是否已经有重复的选票信息
    public Boolean hasDuplicatedBallotSerialNo(Integer serialNo) {
        BallotInfo ballotInfo = this.getBallotInfo(serialNo);
        return Objects.isNull(ballotInfo) ? Boolean.FALSE : Boolean.TRUE;
    }

    // 根据 serialNo 获取选票信息
    public BallotInfo getBallotInfo(Integer serialNo) {
        return this.ballotInfoRepository.findBySerialNo(serialNo);
    }

    public BallotInfo getBallotInfoByBallotId(Integer ballotId) {
        return this.ballotInfoRepository.findByBallotId(ballotId);
    }

    // 根据 ballotId 删除现有选票内容
    @Transactional
    public void deleteBallotContents(Integer ballotId, Boolean ipUpdate) {
        BallotInfo ballotInfo = this.getBallotInfoByBallotId(ballotId);
        this.ballotDetailsRepository.removeBallotDetailsByBallot(ballotInfo);
        if (!ipUpdate) {
            this.ballotInfoRepository.delete(ballotInfo);
        }
    }

    // 保存页面录入的选票内容 [选票信息 + 选票明细]
    // 根据 isUpdate 的值控制 更新 [选票信息] 或 新生成 [选票信息]
    @Transactional
    public BallotContents saveBallotContents(BallotContents ballotContents, Boolean isUpdate) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String now = formatter.format(currentDateTime);
        // 获取页面录入的选票信息
        Integer serialNo = ballotContents.getSerialNo();

        BallotInfo ballotInfo;
        if (isUpdate) {
            // 更新现有选票内容
            ballotInfo = this.ballotInfoRepository.findBySerialNo(serialNo);
        } else {
            ballotInfo = new BallotInfo();
        }

        // 保存页面录入的选票信息
        ballotInfo.setSerialNo(serialNo);
        ballotInfo.setCreatedAt(now);
        ballotInfo.setUpdatedAt(now);
        // 为新选票信息生成主键 ballotId
        ballotInfo = this.ballotInfoRepository.save(ballotInfo);

        Iterable<BallotItem> supervisorList = ballotContents.getSupervisorList();
        Iterator<BallotItem> supervisorIterator = supervisorList.iterator();
        BallotDetails supervisorDetails;
        List<BallotDetails> supervisorDetailsList = new ArrayList<>();
        while (supervisorIterator.hasNext()) {
            BallotItem ballotItem = supervisorIterator.next();
            if (ballotItem.getChecked() == 1) {
                supervisorDetails = new BallotDetails();
                supervisorDetails.setBallot(ballotInfo);
                ConfigCandidateInfo candidateInfo
                        = this.configCandidateRepository.findByCandidateId(ballotItem.getCandidateId());
                supervisorDetails.setCandidateType(Constants.CANDIDATE_TYPE_SUPERVISOR);
                supervisorDetails.setCreatedAt(now);
                supervisorDetails.setUpdatedAt(now);
                supervisorDetails.setCandidate(candidateInfo);
                supervisorDetailsList.add(supervisorDetails);
            }
        }

        Iterable<BallotItem> directorList = ballotContents.getSupervisorList();
        Iterator<BallotItem> directorIterator = directorList.iterator();
        BallotDetails directorDetails;
        List<BallotDetails> directorDetailsList = new ArrayList<>();
        while (directorIterator.hasNext()) {
            BallotItem ballotItem = directorIterator.next();
            if (ballotItem.getChecked() == 1) {
                directorDetails = new BallotDetails();
                directorDetails.setBallot(ballotInfo);
                ConfigCandidateInfo candidateInfo
                        = this.configCandidateRepository.findByCandidateId(ballotItem.getCandidateId());
                directorDetails.setCandidateType(Constants.CANDIDATE_TYPE_DIRECTOR);
                directorDetails.setCreatedAt(now);
                directorDetails.setUpdatedAt(now);
                directorDetails.setCandidate(candidateInfo);
                directorDetailsList.add(directorDetails);
            }
        }

        // 更新选票中的统计信息
        ballotInfo.setSupervisorCount(supervisorDetailsList.size());
        ballotInfo.setSupervisorExceeded(supervisorDetailsList.size() > Constants.CANDIDATE_TYPE_SUPERVISOR_EXCEEDED ? 1 : 0);
        ballotInfo.setDirectorCount(directorDetailsList.size());
        ballotInfo.setDirectorExceed(directorDetailsList.size() > Constants.CANDIDATE_TYPE_DIRECTOR_EXCEEDED ? 1 : 0);

        this.ballotInfoRepository.save(ballotInfo);
        this.ballotDetailsRepository.saveAll(supervisorDetailsList);
        this.ballotDetailsRepository.saveAll(directorDetailsList);

        return ballotContents;
    }

    // 更新页面再次录入的现有选票内容 [选票信息 + 选票明细]
    @Transactional
    public BallotContents updateBallotContents(BallotContents ballotContents) {
        // 删除现有的选票内容 更新 [选票信息] 删除 [选票明细]
        this.deleteBallotContents(ballotContents.getBallotId(), Boolean.TRUE);

        // 保存新的选票内容
        return this.saveBallotContents(ballotContents, Boolean.TRUE);
    }

    // 完全删除现有选票内容 [选票信息 + 选票明细]
    @Transactional
    public Integer deleteBallotContents(Integer serialNo) {
        BallotInfo ballotInfo = this.getBallotInfo(serialNo);
        if (ballotInfo != null) {
            this.deleteBallotContents(ballotInfo.getBallotId(), Boolean.FALSE);
        }
        return serialNo;
    }

}
