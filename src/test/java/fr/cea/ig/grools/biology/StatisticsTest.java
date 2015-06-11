package fr.cea.ig.grools.biology;
/*
 * Copyright LABGeM 04/03/15
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
import fr.cea.ig.grools.model.KnowledgeStatistics;
import fr.cea.ig.grools.model.NodeType;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;

public class StatisticsTest {

    private static final Logger LOG = (Logger) LoggerFactory.getLogger(StatisticsTest.class);

    private Grools grools;

    @Before
    public void setUp(){
        final KieSession kieSession = DroolsBuilder.useKieBase("knowledge-reasonning");
        kieSession.setGlobal("logger", LOG);
        grools = new Grools(kieSession);
        assertNotNull(grools);
    }

    @Test
    public void completeness1() {
        LOG.debug("completeness (1)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                .setSource("junit")
                .setNodeType(NodeType.AND)
                .create();
        BioKnowledge bk01 = new BioKnowledgeBuilder().setName("bk01")
                .setSource("junit")
                .addPartOf(bk0)
                .create();
        BioKnowledge bk02 = new BioKnowledgeBuilder().setName("bk02")
                .setSource("junit")
                .addPartOf(bk0)
                .create();
        BioPrediction bp01= new BioPredictionBuilder().setName("bp01")
                .setKnowledgeName("bk01")
                .setSource("junit")
                .setPresence(FourState.FALSE)
                .create();
        BioPrediction bp02= new BioPredictionBuilder().setName("bp02")
                .setKnowledgeName("bk02")
                .setSource("junit")
                .setPresence(FourState.FALSE)
                .create();

        grools.insert( bk0 );
        grools.insert( bk01 );
        grools.insert( bk02 );
        grools.insert( bp01 );
        grools.insert(bp02);
        grools.fireAllRules();

        final KnowledgeStatistics ks  =  grools.getStatistics( bk0.getName()  );
        LOG.debug(ks.toString());
        assert( ks.getCompleteness().compareTo( new BigDecimal( 0 ) ) == 0 );
        assert( ks.getConsistency().compareTo( new BigDecimal( 100 ) ) == 0 );
    }

    @Test
    public void completeness2() {
        LOG.debug("completeness (2)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                .setSource("junit")
                .setNodeType(NodeType.AND)
                .create();
        BioKnowledge bk01 = new BioKnowledgeBuilder().setName("bk01")
                .setSource("junit")
                .addPartOf(bk0)
                .create();
        BioKnowledge bk02 = new BioKnowledgeBuilder().setName("bk02")
                .setSource("junit")
                .addPartOf(bk0)
                .create();
        BioPrediction bp01= new BioPredictionBuilder().setName("bp01")
                .setKnowledgeName("bk01")
                .setSource("junit")
                .setPresence(FourState.TRUE)
                .create();
        BioPrediction bp02= new BioPredictionBuilder().setName("bp02")
                .setKnowledgeName("bk02")
                .setSource("junit")
                .setPresence(FourState.TRUE)
                .create();

        grools.insert( bk0 );
        grools.insert( bk01 );
        grools.insert( bk02 );
        grools.insert( bp01 );
        grools.insert( bp02 );
        grools.fireAllRules();

        final KnowledgeStatistics ks  =  grools.getStatistics( bk0.getName()  );

        assert( ks.getCompleteness().compareTo( new BigDecimal( 100 ) ) == 0 );
        assert( ks.getConsistency().compareTo( new BigDecimal( 100 ) ) == 0 );
    }

    @Test
    public void completeness3() {
        LOG.debug("completeness (3)");
        BioKnowledge bk0 = new BioKnowledgeBuilder().setName("bk0")
                .setSource("junit")
                .setNodeType(NodeType.AND)
                .create();
        BioKnowledge bk01 = new BioKnowledgeBuilder().setName("bk01")
                .setSource("junit")
                .addPartOf(bk0)
                .create();
        BioKnowledge bk02 = new BioKnowledgeBuilder().setName("bk02")
                .setSource("junit")
                .addPartOf(bk0)
                .create();
        BioPrediction bp01= new BioPredictionBuilder().setName("bp01")
                .setKnowledgeName("bk01")
                .setSource("junit")
                .setPresence(FourState.FALSE)
                .create();
        BioPrediction bp02= new BioPredictionBuilder().setName("bp02")
                .setKnowledgeName("bk02")
                .setSource("junit")
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
        grools.insert( bp01 );
        grools.insert( bp02 );
        grools.insert( ba0 );
        grools.fireAllRules();

        final KnowledgeStatistics ks  =  grools.getStatistics( bk0.getName()  );
        LOG.debug(ks.toString());
        System.out.println(ks.getConsistency());
        System.out.println(new BigDecimal( 2f*100f/3f ) );
        BigDecimal consistencyExpected = new BigDecimal(2f * 100f / 3f).setScale(3, BigDecimal.ROUND_HALF_UP);
        BigDecimal consistency = ks.getConsistency().setScale(3, BigDecimal.ROUND_HALF_UP);
        System.out.println( consistency.compareTo(consistencyExpected) );
        assert( ks.getCompleteness().compareTo( new BigDecimal(0) ) == 0 );
        assert( consistency.compareTo(consistencyExpected) == 0);

    }
}
