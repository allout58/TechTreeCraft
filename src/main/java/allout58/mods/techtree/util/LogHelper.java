package allout58.mods.techtree.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * Created by James Hollowell on 12/5/2014.
 */
public class LogHelper
{
    public static final Marker PREINIT = MarkerManager.getMarker("PreInit");
    public static final Marker INIT = MarkerManager.getMarker("Init");

    public static Logger logger;

    public static void init(Logger log)
    {
        LogHelper.logger = log;
    }

    public static void log(Level level, String msg)
    {
        logger.log(level, msg);
    }

    public static void fatal(String msg, Object... params)
    {
        log(Level.FATAL, String.format(msg, params));
    }

    public static void error(String msg, Object... params)
    {
        log(Level.ERROR, String.format(msg, params));
    }

    public static void info(String msg, Object... params)
    {
        log(Level.INFO, String.format(msg, params));
    }

}
