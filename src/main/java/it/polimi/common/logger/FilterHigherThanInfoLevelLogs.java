package it.polimi.common.logger;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public final class FilterHigherThanInfoLevelLogs implements Filter {

    @Override
    public boolean isLoggable(LogRecord record) {
        if(isLevelHigherThanInfo(record)) return false;
        return true;
    }
    
    /**
     * controlla se level del record è WARNING
     * @param record
     * @return
     */
    private static boolean isLevelWarning(LogRecord record){
        return record.getLevel().equals(Level.WARNING);
    }
    
    /**
     * controlla se level del record è SEVERE
     * @param record
     * @return
     */
    private static boolean isLevelSevere(LogRecord record){
        return record.getLevel().equals(Level.SEVERE);
    }
    
    private static boolean isLevelHigherThanInfo(LogRecord record){
        return isLevelWarning(record) || isLevelSevere(record);
    }

}
