/*
 * ParameterReader.java
 * 
 * 14 lug 2022
 */
package com.cucco.stipendi.utility;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>Executive summary - A precise and concise description for the object. Useful to describe groupings of methods and introduce major terms.</p>
 * <p>State Information - Specify the state information associated with the object, described in a manner that decouples the states from the operations that may query or change these states. This should also include whether instances of this class are thread safe. (For multi-state objects, a state diagram may be the clearest way to present this information.) If the class allows only single state instances, such as java.lang.Integer, and for interfaces, this section may be skipped.</p>
 * 
 *
 * @author giovanni -- Auriga S.p.A.
 */
@Singleton(name = ParameterReader.PARAMETER_READER_EJB_NAME)
@Lock(LockType.READ)
public class ParameterReader extends Config {

    /**
     * <p>ConfReader EJB name</p>
     */
    public static final String PARAMETER_READER_EJB_NAME = "ParameterReader";

    /**
     * <p>The property file from which read configuration parameters</p>
     */
    private File propertyFile;

    /**
     * <p>Configuration file reader intertime, default 1 minutes</p>
     */
    private long lastIntertime = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);

    /**
     * <p>Timer instance, used to change, at runtime, configuration file refresh time</p>
     */
    private Timer timer = null;

    /**
     * <p>Logger instance</p>
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * <p>TimerService instance, used to schedule configuration file reading timer</p>
     */
    @Resource
    private TimerService timerService;

    /**
     * <p>The last read properties</p>
     */
    private Properties lastReadedProperties = new Properties();

    /**
     * <p>Initialize the reading timer with a refresh rate of 1 minute</p>
     *
     */
    @PostConstruct
    private void initialize() {
        timer = timerService.createTimer(new Date(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES), "PropertyReader");
    }

    @PreDestroy
    private void destroy() {
        logger.info("destroying ConfReader instance, releasing resources");
        /*
         * cancello tutti i timer collegati al timer service
         */
        timerService.getTimers().forEach(Timer::cancel);
    }

    /**
     * Set propertyFile value or reference.
     *
     * @param propertyFile Value to set.
     */
    public void setPropertyFile(File propertyFile) {
        this.propertyFile = propertyFile;
        readConf();
    }

    @Lock(LockType.WRITE)
    @Timeout
    public void readConf() {
        logger.trace(">>>Start conf file reading: [{}]<<<", propertyFile);
        if (propertyFile != null && propertyFile.exists()) {
            try (FileReader fileReader = new FileReader(propertyFile)) {
                lastReadedProperties.clear();
                lastReadedProperties.load(fileReader);
                this.setConf(lastReadedProperties);
                refreshTimer();
            } catch (IOException e) {
                logger.error("Exception during lastReadedProperties reading", e);
            }
        } else {
            logger.error("Cannot find configuration file: [{}]", propertyFile);
        }
    }

    /**
     * <p>This method checks for readerRefreshIntertime parameter presence into configuration file.
     * if this parameter is present, then, it's value is used as interval time between configuration file readings operations </p>
     *
     */
    private void refreshTimer() {
        long readerRefreshIntertime = this.get("readerRefreshIntertime", 60000L);
        if (readerRefreshIntertime != lastIntertime) {
            lastIntertime = readerRefreshIntertime;
            timerService.getTimers().forEach(Timer::cancel);
            timer = timerService.createTimer(new Date(), lastIntertime, "PropertyReader");
            logger.trace("ConfReader refresh intertime changed to [{}] ms", Long.valueOf(lastIntertime));
        }
        logger.trace("ConfReader next scheduled execution time: [{}]", timer.getNextTimeout());
    }

    /**
     * Return lastReadedProperties value or reference.
     *
     * @return lastReadedProperties value or reference.
     */
    public Properties getLastReadedProperties() {
        return lastReadedProperties;
    }

}
