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
import fr.cea.ig.grools.model.OperatorLogic;
import fr.cea.ig.grools.relevant.terms.ObservationTerms;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

import org.kie.api.runtime.KieSession;

import fr.cea.ig.grools.relevant.RelevantObservation;
import fr.cea.ig.grools.relevant.RelevantTheory;
import fr.cea.ig.grools.relevant.terms.ConclusionTerms;

import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ConclusionTest {

    private static final Logger LOG = ( Logger ) LoggerFactory.getLogger( ConclusionTest.class );


    private Reasoner grools;

    @Before
    public void setUp() {
        final KieSession kieSession = DroolsBuilder.useKieBase( "grools-reasonning" );
        grools = new Reasoner( kieSession );
        assertNotNull( grools );
        grools.disableMandatoryReasoning();
        grools.disableSpecificReasoning();
        final Mode mode = grools.getMode();
        assertFalse( mode.getIsMandatoryRuleEnabled() );
        assertFalse( mode.getIsSpecificRuleEnabled() );
    }

    @Test
    public void presentKnowledgeIsNotAvoidedNorRequiredConclusionTermsIsUnconfirmedPresence1() {
        LOG.debug( "Present knowledge without avoided or required assertion: ConclusionTerms is unconfirmed presence (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction0 = RelevantObservation.builder()
                                                        .name( "prediction0" )
                                                        .id( "prediction0" )
                                                        .theoryId( "theory0" )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.UNKNOWN )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction0 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.UNCONFIRMED_PRESENCE );
    }


    @Test
    public void absentKnowledgeIsNotRequiredNorAvoidedConclusionTermsIsUnconfirmedAbsence1() {
        LOG.debug( "Absent knowledge without avoided or required assertion: ConclusionTerms is unconfirmed absence (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction0 = RelevantObservation.builder()
                                                        .name( "prediction0" )
                                                        .id( "prediction0" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.FALSE )
                                                        .build();

        grools.insert( theory0 );
        grools.insert( prediction0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.UNCONFIRMED_ABSENCE );

    }


    @Test
    public void absentKnowledgeIsNotRequiredNorAvoidedConclusionTermsIsUnconfirmedAbsence2() {
        LOG.debug( "Absent knowledge without avoided or required assertion: ConclusionTerms is unconfirmed absence (2)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction0 = RelevantObservation.builder()
                                                        .name( "prediction0" )
                                                        .id( "prediction0" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.FALSE )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.UNKNOWN )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction0 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.UNCONFIRMED_ABSENCE );

    }


    @Test
    public void presentAndAbsentKnowledgeIsNotRequiredNorAvoidedConclusionTermsIsUnconfirmedContradictory1() {
        LOG.debug( "Present and absent knowledge without avoided or required assertion: ConclusionTerms is unconfirmed contradictory (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction0 = RelevantObservation.builder()
                                                        .name( "prediction0" )
                                                        .id( "prediction0" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.BOTH )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.UNKNOWN )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction0 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.UNCONFIRMED_CONTRADICTORY );

    }

    @Test
    public void presentAndAbsentKnowledgeIsNotRequiredNorAvoidedConclusionTermsIsUnconfirmedContradictory2() {
        LOG.debug( "Present and absent knowledge without avoided or required assertion: ConclusionTerms is unconfirmed contradictory (2)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction1 = RelevantObservation.builder()
                                                        .name( "prediction1" )
                                                        .id( "prediction1" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.TRUE )
                                                        .build();
        final RelevantObservation prediction2 = RelevantObservation.builder()
                                                        .name( "prediction2" )
                                                        .id( "prediction2" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.FALSE )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.UNKNOWN )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.UNCONFIRMED_CONTRADICTORY );

    }


    @Test
    public void presentAndAbsentKnowledgeIsNotRequiredNorAvoidedConclusionTermsIsContradictory3() {
        LOG.debug( "Present and absent knowledge without avoided or required assertion: ConclusionTerms is unconfirmed contradictory (3)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .operator( OperatorLogic.OR )
                                               .build();
        final RelevantTheory theory1 = RelevantTheory.builder()
                                               .name( "theory1" )
                                               .id( "theory1" )
                                               .source( "junit" )
                                               .parent( theory0 )
                                               .build();
        final RelevantTheory theory2 = RelevantTheory.builder()
                                               .name( "theory2" )
                                               .id( "theory2" )
                                               .source( "junit" )
                                               .parent( theory0 )
                                               .build();
        final RelevantObservation prediction1a = RelevantObservation.builder()
                                                         .name( "prediction1a" )
                                                         .id( "prediction1a" )
                                                         .theoryId( "theory1" )
                                                         .source( "junit" )
                                                         .state( ObservationTerms.TRUE )
                                                         .build();
        final RelevantObservation prediction1b = RelevantObservation.builder()
                                                         .name( "prediction1z" )
                                                         .id( "prediction1z" )
                                                         .theoryId( "theory1" )
                                                         .source( "junit" )
                                                         .state( ObservationTerms.FALSE )
                                                         .build();
        final RelevantObservation prediction2 = RelevantObservation.builder()
                                                        .name( "prediction2" )
                                                        .id( "prediction2" )
                                                        .theoryId( "theory2" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.FALSE )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.UNKNOWN )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( theory1 );
        grools.insert( theory2 );
        grools.insert( prediction1a );
        grools.insert( prediction1b );
        grools.insert( prediction2 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.UNCONFIRMED_CONTRADICTORY );
        assertTrue( theory1.getConclusion() == ConclusionTerms.UNCONFIRMED_CONTRADICTORY );
        assertTrue( theory2.getConclusion() == ConclusionTerms.UNCONFIRMED_ABSENCE );

    }

    @Test
    public void notObservedKnowledgeIsNotRequiredNorAvoidedConclusionTermsIsNormal1() {
        LOG.debug( "Not observed knowledge without avoided or required assertion: ConclusionTerms is unknown (1)" );
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();

        grools.insert( theory0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.UNEXPLAINED );
    }

    @Test
    public void presentKnowledgeIsRequiredConclusionTermsIsNormal1() {
        LOG.debug( "Present knowledge has required assertion: ConclusionTerms is confirmed presence (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction0 = RelevantObservation.builder()
                                                        .name( "prediction0" )
                                                        .id( "prediction0" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.TRUE )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.TRUE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction0 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.CONFIRMED_PRESENCE );
    }

    @Test
    public void absentKnowledgeIsRequiredConclusionTermsIsAnUnexpectedAbsence1() {
        LOG.debug( "Absent knowledge has required assertion: ConclusionTerms is an unexpected absence (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction0 = RelevantObservation.builder()
                                                        .name( "prediction0" )
                                                        .id( "prediction0" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.FALSE )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.TRUE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction0 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.UNEXPECTED_ABSENCE );
    }

    @Test
    public void presentAndAbsentKnowledgeIsRequiredConclusionTermsIsAcontradictoryAbsence1() {
        LOG.debug( "Present and absent knowledge has required assertion: ConclusionTerms is a contradictory absence (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction1 = RelevantObservation.builder()
                                                        .name( "prediction1" )
                                                        .id( "prediction1" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.TRUE )
                                                        .build();
        final RelevantObservation prediction2 = RelevantObservation.builder()
                                                        .name( "prediction2" )
                                                        .id( "prediction2" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.FALSE )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.TRUE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.CONTRADICTORY_ABSENCE );
    }

    @Test
    public void notObservedKnowledgeIsRequiredConclusionTermsIsMissing1() {
        LOG.debug( "Not observed knowledge has required assertion: ConclusionTerms is missing (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .source( "junit" )
                                                         .theoryId( "theory0" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.TRUE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.MISSING );

    }

    @Test
    public void notObservedKnowledgeIsRequiredConclusionTermsIsMissing2() {
        LOG.debug( "Not observed knowledge has required assertion: ConclusionTerms is missing (2)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction0 = RelevantObservation.builder()
                                                        .name( "prediction0" )
                                                        .id( "prediction0" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.UNKNOWN )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.TRUE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction0 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.MISSING );

    }

    @Test
    public void presentKnowledgeIsAvoidedConclusionTermsIsAnUnexpectedPresence1() {
        LOG.debug( "Present knowledge has avoided assertion: ConclusionTerms is an unexpected presence (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction0 = RelevantObservation.builder()
                                                        .name( "prediction0" )
                                                        .id( "prediction0" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.TRUE )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .source( "junit" )
                                                         .theoryId( "theory0" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.FALSE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction0 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.UNEXPECTED_PRESENCE );
    }

    @Test
    public void absentKnowledgeIsAvoidedConclusionTermsIsNormal1() {
        LOG.debug( "Absent knowledge has avoided assertion: ConclusionTerms is confirmed absence (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction0 = RelevantObservation.builder()
                                                        .name( "prediction0" )
                                                        .id( "prediction0" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.FALSE )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.FALSE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction0 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.CONFIRMED_ABSENCE );
    }

    @Test
    public void presentAndAbsentKnowledgeIsAvoidedConclusionTermsIsAContradictoryPresence1() {
        LOG.debug( "Present and absent knowledge has avoided assertion: ConclusionTerms is a contradictory presence (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction1 = RelevantObservation.builder()
                                                        .name( "prediction1" )
                                                        .id( "prediction1" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.TRUE )
                                                        .build();
        final RelevantObservation prediction2 = RelevantObservation.builder()
                                                        .name( "prediction2" )
                                                        .id( "prediction2" )
                                                        .theoryId( "theory0" )
                                                        .state( ObservationTerms.FALSE )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.FALSE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.CONTRADICTORY_PRESENCE );
    }

    @Test
    public void notObservedKnowledgeIsAvoidedConclusionTermsIsNormal1() {
        LOG.debug( "Not Observed knowledge has avoided assertion: ConclusionTerms is confirmed absence (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction1 = RelevantObservation.builder()
                                                        .name( "prediction1" )
                                                        .id( "theory1" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.UNKNOWN )
                                                        .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.FALSE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction1 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.ABSENT );
    }

    @Test
    public void notObservedKnowledgeIsAvoidedConclusionTermsIsNormal2() {
        LOG.debug( "NNot Observed knowledge has avoided assertion: ConclusionTerms is confirmed absence (2)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .id( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.FALSE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.ABSENT );
    }

    @Test
    public void presentKnowledgeIsRequiredAndAvoidedConclusionTermsIsAnAmbiguousPresence1() {
        LOG.debug( "Present knowledge has required and avoided assertion: ConclusionTerms is an ambiguous presence (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction1 = RelevantObservation.builder()
                                                        .name( "prediction1" )
                                                        .theoryId( "theory0" )
                                                        .state( ObservationTerms.TRUE )
                                                        .build();
        final RelevantObservation expectation1 = RelevantObservation.builder()
                                                         .name( "expectation1" )
                                                         .id( "expectation1" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.TRUE )
                                                         .build();
        final RelevantObservation expectation2 = RelevantObservation.builder()
                                                         .name( "expectation2" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.FALSE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction1 );
        grools.insert( expectation1 );
        grools.insert( expectation2 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.AMBIGUOUS_PRESENCE );
    }

    @Test
    public void absentKnowledgeIsRequiredAndAvoidedConclusionTermsIsAnAmbiguousAbsence1() {
        LOG.debug( "Absent knowledge has required and avoided assertion: ConclusionTerms is an ambiguous absence (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction1 = RelevantObservation.builder()
                                                        .name( "prediction1" )
                                                        .id( "prediction1" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.FALSE )
                                                        .build();
        final RelevantObservation expectation1 = RelevantObservation.builder()
                                                         .name( "expectation1" )
                                                         .id( "expectation1" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.TRUE )
                                                         .build();
        final RelevantObservation expectation2 = RelevantObservation.builder()
                                                         .name( "expectation2" )
                                                         .id( "expectation2" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.FALSE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction1 );
        grools.insert( expectation1 );
        grools.insert( expectation2 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.AMBIGUOUS_ABSENCE );
    }

    @Test
    public void presentAndAbsentKnowledgeIsRequiredAndAvoidedConclusioIsAnAmbiguousPresenceAbsence1() {
        LOG.debug( "Present and absent knowledge has required and avoided assertion: ConclusionTerms is contradictory (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction1 = RelevantObservation.builder()
                                                        .name( "prediction1" )
                                                        .theoryId( "theory0" )
                                                        .source( "junit" )
                                                        .state( ObservationTerms.FALSE )
                                                        .build();
        final RelevantObservation prediction2 = RelevantObservation.builder()
                                                        .name( "prediction2" )
                                                        .id( "prediction2" )
                                                        .theoryId( "theory0" )
                                                        .state( ObservationTerms.TRUE )
                                                        .build();
        final RelevantObservation expectation1 = RelevantObservation.builder()
                                                         .name( "expectation1" )
                                                         .id( "expectation1" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.TRUE )
                                                         .build();
        final RelevantObservation expectation2 = RelevantObservation.builder()
                                                         .name( "expectation2" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .state( ObservationTerms.FALSE )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction1 );
        grools.insert( prediction2 );
        grools.insert( expectation1 );
        grools.insert( expectation2 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.AMBIGUOUS_CONTRADICTORY );
    }

    @Test
    public void notObservedKnowledgeIsRequiredAndAvoidedConclusionTermsIsAmbiguous1() {
        LOG.debug( "Not Observed knowledge is required and avoided ConclusionTerms is ambiguous (1)" );

        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .id( "theory0" )
                                               .source( "junit" )
                                               .build();
        final RelevantObservation prediction1 = RelevantObservation.builder()
                                                        .name( "prediction1" )
                                                        .id( "prediction1" )
                                                        .theoryId( "theory0" )
                                                        .state( ObservationTerms.UNKNOWN )
                                                        .build();
        final RelevantObservation expectation1 = RelevantObservation.builder()
                                                         .name( "expectation1" )
                                                         .id( "expectation1" )
                                                         .source( "junit" )
                                                         .theoryId( "theory0" )
                                                         .state( ObservationTerms.TRUE )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .build();
        final RelevantObservation expectation2 = RelevantObservation.builder()
                                                         .name( "expectation2" )
                                                         .id( "expectation2" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .state( ObservationTerms.FALSE )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( prediction1 );
        grools.insert( expectation1 );
        grools.insert( expectation2 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.AMBIGUOUS );
    }

    @Test
    public void notObservedKnowledgeIsRequiredAndAvoidedConclusionTermsIsAmbiguous2() {
        LOG.debug( "Not Observed knowledge has required and avoided assertion: ConclusionTerms is ambiguous (2)" );

        RelevantTheory theory0 = RelevantTheory.builder()
                                         .name( "theory0" )
                                         .source( "junit" )
                                         .build();
        final RelevantObservation expectation1 = RelevantObservation.builder()
                                                         .name( "expectation1" )
                                                         .id( "expectation1" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .state( ObservationTerms.TRUE )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .build();
        final RelevantObservation expectation2 = RelevantObservation.builder()
                                                         .name( "expectation2" )
                                                         .id( "expectation2" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .state( ObservationTerms.FALSE )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .build();

        grools.insert( theory0 );
        grools.insert( expectation1 );
        grools.insert( expectation2 );
        grools.fireAllRules();

        assertTrue( theory0.getConclusion() == ConclusionTerms.AMBIGUOUS );
    }

    @Test
    public void complex1() {
        LOG.debug( "complex1" );
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .source( "junit" )
                                               .operator( OperatorLogic.AND )
                                               .build();
        final RelevantTheory theory01 = RelevantTheory.builder()
                                                .name( "theory01" )
                                                .source( "junit" )
                                                .operator( OperatorLogic.AND )
                                                .parent( theory0 )
                                                .build();
        final RelevantTheory theory02 = RelevantTheory.builder()
                                                .name( "theory02" )
                                                .source( "junit" )
                                                .parent( theory0 )
                                                .build();
        final RelevantTheory theory011 = RelevantTheory.builder()
                                                 .name( "theory011" )
                                                 .source( "junit" )
                                                 .parent( theory01 )
                                                 .build();
        final RelevantTheory theory012 = RelevantTheory.builder()
                                                 .name( "theory012" )
                                                 .source( "junit" )
                                                 .parent( theory01 )
                                                 .build();

        final RelevantObservation prediction011 = RelevantObservation.builder()
                                                          .name( "prediction011" )
                                                          .theoryId( "theory011" )
                                                          .state( ObservationTerms.TRUE )
                                                          .build();
        final RelevantObservation prediction012 = RelevantObservation.builder()
                                                          .name( "prediction012" )
                                                          .theoryId( "theory012" )
                                                          .state( ObservationTerms.TRUE )
                                                          .build();
        final RelevantObservation prediction02 = RelevantObservation.builder()
                                                         .name( "prediction02" )
                                                         .theoryId( "theory02" )
                                                         .state( ObservationTerms.FALSE )
                                                         .build();

        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .state( ObservationTerms.TRUE )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .build();
        grools.insert( theory0 );
        grools.insert( theory01 );
        grools.insert( theory02 );
        grools.insert( theory011 );
        grools.insert( theory012 );
        grools.insert( prediction011 );
        grools.insert( prediction012 );
        grools.insert( prediction02 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getIsPredicted() == ObservationTerms.FALSE );
        assertTrue( theory01.getIsPredicted() == ObservationTerms.TRUE );
        assertTrue( theory02.getIsPredicted() == ObservationTerms.FALSE );
        assertTrue( theory011.getIsPredicted() == ObservationTerms.TRUE );
        assertTrue( theory012.getIsPredicted() == ObservationTerms.TRUE );
    }


    @Test
    public void complex2() {
        LOG.debug( "complex2" );
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .source( "junit" )
                                               .operator( OperatorLogic.OR )
                                               .build();
        final RelevantTheory theory01 = RelevantTheory.builder()
                                                .name( "theory01" )
                                                .operator( OperatorLogic.AND )
                                                .source( "junit" )
                                                .parent( theory0 )
                                                .build();
        final RelevantTheory theory02 = RelevantTheory.builder()
                                                .name( "theory02" )
                                                .source( "junit" )
                                                .parent( theory0 )
                                                .build();
        final RelevantTheory theory03 = RelevantTheory.builder()
                                                .name( "theory03" )
                                                .operator( OperatorLogic.AND )
                                                .source( "junit" )
                                                .parent( theory0 )
                                                .build();
        final RelevantTheory theory011 = RelevantTheory.builder()
                                                 .name( "theory011" )
                                                 .source( "junit" )
                                                 .parent( theory01 )
                                                 .build();
        final RelevantTheory theory012 = RelevantTheory.builder()
                                                 .name( "theory012" )
                                                 .source( "junit" )
                                                 .parent( theory01 )
                                                 .build();
        final RelevantTheory theory031 = RelevantTheory.builder()
                                                 .name( "theory031" )
                                                 .source( "junit" )
                                                 .parent( theory03 )
                                                 .build();

        final RelevantObservation prediction011 = RelevantObservation.builder()
                                                          .name( "prediction011" )
                                                          .theoryId( "theory011" )
                                                          .state( ObservationTerms.TRUE )
                                                          .build();
        final RelevantObservation prediction012 = RelevantObservation.builder()
                                                          .name( "prediction012" )
                                                          .theoryId( "theory012" )
                                                          .state( ObservationTerms.TRUE )
                                                          .build();
        final RelevantObservation prediction02 = RelevantObservation.builder()
                                                         .name( "prediction02" )
                                                         .theoryId( "theory02" )
                                                         .state( ObservationTerms.FALSE )
                                                         .build();
        final RelevantObservation prediction031 = RelevantObservation.builder()
                                                          .name( "prediction031" )
                                                          .theoryId( "theory031" )
                                                          .state( ObservationTerms.TRUE )
                                                          .build();

        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .state( ObservationTerms.TRUE )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .build();

        final RelevantObservation expectation03 = RelevantObservation.builder()
                                                          .name( "expectation03" )
                                                          .theoryId( "theory03" )
                                                          .source( "junit" )
                                                          .state( ObservationTerms.TRUE )
                                                          .category( ObservationType.EXPERIMENTATION )
                                                          .build();

        grools.insert( theory0 );
        grools.insert( theory01 );
        grools.insert( theory02 );
        grools.insert( theory03 );
        grools.insert( theory011 );
        grools.insert( theory012 );
        grools.insert( theory031 );
        grools.insert( prediction011 );
        grools.insert( prediction012 );
        grools.insert( prediction02 );
        grools.insert( prediction031 );
        grools.insert( expectation0 );
        grools.insert( expectation03 );
        grools.fireAllRules();

        assertTrue( theory0.getIsPredicted() == ObservationTerms.TRUE );
        assertTrue( theory01.getIsPredicted() == ObservationTerms.TRUE );
        assertTrue( theory02.getIsPredicted() == ObservationTerms.FALSE );
        assertTrue( theory011.getIsPredicted() == ObservationTerms.TRUE );
        assertTrue( theory012.getIsPredicted() == ObservationTerms.TRUE );
    }


    @Test
    public void complex2Unordered() {
        LOG.debug( "complex2 unordered" );
        final RelevantTheory theory0 = RelevantTheory.builder()
                                               .name( "theory0" )
                                               .source( "junit" )
                                               .operator( OperatorLogic.OR )
                                               .build();
        final RelevantTheory theory01 = RelevantTheory.builder()
                                                .name( "theory01" )
                                                .operator( OperatorLogic.AND )
                                                .source( "junit" )
                                                .parent( theory0 )
                                                .build();
        final RelevantTheory theory02 = RelevantTheory.builder()
                                                .name( "theory02" )
                                                .source( "junit" )
                                                .parent( theory0 )
                                                .build();
        final RelevantTheory theory03 = RelevantTheory.builder()
                                                .name( "theory03" )
                                                .operator( OperatorLogic.AND )
                                                .source( "junit" )
                                                .parent( theory0 )
                                                .build();
        final RelevantTheory theory011 = RelevantTheory.builder()
                                                 .name( "theory011" )
                                                 .source( "junit" )
                                                 .parent( theory01 )
                                                 .build();
        final RelevantTheory theory012 = RelevantTheory.builder()
                                                 .name( "theory012" )
                                                 .source( "junit" )
                                                 .parent( theory01 )
                                                 .build();
        final RelevantTheory theory031 = RelevantTheory.builder()
                                                 .name( "theory031" )
                                                 .source( "junit" )
                                                 .parent( theory03 )
                                                 .build();

        final RelevantObservation prediction011 = RelevantObservation.builder()
                                                          .name( "prediction011" )
                                                          .theoryId( "theory011" )
                                                          .state( ObservationTerms.TRUE )
                                                          .build();
        final RelevantObservation prediction012 = RelevantObservation.builder()
                                                          .name( "prediction012" )
                                                          .theoryId( "theory012" )
                                                          .state( ObservationTerms.TRUE )
                                                          .build();
        final RelevantObservation prediction02 = RelevantObservation.builder()
                                                         .name( "prediction02" )
                                                         .theoryId( "theory02" )
                                                         .state( ObservationTerms.FALSE )
                                                         .build();
        final RelevantObservation prediction031 = RelevantObservation.builder()
                                                          .name( "prediction031" )
                                                          .theoryId( "theory031" )
                                                          .state( ObservationTerms.TRUE )
                                                          .build();

        final RelevantObservation expectation0 = RelevantObservation.builder()
                                                         .name( "expectation0" )
                                                         .theoryId( "theory0" )
                                                         .source( "junit" )
                                                         .state( ObservationTerms.TRUE )
                                                         .category( ObservationType.EXPERIMENTATION )
                                                         .build();

        final RelevantObservation expectation03 = RelevantObservation.builder()
                                                          .name( "expectation03" )
                                                          .theoryId( "theory03" )
                                                          .source( "junit" )
                                                          .state( ObservationTerms.TRUE )
                                                          .category( ObservationType.EXPERIMENTATION )
                                                          .build();

        grools.insert( theory012 );
        grools.insert( prediction012 );
        grools.insert( theory01 );
        grools.insert( expectation03 );
        grools.insert( theory02 );
        grools.insert( theory011 );
        grools.insert( theory03 );
        grools.insert( theory031 );
        grools.insert( theory0 );
        grools.insert( prediction011 );
        grools.insert( prediction02 );
        grools.insert( prediction031 );
        grools.insert( expectation0 );
        grools.fireAllRules();

        assertTrue( theory0.getIsPredicted() == ObservationTerms.TRUE );
        assertTrue( theory01.getIsPredicted() == ObservationTerms.TRUE );
        assertTrue( theory02.getIsPredicted() == ObservationTerms.FALSE );
        assertTrue( theory011.getIsPredicted() == ObservationTerms.TRUE );
        assertTrue( theory012.getIsPredicted() == ObservationTerms.TRUE );
    }
}