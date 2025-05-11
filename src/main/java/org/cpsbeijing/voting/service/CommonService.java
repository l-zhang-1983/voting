package org.cpsbeijing.voting.service;

import org.cpsbeijing.voting.common.PagingConfig;
import org.cpsbeijing.voting.entity.ConfigProvince;
import org.cpsbeijing.voting.repository.ConfigProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CommonService {
    private ConfigProvinceRepository configProvinceRepository;

    @Autowired
    public void setConfigProvinceRepository(ConfigProvinceRepository configProvinceRepository) {
        this.configProvinceRepository = configProvinceRepository;
    }

    public Page<ConfigProvince> getConfigProvinceForPage(PagingConfig pagingConfig) {
        String direction = Optional.ofNullable(pagingConfig.getDirection()).orElse("desc");
        String sortField = Optional.ofNullable(pagingConfig.getSortField()).orElse("provinceCode");
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(pagingConfig.getPage(), pagingConfig.getSize(), sort);
        return configProvinceRepository.findAll(pageable);
    }

    public Iterable<ConfigProvince> getConfigProvinces(Integer provinceCode) {
        if (Objects.isNull(provinceCode)) {
            return this.configProvinceRepository.findAll();
        } else {
            return this.configProvinceRepository.findByProvinceCode(provinceCode);
        }
    }
}
