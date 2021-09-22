package com.dbapp.ahcloud.adapter.sdk;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)//根据测试方法名字搞定执行顺序
public class AptIpAuditSdkTest extends BaseTest {
    @Autowired
    private AptIpAuditClient aptIpAuditSdk;

    @Test()
    public void a() {
        aptIpAuditSdk.getAccessKey("https://10.20.144.143:10026");
    }

    @Test
    public void b() {
        aptIpAuditSdk.getToken("https://10.20.144.143:10026");
    }

//    @Test
//    public void c() {
//        IpAuditReq req = new IpAuditReq();
//        req.setId(3);
//        req.setIp("1.1.1.3");
//        req.setDesc("test1111");
//        req.setEnable(1);
//        aptIpAuditSdk.addOrUpdate("https://10.20.144.143:10026",req);
//    }

    @Test
    public void d() {
        ArrayList<Integer> idList = new ArrayList<>();
        idList.add(5);
        aptIpAuditSdk.delete("https://10.20.144.143:10026",idList.toArray(new Integer[]{}));
    }
}