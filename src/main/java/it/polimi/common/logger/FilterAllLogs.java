package it.polimi.common.logger;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public final class FilterAllLogs implements Filter {

    @Override
    public boolean isLoggable(LogRecord record) {
        return false;
    }

}
