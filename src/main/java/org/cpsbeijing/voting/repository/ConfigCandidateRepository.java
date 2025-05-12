package org.cpsbeijing.voting.repository;

import org.cpsbeijing.voting.entity.ConfigCandidateInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigCandidateRepository extends CrudRepository<ConfigCandidateInfo, Integer> {
    Iterable<ConfigCandidateInfo> findAllByOrderByCandidateTypeAscCandidateTypeAsc();
}
