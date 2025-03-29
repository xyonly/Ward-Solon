package io.github.xyonly.ward.service;

import io.github.xyonly.ward.Ward;
import io.github.xyonly.ward.dao.UsageDto;
import io.github.xyonly.ward.exception.ApplicationNotConfiguredException;
import org.noear.solon.annotation.Component;

import org.noear.solon.annotation.Inject;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.util.Util;

import java.util.Arrays;

/**
 * UsageService provides principal information of processor, RAM and storage usage to rest controller
 *
 * @author Rudolf Barbu
 * @version 1.0.3
 */
@Component
public class UsageService
{
    /**
     * Inject SystemInfo object
     * Used for getting usage information
     */
    @Inject
    private SystemInfo systemInfo;

    /**
     * Gets processor usage
     *
     * @return int that display processor usage
     */
    private int getProcessor() {
        CentralProcessor centralProcessor = systemInfo.getHardware().getProcessor();
        long[] prevTicksArray = centralProcessor.getSystemCpuLoadTicks();
        long prevTotalTicks = Arrays.stream(prevTicksArray).sum();
        long prevIdleTicks = prevTicksArray[CentralProcessor.TickType.IDLE.getIndex()];

        Util.sleep(1000);

        long[] currTicksArray = centralProcessor.getSystemCpuLoadTicks();
        long currTotalTicks = Arrays.stream(currTicksArray).sum();
        long currIdleTicks = currTicksArray[CentralProcessor.TickType.IDLE.getIndex()];

        long idleTicksDelta = currIdleTicks - prevIdleTicks;
        long totalTicksDelta = currTotalTicks - prevTotalTicks;

        // Handle possible division by zero
        if (totalTicksDelta == 0) {
            return 0; // or handle in a way suitable for your application
        }

        // Calculate CPU usage percentage
        return (int) ((1 - (double) idleTicksDelta / totalTicksDelta) * 100);
    }

    /**
     * Gets ram usage
     *
     * @return int that display ram usage
     */
    private int getRam() {
        GlobalMemory globalMemory = systemInfo.getHardware().getMemory();
        long totalMemory = globalMemory.getTotal();       // 总内存（字节）
        long availableMemory = globalMemory.getAvailable(); // 可用内存（字节）

        // 处理 totalMemory = 0 的情况，避免除零异常
        if (totalMemory <= 0) {
            return 0;
        }

        // 使用 double 进行计算，防止精度丢失
        double usage = 100.0 * (1.0 - (double) availableMemory / totalMemory);

        return (int) Math.round(usage); // 四舍五入返回整数
    }

    /**
     * Gets storage usage
     *
     * @return int that display storage usage
     */
    private int getStorage() {
        FileSystem fileSystem = systemInfo.getOperatingSystem().getFileSystem();

        // Calculate total storage and free storage for all drives
        long totalStorage = 0;
        long freeStorage = 0;
        for (OSFileStore fileStore : fileSystem.getFileStores()) {
            totalStorage += fileStore.getTotalSpace();
            freeStorage += fileStore.getFreeSpace();
        }

        // Handle possible division by zero
        if (totalStorage == 0) {
            return 0; // or handle in a way suitable for your application
        }

        // Calculate total storage usage percentage for all drives
        return (int) Math.round(((double) (totalStorage - freeStorage) / totalStorage) * 100);
    }

    /**
     * Used to deliver dto to corresponding controller
     *
     * @return ResponseEntityWrapperAsset filled with usageDto
     */
    public UsageDto getUsage() throws ApplicationNotConfiguredException
    {
        if (!Ward.isFirstLaunch())
        {
            UsageDto usageDto = new UsageDto();

            usageDto.setProcessor(getProcessor());
            usageDto.setRam(getRam());
            usageDto.setStorage(getStorage());

            return usageDto;
        }
        else
        {
            throw new ApplicationNotConfiguredException();
        }
    }
}