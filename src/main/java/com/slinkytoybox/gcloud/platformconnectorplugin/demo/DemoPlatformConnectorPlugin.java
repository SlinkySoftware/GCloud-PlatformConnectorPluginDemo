/*
 *   platformconnectorplugindemo - DemoPlatformConnectorPlugin.java
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
import com.slinkytoybox.gcloud.platformconnectorplugin.request.*;
import com.slinkytoybox.gcloud.platformconnectorplugin.response.*;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Michael Junek (michael@juneks.com.au)
 */
@Slf4j
public class DemoPlatformConnectorPlugin implements PlatformConnectorPlugin {

    @Override
    public PluginResponse getResponseFromRequest(PluginRequest request) {
        final String logPrefix = "getResponseFromRequest() - ";
        log.trace("{}Entering Method", logPrefix);
        log.debug("{}Determining request type from class: {}", logPrefix, request.getClass().getName());

        if (request.getClass().isAssignableFrom(CreateRequest.class)) {
            log.debug("{} - Create Request", logPrefix);
            return doWork((CreateRequest)request);
        }
        if (request.getClass().isAssignableFrom(UpdateRequest.class)) {
            log.debug("{} - Create Request", logPrefix);
            return doWork((UpdateRequest)request);
        }
        if (request.getClass().isAssignableFrom(ReadRequest.class)) {
            log.debug("{} - Create Request", logPrefix);
            return doWork((ReadRequest)request);
        }
        if (request.getClass().isAssignableFrom(DeleteRequest.class)) {
            log.debug("{} - Create Request", logPrefix);
            return doWork((DeleteRequest)request);
        }
        
        log.error("{}Request class type not implemented", logPrefix);
        throw new IllegalArgumentException("Request class type not implemented");
    }
    
    private CreateResponse doWork(CreateRequest req) {
        return null;
        
    }
    
    private UpdateResponse doWork(UpdateRequest req) {
        return null;
        
    }

    private ReadResponse doWork(ReadRequest req) {
        return null;
        
    }

    private DeleteResponse doWork(DeleteRequest req) {
        return null;
        
    }
            
    
}
