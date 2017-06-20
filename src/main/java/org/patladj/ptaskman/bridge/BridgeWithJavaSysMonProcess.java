package org.patladj.ptaskman.bridge;

import com.jezhumble.javasysmon.CpuTimes;
import org.patladj.ptaskman.model.Process;

/**
 * Created by PatlaDJ on 20.6.2017 г..
 * We use this class from the implementation of PsLibPTaskmanagerBridge for JavaSysMon.
 * We need it only to store behind the interface - the value of the current/previous CpuTimes object.
 * We only need this to measure the relative CPU usage for a process
 */
public class BridgeWithJavaSysMonProcess extends Process {

    /**
     * Contains the previou/current CPU times so we can get the new one when passing the previous to the new one
     * It is used for calculating how much CPU percentage of the global CPU this process is taking.
     */
    public CpuTimes currPrevCpuTimesSnapshot =null;
}
