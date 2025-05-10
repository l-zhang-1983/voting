package org.cpsbeijing.voting.repository;

import org.cpsbeijing.voting.entity.DeputyProvince;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeputyProvinceRepository extends PagingAndSortingRepository<DeputyProvince, Integer> {
}
