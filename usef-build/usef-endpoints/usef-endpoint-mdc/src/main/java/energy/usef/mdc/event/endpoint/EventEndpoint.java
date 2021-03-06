/*
 * Copyright 2015-2016 USEF Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package energy.usef.mdc.event.endpoint;

import energy.usef.mdc.workflow.CommonReferenceQueryEvent;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Restful service to send events for the MDC role.
 */
@Path("/Event")
public class EventEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventEndpoint.class);

    @Inject
    private Event<CommonReferenceQueryEvent> commonReferenceQueryEventManager;

    /**
     * Sends a new {@link CommonReferenceQueryEvent}.
     *
     * @return a HTTP {@link javax.ws.rs.core.Response}.
     */
    @GET
    @Path("/CommonReferenceQueryEvent")
    public Response sendCommonReferenceQueryEvent() {
        commonReferenceQueryEventManager.fire(new CommonReferenceQueryEvent());
        LOGGER.info("Fired new CommonReferenceQueryEvent.");
        return Response.status(Response.Status.OK).build();
    }

}
