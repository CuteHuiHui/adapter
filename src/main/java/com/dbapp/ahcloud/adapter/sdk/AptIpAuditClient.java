package com.dbapp.ahcloud.adapter.sdk;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dbapp.ahcloud.adapter.sdk.req.IpAuditReq;
import com.dbapp.xplan.common.enums.YesOrNo;
import com.dbapp.xplan.common.exception.ServiceInvokeException;
import com.dbapp.xplan.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Objects;

/**
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
    private String accessKeyIdConfig;
    private String accessKeySecretConfig;
    private String token;

    public void getAccessKey(String accessUrl) {
        log.info("getAccessKey accessUrl is:{}", accessUrl);
        String responseStr;
        try {
            responseStr = HttpRequest.get(accessUrl + "/auth/accessKey")
                    .execute().body();

            JSONObject jsonObject = JSONUtil.parseObj(responseStr);
            if (!Objects.isNull(jsonObject.get(CODE)) && jsonObject.get(CODE).equals(YesOrNo.NO.getValue())) {
                this.accessKeyIdConfig = jsonObject.get(ACCESS_KEY_ID_CONFIG).toString();
                this.accessKeySecretConfig = jsonObject.get(ACCESS_KEY_SECRET_CONFIG).toString();
            } else {
                throw ServiceInvokeException.newException(jsonObject.toString());
            }
        } catch (Exception e) {
            log.error("APT获取accessKey失败", e);
        }
    }

    public void getToken(String accessUrl) {
        log.info("getToken accessUrl is:{}", accessUrl);

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put(accessKeyId, accessKeyIdConfig);
        paramMap.put(accessKeySecret, accessKeySecretConfig);

        String responseStr;
        try {
            responseStr = HttpRequest.post(accessUrl + "/auth/token")
                    .addHeaders(paramMap)
                    .execute().body();

            JSONObject jsonObject = JSONUtil.parseObj(responseStr);
            if (!Objects.isNull(jsonObject.get(CODE)) && jsonObject.get(CODE).equals(YesOrNo.NO.getValue())) {
                this.token = jsonObject.get(TOKEN).toString();
            } else {
                throw ServiceInvokeException.newException(jsonObject.toString());
            }
        } catch (Exception e) {
            log.error("APT获取token失败", e);
        }
    }

    public void addOrUpdate(String accessUrl, IpAuditReq req) {
        String reqStr = JsonUtils.toJSONString(req);
        log.info("addOrUpdate accessUrl is:{}, req is:{}", accessUrl, JsonUtils.toJSONString(req));

        String responseStr;
        try {
            responseStr = HttpRequest.post(accessUrl + "/config/ipaudit/edit")
                    .header(AUTHORIZATION, token)
                    .body(reqStr)
                    .execute().body();

            JSONObject jsonObject = JSONUtil.parseObj(responseStr);
            if (Objects.isNull(jsonObject.get(ERROR_CODE)) || !jsonObject.get(ERROR_CODE).equals(HttpStatus.HTTP_OK)) {
                throw ServiceInvokeException.newException(jsonObject.toString());
            }
        } catch (Exception e) {
            log.error("APT添加或修改IP检测配置失败", e);
        }
    }

    public void delete(String accessUrl, Integer[] req) {
        String reqStr = JsonUtils.toJSONString(req);
        log.info("delete accessUrl is:{}, req is:{}", accessUrl, JsonUtils.toJSONString(req));

        String responseStr;
        try {
            responseStr = HttpRequest.post(accessUrl + "/config/ipaudit/delete")
                    .header(AUTHORIZATION, token)
                    .body(reqStr)
                    .execute().body();

            JSONObject jsonObject = JSONUtil.parseObj(responseStr);
            if (Objects.isNull(jsonObject.get(ERROR_CODE)) || !jsonObject.get(ERROR_CODE).equals(HttpStatus.HTTP_OK)) {
                throw ServiceInvokeException.newException(jsonObject.toString());
            }
        } catch (Exception e) {
            log.error("APT删除IP检测配置失败", e);
        }
    }


//    public void getVersion(String accessUrl) {
//        log.info("accessUrl:{}", accessUrl);
//
//        String key = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MzIzMjI0MDQsInVzZXJuYW1lIjoibmVpYnVjZXNoaSJ9" +
//                ".Jag0Qv1hglUtHOdNjB_Kx6E26afze7lRFBtLWWvsvJY";
//
//        HashMap<String, Object> paramMap = new HashMap<>(1);
//        paramMap.put("id", "3");
//
//        String responseStr;
//        try {
//            responseStr = HttpRequest.get("https://10.20.144.143:10026/config/ipaudit/get?id=3")
//                    .header("Authorization", key)
//                    .form(paramMap)
//                    .execute().body();
//
//            if (StringUtils.isNotBlank(responseStr)) {
//                JSONObject jsonObject = JSONUtil.parseObj(responseStr);
//                System.out.println(jsonObject);
////                //成功
////                if (!Objects.isNull(jsonObject.get(CODE)) && jsonObject.get(CODE).equals(HttpStatus.HTTP_OK)) {
////                    ProductVersionDTO resp = new ProductVersionDTO();
////                    resp.setVersion(jsonObject.getJSONObject(DATA).toBean(EDRVersionResponseDTO.class).getVersion());
////                    return resp;
////                } else {
////                    throw ServiceInvokeException.newException(jsonObject.get(MESSAGE).toString());
////                }
//            }
//        } catch (Exception e) {
//            log.error("EDR查询版本号失败", e);
////            return new ProductVersionDTO();
//        }
////        return new ProductVersionDTO();
//    }
}
