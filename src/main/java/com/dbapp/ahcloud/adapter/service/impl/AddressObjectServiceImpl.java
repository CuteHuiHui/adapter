package com.dbapp.ahcloud.adapter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dbapp.ahcloud.adapter.dao.AddressObjectMapper;
import com.dbapp.ahcloud.adapter.model.AddressObject;
import com.dbapp.ahcloud.adapter.req.AddressObjectReq;
import com.dbapp.ahcloud.adapter.service.AddressObjectService;
import com.dbapp.xplan.common.enums.YesOrNo;
import com.dbapp.xplan.common.utils.BeanUtil;
import com.dbapp.xplan.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 安全策略
 *
 * @author huixia.hu
 * Date:     2021年09月15日 14:48
 * @version 1.0
 */
@Service
@Slf4j
public class AddressObjectServiceImpl implements AddressObjectService {
    @Resource
    private AddressObjectMapper addressObjectMapper;


    @Override
    public void addAddressObject(AddressObjectReq addressObjectReq) {
        AddressObject addressObject = this.getAddressObject(addressObjectReq);
        addressObjectMapper.insert(addressObject);
    }

    @Override
    public void deleteAddressObject(String ipObjectId) {
        List<AddressObject> addressObjects = addressObjectMapper.selectList(new LambdaQueryWrapper<AddressObject>()
                .eq(AddressObject::getIpObjectId, ipObjectId)
                .eq(AddressObject::getIsDeleted, YesOrNo.NO.getValue()));
        if (CollectionUtils.isEmpty(addressObjects)) {
            log.error("ipObjectId:{}不存在", ipObjectId);
        } else {
            AddressObject addressObject = new AddressObject();
            addressObject.setIsDeleted(YesOrNo.YES.getValue());
            addressObjectMapper.update(addressObject,
                    new LambdaQueryWrapper<AddressObject>().eq(AddressObject::getIpObjectId, ipObjectId));
        }
    }

    @Override
    public void modifyAddressObject(AddressObjectReq addressObjectReq) {
        List<AddressObject> addressObjects = addressObjectMapper.selectList(new LambdaQueryWrapper<AddressObject>()
                .eq(AddressObject::getIpObjectId, addressObjectReq.getIpObjectId())
                .eq(AddressObject::getIsDeleted, YesOrNo.NO.getValue()));
        if (CollectionUtils.isEmpty(addressObjects)) {
            log.error("ipObjectId:{}不存在", addressObjectReq.getIpObjectId());
        } else {
            AddressObject addressObject = this.getAddressObject(addressObjectReq);
            addressObjectMapper.update(addressObject,
                    new LambdaQueryWrapper<AddressObject>().eq(AddressObject::getIpObjectId,
                            addressObjectReq.getIpObjectId()));
        }
    }

    /**
     * 入参转SecurityPolicy实体
     *
     * @param addressObjectReq
     * @return
     */
    private AddressObject getAddressObject(AddressObjectReq addressObjectReq) {
        AddressObject addressObject = BeanUtil.genBean(addressObjectReq, AddressObject.class);
        addressObject.setObjectList(JsonUtils.toJSONString(addressObjectReq.getObjectList()));
        addressObject.setExceptObjectList(JsonUtils.toJSONString(addressObjectReq.getExceptObjectList()));
        return addressObject;
    }

}