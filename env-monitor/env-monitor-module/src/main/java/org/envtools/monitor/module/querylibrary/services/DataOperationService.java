package org.envtools.monitor.module.querylibrary.services;

import javax.transaction.*;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created: 11.04.16 16:02
 *
 * @author Anastasiya Plotnikova
 */
public interface DataOperationService<T> {

    DataOperationResult create(String entity, Map<String, String> fields) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, IntrospectionException, InstantiationException, SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException;

    DataOperationResult update(String entity, T id, Map<String, String> fields);

    DataOperationResult delete(String entity, T id);
}
