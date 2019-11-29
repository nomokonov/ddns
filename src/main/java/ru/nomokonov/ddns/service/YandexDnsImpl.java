package ru.nomokonov.ddns.service;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nomokonov.ddns.model.SubDomain;
import ru.nomokonov.ddns.model.YandexResponse;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PropertySource(value = "classpath:yandexApi.properties", encoding = "UTF-8")
public class YandexDnsImpl implements YandexDns {

    private Environment env;

    public YandexDnsImpl(Environment env) {
        this.env = env;
    }

    public String setNewIp(String inetAddressFromRequest, String inetAddressFromParam, String inetAddressHostname) throws UnknownHostException {
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
        if (ipFromRequest.equals(ipFromParam)) {
            if (!ipHostname.equals(ipFromParam)) {
                SubDomain subDomain = getSubDomainList();
                InetAddress ipFromDNS = InetAddress.getByName(subDomain.getContent());
//                if change
                if (!ipFromDNS.equals(ipFromParam)) {

                    return "";
                }
            }
            return "no cnahge ip";
        }

        return "server error";
    }

    public SubDomain getSubDomainList() {

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
