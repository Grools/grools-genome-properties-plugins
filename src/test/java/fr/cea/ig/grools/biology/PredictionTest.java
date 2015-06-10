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
import fr.cea.ig.grools.model.FiveState;
import fr.cea.ig.grools.Grools;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PredictionTest {

    private static final Logger LOG = (Logger) LoggerFactory.getLogger(PredictionTest.class);


    private Grools grools;

    @Before
    public void setUp(){
        final KieSession kieSession = DroolsBuilder.useKieBase("knowledge-reasonning");
        grools = new Grools(kieSession);
        assertNotNull(grools);
    }


    @Test
    public void predictionInferHisNoneExistence1(){
        LOG.debug("Prediction infer his none existence");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .create();

        grools.insert( bk0 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FiveState.UNKNOWN);
    }

    @Test
    public void predictionInferHisNoneExistence2(){
        LOG.debug("Prediction infer his none existence");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0   = new BioPredictionBuilder().setName("bp0")
                                                        .setKnowledgeName("bk0")
                                                        .setPresence(FiveState.UNKNOWN)
                                                        .create();
        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FiveState.UNKNOWN);
    }

    @Test
    public void predictionInferHisPresenceAbsence1(){
        LOG.debug("Prediction infer his presence/absence");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0   = new BioPredictionBuilder().setName("bp0")
                                                        .setKnowledgeName("bk0")
                                                        .setPresence(FiveState.TRUE)
                                                        .create();
        BioPrediction bp1   = new BioPredictionBuilder().setName("bp1")
                                                        .setKnowledgeName("bk0")
                                                        .setPresence(FiveState.FALSE)
                                                        .create();
        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.insert( bp1 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FiveState.BOTH);

    }


    @Test
    public void predictionInferHisPresenceAbsence2(){
        LOG.debug("Prediction infer his presence/absence");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0   = new BioPredictionBuilder().setName("bp0")
                                                        .setKnowledgeName("bk0")
                                                        .setPresence(FiveState.BOTH)
                                                        .create();
        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FiveState.BOTH);
    }


    @Test
    public void predictionInferHisAbsence(){
        LOG.debug("Prediction infer his none existence");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0   = new BioPredictionBuilder().setName("bp0")
                                                        .setKnowledgeName("bk0")
                                                        .setPresence(FiveState.FALSE)
                                                        .create();
        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FiveState.FALSE);

    }

    @Test
    public void predictionInferHisPresence(){
        LOG.debug("Prediction infer his none existence");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                                                    .setSource("junit")
                                                    .create();
        BioPrediction bp0   = new BioPredictionBuilder().setName("bp0")
                                                        .setKnowledgeName("bk0")
                                                        .setPresence(FiveState.TRUE)
                                                        .create();
        grools.insert( bk0 );
        grools.insert( bp0 );
        grools.fireAllRules();

        assertTrue(bk0.getPresence() == FiveState.TRUE);

    }
}
