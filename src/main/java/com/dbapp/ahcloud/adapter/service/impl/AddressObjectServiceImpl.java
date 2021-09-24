package com.dbapp.ahcloud.adapter.service.impl;

import com.alibaba.fastjson.JSONObject;
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
        AddressObject addressObject = this.getAddressObject(addressObjectReq);
        addressObjectMapper.insert(addressObject);
    }

    @Override
    public void deleteAddressObject(String ipObjectId) {
        List<AddressObject> addressObjects = addressObjectMapper.selectList(new LambdaQueryWrapper<AddressObject>()
                .eq(AddressObject::getIpObjectId, ipObjectId)
                .eq(AddressObject::getIsDeleted, YesOrNo.NO.getValue()));
        if (CollectionUtils.isEmpty(addressObjects)) {
            String error = "ipObjectId:" + ipObjectId + "地址对象不存在或者已删除";
            throw ServiceInvokeException.newException(error);
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
            String error = "ipObjectId:" + addressObjectReq.getIpObjectId() + "地址对象不存在或者已删除";
            throw ServiceInvokeException.newException(error);
        } else {
            AddressObject addressObject = this.getAddressObject(addressObjectReq);
            addressObjectMapper.update(addressObject,
                    new LambdaQueryWrapper<AddressObject>().eq(AddressObject::getIpObjectId,
                            addressObjectReq.getIpObjectId()));
        }
    }

    @Override
    public List<AddressObject> getAddressObjects(List<String> ipObjectIds) {
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


    public static void main(String[] args) {
//        List<AddressObjectReq.ObjectListItem> objectList = new ArrayList<>();
//        List<AddressObjectReq.ObjectListItem> objectList1 = new ArrayList<>();
//        AddressObjectReq.ObjectListItem o1 = new AddressObjectReq.ObjectListItem();
//        o1.setAddress("1.1.1.1");
//        o1.setType("network");
//        AddressObjectReq.ObjectListItem o2 = new AddressObjectReq.ObjectListItem();
//        o2.setAddress("1.1.1.1");
//        o2.setType("network");
//        objectList.add(o1);
//        objectList.add(o2);
//        String s = JsonUtils.toJSONString(objectList);
//        System.out.println(s);
//        List<AddressObjectReq.ObjectListItem> objectListItems = JSONObject.parseArray(s,
//                AddressObjectReq.ObjectListItem.class);
//
//        List<String> collect =
//                objectListItems.stream().map(AddressObjectReq.ObjectListItem::getAddress).collect(Collectors.toList());
//
//        System.out.println(11111);

        List<String> a = new ArrayList<>();
        a.add("aaa");
        a.add("bbb");
        System.out.println(JsonUtils.toJSONString(a));


        System.out.println(JSONObject.parseArray(JsonUtils.toJSONString(a),String.class));
        System.out.println(JSONObject.parseArray(JsonUtils.toJSONString(a),String.class));
    }

}