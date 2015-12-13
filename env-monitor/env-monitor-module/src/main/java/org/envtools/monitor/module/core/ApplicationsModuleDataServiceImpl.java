package org.envtools.monitor.module.core;

import org.apache.log4j.Logger;
import org.envtools.monitor.module.core.selection.Extractor;
import org.envtools.monitor.module.core.selection.SimplePathSelector;
import org.envtools.monitor.module.core.selection.exception.IllegalSelectorException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created: 11/28/15 3:27 AM
 *
 * @author Yury Yakovlev
 */
public class ApplicationsModuleDataServiceImpl implements ApplicationsModuleDataService{

    private static final Logger LOGGER = Logger.getLogger(ApplicationsModuleDataServiceImpl.class);

    private String serializedApplicationsData;

    private Extractor<String, SimplePathSelector> extractor;

    @Override
    public String extractSerializedPartBySelector(String selector) {
        try {
            SimplePathSelector simplePathSelector = SimplePathSelector.of(selector);
            return extractor.extract(serializedApplicationsData, simplePathSelector);
        } catch (IllegalSelectorException e) {
            LOGGER.error("ApplicationsModuleDataServiceImpl.extractSerializedPartBySelector - invalid selector, returning empty result: " +
            selector, e);
            return extractor.emptyExtractionResult();
        }
    }

    @Override
    public void store(String serializedApplicationsData) {

        Lock lock = new ReentrantLock();
        try {
            lock.lock();
            this.serializedApplicationsData = serializedApplicationsData;
        } finally {
            lock.unlock();
        }
    }

    public void setExtractor(Extractor<String, SimplePathSelector> extractor) {
        this.extractor = extractor;
    }
}
