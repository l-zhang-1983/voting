package org.cpsbeijing.voting.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "config_province")
@Getter
@Setter
public class ConfigProvince {
    @Id
    @Column(name = "province_code", nullable = false)
    private Integer provinceCode;

    @Column(name = "province_name", nullable = false, length = 32)
    private String provinceName;

}
