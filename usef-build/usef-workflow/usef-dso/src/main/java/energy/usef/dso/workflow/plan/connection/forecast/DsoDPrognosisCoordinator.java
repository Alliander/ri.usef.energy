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

package energy.usef.dso.workflow.plan.connection.forecast;

import energy.usef.core.data.xml.bean.message.Prognosis;
import energy.usef.core.model.*;
import energy.usef.core.service.business.CorePlanboardBusinessService;
import energy.usef.dso.model.Aggregator;
import energy.usef.dso.service.business.DsoPlanboardBusinessService;
import energy.usef.dso.workflow.validate.gridsafetyanalysis.GridSafetyAnalysisEvent;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.List;

import static energy.usef.core.constant.USEFConstants.LOG_COORDINATOR_FINISHED_HANDLING_EVENT;
import static energy.usef.core.constant.USEFConstants.LOG_COORDINATOR_START_HANDLING_EVENT;

/**
 * DSO Non Aggreagator Connection Forecast workflow, Plan board sub-flow workflow coordinator.
 */
@Singleton
public class DsoDPrognosisCoordinator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DsoDPrognosisCoordinator.class);

    @Inject
    private CorePlanboardBusinessService corePlanboardBusinessService;

    @Inject
    private Event<GridSafetyAnalysisEvent> gridSafetyAnalysisEventManager;

    @Inject
    private DsoPlanboardBusinessService dsoPlanboardBusinessService;

    public void invokeWorkflow(Prognosis prognosis, Message savedMessage) {
        LOGGER.info(LOG_COORDINATOR_START_HANDLING_EVENT, prognosis);

        List<PtuPrognosis> existingPtuPrognoses = corePlanboardBusinessService
                .findLastPrognoses(prognosis.getPeriod(), PrognosisType.D_PROGNOSIS, prognosis.getCongestionPoint());

        if (isPrognosisNewPrognosis(prognosis, existingPtuPrognoses)) {
            LOGGER.info("New prognosis received.");
        } else {
            LOGGER.info("Updated prognosis received.");
            dsoPlanboardBusinessService.handleUpdatedPrognosis(prognosis, existingPtuPrognoses);
        }
        corePlanboardBusinessService.storePrognosis(prognosis.getCongestionPoint(), prognosis, DocumentType.D_PROGNOSIS,
                DocumentStatus.ACCEPTED, prognosis.getMessageMetadata().getSenderDomain(), savedMessage, false);

        triggerGridSafetyAnalysisWorkflow(prognosis.getPeriod(), prognosis.getCongestionPoint());
        LOGGER.info(LOG_COORDINATOR_FINISHED_HANDLING_EVENT, prognosis);
    }

    private boolean isPrognosisNewPrognosis(Prognosis dprognosis, List<PtuPrognosis> prognoses) {
        if (prognoses == null || prognoses.isEmpty()) {
            return true;
        }
        for (PtuPrognosis prognosis : prognoses) {
            if (dprognosis.getMessageMetadata().getSenderDomain().equals(prognosis.getParticipantDomain())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Fires an event to trigger the Grid Safety Analysis workflow.
     *
     * @param analysisDate {@link LocalDate} period of the grid safety analysis to run.
     */
    private void triggerGridSafetyAnalysisWorkflow(LocalDate analysisDate, String congestionPointEntityAddress) {
        // count distinct aggregators
        List<Aggregator> activeAggregators = dsoPlanboardBusinessService.getAggregatorsByEntityAddress(
                congestionPointEntityAddress, analysisDate);

        List<PlanboardMessage> acceptedPrognosisMessages = corePlanboardBusinessService.findAcceptedPrognosisMessages(
                DocumentType.D_PROGNOSIS, analysisDate, congestionPointEntityAddress);

        if (acceptedPrognosisMessages.size() < activeAggregators.size()) {
            LOGGER.debug("Accepted prognoses amount=[{}], aggregators count=[{}].", acceptedPrognosisMessages.size(),
                    activeAggregators.size());
            LOGGER.info("Did not receive all the d-prognoses yet. Grid Safety Analysis will not be triggered.");
            return;
        }
        gridSafetyAnalysisEventManager.fire(new GridSafetyAnalysisEvent(congestionPointEntityAddress, analysisDate));
    }
}
