package com.med.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.med.auth.entity.MedicineOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MedicineOrderMapper extends BaseMapper<MedicineOrder> {
}