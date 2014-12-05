package allout58.mods.bigfactories.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Created by James Hollowell on 7/31/2014.
 */
public class LogHelper
{
    private static Logger log;

    public static void init(Logger log)
    {
        LogHelper.log = log;
    }

    public static void log(Level level, String msg)
    {
        log.log(level, msg);
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
