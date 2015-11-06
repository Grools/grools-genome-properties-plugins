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

import fr.cea.ig.grools.model.ObservationType;
import fr.cea.ig.grools.relevant.RelevantObservation;
import fr.cea.ig.grools.relevant.RelevantTheory;
import fr.cea.ig.grools.relevant.terms.ObservationTerms;
import org.junit.Before;
import org.junit.Test;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import org.kie.api.runtime.KieSession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * PredictionTest
 */
public class PredictionTest {
    private static final Logger LOG = ( Logger ) LoggerFactory.getLogger( PredictionTest.class );
    private KieSession kieSession;
    private Reasoner grools;

    @Before
    public void setUp() {
        kieSession = DroolsBuilder.useKieBase( "grools-reasonning" );
        grools = new Reasoner( kieSession );
        assertNotNull( grools );
        final Mode mode = grools.getMode();
        grools.disableMandatoryReasoning();
        grools.disableSpecificReasoning();
        assertFalse( mode.getIsMandatoryRuleEnabled() );
        assertFalse( mode.getIsSpecificRuleEnabled() );
    }

    @Test
    public void predictionInferHisNoneExistence1() {
        LOG.debug( "Prediction infer his none existence" );
        RelevantTheory theory0 = RelevantTheory.builder()
                                         .name( "theory0" )
                                         .source( "junit" )
                                         .build();

        grools.insert( theory0 );
        grools.fireAllRules();

        assertTrue( theory0.getIsPredicted() == ObservationTerms.UNKNOWN );
    }

    @Test
    public void predictionInferHisNoneExistence2() throws Exception {
        LOG.debug( "Prediction infer his none existence" );
        RelevantTheory theory0 = RelevantTheory.builder()
                                         .name( "theory0" )
                                         .source( "junit" )
                                         .build();
        RelevantObservation observation0 = RelevantObservation.builder()
                                                   .name( "observation0" )
                                                   .theoryId( "theory0" )
                                                   .category( ObservationType.COMPUTATIONAL_ANALYSIS )
                                                   .state( "UNKNOWN" )
                                                   .build();
        grools.insert( theory0 );
        grools.insert( observation0 );
        grools.fireAllRules();

        assertTrue( theory0.getIsPredicted() == ObservationTerms.UNKNOWN );
    }

    @Test
    public void predictionInferHisPresenceAbsence1() throws Exception {
        LOG.debug( "Prediction infer his presence/absence" );
        RelevantTheory theory0 = RelevantTheory.builder()
                                         .name( "theory0" )
                                         .source( "junit" )
                                         .build();
        RelevantObservation observation0 = RelevantObservation.builder()
                                                   .name( "observation0" )
                                                   .theoryId( "theory0" )
                                                   .category( ObservationType.COMPUTATIONAL_ANALYSIS )
                                                   .state( "TRUE" )
                                                   .build();
        RelevantObservation observation1 = RelevantObservation.builder()
                                                   .name( "observation1" )
                                                   .theoryId( "theory0" )
                                                   .category( ObservationType.COMPUTATIONAL_ANALYSIS )
                                                   .state( "FALSE" )
                                                   .build();
        grools.insert( theory0 );
        grools.insert( observation0 );
        grools.insert( observation1 );
        grools.fireAllRules();

        assertTrue( theory0.getIsPredicted() == ObservationTerms.BOTH );

    }


    @Test
    public void predictionInferHisPresenceAbsence2() throws Exception {
        LOG.debug( "Prediction infer his presence/absence" );
        RelevantTheory theory0 = RelevantTheory.builder()
                                         .name( "theory0" )
                                         .source( "junit" )
                                         .build();
        RelevantObservation observation0 = RelevantObservation.builder()
                                                   .name( "observation0" )
                                                   .theoryId( "theory0" )
                                                   .category( ObservationType.COMPUTATIONAL_ANALYSIS )
                                                   .state( "BOTH" )
                                                   .build();
        grools.insert( theory0 );
        grools.insert( observation0 );
        grools.fireAllRules();

        assertTrue( theory0.getIsPredicted() == ObservationTerms.BOTH );
    }


    @Test
    public void predictionInferHisAbsence() throws Exception {
        LOG.debug( "Prediction infer his none existence" );
        RelevantTheory theory0 = RelevantTheory.builder()
                                         .name( "theory0" )
                                         .source( "junit" )
                                         .build();
        RelevantObservation observation0 = RelevantObservation.builder()
                                                   .name( "observation0" )
                                                   .theoryId( "theory0" )
                                                   .category( ObservationType.COMPUTATIONAL_ANALYSIS )
                                                   .state( "FALSE" )
                                                   .build();
        grools.insert( theory0 );
        grools.insert( observation0 );
        grools.fireAllRules();

        assertTrue( theory0.getIsPredicted() == ObservationTerms.FALSE );

    }

    @Test
    public void predictionInferHisPresence() throws Exception {
        LOG.debug( "Prediction infer his none existence" );
        RelevantTheory theory0 = RelevantTheory.builder()
                                         .name( "theory0" )
                                         .source( "junit" )
                                         .build();
        RelevantObservation observation0 = RelevantObservation.builder()
                                                   .name( "observation0" )
                                                   .theoryId( "theory0" )
                                                   .category( ObservationType.COMPUTATIONAL_ANALYSIS )
                                                   .state( "TRUE" )
                                                   .build();
        grools.insert( theory0 );
        grools.insert( observation0 );
        grools.fireAllRules();

        assertTrue( theory0.getIsPredicted() == ObservationTerms.TRUE );

    }
}
