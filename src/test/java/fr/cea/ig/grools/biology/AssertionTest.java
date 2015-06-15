package fr.cea.ig.grools.biology;
/*
 * Copyright LABGeM 16/02/15
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
 */


import ch.qos.logback.classic.Logger;
import fr.cea.ig.grools.model.FourState;
import fr.cea.ig.grools.Grools;
import fr.cea.ig.grools.model.Conclusion;
import fr.cea.ig.grools.model.NodeType;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AssertionTest {

    private static final Logger LOG = (Logger) LoggerFactory.getLogger(AssertionTest.class);


    private Grools grools;

    @Before
    public void setUp(){
        final KieSession kieSession = DroolsBuilder.useKieBase("knowledge-reasonning");
        grools = new Grools(kieSession);
        assertNotNull(grools);
    }

    @Test
    public void presentKnowledgeIsNotAvoidedNorRequiredConclusionIsUnconfirmedPresence1(){
        LOG.debug("Present knowledge without avoided or required assertion: conclusion is unconfirmed presence (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                .setId("bk0")
                .setSource("junit")
                .create();
        BioPrediction bp0= new BioPredictionBuilder().setName("bp0")
                .setId("bp0")
                .setKnowledgeName("bk0")
                .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                .setId("ba0")
                .setKnowledgeId("bk0")
                .setSource("junit")
                .setPresence(FourState.UNKNOWN)
                .create();

        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.UNCONFIRMED_PRESENCE);
    }


    @Test
    public void absentKnowledgeIsNotRequiredNorAvoidedConclusionIsUnconfirmedAbsence1(){
        LOG.debug("Absent knowledge without avoided or required assertion: conclusion is unconfirmed absence (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0= new BioPredictionBuilder().setName("bp0")
                                                     .setId("bp0")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.FALSE)
                                                     .create();

        grools.insert( bk0 );
        grools.insert(bp0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.UNCONFIRMED_ABSENCE);

    }


    @Test
    public void absentKnowledgeIsNotRequiredNorAvoidedConclusionIsUnconfirmedAbsence2(){
        LOG.debug("Absent knowledge without avoided or required assertion: conclusion is unconfirmed absence (2)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0= new BioPredictionBuilder().setName("bp0")
                                                     .setId("bp0")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.FALSE)
                                                     .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.UNKNOWN)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.UNCONFIRMED_ABSENCE);

    }


    @Test
    public void presentAndAbsentKnowledgeIsNotRequiredNorAvoidedConclusionIsUnconfirmedContradictory1(){
        LOG.debug("Present and absent knowledge without avoided or required assertion: conclusion is unconfirmed contradictory (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0= new BioPredictionBuilder().setName("bp0")
                                                     .setId("bp0")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.BOTH)
                                                     .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.UNKNOWN)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.UNCONFIRMED_CONTRADICTORY);

    }
    @Test
    public void presentAndAbsentKnowledgeIsNotRequiredNorAvoidedConclusionIsUnconfirmedContradictory2(){
        LOG.debug("Present and absent knowledge without avoided or required assertion: conclusion is unconfirmed contradictory (2)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp1= new BioPredictionBuilder().setName("bp1")
                                                    .setId("bp1")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.TRUE)
                                                     .create();
        BioPrediction bp2= new BioPredictionBuilder().setName("bp2")
                                                     .setId("bp2")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.FALSE)
                                                     .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.UNKNOWN)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.UNCONFIRMED_CONTRADICTORY);

    }


    @Test
    public void presentAndAbsentKnowledgeIsNotRequiredNorAvoidedConclusionIsContradictory3(){
        LOG.debug("Present and absent knowledge without avoided or required assertion: conclusion is unconfirmed contradictory (3)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.OR)
                                                    .create();
        BioPriorKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setId("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPriorKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setId("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1a= new BioPredictionBuilder().setName("bp1a")
                                                     .setId("bp1a")
                                                     .setKnowledgeName("bk1")
                                                     .setSource("junit")
                                                     .setPresence(FourState.TRUE)
                                                     .create();
        BioPrediction bp1b= new BioPredictionBuilder().setName("bp1z")
                                                     .setId("bp1z")
                                                     .setKnowledgeName("bk1")
                                                     .setSource("junit")
                                                     .setPresence(FourState.FALSE)
                                                     .create();
        BioPrediction bp2= new BioPredictionBuilder().setName("bp2")
                                                     .setId("bp2")
                                                     .setKnowledgeName("bk2")
                                                     .setSource("junit")
                                                     .setPresence(FourState.FALSE)
                                                     .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.UNKNOWN)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1a );
        grools.insert( bp1b );
        grools.insert( bp2 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.UNCONFIRMED_CONTRADICTORY);
        assertTrue(bk1.getConclusion() == Conclusion.UNCONFIRMED_CONTRADICTORY);
        assertTrue(bk2.getConclusion() == Conclusion.UNCONFIRMED_ABSENCE);

    }

    @Test
    public void notObservedKnowledgeIsNotRequiredNorAvoidedConclusionIsNormal1(){
        LOG.debug("Not observed knowledge without avoided or required assertion: conclusion is unknown (1)");
        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();

        grools.insert(bk0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.UNKNOWN);
    }

    @Test
    public void presentKnowledgeIsRequiredConclusionIsNormal1(){
        LOG.debug("Present knowledge has required assertion: conclusion is confirmed presence (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0= new BioPredictionBuilder().setName("bp0")
                                                     .setId("bp0")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.TRUE)
                                                     .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.TRUE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.CONFIRMED_PRESENCE);
    }

    @Test
    public void absentKnowledgeIsRequiredConclusionIsAnUnexpectedAbsence1(){
        LOG.debug("Absent knowledge has required assertion: conclusion is an unexpected absence (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0= new BioPredictionBuilder().setName("bp0")
                                                     .setId("bp0")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.FALSE)
                                                     .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.TRUE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.UNEXPECTED_ABSENCE);
    }

    @Test
    public void presentAndAbsentKnowledgeIsRequiredConclusionIsAcontradictoryAbsence1(){
        LOG.debug("Present and absent knowledge has required assertion: conclusion is a contradictory absence (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp1= new BioPredictionBuilder().setName("bp1")
                                                     .setId("bp1")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.TRUE)
                                                     .create();
        BioPrediction bp2= new BioPredictionBuilder().setName("bp2")
                                                     .setId("bp2")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.FALSE)
                                                     .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.TRUE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.CONTRADICTORY_ABSENCE);
    }

    @Test
    public void notObservedKnowledgeIsRequiredConclusionIsMissing1(){
        LOG.debug("Not observed knowledge has required assertion: conclusion is missing (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setSource("junit")
                                                    .setKnowledgeId("bk0")
                .setPresence(FourState.TRUE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.MISSING);

    }

    @Test
    public void notObservedKnowledgeIsRequiredConclusionIsMissing2(){
        LOG.debug("Not observed knowledge has required assertion: conclusion is missing (2)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0= new BioPredictionBuilder().setName("bp0")
                                                     .setId("bp0")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.UNKNOWN)
                                                     .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.TRUE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.MISSING);

    }

    @Test
    public void presentKnowledgeIsAvoidedConclusionIsAnUnexpectedPresence1(){
        LOG.debug("Present knowledge has avoided assertion: conclusion is an unexpected presence (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0= new BioPredictionBuilder().setName("bp0")
                                                     .setId("bp0")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.TRUE)
                                                     .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setSource("junit")
                                                    .setKnowledgeId("bk0")
                .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.UNEXPECTED_PRESENCE);
    }

    @Test
    public void absentKnowledgeIsAvoidedConclusionIsNormal1(){
        LOG.debug("Absent knowledge has avoided assertion: conclusion is confirmed absence (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0= new BioPredictionBuilder().setName("bp0")
                                                     .setId("bp0")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.FALSE)
                                                     .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.CONFIRMED_ABSENCE);
    }

    @Test
    public void presentAndAbsentKnowledgeIsAvoidedConclusionIsAcontradictoryPresence1(){
        LOG.debug("Present and absent knowledge has avoided assertion: conclusion is a contradictory presence (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp1= new BioPredictionBuilder().setName("bp1")
                                                     .setId("bp1")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.TRUE)
                                                     .create();
        BioPrediction bp2= new BioPredictionBuilder().setName("bp2")
                                                     .setId("bp2")
                                                     .setKnowledgeName("bk0")
                                                     .setPresence(FourState.FALSE)
                                                     .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.CONTRADICTORY_PRESENCE);
    }

    @Test
    public void notObservedKnowledgeIsAvoidedConclusionIsNormal1(){
        LOG.debug("Not Observed knowledge has avoided assertion: conclusion is confirmed absence (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp1= new BioPredictionBuilder().setName("bp1")
                                                    .setId("bk1")
                                                     .setKnowledgeName("bk0")
                                                    .setSource("junit")
                                                     .setPresence(FourState.UNKNOWN)
                                                     .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setKnowledgeId("bk0")
                                                    .setSource("junit")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp1 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.CONFIRMED_ABSENCE);
    }

    @Test
    public void notObservedKnowledgeIsAvoidedConclusionIsNormal2(){
        LOG.debug("NNot Observed knowledge has avoided assertion: conclusion is confirmed absence (2)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setId("ba0")
                                                    .setKnowledgeId("bk0")
                                                    .setSource("junit")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert(ba0);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.CONFIRMED_ABSENCE);
    }

    @Test
    public void presentKnowledgeIsRequiredAndAvoidedConclusionIsAnAmbiguousPresence1(){
        LOG.debug("Present knowledge has required and avoided assertion: conclusion is an ambiguous presence (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp1= new BioPredictionBuilder().setName("bp1")
                                                     .setKnowledgeName("bk0")
                                                    .setPresence(FourState.TRUE)
                                                    .create();
        BioAssertion ba1 = new BioAssertionBuilder().setName("ba1")
                                                    .setId("ba1")
                                                    .setKnowledgeId("bk0")
                                                    .setSource("junit")
                                                    .setPresence(FourState.TRUE)
                                                    .create();
        BioAssertion ba2 = new BioAssertionBuilder().setName("ba2")
                                                    .setKnowledgeId("bk0")
                                                    .setSource("junit")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp1 );
        grools.insert( ba1 );
        grools.insert(ba2);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.AMBIGUOUS_PRESENCE);
    }

    @Test
    public void absentKnowledgeIsRequiredAndAvoidedConclusionIsAnAmbiguousAbsence1(){
        LOG.debug("Absent knowledge has required and avoided assertion: conclusion is an ambiguous absence (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp1= new BioPredictionBuilder().setName("bp1")
                                                    .setId("bp1")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.FALSE)
                                                     .create();
        BioAssertion ba1 = new BioAssertionBuilder().setName("ba1")
                                                    .setId("ba1")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.TRUE)
                                                    .create();
        BioAssertion ba2 = new BioAssertionBuilder().setName("ba2")
                                                    .setId("ba2")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp1 );
        grools.insert( ba1 );
        grools.insert(ba2);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.AMBIGUOUS_ABSENCE);
    }

    @Test
    public void presentAndAbsentKnowledgeIsRequiredAndAvoidedConclusioIsAnAmbiguousPresenceAbsence1(){
        LOG.debug("Present and absent knowledge has required and avoided assertion: conclusion is contradictory (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp1= new BioPredictionBuilder().setName("bp1")
                                                     .setKnowledgeName("bk0")
                                                     .setSource("junit")
                                                     .setPresence(FourState.FALSE)
                                                     .create();
        BioPrediction bp2= new BioPredictionBuilder().setName("bp2")
                                                     .setId("bp2")
                                                     .setKnowledgeName("bk0")
                .setPresence(FourState.TRUE)
                .create();
        BioAssertion ba1 = new BioAssertionBuilder().setName("ba1")
                                                    .setId("ba1")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.TRUE)
                                                    .create();
        BioAssertion ba2 = new BioAssertionBuilder().setName("ba2")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.insert( ba1 );
        grools.insert(ba2);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.AMBIGUOUS_CONTRADICTORY);
    }

    @Test
    public void notObservedKnowledgeIsRequiredAndAvoidedConclusionIsAmbiguous1(){
        LOG.debug("Not Observed knowledge is required and avoided conclusion is ambiguous (1)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setId("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp1= new BioPredictionBuilder().setName("bp1")
                                                    .setId("bp1")
                                                     .setKnowledgeName("bk0")
                                                     .setPresence(FourState.UNKNOWN)
                                                     .create();
        BioAssertion ba1 = new BioAssertionBuilder().setName("ba1")
                                                    .setId("ba1")
                                                    .setSource("junit")
                                                    .setKnowledgeId("bk0")
                .setPresence(FourState.TRUE)
                                                    .create();
        BioAssertion ba2 = new BioAssertionBuilder().setName("ba2")
                                                    .setId("ba2")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bp1 );
        grools.insert( ba1 );
        grools.insert(ba2);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.AMBIGUOUS);
    }

    @Test
    public void notObservedKnowledgeIsRequiredAndAvoidedConclusionIsAmbiguous2(){
        LOG.debug("Not Observed knowledge has required and avoided assertion: conclusion is ambiguous (2)");

        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                .setSource("junit")
                .create();
        BioAssertion ba1 = new BioAssertionBuilder().setName("ba1")
                                                    .setId("ba1")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.TRUE)
                                                    .create();
        BioAssertion ba2 = new BioAssertionBuilder().setName("ba2")
                                                    .setId("ba2")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( ba1 );
        grools.insert(ba2);
        grools.fireAllRules();

        assertTrue(bk0.getConclusion() == Conclusion.AMBIGUOUS);
    }

    @Test
    public void complex1(){
        LOG.debug("complex1");
        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.AND)
                                                    .create();
        BioPriorKnowledge bk01 = new BioKnowledgeBuilder().setName("bk01")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.AND)
                                                    .addPartOf(bk0)
                                                    .create();
        BioPriorKnowledge bk02 = new BioKnowledgeBuilder().setName("bk02")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPriorKnowledge bk011 = new BioKnowledgeBuilder().setName("bk011")
                                                      .setSource("junit")
                                                      .addPartOf(bk01)
                                                      .create();
        BioPriorKnowledge bk012 = new BioKnowledgeBuilder().setName("bk012")
                                                      .setSource("junit")
                                                      .addPartOf(bk01)
                                                      .create();

        BioPrediction bp011 = new BioPredictionBuilder().setName("bp011")
                                                      .setKnowledgeName("bk011")
                                                      .setPresence(FourState.TRUE)
                                                      .create();
        BioPrediction bp012 = new BioPredictionBuilder().setName("bp012")
                                                        .setKnowledgeName("bk012")
                                                        .setPresence(FourState.TRUE)
                                                        .create();
        BioPrediction bp02 = new BioPredictionBuilder().setName("bp02")
                                                       .setKnowledgeName("bk02")
                                                       .setPresence(FourState.FALSE)
                                                       .create();

        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                                                    .setKnowledgeId("bk0")
                .setSource("junit")
                                                    .setPresence(FourState.TRUE)
                                                    .create();
        grools.insert( bk0 );
        grools.insert( bk01 );
        grools.insert( bk02 );
        grools.insert( bk011 );
        grools.insert( bk012 );
        grools.insert( bp011 );
        grools.insert( bp012 );
        grools.insert( bp02 );
        grools.insert( ba0 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.BOTH);
        assertTrue(bk01.getPresence() == FourState.TRUE);
        assertTrue(bk02.getPresence() == FourState.FALSE);
        assertTrue(bk011.getPresence() == FourState.TRUE);
        assertTrue(bk012.getPresence() == FourState.TRUE);
    }


    @Test
    public void complex2(){
        LOG.debug("complex2");
        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                .setSource("junit")
                .setNodeType(NodeType.OR)
                .create();
        BioPriorKnowledge bk01 = new BioKnowledgeBuilder().setName("bk01")
                .setNodeType(NodeType.AND)
                .setSource("junit")
                .addPartOf(bk0)
                .create();
        BioPriorKnowledge bk02 = new BioKnowledgeBuilder().setName("bk02")
                .setSource("junit")
                .addPartOf(bk0)
                .create();
        BioPriorKnowledge bk03 = new BioKnowledgeBuilder().setName("bk03")
                .setNodeType(NodeType.AND)
                .setSource("junit")
                .addPartOf(bk0)
                .create();
        BioPriorKnowledge bk011 = new BioKnowledgeBuilder().setName("bk011")
                .setSource("junit")
                .addPartOf(bk01)
                .create();
        BioPriorKnowledge bk012 = new BioKnowledgeBuilder().setName("bk012")
                .setSource("junit")
                .addPartOf(bk01)
                .create();
        BioPriorKnowledge bk031 = new BioKnowledgeBuilder().setName("bk031")
                .setSource("junit")
                .addPartOf(bk03)
                .create();

        BioPrediction bp011   = new BioPredictionBuilder().setName("bp011")
                .setKnowledgeName("bk011")
                .setPresence(FourState.TRUE)
                .create();
        BioPrediction bp012   = new BioPredictionBuilder().setName("bp012")
                .setKnowledgeName("bk012")
                .setPresence(FourState.TRUE)
                .create();
        BioPrediction bp02   = new BioPredictionBuilder().setName("bp02")
                .setKnowledgeName("bk02")
                .setPresence(FourState.FALSE)
                .create();
        BioPrediction bp031   = new BioPredictionBuilder().setName("bp031")
                .setKnowledgeName("bk031")
                .setPresence(FourState.TRUE)
                .create();

        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                .setKnowledgeId("bk0")
                .setSource("junit")
                .setPresence(FourState.TRUE)
                .create();

        BioAssertion ba03 = new BioAssertionBuilder().setName("ba03")
                .setKnowledgeId("bk03")
                .setSource("junit")
                .setPresence(FourState.TRUE)
                .create();

        grools.insert( bk0 );
        grools.insert( bk01 );
        grools.insert( bk02 );
        grools.insert( bk03 );
        grools.insert( bk011 );
        grools.insert( bk012 );
        grools.insert( bk031 );
        grools.insert( bp011 );
        grools.insert( bp012 );
        grools.insert( bp02 );
        grools.insert( bp031 );
        grools.insert( ba0 );
        grools.insert( ba03 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.TRUE);
        assertTrue(bk01.getPresence() == FourState.TRUE);
        assertTrue(bk02.getPresence() == FourState.FALSE);
        assertTrue(bk011.getPresence() == FourState.TRUE);
        assertTrue(bk012.getPresence() == FourState.TRUE);
    }


    @Test
    public void complex2Unordered(){
        LOG.debug("complex2 unordered");
        BioPriorKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                .setSource("junit")
                .setNodeType(NodeType.OR)
                .create();
        BioPriorKnowledge bk01 = new BioKnowledgeBuilder().setName("bk01")
                .setNodeType(NodeType.AND)
                .setSource("junit")
                .addPartOf(bk0)
                .create();
        BioPriorKnowledge bk02 = new BioKnowledgeBuilder().setName("bk02")
                .setSource("junit")
                .addPartOf(bk0)
                .create();
        BioPriorKnowledge bk03 = new BioKnowledgeBuilder().setName("bk03")
                .setNodeType(NodeType.AND)
                .setSource("junit")
                .addPartOf(bk0)
                .create();
        BioPriorKnowledge bk011 = new BioKnowledgeBuilder().setName("bk011")
                .setSource("junit")
                .addPartOf(bk01)
                .create();
        BioPriorKnowledge bk012 = new BioKnowledgeBuilder().setName("bk012")
                .setSource("junit")
                .addPartOf(bk01)
                .create();
        BioPriorKnowledge bk031 = new BioKnowledgeBuilder().setName("bk031")
                .setSource("junit")
                .addPartOf(bk03)
                .create();

        BioPrediction bp011   = new BioPredictionBuilder().setName("bp011")
                .setKnowledgeName("bk011")
                .setPresence(FourState.TRUE)
                .create();
        BioPrediction bp012   = new BioPredictionBuilder().setName("bp012")
                .setKnowledgeName("bk012")
                .setPresence(FourState.TRUE)
                .create();
        BioPrediction bp02   = new BioPredictionBuilder().setName("bp02")
                .setKnowledgeName("bk02")
                .setPresence(FourState.FALSE)
                .create();
        BioPrediction bp031   = new BioPredictionBuilder().setName("bp031")
                .setKnowledgeName("bk031")
                .setPresence(FourState.TRUE)
                .create();

        BioAssertion ba0 = new BioAssertionBuilder().setName("ba0")
                .setKnowledgeId("bk0")
                .setSource("junit")
                .setPresence(FourState.TRUE)
                .create();

        BioAssertion ba03 = new BioAssertionBuilder().setName("ba03")
                .setKnowledgeId("bk03")
                .setSource("junit")
                .setPresence(FourState.TRUE)
                .create();

        grools.insert( bk012 );
        grools.insert( bp012 );
        grools.insert( bk01 );
        grools.insert( ba03 );
        grools.insert( bk02 );
        grools.insert( bk011 );
        grools.insert( bk03 );
        grools.insert( bk031 );
        grools.insert( bk0 );
        grools.insert( bp011 );
        grools.insert( bp02 );
        grools.insert( bp031 );
        grools.insert( ba0 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.TRUE);
        assertTrue(bk01.getPresence() == FourState.TRUE);
        assertTrue(bk02.getPresence() == FourState.FALSE);
        assertTrue(bk011.getPresence() == FourState.TRUE);
        assertTrue(bk012.getPresence() == FourState.TRUE);
    }
}
