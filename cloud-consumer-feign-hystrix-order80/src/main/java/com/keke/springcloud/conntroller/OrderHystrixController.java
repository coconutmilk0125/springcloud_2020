package com.keke.springcloud.conntroller;


import com.keke.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystrixController {

    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping(value = "/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){
        String result =  paymentHystrixService.paymentInfo_OK(id);
        return result;
    }

    @GetMapping(value = "/consumer/payment/hystrix/timeout/{id}")
 //   @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler",commandProperties = {
 //           @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1500")
   // })
    @HystrixCommand
    public String paymentInfo_Timeout(Integer id){
        int age = 10/0;
        String result =  paymentHystrixService.paymentInfo_Timeout(id);
        return result;
    }

    public String paymentInfo_TimeoutHandler(Integer id){
        return "系统繁忙或者运行报错，请稍后再试！";
    }

    public String payment_Global_FallbackMethod(){
        return "Global异常处理信息，请稍后再试！";
    }


}
