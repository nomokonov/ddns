package ru.nomokonov.ddns.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nomokonov.ddns.model.SubDomain;
import ru.nomokonov.ddns.service.YandexDnsImpl;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RequestMapping("/rest/api")
@RestController
public class DdnsController {

    private YandexDnsImpl yandexDns;

    public DdnsController(YandexDnsImpl yandexDns) {
        this.yandexDns = yandexDns;
    }


    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllSurvey(
            HttpServletRequest httpServletRequest, @RequestParam(defaultValue = "hostname") String hostname,
            @RequestParam(defaultValue = "hostname") String myip) {

        String clientIp = httpServletRequest.getRemoteAddr();
        SubDomain subDomain = yandexDns.getSubDomainList();
        String result = yandexDns.setNewIp(clientIp,myip,hostname);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
