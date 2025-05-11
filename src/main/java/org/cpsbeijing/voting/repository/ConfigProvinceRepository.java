package org.cpsbeijing.voting.repository;

import org.cpsbeijing.voting.entity.ConfigProvince;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigProvinceRepository extends PagingAndSortingRepository<ConfigProvince, Integer> {
    Iterable<ConfigProvince> findByProvinceCode(Integer provinceCode);
}
