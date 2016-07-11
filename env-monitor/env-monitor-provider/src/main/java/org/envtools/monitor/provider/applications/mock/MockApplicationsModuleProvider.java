package org.envtools.monitor.provider.applications.mock;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.applications.ApplicationsData;
import org.envtools.monitor.model.applications.ApplicationsModuleProvider;
import org.envtools.monitor.model.applications.update.UpdateNotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created: 10/31/15 1:49 AM
 *
 * @author Yury Yakovlev
 */
//@Component
//@Profile("mock")
//@Repository

public class MockApplicationsModuleProvider implements ApplicationsModuleProvider {

    private static final Logger LOGGER = Logger.getLogger(MockApplicationsModuleProvider.class);

    private ApplicationsData data = new ApplicationsData();

    private UpdateNotificationHandler handler;
    @PersistenceContext
    protected EntityManager em;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MemoryDataProvider memoryDataProvider;

    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager transactionManager;

    @Override
    public void initialize(UpdateNotificationHandler handler) {
        LOGGER.info("MockApplicationsModuleProvider.initialize - populating data model...");
        this.handler = handler;
        updateFreeMemory();
        updateMockApplicationsModel();

    }


    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    protected void updateFreeMemory() {
        Long newFreeMemory = memoryDataProvider.getFreeMemory();
        boolean sendUpdate = false;
        if (!newFreeMemory.equals(data.getFreeMemory())) {
            synchronized (data) {
                if (newFreeMemory != data.getFreeMemory()) {
                    data.setFreeMemory(newFreeMemory);
                    sendUpdate = true;
                }
            }
        }

        if (sendUpdate) {
            if (handler != null) {
                handler.sendUpdateNotification();
            } else {
                LOGGER.warn("MockApplicationsModuleProvider.updateFreeMemory - handler not initialized!");

            }
        }
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 10000)
    protected void updateMockApplicationsModel() {
        synchronized (data) {
            data.setPlatforms(MockApplicationsDataCreator.createPlatforms());
        }
        if (handler != null) {
            handler.sendUpdateNotification();
        } else {
            LOGGER.warn("MockApplicationsModuleProvider.updateMockApplicationsModel - handler not initialized!");
        }
    }

    @Override
    public ApplicationsData getApplicationsData() {
        return data;
    }

}
