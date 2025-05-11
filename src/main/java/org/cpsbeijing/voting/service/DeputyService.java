package org.cpsbeijing.voting.service;

import org.cpsbeijing.voting.common.PagingConfig;
import org.cpsbeijing.voting.entity.ConfigProvince;
import org.cpsbeijing.voting.repository.DeputyProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeputyService {
    private DeputyProvinceRepository deputyProvinceRepository;

    @Autowired
    public void setDeputyProvinceRepository(DeputyProvinceRepository deputyProvinceRepository) {
        this.deputyProvinceRepository = deputyProvinceRepository;
    }

    public Page<ConfigProvince> getDeputyProvinceForPage(PagingConfig pagingConfig) {
        String direction = Optional.ofNullable(pagingConfig.getDirection()).orElse("desc");
        String sortField = Optional.ofNullable(pagingConfig.getSortField()).orElse("provinceCode");
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(pagingConfig.getPage(), pagingConfig.getSize(), sort);
        return deputyProvinceRepository.findAll(pageable);
    }
}
