package com.jmd.cafe.order.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@EnableFeignClients(basePackages = "com.jmd.cafe.order.fiegn")
@Configuration
public class FeignConfiguration implements Jackson2ObjectMapperBuilderCustomizer {

    /**
     * requestHeader 공통부분 세팅가능
     * */
    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> {
            requestTemplate.query("default-value","haha");
            requestTemplate.query("event-specific-value","hahaha");
        };
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        builder
//                .featuresToEnable(JsonInclude.Include.NON_NULL)
                .featuresToEnable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * feign 로그레벨설정
     * */
    @Bean
    Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }



    /**
     * Get요청 시 날짜에 대한 부분 인코딩관련 해결
     * */
    @Bean
    public FeignFormatterRegistrar localDataFormatterRegistrar(){
        return formatterRegistry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(true);
            registrar.registerFormatters(formatterRegistry);
        };
    }

    /**
     * Retry 설정 : default 설정을 사용하게 됨.
     * */
    @Bean
    public Retryer retryer(){
        return new Retryer.Default();
    }
}
