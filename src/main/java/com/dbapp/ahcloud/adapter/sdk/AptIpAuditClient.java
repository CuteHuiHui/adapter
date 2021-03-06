package com.dbapp.ahcloud.adapter.sdk;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dbapp.ahcloud.adapter.sdk.dto.IpAuditDTO;
import com.dbapp.xplan.common.enums.YesOrNo;
import com.dbapp.xplan.common.exception.ServiceInvokeException;
import com.dbapp.xplan.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;


/**
 * 调用APT IP检测接口
 *
 * @author huixia.hu
 * Date:     2021年09月18日 17:28
 * @version 1.0
 */
@Slf4j
@Component
public class AptIpAuditClient {

    private static final String CODE = "code";
    private static final String ERROR_CODE = "error_code";
    private static final String accessKeyId = "accessKeyId";
    private static final String accessKeySecret = "accessKeySecret";
    private static final String ACCESS_KEY_ID_CONFIG = "accessKeyIdConfig";
    private static final String ACCESS_KEY_SECRET_CONFIG = "accessKeySecretConfig";
    private static final String TOKEN = "token";
    private static final String AUTHORIZATION = "Authorization";
//    private static final String URL = "https://localhost";
    private static final String URL = "https://10.20.144.143:10026";
    private static final String ACCESS_KEY_URL = "/auth/accessKey";
    private static final String TOKEN_URL = "/auth/token";
    private static final String EDIT_URL = "/config/ipaudit/edit";
    private static final String DELETE_URL = "/config/ipaudit/delete";
    private static final String LIST_URL = "/config/ipaudit/list";
    private static final String GET_URL = "/config/ipaudit/get";
    private String accessKeyIdConfig;
    private String accessKeySecretConfig;
    private String token;

    /**
     * 获取密钥
     */
    public void getAccessKey() {
        String responseStr;
        try {
            responseStr = HttpRequest.get(URL + ACCESS_KEY_URL)
                    .execute().body();

            JSONObject jsonObject = JSONUtil.parseObj(responseStr);
            if (StringUtils.isNotBlank(jsonObject.get(CODE).toString()) && jsonObject.get(CODE).equals(YesOrNo.NO.getValue())) {
                this.accessKeyIdConfig = jsonObject.get(ACCESS_KEY_ID_CONFIG).toString();
                this.accessKeySecretConfig = jsonObject.get(ACCESS_KEY_SECRET_CONFIG).toString();
            } else {
                throw ServiceInvokeException.newException(jsonObject.toString());
            }
        } catch (Exception e) {
            throw ServiceInvokeException.newException("APT获取accessKey失败", e);
        }
    }

    /**
     * 获取token
     */
    public void getToken() {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put(accessKeyId, accessKeyIdConfig);
        paramMap.put(accessKeySecret, accessKeySecretConfig);

        String responseStr;
        try {
            responseStr = HttpRequest.post(URL + TOKEN_URL)
                    .addHeaders(paramMap)
                    .execute().body();

            JSONObject jsonObject = JSONUtil.parseObj(responseStr);
            if (StringUtils.isNotBlank(jsonObject.get(CODE).toString()) && jsonObject.get(CODE).equals(YesOrNo.NO.getValue())) {
                this.token = jsonObject.get(TOKEN).toString();
            } else {
                throw ServiceInvokeException.newException(jsonObject.toString());
            }
        } catch (Exception e) {
            throw ServiceInvokeException.newException("APT获取token失败", e);
        }
    }

    /**
     * 新增或修改IP检测
     * @param req
     */
    public void addOrUpdate(IpAuditDTO req) {
        String reqStr = JsonUtils.toJSONString(req);
        log.info("新增或修改IP检测req:{}", JsonUtils.toJSONString(req));

        String responseStr;
        try {
            responseStr = HttpRequest.post(URL + EDIT_URL)
                    .header(AUTHORIZATION, token)
                    .body(reqStr)
                    .execute().body();

            JSONObject jsonObject = JSONUtil.parseObj(responseStr);
            if (StringUtils.isBlank(jsonObject.get(ERROR_CODE).toString()) || !jsonObject.get(ERROR_CODE).equals(HttpStatus.HTTP_OK)) {
                throw ServiceInvokeException.newException(jsonObject.toString());
            }
        } catch (Exception e) {
            throw ServiceInvokeException.newException("APT添加或修改IP检测配置失败", e);
        }
    }

    /**
     * 查询IP检测列表
     */
    public List<IpAuditDTO> list() {
        String responseStr;
        try {
            responseStr = HttpRequest.get(URL + LIST_URL)
                    .header(AUTHORIZATION, token)
                    .form("limit",Integer.MAX_VALUE)
                    .execute().body();

            JSONObject jsonObject = JSONUtil.parseObj(responseStr);
            if (StringUtils.isNotBlank(jsonObject.get(ERROR_CODE).toString()) && jsonObject.get(ERROR_CODE).equals(HttpStatus.HTTP_OK)) {
                List<IpAuditDTO> resp = com.alibaba.fastjson.JSONObject.parseArray(JSONUtil.parseObj(jsonObject.get(
                        "data")).get("data").toString(), IpAuditDTO.class);
                return resp;
            }else{
                throw ServiceInvokeException.newException(jsonObject.toString());
            }
        } catch (Exception e) {
            throw ServiceInvokeException.newException("APT查询IP检测列表失败", e);
        }
    }

    /**
     * 删除IP检测
     * @param ids
     */
    public void delete(Integer[] ids) {
        String reqStr = JsonUtils.toJSONString(ids);
        log.info("删除IP检测ids:{}", JsonUtils.toJSONString(ids));

        String responseStr;
        try {
            responseStr = HttpRequest.post(URL + DELETE_URL)
                    .header(AUTHORIZATION, token)
                    .body(reqStr)
                    .execute().body();

            JSONObject jsonObject = JSONUtil.parseObj(responseStr);
            if (StringUtils.isBlank(jsonObject.get(ERROR_CODE).toString()) || !jsonObject.get(ERROR_CODE).equals(HttpStatus.HTTP_OK)) {
                throw ServiceInvokeException.newException(jsonObject.toString());
            }
        } catch (Exception e) {
            throw ServiceInvokeException.newException("APT删除IP检测配置失败", e);
        }
    }


    /**
     * 查询IP检测
     * @param id
     */
    public IpAuditDTO get(Integer id) {
        log.info("查询IP检测id:{}", id);

        String responseStr;
        try {
            responseStr = HttpRequest.get(URL + GET_URL)
                    .header(AUTHORIZATION, token)
                    .form("id",id)
                    .execute().body();

            JSONObject jsonObject = JSONUtil.parseObj(responseStr);
            if (StringUtils.isNotBlank(jsonObject.get(ERROR_CODE).toString()) && jsonObject.get(ERROR_CODE).equals(HttpStatus.HTTP_OK)) {
                IpAuditDTO resp = com.alibaba.fastjson.JSONObject.parseObject(JSONUtil.parseObj(jsonObject.get(
                        "data")).toString(), IpAuditDTO.class);
                return resp;
            } else {
                throw ServiceInvokeException.newException(jsonObject.toString());
            }
        } catch (Exception e) {
            throw ServiceInvokeException.newException("APT查询IP检测失败", e);
        }
    }
}
