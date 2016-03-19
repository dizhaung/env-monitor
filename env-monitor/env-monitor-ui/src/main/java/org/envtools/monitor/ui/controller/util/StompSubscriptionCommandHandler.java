package org.envtools.monitor.ui.controller.util;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.model.messaging.ResponsePayload;
import org.envtools.monitor.module.ModuleConstants;
import org.envtools.monitor.module.core.cache.ApplicationsDataPushService;
import org.envtools.monitor.module.core.cache.ApplicationsModuleStorageService;
import org.envtools.monitor.module.core.selection.DestinationUtil;
import org.envtools.monitor.module.core.subscription.SubscriptionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created: 12/11/15 1:12 PM
 *
 * @author Yury Yakovlev
 */
@Component
public class StompSubscriptionCommandHandler {

    private static final Logger LOGGER = Logger.getLogger(StompSubscriptionCommandHandler.class);

    @Autowired
    private ApplicationsDataPushService applicationsDataPushService;

    @Autowired
    ApplicationsModuleStorageService applicationsModuleStorageService;

    @Autowired
    SubscriptionManager subscriptionManager;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

    public static boolean isSubscriptionCommand(StompCommand stompCommand) {
        return isSubscribe(stompCommand) || isUnSubscribe(stompCommand);
    }

    public static boolean isSubscribe(StompCommand stompCommand) {
        return stompCommand.equals(StompCommand.SUBSCRIBE);
    }

    public static boolean isUnSubscribe(StompCommand stompCommand) {
        return stompCommand.equals(StompCommand.UNSUBSCRIBE);
    }

    public void processSubscriptionCommand(StompCommand stompCommand, StompHeaderAccessor stompHeaderAccessor) {

        //This is unique identifier of browser websocket session
        String sessionId = stompHeaderAccessor.getSessionId();

        //This is unique identifier for single websocket subscription
        String stompSubscriptionId = stompHeaderAccessor.getSubscriptionId();

        //Destination
        String destination = stompHeaderAccessor.getDestination();

        LOGGER.info(String.format(
                "StompSubscriptionCommandHandler.processSubscriptionCommand - %s - session %s with stomp subscription id %s for destination %s",
                stompCommand,
                sessionId,
                stompSubscriptionId,
                destination));

        if (DestinationUtil.isDestinationForModule(destination, ModuleConstants.APPLICATIONS_MODULE_ID)) {

            String selector = DestinationUtil.extractSelector(destination);
            LOGGER.info(String.format(
                    "StompSubscriptionCommandHandler.processSubscriptionCommand - selector =  %s", selector));

            if (isSubscribe(stompCommand)) {
                subscriptionManager.registerSubscription(sessionId, destination);

                //TODO: rework to run instantly after subscription has been internally processed (in the same thread)!

                //Send initial data immediately after subscription has been processed (how to do it more gracefully? )
                executor.schedule(() -> {
                    sendDataToSubscriberImmediately(destination);
                }, 100, TimeUnit.MILLISECONDS);


            } else if (isUnSubscribe(stompCommand)) {
                subscriptionManager.unregisterSubscription(sessionId, destination);
            }

        } else {
            LOGGER.warn("StompSubscriptionCommandHandler.handleSubscription - destination module not specified or not supported in destination " + destination);
        }
    }

    private void sendDataToSubscriberImmediately(String subscribedDestination) {

        String contentPart = applicationsModuleStorageService.extractPartBySelector(
                DestinationUtil.extractSelector(subscribedDestination));

        applicationsDataPushService.pushToSubscribedClient(subscribedDestination, contentPart);

    }

    @PreDestroy
    public void destroy() {
        executor.shutdown();
    }
}
