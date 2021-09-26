package com.dbapp.ahcloud.adapter.sdk;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)//根据测试方法名字搞定执行顺序
public class AptIpAuditSdkTest extends BaseTest {
    @Autowired
    private AptIpAuditClient aptIpAuditSdk;

    @Test()
    public void a() {
        aptIpAuditSdk.getAccessKey();
    }

    @Test
    public void b() {
        aptIpAuditSdk.getToken();
    }

//    @Test
//    public void c() {
//        IpAuditReq req = new IpAuditReq(null,"1.1.1.1-1.1.1.100","test1111",1);
//        aptIpAuditSdk.addOrUpdate(req);
//    }

//    @Test
//    public void d() {
//        ArrayList<Integer> idList = new ArrayList<>();
//        idList.add(5);
//        aptIpAuditSdk.delete(idList.toArray(new Integer[]{}));
//    }

//    @Test
//    public void e() {
//        aptIpAuditSdk.list();
//    }

    @Test
    public void f() {
        aptIpAuditSdk.get(100);
    }




}