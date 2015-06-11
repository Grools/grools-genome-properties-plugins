package fr.cea.ig.grools.biology;
/*
 * Copyright LABGeM 10/02/15
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
import fr.cea.ig.grools.Grools;
import fr.cea.ig.grools.model.FourState;
import fr.cea.ig.grools.model.NodeType;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class KnowledgeTest {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(KnowledgeTest.class);

    private Grools grools;

    @Before
    public void setUp(){
        final KieSession kieSession = DroolsBuilder.useKieBase("knowledge-reasonning");
        grools = new Grools(kieSession);
        assertNotNull(grools);
    }


    @Test
    public void andKnowledgeHasUnknownExistence1() {
        LOG.debug("And Knowledge has an unknown existence (1)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.AND)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.fireAllRules();

        grools.getObjects().stream().filter(o -> o instanceof BioKnowledge).forEach(o -> {
            final BioKnowledge bk = ((BioKnowledge) o);
            LOG.debug(bk.toString());
            assertTrue(bk.getPresence() == FourState.UNKNOWN);
        });

    }


    @Test
    public void andKnowledgeHasUnknownExistence2() {
        LOG.debug("And Knowledge has an unknown existence (2)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.AND)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1 = new BioPredictionBuilder().setName("bp1")
                                                    .setKnowledgeName("bk1")
                                                    .setPresence(FourState.UNKNOWN)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.fireAllRules();

        grools.getKnowledges().forEach(o -> {
            final BioKnowledge bk = ((BioKnowledge) o);
            LOG.debug(bk.toString());
            assertTrue(bk.getPresence() == FourState.UNKNOWN);
        });

    }


    @Test
    public void andKnowledgeHasUnknownExistence3() {
        LOG.debug("And Knowledge has an unknown existence (3)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.AND)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1 = new BioPredictionBuilder().setName("bp1")
                                                    .setKnowledgeName("bk1")
                                                    .setPresence(FourState.TRUE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.fireAllRules();

        grools.getKnowledges().forEach(o -> {
            final BioKnowledge bk = ((BioKnowledge) o);
            LOG.debug(bk.toString());
            switch (bk.getName()) {
                case "bk1": assertTrue(bk.getPresence() == FourState.TRUE); break;
                default:assertTrue(bk.getPresence() == FourState.UNKNOWN); break;
            }
        });

    }


    @Test
    public void andKnowledgeIsPresentAbsent1() {
        LOG.debug("And Knowledge is present/absent (1)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.AND)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                        .setKnowledgeName("bk1")
                                                        .setPresence(FourState.TRUE)
                                                        .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                        .setKnowledgeName("bk2")
                                                        .setPresence(FourState.BOTH)
                                                        .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.BOTH);
        assertTrue(bk1.getPresence() == FourState.TRUE);
        assertTrue(bk2.getPresence() == FourState.BOTH);
    }

    @Test
    public void andKnowledgeIsPresentAbsent2() {
        LOG.debug("And Knowledge is present/absent (2)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.AND)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                        .setKnowledgeName("bk1")
                                                        .setPresence(FourState.BOTH)
                                                        .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                        .setKnowledgeName("bk2")
                                                        .setPresence(FourState.BOTH)
                                                        .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.BOTH);
        assertTrue(bk1.getPresence() == FourState.BOTH);
        assertTrue(bk2.getPresence() == FourState.BOTH);

    }
    @Test
    public void andKnowledgeIsPresentAbsent3() {
        LOG.debug("And Knowledge is present/absent (3)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.AND)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                        .setKnowledgeName("bk1")
                                                        .setPresence(FourState.UNKNOWN)
                                                        .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                        .setKnowledgeName("bk2")
                                                        .setPresence(FourState.BOTH)
                                                        .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.BOTH);
        assertTrue(bk1.getPresence() == FourState.UNKNOWN);
        assertTrue(bk2.getPresence() == FourState.BOTH);
    }


    @Test
    public void andKnowledgeIsPresentAbsent4(){
        LOG.debug("And Knowledge is present/absent (4)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.AND)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                    .setKnowledgeName("bk1")
                                                    .setPresence(FourState.TRUE)
                                                    .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                    .setKnowledgeName("bk2")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.BOTH);
        assertTrue(bk1.getPresence() == FourState.TRUE);
        assertTrue(bk2.getPresence() == FourState.FALSE);
    }


    @Test
    public void andKnowledgeIsPresentAbsent5(){
        LOG.debug("And Knowledge is present/absent (5)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.AND)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1 = new BioPredictionBuilder().setName("bp1")
                                                      .setKnowledgeName("bk1")
                                                      .setPresence(FourState.FALSE)
                .create();
        BioPrediction bp2 = new BioPredictionBuilder().setName("bp2")
                                                      .setKnowledgeName("bk2")
                                                      .setPresence(FourState.TRUE)
                .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.BOTH);
        assertTrue(bk1.getPresence() == FourState.FALSE);
        assertTrue(bk2.getPresence() == FourState.TRUE);
    }


    @Test
    public void andKnowledgeIsAbsent1(){
            LOG.debug("And Knowledge is absent (1)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.AND)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                        .setKnowledgeName("bk1")
                                                        .setPresence(FourState.FALSE)
                                                        .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                        .setKnowledgeName("bk2")
                                                        .setPresence(FourState.FALSE)
                                                        .create();

        assertTrue(bk1.getPartOf()[0] == bk0);

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert(bk2);
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.FALSE);
        assertTrue(bk1.getPresence() == FourState.FALSE);
        assertTrue(bk2.getPresence() == FourState.FALSE);
    }


    @Test
    public void andKnowledgeIsAbsent2(){
        LOG.debug("And Knowledge is absent (2)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.AND)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                        .setKnowledgeName("bk1")
                                                        .setPresence(FourState.FALSE)
                                                        .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                        .setKnowledgeName("bk2")
                                                        .setPresence(FourState.BOTH)
                                                        .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.FALSE);
        assertTrue(bk1.getPresence() == FourState.FALSE);
        assertTrue(bk2.getPresence() == FourState.BOTH);
    }


    @Test
    public void orKnowledgeHasNoneExistence1(){
        LOG.debug("Or Knowledge has an unknown existence(1)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.OR)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                        .setKnowledgeName("bk1")
                                                        .setPresence(FourState.UNKNOWN)
                                                        .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                        .setKnowledgeName("bk2")
                                                        .setPresence(FourState.UNKNOWN)
                                                        .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert(bk2);
        grools.insert(bp1);
        grools.insert(bp2);
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.UNKNOWN);
        assertTrue(bk1.getPresence() == FourState.UNKNOWN   );
        assertTrue(bk2.getPresence() == FourState.UNKNOWN);
    }



    @Test
    public void orKnowledgeIsPresentAbsent1(){
        LOG.debug("Or Knowledge is present/absent (1)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.OR)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                    .setKnowledgeName("bk1")
                                                    .setPresence(FourState.BOTH)
                                                    .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                    .setKnowledgeName("bk2")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.BOTH);
        assertTrue(bk1.getPresence() == FourState.BOTH);
        assertTrue(bk2.getPresence() == FourState.FALSE);
    }



    @Test
    public void orKnowledgeIsPresentAbsent2(){
        LOG.debug("Or Knowledge is present/absent (2)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.OR)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                    .setKnowledgeName("bk1")
                                                    .setPresence(FourState.BOTH)
                                                    .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                    .setKnowledgeName("bk2")
                                                    .setPresence(FourState.BOTH)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert(bp2);
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.BOTH);
        assertTrue(bk1.getPresence() == FourState.BOTH);
        assertTrue(bk2.getPresence() == FourState.BOTH);
    }


    @Test
    public void orKnowledgeIsAbsent1(){
        LOG.debug("Or Knowledge is absent (1)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.OR)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                    .setKnowledgeName("bk1")
                                                    .setPresence(FourState.FALSE)
                                                    .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                    .setKnowledgeName("bk2")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.FALSE);
        assertTrue(bk1.getPresence() == FourState.FALSE);
        assertTrue(bk2.getPresence() == FourState.FALSE);
    }


    @Test
    public void orKnowledgeIsPresent1(){
        LOG.debug("Or Knowledge is present (1)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.OR)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                    .setKnowledgeName("bk1")
                                                    .setPresence(FourState.TRUE)
                                                    .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                    .setKnowledgeName("bk2")
                                                    .setPresence(FourState.TRUE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.TRUE);
        assertTrue(bk1.getPresence() == FourState.TRUE);
        assertTrue(bk2.getPresence() == FourState.TRUE);
    }


    @Test
    public void orKnowledgeIsPresent2(){
        LOG.debug("Or Knowledge is present (2)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.OR)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                    .setKnowledgeName("bk1")
                                                    .setPresence(FourState.TRUE)
                                                    .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                    .setKnowledgeName("bk2")
                                                    .setPresence(FourState.FALSE)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.TRUE);
        assertTrue(bk1.getPresence() == FourState.TRUE);
        assertTrue(bk2.getPresence() == FourState.FALSE);
    }


    @Test
    public void orKnowledgeIsPresent3(){
        LOG.debug("Or Knowledge is present (3)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.OR)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                    .setKnowledgeName("bk1")
                                                    .setPresence(FourState.TRUE)
                                                    .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                    .setKnowledgeName("bk2")
                                                    .setPresence(FourState.BOTH)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.TRUE);
        assertTrue(bk1.getPresence() == FourState.TRUE);
        assertTrue(bk2.getPresence() == FourState.BOTH);
    }


    @Test
    public void orKnowledgeIsPresent4(){
        LOG.debug("Or Knowledge is present (4)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .setNodeType(NodeType.OR)
                                                    .create();
        BioKnowledge bk1 = new BioKnowledgeBuilder().setName("bk1")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioKnowledge bk2 = new BioKnowledgeBuilder().setName("bk2")
                                                    .setSource("junit")
                                                    .addPartOf(bk0)
                                                    .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                    .setKnowledgeName("bk1")
                                                    .setPresence(FourState.TRUE)
                                                    .create();
        BioPrediction bp2   = new BioPredictionBuilder().setName("bp2")
                                                    .setKnowledgeName("bk2")
                                                    .setPresence(FourState.UNKNOWN)
                                                    .create();

        grools.insert( bk0 );
        grools.insert( bk1 );
        grools.insert( bk2 );
        grools.insert( bp1 );
        grools.insert( bp2 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FourState.TRUE);
        assertTrue(bk1.getPresence() == FourState.TRUE);
        assertTrue(bk2.getPresence() == FourState.UNKNOWN);
    }


}
