package ru.nomokonov.ddns.service;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.nomokonov.ddns.model.SubDomain;
import ru.nomokonov.ddns.model.YandexResponse;
import ru.nomokonov.ddns.model.YandexSetResponse;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@PropertySource(value = "classpath:yandexApi.properties", encoding = "UTF-8")
public class YandexDnsImpl implements YandexDns {

    private Environment env;

    public YandexDnsImpl(Environment env) {
        this.env = env;
    }

    public String setNewIp(String inetAddressFromRequest, String inetAddressFromParam, String inetAddressHostname) {
        InetAddress ipFromRequest = null;
        InetAddress ipFromParam = null;
        InetAddress ipHostname = null;
        try {
            ipFromRequest = InetAddress.getByName(inetAddressFromRequest);
            ipFromParam = InetAddress.getByName(inetAddressFromParam);
            ipHostname = InetAddress.getByName(inetAddressHostname);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "server error = bad param or ip not resolve";
        }
//проверка с какого IP запрос прилетел и соответствует
//        if (ipFromRequest.equals(ipFromParam)) {
        if (!ipHostname.equals(ipFromParam)) {
            SubDomain subDomain = getSubDomainList();
            return setNewIpForDomain(subDomain.getRecord_id(), ipFromParam);
        }
        return "no cnahge ip";
//        }
//        return "server error";
    }

    private String setNewIpForDomain(Long record_id, InetAddress ipFromParam) {
        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("PddToken", env.getProperty("yandex.PddToken"));
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("domain", env.getProperty("yandex.domain"));
        parameters.add("record_id", record_id.toString());
        parameters.add("subdomain", env.getProperty("yandex.subdomain"));
        parameters.add("content", ipFromParam.getHostAddress());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(parameters, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<YandexSetResponse> response = restTemplate.postForEntity(env.getProperty("yandex.url_set_subdomain"), //
                entity, YandexSetResponse.class);

        YandexSetResponse result = response.getBody();
        if (result.getSuccess().equals("ok")) {
            return "set new ip:" + result.getRecord().getContent();
        }
        return "error set new ip in Yandex";
    }

    private SubDomain getSubDomainList() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("PddToken", env.getProperty("yandex.PddToken"));
        HttpEntity<YandexResponse> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<YandexResponse> response = restTemplate.exchange(env.getProperty("yandex.url_get_subdomain") + env.getProperty("yandex.domain"), //
                HttpMethod.GET, entity, YandexResponse.class);

        YandexResponse result = response.getBody();
        if (result.getSuccess().equals("ok")) {
            List<SubDomain> subDomains = result.getRecords().stream().filter(
                    x -> x.getSubdomain().equals(env.getProperty("yandex.subdomain"))).collect(Collectors.toList()
            );
            if (subDomains.size() > 0) {

                return subDomains.get(0);
            }
        }
        return null;
    }

}
