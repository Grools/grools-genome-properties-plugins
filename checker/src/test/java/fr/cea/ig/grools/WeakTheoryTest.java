/*
 *
 * Copyright LABGeM 2015
 *
 * author: Jonathan MERCIER
 *
 * This software is a computer program whose purpose is to annotate a complete genome.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 */

package fr.cea.ig.grools;


import ch.qos.logback.classic.Logger;
import fr.cea.ig.grools.model.ObservationType;
import fr.cea.ig.grools.model.Observation;
import fr.cea.ig.grools.model.OperatorLogic;
import fr.cea.ig.grools.relevant.RelevantObservation;
import fr.cea.ig.grools.relevant.RelevantTheory;
import fr.cea.ig.grools.relevant.terms.ObservationTerms;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;

public class WeakTheoryTest {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(WeakTheoryTest.class);

    private Reasoner grools;

    @Before
    public void setUp(){
        final KieSession kieSession = DroolsBuilder.useKieBase("grools-reasonning");
        grools = new Reasoner(kieSession);
        assertNotNull(grools);
    }

    @Test
    public void genpropMode1() {
        LOG.debug("GenProp Mode (1)");
        grools.disableMandatoryReasoning();
        grools.disableSpecificReasoning();
        final RelevantTheory propG0     = RelevantTheory.builder()
                                                  .name("propG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compG0     = RelevantTheory.builder()
                                                  .name("compG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propG0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceG0 = RelevantTheory.builder()
                                                  .name("evidenceG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.OR)
                                                  .parent(compG0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory propG1     = RelevantTheory.builder()
                                                  .name("propG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compG1     = RelevantTheory.builder()
                                                  .name("compG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceG1 = RelevantTheory.builder()
                                                  .name("evidenceG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.OR)
                                                  .parent(compG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory propT0     = RelevantTheory.builder()
                                                  .name("propT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(evidenceG0)
                                                  .parent(evidenceG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compT0     = RelevantTheory.builder()
                                                  .name("compT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propT0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceT0 = RelevantTheory.builder()
                                                  .name("evidenceT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.OR)
                                                  .parent(compT0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceTL00= RelevantTheory.builder()
                                                   .name("evidenceTL00")
                                                   .source("junit")
                                                   .parent(evidenceT0)
                                                   .isMandatory(true)
                                                   .build();
        final RelevantTheory evidenceTL01= RelevantTheory.builder()
                                                   .name("evidenceTL01")
                                                   .source("junit")
                                                   .parent(evidenceT0)
                                                   .isMandatory(true)
                                                   .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("evidenceTL00")
                                                  .state( ObservationTerms.TRUE )
                                                  .build();

        grools.insert( propG0 );
        grools.insert( compG0 );
        grools.insert( evidenceG0 );
        grools.insert( propG1 );
        grools.insert( compG1 );
        grools.insert( evidenceG1 );
        grools.insert( propT0 );
        grools.insert( compT0 );
        grools.insert( evidenceT0 );
        grools.insert( evidenceTL00 );
        grools.insert( evidenceTL01 );
        grools.insert( prediction1 );
        grools.fireAllRules();
        assertEquals(propG0.getIsPredicted(), ObservationTerms.TRUE);
    }

    @Test
    public void genpropMode2() {
        LOG.debug("GenProp Mode (2)");
        grools.enableMandatoryReasoning();
        grools.disableSpecificReasoning();
        final RelevantTheory propG0     = RelevantTheory.builder()
                                                  .name("propG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compG0     = RelevantTheory.builder()
                                                  .name("compG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propG0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceG0 = RelevantTheory.builder()
                                                  .name("evidenceG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.OR)
                                                  .parent(compG0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory propG1     = RelevantTheory.builder()
                                                  .name("propG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compG1     = RelevantTheory.builder()
                                                  .name("compG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceG1 = RelevantTheory.builder()
                                                  .name("evidenceG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.OR)
                                                  .parent(compG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory propT0     = RelevantTheory.builder()
                                                  .name("propT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(evidenceG0)
                                                  .parent(evidenceG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compT0     = RelevantTheory.builder()
                                                  .name("compT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propT0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceT0 = RelevantTheory.builder()
                                                  .name("evidenceT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(compT0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceTL00= RelevantTheory.builder()
                                                   .name("evidenceTL00")
                                                   .source("junit")
                                                   .parent(evidenceT0)
                                                   .isMandatory(true)
                                                   .build();
        final RelevantTheory evidenceTL01= RelevantTheory.builder()
                                                   .name("evidenceTL01")
                                                   .source("junit")
                                                   .parent(evidenceT0)
                                                   .isMandatory(false)
                                                   .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("evidenceTL00")
                                                  .state( ObservationTerms.TRUE )
                                                  .build();

        grools.insert( propG0 );
        grools.insert( compG0 );
        grools.insert( evidenceG0 );
        grools.insert( propG1 );
        grools.insert( compG1 );
        grools.insert( evidenceG1 );
        grools.insert( propT0 );
        grools.insert( compT0 );
        grools.insert( evidenceT0 );
        grools.insert( evidenceTL00 );
        grools.insert( evidenceTL01 );
        grools.insert( prediction1 );
        grools.fireAllRules();
        assertEquals(propG0.getIsPredicted(), ObservationTerms.TRUE);
    }

    @Test
    public void genpropMode3() {
        LOG.debug("GenProp Mode (3)");
        grools.disableMandatoryReasoning();
        grools.enableSpecificReasoning();
        final RelevantTheory propG0     = RelevantTheory.builder()
                                                  .name("propG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compG0     = RelevantTheory.builder()
                                                  .name("compG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propG0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceG0 = RelevantTheory.builder()
                                                  .name("evidenceG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.OR)
                                                  .parent(compG0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory propG1     = RelevantTheory.builder()
                                                  .name("propG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compG1     = RelevantTheory.builder()
                                                  .name("compG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceG1 = RelevantTheory.builder()
                                                  .name("evidenceG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.OR)
                                                  .parent(compG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory propT0     = RelevantTheory.builder()
                                                  .name("propT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(evidenceG0)
                                                  .parent(evidenceG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compT0     = RelevantTheory.builder()
                                                  .name("compT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propT0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceT0 = RelevantTheory.builder()
                                                  .name("evidenceT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(compT0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceTL00= RelevantTheory.builder()
                                                   .name("evidenceTL00")
                                                   .source("junit")
                                                   .parent(evidenceT0)
                                                   .isMandatory(true)
                                                   .build();
        final RelevantTheory evidenceTL01= RelevantTheory.builder()
                                                   .name("evidenceTL01")
                                                   .source("junit")
                                                   .parent(evidenceT0)
                                                   .isMandatory(true)
                                                   .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("evidenceTL00")
                                                  .state( ObservationTerms.TRUE )
                                                  .build();

        grools.insert( propG0 );
        grools.insert( compG0 );
        grools.insert( evidenceG0 );
        grools.insert( propG1 );
        grools.insert( compG1 );
        grools.insert( evidenceG1 );
        grools.insert( propT0 );
        grools.insert( compT0 );
        grools.insert( evidenceT0 );
        grools.insert( evidenceTL00 );
        grools.insert( evidenceTL01 );
        grools.insert( prediction1 );
        grools.fireAllRules();
        assertEquals(propG0.getIsPredicted(), ObservationTerms.TRUE);
    }


    @Test
    public void genpropMode4() {
        LOG.debug("GenProp Mode (4)");
        grools.enableMandatoryReasoning();
        grools.enableSpecificReasoning();
        final RelevantTheory propG0     = RelevantTheory.builder()
                                                  .name("propG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compG0     = RelevantTheory.builder()
                                                  .name("compG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propG0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceG0 = RelevantTheory.builder()
                                                  .name("evidenceG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.OR)
                                                  .parent(compG0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory propG1     = RelevantTheory.builder()
                                                  .name("propG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compG1     = RelevantTheory.builder()
                                                  .name("compG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceG1 = RelevantTheory.builder()
                                                  .name("evidenceG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.OR)
                                                  .parent(compG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory propT0     = RelevantTheory.builder()
                                                  .name("propT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(evidenceG0)
                                                  .parent(evidenceG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compT0     = RelevantTheory.builder()
                                                  .name("compT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propT0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceT0 = RelevantTheory.builder()
                                                  .name("evidenceT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(compT0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceTL00= RelevantTheory.builder()
                                                   .name("evidenceTL00")
                                                   .source("junit")
                                                   .parent(evidenceT0)
                                                   .isMandatory(true)
                                                   .build();
        final RelevantTheory evidenceTL01= RelevantTheory.builder()
                                                   .name("evidenceTL01")
                                                   .source("junit")
                                                   .parent(evidenceT0)
                                                   .isMandatory(true)
                                                   .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("evidenceTL00")
                                                  .state( ObservationTerms.TRUE )
                                                  .build();

        grools.insert( propG0 );
        grools.insert( compG0 );
        grools.insert( evidenceG0 );
        grools.insert( propG1 );
        grools.insert( compG1 );
        grools.insert( evidenceG1 );
        grools.insert( propT0 );
        grools.insert( compT0 );
        grools.insert( evidenceT0 );
        grools.insert( evidenceTL00 );
        grools.insert( evidenceTL01 );
        grools.insert( prediction1 );
        grools.fireAllRules();
        assertEquals(propG0.getIsPredicted(), ObservationTerms.TRUE);
    }


    @Test
    public void genpropMode5() {
        LOG.debug("GenProp Mode (5)");
        grools.enableMandatoryReasoning();
        grools.enableSpecificReasoning();
        final RelevantTheory propG0     = RelevantTheory.builder()
                                                  .name("propG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compG0     = RelevantTheory.builder()
                                                  .name("compG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propG0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceG0 = RelevantTheory.builder()
                                                  .name("evidenceG0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.OR)
                                                  .parent(compG0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory propG1     = RelevantTheory.builder()
                                                  .name("propG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compG1     = RelevantTheory.builder()
                                                  .name("compG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceG1 = RelevantTheory.builder()
                                                  .name("evidenceG1")
                                                  .source("junit")
                                                  .operator(OperatorLogic.OR)
                                                  .parent(compG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory propT0     = RelevantTheory.builder()
                                                  .name("propT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(evidenceG0)
                                                  .parent(evidenceG1)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory compT0     = RelevantTheory.builder()
                                                  .name("compT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(propT0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceT0 = RelevantTheory.builder()
                                                  .name("evidenceT0")
                                                  .source("junit")
                                                  .operator(OperatorLogic.AND)
                                                  .parent(compT0)
                                                  .isMandatory(true)
                                                  .build();
        final RelevantTheory evidenceTL00= RelevantTheory.builder()
                                                   .name("evidenceTL00")
                                                   .source("junit")
                                                   .parent(evidenceT0)
                                                   .isMandatory(true)
                                                   .build();
        final RelevantTheory evidenceTL01= RelevantTheory.builder()
                                                   .name("evidenceTL01")
                                                   .source("junit")
                                                   .parent(evidenceT0)
                                                   .isMandatory(false)
                                                   .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("evidenceTL00")
                                                  .state( ObservationTerms.TRUE )
                                                  .build();

        grools.insert( propG0 );
        grools.insert( compG0 );
        grools.insert( evidenceG0 );
        grools.insert( propG1 );
        grools.insert( compG1 );
        grools.insert( evidenceG1 );
        grools.insert( propT0 );
        grools.insert( compT0 );
        grools.insert( evidenceT0 );
        grools.insert( evidenceTL00 );
        grools.insert( evidenceTL01 );
        grools.insert( prediction1 );
        grools.fireAllRules();
        assertEquals(propG0.getIsPredicted(), ObservationTerms.TRUE);
    }
}