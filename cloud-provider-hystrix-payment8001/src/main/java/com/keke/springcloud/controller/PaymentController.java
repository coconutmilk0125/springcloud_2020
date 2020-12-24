package com.keke.springcloud.controller;

import com.keke.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;

@Slf4j
@RestController
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){
        String result = paymentService.paymentInfo_OK(id);

     //   Log.info("*****result:"+result);
        System.out.println("*****result:"+result);
        return result;
    }

    @GetMapping(value = "/payment/circuit/{id}")
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        String result = paymentService.paymentCircuitBreaker(id);
        System.out.println("*********result:"+result);
        return result;
    }

    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
    })
    public String paymentInfo_Timeout(Integer id){
     /*   int timeNumber = 5;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        int age = 10/0;
        return "线程池： "+Thread.currentThread().getName()+"paymentInfo_Timeout,id:    "+id+"\t"+"耗时"+"s～～～";
    }

    public String paymentInfo_TimeoutHandler(Integer id){
        return "线程池： "+Thread.currentThread().getName()+"paymentInfo_TimeoutHandler,id:    "+id+"\t"+"系统繁忙或者运行报错，请稍后再试！";
    }


}
