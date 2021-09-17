package com.dbapp.ahcloud.adapter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dbapp.ahcloud.adapter.dao.IdsPolicyMapper;
import com.dbapp.ahcloud.adapter.model.IdsPolicy;
import com.dbapp.ahcloud.adapter.model.SecurityPolicy;
import com.dbapp.ahcloud.adapter.req.IdsPolicyReq;
import com.dbapp.ahcloud.adapter.req.SecurityPolicyReq;
import com.dbapp.ahcloud.adapter.service.IdsPolicyService;
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
public class IdsPolicyServiceImpl implements IdsPolicyService {
    @Resource
    private IdsPolicyMapper idsPolicyMapper;



    @Override
    public void addIdsPolicy(IdsPolicyReq idsPolicyReq) {
        IdsPolicy IdsPolicy = this.getIdsPolicy(idsPolicyReq);
        idsPolicyMapper.insert(IdsPolicy);
    }

    @Override
    public void delteIdsPolicy(String ids_policy_id) {
        List<IdsPolicy> idsPolicies = idsPolicyMapper.selectList(new LambdaQueryWrapper<IdsPolicy>()
                .eq(IdsPolicy::getIdsPolicyId, ids_policy_id)
                .eq(IdsPolicy::getIsDeleted, YesOrNo.NO.getValue()));
        if (CollectionUtils.isEmpty(idsPolicies)) {
            log.error("ids_policy_id:{}不存在",ids_policy_id);
        }else {
            IdsPolicy idsPolicy = new IdsPolicy();
            idsPolicy.setIsDeleted(YesOrNo.YES.getValue());
            idsPolicyMapper.update(idsPolicy,
                    new LambdaQueryWrapper<IdsPolicy>().eq(IdsPolicy::getIdsPolicyId, ids_policy_id));
        }

    }

    @Override
    public void modifyIdsPolicy(IdsPolicyReq idsPolicyReq) {
        List<IdsPolicy> idsPolicies = idsPolicyMapper.selectList(new LambdaQueryWrapper<IdsPolicy>()
                .eq(IdsPolicy::getIdsPolicyId, idsPolicyReq.getIdsPolicyId())
                .eq(IdsPolicy::getIsDeleted, YesOrNo.NO.getValue()));
        if (CollectionUtils.isEmpty(idsPolicies)) {
            log.error("ids_policy_id:{}不存在",idsPolicyReq.getIdsPolicyId());
        }else {
            IdsPolicy idsPolicy = this.getIdsPolicy(idsPolicyReq);
            idsPolicyMapper.update(idsPolicy,
                    new LambdaQueryWrapper<IdsPolicy>().eq(IdsPolicy::getIdsPolicyId, idsPolicyReq.getIdsPolicyId()));
        }
    }

    /**
     * 入参转IdsPolicy实体
     * @param idsPolicyReq
     * @return
     */
    private IdsPolicy getIdsPolicy(IdsPolicyReq idsPolicyReq) {
        IdsPolicy idsPolicy = BeanUtil.genBean(idsPolicyReq, IdsPolicy.class);
        idsPolicy.setSecurityPolicyIds(JsonUtils.toJSONString(idsPolicyReq.getSecurityPolicyIds()));
        return idsPolicy;
    }

}