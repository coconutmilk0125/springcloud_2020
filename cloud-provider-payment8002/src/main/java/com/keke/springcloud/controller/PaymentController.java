package com.keke.springcloud.controller;

import com.alibaba.fastjson.JSON;
import com.keke.springcloud.entities.CommonResult;
import com.keke.springcloud.entities.Payment;
import com.keke.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    @ResponseBody
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        System.out.println(("****插入结果：" + result));
      //  Log.info("****插入结果：" + result);

        if(result>0){
            return new CommonResult(200,"插入数据库成功！serverPort:"+serverPort,result);
        }else{
            return new CommonResult(444,"插入数据库失败",null);
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public String getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        //     Log.info("*******查询结果："+payment);
        System.out.println("*******查询结果："+payment);

        Map< String , Object > jsonMap = new HashMap< String , Object>();
        Map< String , Object > inner = new HashMap< String , Object>();

        inner.put("id",payment.getId());
        inner.put("serial",payment.getSerial());

        if (payment != null) {
            jsonMap.put("code",200);
            jsonMap.put("message","查询成功！serverPort:"+serverPort);
            jsonMap.put("data",inner);
            System.out.println("js:"+jsonMap);
            return JSON.toJSONString(jsonMap,true);
        }else{
            jsonMap.put("code",444);
            jsonMap.put("message","没有对应记录，查询ID："+id);
            jsonMap.put("data",null);
            System.out.println("js:"+jsonMap);
            return JSON.toJSONString(jsonMap,true);
        }

    }

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("serverport:"+serverPort);
        return serverPort;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }

    @GetMapping(value = "/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String element :
                services) {
        //    Log.info("********element:" +element);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance :
                instances) {
       //     Log.info(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
        }
        return this.discoveryClient;
    }
}
