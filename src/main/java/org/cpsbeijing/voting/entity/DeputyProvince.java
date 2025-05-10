package org.cpsbeijing.voting.entity;

import javax.persistence.*;

@Entity
@Table(name = "deputy_province")
public class DeputyProvince {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer provinceCode;

    @Column(nullable = false)
    private String provinceName;

    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
