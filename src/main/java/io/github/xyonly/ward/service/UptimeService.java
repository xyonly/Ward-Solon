package io.github.xyonly.ward.service;

import io.github.xyonly.ward.dao.UptimeDto;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import oshi.SystemInfo;

@Component()
public class UptimeService
{
    @Inject
    private SystemInfo systemInfo;

    /**
     * Gets uptime information
     *
     * @return UptimeDto with filled fields
     */
    public UptimeDto getUptime()
    {
        UptimeDto uptimeDto = new UptimeDto();

        long uptimeInSeconds = systemInfo.getOperatingSystem().getSystemUptime();

        uptimeDto.setDays(String.format("%02d", (int) uptimeInSeconds / 86400));
        uptimeDto.setHours(String.format("%02d", (int) (uptimeInSeconds % 86400) / 3600));
        uptimeDto.setMinutes(String.format("%02d", (int) (uptimeInSeconds / 60) % 60));
        uptimeDto.setSeconds(String.format("%02d", (int) uptimeInSeconds % 60));

        return uptimeDto;
    }
}