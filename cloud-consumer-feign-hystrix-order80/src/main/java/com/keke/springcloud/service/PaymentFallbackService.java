package com.keke.springcloud.service;


import org.springframework.stereotype.Component;

@Component
public class PaymentFallbackService implements PaymentHystrixService {
    @Override
    public String paymentInfo_OK(Integer id) {
        return "From PaymentFallbackService and paymentInfo_OK!";
    }

    @Override
    public String paymentInfo_Timeout(Integer id) {
        return "From PaymentFallbackService and paymentInfo_Timeout~";
    }
}
