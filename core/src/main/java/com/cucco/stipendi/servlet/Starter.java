/*
 * Starter.java
 * 
 * 15 lug 2022
 */
package com.cucco.stipendi.servlet;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import com.cucco.stipendi.utility.Utility;

/**
 * <p>Executive summary - A precise and concise description for the object. Useful to describe groupings of methods and introduce major terms.</p>
 * <p>State Information - Specify the state information associated with the object, described in a manner that decouples the states from the operations that may query or change these states. This should also include whether instances of this class are thread safe. (For multi-state objects, a state diagram may be the clearest way to present this information.) If the class allows only single state instances, such as java.lang.Integer, and for interfaces, this section may be skipped.</p>
 * 
 *
 * @author giovanni -- Auriga S.p.A.
 */
@WebServlet(urlPatterns = "stipendi", loadOnStartup = 1, initParams = {
        @WebInitParam(name = Starter.APP_CONF_FILE_NAME_PARAMETER, value = "stipendi.conf"),
        @WebInitParam(name = Starter.LOG_CONF_FILE_NAME_PARAMETER, value = "stipendi.log4j2.xml"),
})
public class Starter extends HttpServlet {

    /**
     * <p>Serial uid</p>
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>Servlet init parameter used to specify configuration files path. This parameter is optional, if not present the value contained into "configfile.path"
     *  system property will be used as configuration file path</p>
     */
    public static final String CONF_PATH_PARAMETER = "config_path";

    /**
     * <p>Servlet init parameter used to retrieve application conf file name</p>
     */
    public static final String APP_CONF_FILE_NAME_PARAMETER = "config_file_name";

    /**
     * <p>Servlet init parameter used to retrieve application log4j2 configuration file name</p>
     */
    public static final String LOG_CONF_FILE_NAME_PARAMETER = "log_config_file_name";

    /**
     * <p>Configuration default path</p>
     */
    private static final String DEFAULT_PATH = "${configfile.path}";

    private static Logger logger = LogManager.getLogger();

    /**
     * <p>Logger context instance</p>
     */
    private transient LoggerContext loggerContext;

    /**
     * <p>Conf reader singleton instance. This servlet will configure file from which read configuration properties</p>
     */
    //@EJB
    //private ParameterReader parameterReader;

    /**
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException {
        String configPath = getInitParameter(CONF_PATH_PARAMETER);
        String configFileName = getInitParameter(APP_CONF_FILE_NAME_PARAMETER);
        String log4j2FileName = getInitParameter(LOG_CONF_FILE_NAME_PARAMETER);
        System.out.println(">>>STARTING... READING CONFIGURATIONS FILE<<<");
        String confPath = resolvePath(configPath, configFileName);
        String log4jPath = resolvePath(configPath, log4j2FileName);
        System.out.println(String.format("Module conf path: [%s], log4j2 path: [%s]", confPath, log4jPath));
        File log4jConfiguration = new File(log4jPath);
        loggerContext = LoggerContext.getContext(false);
        loggerContext.setConfigLocation(log4jConfiguration.toURI());

        logger.info("Log4j initialized successfully");
        //File confFile = new File(confPath);
        //parameterReader.setPropertyFile(confFile);
        //logger.info("CUCCO parametro:[" + parameterReader.get("test", ""));
        //System.out.println("CUCCO parametro:[" + parameterReader.get("test", ""));
    }

    /**
     * <p>Resolve the path with input: first will be checked for path and file name system property existence, if not then the defaults value
     * will be used to retrieve full file path </p>
     *
     * @param pathParameter - path system property value
     * @param fileName - file name system property
     * @return the resolved path
     */
    private static String resolvePath(String pathParameter, String fileName) {
        String pathProperty = DEFAULT_PATH;
        if (pathParameter != null) {
            pathProperty = pathParameter;
        }
        String normalizedPath = normalizePath(pathProperty);
        return new StringBuilder(normalizedPath).append(fileName).toString();
    }

    /**
     * <p>Normalize the path separator files.</p>
     *
     * @param path - the path to be normalized
     * @return the normalized path
     */
    private static String normalizePath(String path) {
        return Utility.normalizePath(Utility.replaceEnvRefereces(path));
    }

    /**
     * @see javax.servlet.GenericServlet#destroy()
     */
    @Override
    public void destroy() {
        super.destroy();
        if (this.loggerContext != null) {
            this.loggerContext.stop(4, TimeUnit.SECONDS);
        }
    }
}
