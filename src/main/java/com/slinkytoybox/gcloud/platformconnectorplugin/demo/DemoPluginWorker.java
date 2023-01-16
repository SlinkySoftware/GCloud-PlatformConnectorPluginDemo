/*
 *   platformconnectorplugindemo - DemoPluginWorker.java
 *
 *   Copyright (c) 2022-2023, Slinky Software
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as
 *   published by the Free Software Foundation, either version 3 of the
 *   License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   A copy of the GNU Affero General Public License is located in the 
 *   AGPL-3.0.md supplied with the source code.
 *
 */
package com.slinkytoybox.gcloud.platformconnectorplugin.demo;

import com.slinkytoybox.gcloud.platformconnectorplugin.PlatformConnectorPlugin;
import com.slinkytoybox.gcloud.platformconnectorplugin.PluginOperation;
import com.slinkytoybox.gcloud.platformconnectorplugin.health.HealthMetric;
import com.slinkytoybox.gcloud.platformconnectorplugin.health.HealthResult;
import com.slinkytoybox.gcloud.platformconnectorplugin.health.HealthState;
import com.slinkytoybox.gcloud.platformconnectorplugin.health.HealthStatus;
import com.slinkytoybox.gcloud.platformconnectorplugin.request.*;
import com.slinkytoybox.gcloud.platformconnectorplugin.response.*;
import com.slinkytoybox.gcloud.platformconnectorplugin.response.PluginResponse.ResponseStatus;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * This class is the main worker for the plugin. It must implement the
 * PlatformConnectorPlugin interface which defines the tasks that can be called
 * from within the main application. The class is instantiated when the plugin
 * is started, and destroyed when the plugin is stopped. 6 functions need to be
 * customised, pluginSetup, pluginDestroy, and the doWork routines with four
 * different signatures.
 *
 *
 * @author Michael Junek (michael@juneks.com.au)
 *
 */
@Slf4j
@Component
public class DemoPluginWorker implements PlatformConnectorPlugin {

    private final Properties config;
    private final String pluginId;
    private final String pluginDescription;

    private final List<PluginOperation> supportedOperations = new ArrayList<>();

    // Custom Setup Routine
    private void pluginSetup() {
        final String logPrefix = "pluginSetup() - ";
        log.trace("{}Entering Method", logPrefix);

        // TODO: Define which operations are supported by the plugin. Comment out ones not applicable
        supportedOperations.add(PluginOperation.CREATE);
        supportedOperations.add(PluginOperation.READ);
        supportedOperations.add(PluginOperation.UPDATE);
        supportedOperations.add(PluginOperation.DELETE);

        // TODO: Any Setup work in here
    }

    // Custom destruction routine
    private void pluginDestroy() {
        final String logPrefix = "pluginDestroy() - ";
        log.trace("{}Entering Method", logPrefix);
        // TODO: Any Setup work in here

    }

    /// Note: When implementing the below methods, you need to leave the function
    /// shell for any function that the plugin will NOT support. It can simply just
    /// be a 'return null;' 
    /// The code will not be called due to the checks done in getResponseFromRequest 
    /// at the bottom of this file.
    /**
     * Method for Creating a new object
     *
     * @param req
     * @return
     */
    private CreateResponse doWork(CreateRequest req) {
        final String logPrefix = "doWork(CreateRequest) - ";
        log.trace("{}Entering Method", logPrefix);
        log.info("{}Issuing create request for new record", logPrefix);
        CreateResponse response = new CreateResponse(); // Create new response object

        // TODO: Modify code here to do the actual work
        Map<String, Serializable> objectDataMap = new HashMap<>();
        objectDataMap.put("Field1", "Value1");
        objectDataMap.put("Field2", 2);
        objectDataMap.put("Field3", OffsetDateTime.now().toString());
        objectDataMap.put("Field4", "Value4");
        objectDataMap.put("Field5", true);
        objectDataMap.putAll(req.getObjectDetails());
        response.setObjectDetails(objectDataMap)
                .setRequestId(req.getRequestId())
                .setStatus(ResponseStatus.SUCCESS)
                .setObjectId("DemoCreateObjectId");

        // END actual work code
        log.debug("{}Returning response: {}", logPrefix, response);
        return response;

    }

    /**
     * Method for Updating an existing object
     *
     * @param req
     * @return
     */
    private UpdateResponse doWork(UpdateRequest req) {
        final String logPrefix = "doWork(UpdateRequest) - ";
        log.trace("{}Entering Method", logPrefix);
        log.info("{}Issuing update request for record {}", logPrefix, req.getObjectId());
        UpdateResponse response = new UpdateResponse(); // Create new response object

        // TODO: Modify code here to do the actual work
        Map<String, Serializable> objectDataMap = new HashMap<>();
        objectDataMap.put("Field1", "Value1");
        objectDataMap.put("Field2", 2);
        objectDataMap.put("Field3", OffsetDateTime.now().toString());
        objectDataMap.put("Field4", "Value4");
        objectDataMap.put("Field5", true);

        response.setObjectDetails(objectDataMap)
                .setRequestId(req.getRequestId())
                .setStatus(ResponseStatus.SUCCESS)
                .setObjectId(req.getObjectId());

        // END actual work code
        log.debug("{}Returning response: {}", logPrefix, response);
        return response;

    }

    /**
     * Method for searching for and reading an object
     *
     * @param req
     * @return
     */
    private ReadResponse doWork(ReadRequest req) {
        final String logPrefix = "doWork(ReadRequest) - ";
        log.trace("{}Entering Method", logPrefix);

        ReadResponse response = new ReadResponse(); // Create new response object
        response.setRequestId(req.getRequestId());

        // TODO: Modify code here to do the actual work
        if (req.getObjectId() == null || req.getObjectId().isEmpty()) {
            log.info("{}Issuing read request with search parameters: {}", logPrefix, req.getSearchParameters());
            response.setObjectId("DemoReadObjectId");
        }
        else {
            log.info("{}Issuing read request for record {}", logPrefix, req.getObjectId());
            response.setObjectId(req.getObjectId());
            if (req.getObjectId().equalsIgnoreCase("notfound")) {
                log.warn("{}Faking record not found error", logPrefix);
                response.setErrorMessage("Record was not found");
                response.setStatus(ResponseStatus.RECORD_NOT_FOUND);
                return response;
            }
        }

        Map<String, Serializable> objectDataMap = new HashMap<>();
        objectDataMap.put("Field1", "Value1");
        objectDataMap.put("Field2", 2);
        objectDataMap.put("Field3", OffsetDateTime.now().toString());
        objectDataMap.put("Field4", "Value4");
        objectDataMap.put("Field5", true);

        response.setObjectDetails(objectDataMap)
                .setStatus(ResponseStatus.SUCCESS);

        // END actual work code
        log.debug("{}Returning response: {}", logPrefix, response);
        return response;

    }

    /**
     * Method for Deleting an object
     *
     * @param req
     * @return
     */
    private DeleteResponse doWork(DeleteRequest req) {
        final String logPrefix = "doWork(DeleteRequest) - [" + req.getRequestId() + "]";
        log.trace("{}Entering Method", logPrefix);
        log.info("{}Issuing delete request for record {}", logPrefix, req.getObjectId());
        DeleteResponse response = new DeleteResponse(); // Create new response object

        // TODO: Modify code here to do the actual work
        response.setRequestId(req.getRequestId())
                .setStatus(ResponseStatus.SUCCESS)
                .setObjectId(req.getObjectId());

        // END actual work code
        log.debug("{}Returning response: {}", logPrefix, response);
        return response;

    }

    @Override
    public HealthResult getPluginHealth() {
        final String logPrefix = "getPluginHealth() - ";
        log.trace("{}Entering Method", logPrefix);
        log.info("{}Getting plugin health", logPrefix);

        // TODO: Modify code here to do the actual health checks and create metrics etc.
        Map<String, HealthStatus> componentStatus = new HashMap<>();
        componentStatus.put("Component1", new HealthStatus().setHealthState(HealthState.HEALTHY));
        componentStatus.put("Component2", new HealthStatus().setHealthState(HealthState.FAILED).setHealthComment("Database connection down"));
        componentStatus.put("Component3", new HealthStatus().setHealthState(HealthState.WARNING).setHealthComment("Connected to backup API instance"));

        List<HealthMetric> metrics = new ArrayList<>();
        metrics.add(new HealthMetric().setMetricName("responseTime").setMetricValue(100));
        metrics.add(new HealthMetric().setMetricName("SomeStringMetric").setMetricValue("string"));
        metrics.add(new HealthMetric().setMetricName("dateTimeMetric").setMetricValue(OffsetDateTime.now()));

        HealthResult response = new HealthResult()
                .setOverallStatus(new HealthStatus().setHealthState(HealthState.HEALTHY)) // this is the most important thing to return
                .setComponentStatus(componentStatus) // component statuses are optional
                .setMetrics(metrics);                   // metrics are optional

        // END actual work code
        log.debug("{}Returning response: {}", logPrefix, response);
        return response;

    }

    /* 
////////
//////// NO NEED TO MODIFY ANYTHING DOWN HERE
////////
     */
    // Default CTOR called by instantiator
    public DemoPluginWorker(String pluginId, String pluginDescription, Properties config) {
        this.config = config;
        this.pluginDescription = pluginDescription;
        this.pluginId = pluginId;
    }

    // Initial post-construction routine - don't need to modify this, it calls the custom one
    @PostConstruct
    private void setup() {
        final String logPrefix = "setup() - ";
        log.trace("{}Entering Method", logPrefix);
        log.info("----------------------------------------------------------------------------");
        log.info("{}Startup tasks for plugin running", logPrefix);
        log.info("Plugin Id: {}", pluginId);
        log.info("Decription: {}", pluginDescription);
        log.info("----------------------------------------------------------------------------");
        log.info("{}Configuration", logPrefix);
        log.info("{}", config);
        log.info("----------------------------------------------------------------------------");
        pluginSetup();

        log.trace("{}Leaving Method", logPrefix);
    }

    // Initial pre-destruction routine - don't need to modify this, it calls the custom one
    @PreDestroy
    private void destroy() {
        final String logPrefix = "destroy() - ";
        log.trace("{}Entering Method", logPrefix);
        log.info("{}Shutdown tasks for plugin running", logPrefix);
        pluginDestroy();
        log.trace("{}Leaving Method", logPrefix);

    }

    // Default routine to break the incoming request into four different work types depending on class instance
    @Override
    public PluginResponse getResponseFromRequest(PluginRequest request) {
        final String logPrefix = "getResponseFromRequest() - ";
        log.trace("{}Entering Method", logPrefix);
        log.debug("{}Determining request type from class type: {}", logPrefix, request.getClass().getName());

        if (request instanceof CreateRequest createRequest) {
            log.debug("{} - Create Request", logPrefix);
            if (supportedOperations.contains(PluginOperation.CREATE)) {
                return doWork(createRequest);
            }
            else {
                log.error("{}Create requests are not supported by this plugin", logPrefix);
                throw new UnsupportedOperationException("Create requests are not supported by this plugin");
            }
        }
        if (request instanceof UpdateRequest updateRequest) {
            log.debug("{} - Update Request", logPrefix);
            if (supportedOperations.contains(PluginOperation.UPDATE)) {
                return doWork(updateRequest);
            }
            else {
                log.error("{}Update requests are not supported by this plugin", logPrefix);
                throw new UnsupportedOperationException("Update requests are not supported by this plugin");
            }
        }
        if (request instanceof ReadRequest readRequest) {
            log.debug("{} - Read Request", logPrefix);
            if (supportedOperations.contains(PluginOperation.READ)) {
                return doWork(readRequest);
            }
            else {
                log.error("{}Read requests are not supported by this plugin", logPrefix);
                throw new UnsupportedOperationException("Read requests are not supported by this plugin");
            }
        }
        if (request instanceof DeleteRequest deleteRequest) {
            log.debug("{} - Delete Request", logPrefix);
            if (supportedOperations.contains(PluginOperation.DELETE)) {
                return doWork(deleteRequest);
            }
            else {
                log.error("{}Delete requests are not supported by this plugin", logPrefix);
                throw new UnsupportedOperationException("Delete requests are not supported by this plugin");
            }
        }

        log.error("{}Request class type not implemented", logPrefix);
        throw new UnsupportedOperationException("Request class type not implemented");
    }

    @Override
    public List<PluginOperation> getValidOperations() {
        return supportedOperations;
    }

}
