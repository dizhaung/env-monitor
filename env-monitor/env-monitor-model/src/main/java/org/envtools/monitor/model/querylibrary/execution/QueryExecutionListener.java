package org.envtools.monitor.model.querylibrary.execution;

import java.util.UUID;

/**
 * Created: 27.02.16 3:35
 *
 * @author Yury Yakovlev
 */
public interface QueryExecutionListener {

    void onQueryCompleted(UUID uuid, QueryExecutionResult queryResult);

}
