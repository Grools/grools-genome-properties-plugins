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
import fr.cea.ig.grools.model.Theory;
import fr.cea.ig.grools.relevant.RelevantObservation;
import fr.cea.ig.grools.relevant.RelevantTheory;
import fr.cea.ig.grools.relevant.terms.ObservationTerms;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StrongTheoryTest {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(StrongTheoryTest.class);

    private Reasoner grools;

    @Before
    public void setUp(){
        final KieSession kieSession = DroolsBuilder.useKieBase("grools-reasonning");
        grools = new Reasoner(kieSession);
        assertNotNull(grools);
        grools.disableMandatoryReasoning();
        grools.disableSpecificReasoning();
        final Mode mode = grools.getMode();
        assertFalse( mode.getIsMandatoryRuleEnabled() );
        assertFalse( mode.getIsSpecificRuleEnabled() );
    }


    @Test
    public void andKnowledgeHasUnknownExistence1() {
        LOG.debug("And Knowledge has an unknown existence (1)");
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.AND)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.fireAllRules();

        grools.getObjects().stream().filter(o -> o instanceof Theory).forEach(o -> {
            final RelevantTheory theory = ((RelevantTheory) o);
            LOG.debug(theory.toString());
            assertTrue(theory.getIsPredicted() == ObservationTerms.UNKNOWN);
        });

    }


    @Test
    public void andKnowledgeHasUnknownExistence2() throws Exception {
        LOG.debug("And Knowledge has an unknown existence (2)");
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.AND)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantObservation prediction1 = RelevantObservation.builder()
                                                                    .name("prediction1")
                                                                    .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                                    .theoryId("theory1")
                                                                    .state(ObservationTerms.UNKNOWN)
                                                                    .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.fireAllRules();

        grools.getTheories().forEach(o -> {
            final RelevantTheory theory = ((RelevantTheory) o);
            LOG.debug(theory.toString());
            assertTrue(theory.getIsPredicted() == ObservationTerms.UNKNOWN);
        });

    }


    @Test
    public void andKnowledgeHasUnknownExistence3() throws Exception {
        LOG.debug("And Knowledge has an unknown existence (3)");
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.AND)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1 = RelevantObservation.builder()
                                                .name("prediction1")
                                                .theoryId("theory1")
                                                .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                .state(ObservationTerms.TRUE)
                                                .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.fireAllRules();

        grools.getTheories().forEach(o -> {
            final RelevantTheory theory = ((RelevantTheory) o);
            LOG.debug(theory.toString());
            switch (theory.getName()) {
                case "theory1": assertTrue(theory.getIsPredicted() == ObservationTerms.TRUE); break;
                default:assertTrue(theory.getIsPredicted() == ObservationTerms.UNKNOWN); break;
            }
        });

    }


    @Test
    public void andKnowledgeIsPresentAbsent1() throws Exception {
        LOG.debug("And Knowledge is present/absent (1)");
        final RelevantTheory theory0 = RelevantTheory.builder().name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.AND)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder().name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder().name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.TRUE)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.BOTH)
                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.BOTH);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.TRUE);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.BOTH);
    }

    @Test
    public void andKnowledgeIsPresentAbsent2() throws Exception {
        LOG.debug("And Knowledge is present/absent (2)");
        final RelevantTheory theory0 = RelevantTheory.builder().name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.AND)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder().name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder().name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.BOTH)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.BOTH)
                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.BOTH);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.BOTH);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.BOTH);

    }
    @Test
    public void andKnowledgeIsPresentAbsent3() throws Exception {
        LOG.debug("And Knowledge is present/absent (3)");
        final RelevantTheory theory0 = RelevantTheory.builder().name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.AND)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder().name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder().name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.UNKNOWN)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.BOTH)
                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.BOTH);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.UNKNOWN);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.BOTH);
    }


    @Test
    public void andKnowledgeIsPresentAbsent4() throws Exception {
        LOG.debug("And Knowledge is present/absent (4)");
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.AND)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1a  = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.TRUE)
                                                  .build();
        final Observation prediction1b  = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.FALSE)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.BOTH)
                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1a );
        grools.insert( prediction1b );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.BOTH);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.BOTH);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.BOTH);
    }


    @Test
    public void andKnowledgeIsAbsent1() throws Exception {
        LOG.debug("And Knowledge is absent (1)");
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.AND)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.FALSE)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.FALSE)
                                                  .build();

        assertTrue(theory1.getParents()[0] == theory0);

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert(theory2);
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.FALSE);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.FALSE);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.FALSE);
    }


    @Test
    public void andKnowledgeIsAbsent2() throws Exception {
        LOG.debug("And Knowledge is absent (2)");
        final RelevantTheory    theory0     = RelevantTheory.builder()
                                                               .name("theory0")
                                                               .source("junit")
                                                               .operator(OperatorLogic.AND)
                                                               .build();
        final RelevantTheory    theory1     = RelevantTheory.builder()
                                                               .name("theory1")
                                                               .source("junit")
                                                               .parent(theory0)
                                                               .build();
        final RelevantTheory    theory2     = RelevantTheory.builder()
                                                               .name("theory2")
                                                               .source("junit")
                                                               .parent(theory0)
                                                               .build();
        final Observation       prediction1 = RelevantObservation.builder()
                                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                                  .name("prediction1")
                                                                  .theoryId("theory1")
                                                                  .state(ObservationTerms.FALSE)
                                                                  .build();
        final Observation       prediction2 = RelevantObservation.builder()
                                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                                  .name("prediction2")
                                                                  .theoryId("theory2")
                                                                  .state(ObservationTerms.BOTH)
                                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.BOTH);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.FALSE);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.BOTH);
    }


    @Test
    public void andKnowledgeIsAbsent3() throws Exception {
        LOG.debug("And Knowledge is absent (2)");
        final RelevantTheory theory0 = RelevantTheory.builder().name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.AND)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder().name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder().name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.FALSE)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.TRUE)
                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.FALSE);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.FALSE);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.TRUE);
    }


    @Test
    public void orKnowledgeHasNoneExistence1() throws Exception {
        LOG.debug("Or Knowledge has an unknown existence(1)");
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.OR)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS).name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.UNKNOWN)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS).name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.UNKNOWN)
                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert(theory2);
        grools.insert(prediction1);
        grools.insert(prediction2);
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.UNKNOWN);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.UNKNOWN   );
        assertTrue(theory2.getIsPredicted() == ObservationTerms.UNKNOWN);
    }



    @Test
    public void orKnowledgeIsPresentAbsent1() throws Exception {
        LOG.debug("Or Knowledge is present/absent (1)");
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.OR)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.BOTH)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.FALSE)
                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.BOTH);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.BOTH);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.FALSE);
    }



    @Test
    public void orKnowledgeIsPresentAbsent2() throws Exception {
        LOG.debug("Or Knowledge is present/absent (2)");
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.OR)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.BOTH)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.BOTH)
                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.insert(prediction2);
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.BOTH);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.BOTH);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.BOTH);
    }


    @Test
    public void orKnowledgeIsAbsent1() throws Exception {
        LOG.debug("Or Knowledge is absent (1)");
        final RelevantTheory        theory0     = RelevantTheory.builder()
                                                                   .name( "theory0" )
                                                                   .source( "junit" )
                                                                   .operator( OperatorLogic.OR )
                                                                   .build();
        final RelevantTheory        theory1     = RelevantTheory.builder()
                                                                   .name( "theory1" )
                                                                   .source( "junit" )
                                                                   .parent( theory0 )
                                                                   .build();
        final RelevantTheory        theory2     = RelevantTheory.builder()
                                                                   .name( "theory2" )
                                                                   .source( "junit" )
                                                                   .parent( theory0 )
                                                                   .build();
        final RelevantObservation   prediction1 = RelevantObservation.builder()
                                                                      .category( ObservationType.COMPUTATIONAL_ANALYSIS )
                                                                      .name( "prediction1" )
                                                                      .theoryId( "theory1" )
                                                                      .state( ObservationTerms.FALSE )
                                                                      .build();
        final RelevantObservation   prediction2 = RelevantObservation.builder()
                                                                      .category( ObservationType.COMPUTATIONAL_ANALYSIS )
                                                                      .name( "prediction2" )
                                                                      .theoryId( "theory2" )
                                                                      .state( ObservationTerms.FALSE )
                                                                      .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.FALSE);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.FALSE);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.FALSE);
    }


    @Test
    public void orKnowledgeIsPresent1() throws Exception {
        LOG.debug("Or Knowledge is present (1)");
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.OR)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.TRUE)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.TRUE)
                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.TRUE);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.TRUE);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.TRUE);
    }


    @Test
    public void orKnowledgeIsPresent2() throws Exception {
        LOG.debug("Or Knowledge is present (2)");
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.OR)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.TRUE)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.FALSE)
                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.TRUE);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.TRUE);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.FALSE);
    }


    @Test
    public void orKnowledgeIsPresent3() throws Exception {
        LOG.debug("Or Knowledge is present (3)");
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.OR)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.TRUE)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.BOTH)
                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.TRUE);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.TRUE);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.BOTH);
    }


    @Test
    public void orKnowledgeIsPresent4() throws Exception {
        LOG.debug("Or Knowledge is present (4)");
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name("theory0")
                                               .source("junit")
                                               .operator(OperatorLogic.OR)
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name("theory1")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name("theory2")
                                               .source("junit")
                                               .parent(theory0)
                                               .build();
        final Observation prediction1   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction1")
                                                  .theoryId("theory1")
                                                  .state(ObservationTerms.TRUE)
                                                  .build();
        final Observation prediction2   = RelevantObservation.builder()
                                                  .category( ObservationType.COMPUTATIONAL_ANALYSIS)
                                                  .name("prediction2")
                                                  .theoryId("theory2")
                                                  .state(ObservationTerms.UNKNOWN)
                                                  .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.fireAllRules();

        assertTrue(theory0.getIsPredicted() == ObservationTerms.TRUE);
        assertTrue(theory1.getIsPredicted() == ObservationTerms.TRUE);
        assertTrue(theory2.getIsPredicted() == ObservationTerms.UNKNOWN);
    }

}