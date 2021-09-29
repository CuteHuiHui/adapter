package com.dbapp.ahcloud.adapter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dbapp.ahcloud.adapter.dao.AddressObjectMapper;
import com.dbapp.ahcloud.adapter.model.AddressObject;
import com.dbapp.ahcloud.adapter.req.AddressObjectReq;
import com.dbapp.ahcloud.adapter.service.AddressObjectService;
import com.dbapp.xplan.common.enums.YesOrNo;
import com.dbapp.xplan.common.exception.ServiceInvokeException;
import com.dbapp.xplan.common.utils.BeanUtil;
import com.dbapp.xplan.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 地址对象
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
        if (!Objects.isNull(this.selectAddressObject(addressObjectReq.getIpObjectId()))) {
            throw ServiceInvokeException.newException("ipObjectId:" + addressObjectReq.getIpObjectId() + "地址对象已存在");
        }
        AddressObject addressObject = this.getAddressObject(addressObjectReq);
        addressObjectMapper.insert(addressObject);
    }

    @Override
    public void deleteAddressObject(String ipObjectId) {
        AddressObject addressObject = this.selectAddressObject(ipObjectId);
        if (Objects.isNull(addressObject)) {
            throw ServiceInvokeException.newException("ipObjectId:" + ipObjectId + "地址对象不存在或者已删除");
        }

        addressObject.setIsDeleted(YesOrNo.YES.getValue());
        addressObjectMapper.update(addressObject,
                new LambdaQueryWrapper<AddressObject>().eq(AddressObject::getIpObjectId, ipObjectId));
    }

    @Override
    public void modifyAddressObject(AddressObjectReq addressObjectReq) {
        if (Objects.isNull(this.selectAddressObject(addressObjectReq.getIpObjectId()))) {
            throw ServiceInvokeException.newException("ipObjectId:" + addressObjectReq.getIpObjectId() +
                    "地址对象不存在或者已删除");
        }

        AddressObject addressObject = this.getAddressObject(addressObjectReq);
        addressObjectMapper.update(addressObject,
                new LambdaQueryWrapper<AddressObject>().eq(AddressObject::getIpObjectId,
                        addressObjectReq.getIpObjectId()));
    }

    @Override
    public List<AddressObject> getAddressObjectList(List<String> ipObjectIds) {
        List<AddressObject> addressObjects = addressObjectMapper.selectList(new LambdaQueryWrapper<AddressObject>()
                .in(AddressObject::getIpObjectId, ipObjectIds)
                .eq(AddressObject::getIsDeleted, YesOrNo.NO.getValue()));
        return CollectionUtils.isEmpty(addressObjects) ? new ArrayList<>() : addressObjects;
    }

    /**
     * 入参转AddressObject实体
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

    private AddressObject selectAddressObject(String ipObjectId) {
        return addressObjectMapper.selectOne(new LambdaQueryWrapper<AddressObject>()
                .eq(AddressObject::getIpObjectId, ipObjectId)
                .eq(AddressObject::getIsDeleted, YesOrNo.NO.getValue()));
    }
}