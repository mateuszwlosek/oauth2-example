package com.github.mateuszwlosek.oauth2.resourceservice;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "resourceServer", url = "${application.resource-server.url}")
public interface ResourceServer {

    @GetMapping("/resources/test")
    String request(@RequestHeader("Authorization") String token);
}
