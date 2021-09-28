
package com.dbapp.ahcloud.adapter.intercepter;

import cn.hutool.crypto.SecureUtil;
import com.dbapp.ahcloud.adapter.enums.ResponseCodeEnum;
import com.dbapp.ahcloud.adapter.exception.MyServiceException;
import com.dbapp.ahcloud.adapter.req.AuthorizationReq;
import com.dbapp.ahcloud.adapter.util.HttpHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * 接口认证
 *
 * @author huixia.hu
 * Date:     2021年09月27日 19:08
 * @version 1.0
 */
@Component
public class ApiInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(ApiInterceptor.class);
    private static final String AUTHORIZATION = "Authorization";
    private static final String TONCE = "tonce";
    private static final String SPLITE_CHAR = "|";
    private static final List<String> methodList = Arrays.asList("GET", "DELETE", "PUT");

    @Value("${plat.authorization.accesskey}")
    private String accesskey;
    @Value("${plat.authorization.secretKey}")
    private String secretKey;
    @Value("${plat.authorization.authFlag}")
    private Boolean authFlag;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            AuthorizationReq authorizationReq = new AuthorizationReq();
            authorizationReq.setAuthorization(request.getHeader(AUTHORIZATION));
            authorizationReq.setUrl(request.getRequestURI());
            authorizationReq.setMethod(request.getMethod());
            authorizationReq.setTonce(request.getHeader(TONCE));
            authorizationReq.setQueryString(request.getQueryString());
            dealUrl(authorizationReq);

            // 防止流读取一次后就没有了, 所以需要将流继续写出去
            String body = HttpHelper.getBodyString(request);
            authorizationReq.setBody(body);

            if (authFlag) {
                authCheck(authorizationReq);
                log.info("------接口认证通过------");
            }
        } catch (Exception e) {
            throw new MyServiceException(ResponseCodeEnum.AUTHENTICATION_FAIL.getCode());
        }
        return true;
    }

    private void dealUrl(AuthorizationReq requestDto) {
        String other = "";
        String url = requestDto.getUrl();
        if (methodList.contains(requestDto.getMethod())) {
            if (StringUtils.isNotBlank(url)) {
                if (StringUtils.isNotBlank(requestDto.getQueryString())) {
                    other = requestDto.getQueryString();
                } else {
                    url.trim();
                    String[] splits = url.split("/");
                    other = splits[splits.length - 1];
                    StringBuffer urlBuffer = new StringBuffer("/");
                    for (int i = 0; i < splits.length - 1; i++) {
                        if (StringUtils.isBlank(splits[i])) {
                            continue;
                        }
                        urlBuffer.append(splits[i]).append("/");
                    }
                    url = urlBuffer.substring(0, urlBuffer.length() - 1).toString();
                }
            }
        }
        requestDto.setUrl(url);
        requestDto.setOther(other);
    }


    private void authCheck(AuthorizationReq requestDto) throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException, MyServiceException {

        if (StringUtils.isBlank(requestDto.getTonce())) {
            log.warn("请求头中tonce为空");
            throw new MyServiceException(ResponseCodeEnum.AUTHENTICATION_FAIL.getCode());
        }
        if (requestDto.getTonce().length() < 16) {
            log.warn("请求头中tonce长度小于16位, tonce={}", requestDto.getTonce());
            throw new MyServiceException(ResponseCodeEnum.AUTHENTICATION_FAIL.getCode());
        }

        long intervalTime = Math.subtractExact(System.currentTimeMillis() * 1000,
                Long.parseLong(requestDto.getTonce()));

        //**tonce与当前时间超过300s则无效
        if (intervalTime > 300000000) {
            log.warn("当前时间与请求头中的tonce相差300s, tonce={}, now={}", requestDto.getTonce(), System.currentTimeMillis());
            throw new MyServiceException(ResponseCodeEnum.AUTHENTICATION_FAIL.getCode());
        }
        //**根据请求信息生成服务端的签名**//
        //**1拼接签名串**PUT /V1.0/ctcontrol/O03/ip_group_object/5dac60ac-ab51-4a52-afbd-5950aaaedb77//
        //**method|url|tonce|accesskey|other|MD5(body)**//
        String body = requestDto.getBody();
        if (StringUtils.isBlank(body)) {
            body = "";
        } else {
            body = generateMD5(body);
        }
        StringBuffer signBuffer = new StringBuffer(requestDto.getMethod());
        signBuffer.append(SPLITE_CHAR).append(requestDto.getUrl()).append(SPLITE_CHAR).append(requestDto.getTonce())
                .append(SPLITE_CHAR).append(accesskey).append(SPLITE_CHAR).append(requestDto.getOther())
                .append(SPLITE_CHAR).append(body);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] bytes = sha256_HMAC.doFinal(signBuffer.toString().getBytes());
        String signatureHash = byteArrayToHexString(bytes);
        // String author = "<" + accesskey + ">" + ":" + "<" + signatureHash + ">";
        String author = accesskey + ":" + signatureHash;

        Base64.Encoder encoder = Base64.getEncoder();
        String nowAuthorization = encoder.encodeToString(author.getBytes("UTF-8"));
        if (!StringUtils.equalsIgnoreCase(nowAuthorization, requestDto.getAuthorization())) {
            log.warn("请求头中的authorization与服务端生成{}的不一致:{}", nowAuthorization, requestDto.getAuthorization());
            throw new MyServiceException(ResponseCodeEnum.AUTHENTICATION_FAIL.getCode());
        }
    }

    public static String generateMD5(String param) {
        String param1 = param;
        String paramRes = param;
        if (!StringUtils.isEmpty(param)) {
            paramRes = SecureUtil.md5(param1).toLowerCase();
        }
        return paramRes;
    }


    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        for (int n = 0; b != null && n < b.length; n++) {
            String stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }
}
