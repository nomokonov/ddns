package ru.nomokonov.ddns.service;

public interface YandexDns {
    String setNewIp(String inetAddressFromRequest, String inetAddressFromParam, String inetAddressHostname);
}
