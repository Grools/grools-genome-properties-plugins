package fr.cea.ig.grools.genome_properties;


import fr.cea.ig.grools.Mode;
import fr.cea.ig.grools.Reasoner;
import fr.cea.ig.grools.Verbosity;
import fr.cea.ig.grools.fact.Concept;
import fr.cea.ig.grools.fact.PriorKnowledge;
import fr.cea.ig.grools.drools.ReasonerImpl;

import fr.cea.ig.grools.fact.Relation;
import fr.cea.ig.grools.fact.RelationType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class GenomePropertiesIntegratorTest {

    @Test
    public void testGenomePropertyIntegration() throws Exception {
        Reasoner reasoner = new ReasonerImpl( Mode.NORMAL, Verbosity.QUIET );
        assertNotNull(reasoner);
        GenomePropertiesIntegrator.integration(reasoner);
        final PriorKnowledge pk = reasoner.getPriorKnowledge( "TIGR01350" );
        assertNotNull(pk);
        assertEquals("TIGR01350", pk.getName());
        /*final List<PriorKnowledge> variants = reasoner.PriorKnowledge( pk );
        assertNotNull( variants );*/
    }
    @Test
    public void testPropertyRelatedToEvidence() throws Exception {
        Reasoner reasoner = new ReasonerImpl( Mode.NORMAL, Verbosity.QUIET );
        assertNotNull(reasoner);
        GenomePropertiesIntegrator.integration(reasoner);

        final PriorKnowledge pk1     = reasoner.getPriorKnowledge( "GenProp0698" ); // link to GenProp0698 by the accession field
        assertNotNull(pk1);
        assertEquals("GenProp0698", pk1.getName());

        final PriorKnowledge pk2     = reasoner.getPriorKnowledge( "54100" ); //id GenProp0698
        assertNotNull(pk2);
        assertEquals("54100", pk2.getName());

        final Relation       relation= reasoner.getRelation( pk1, pk2, RelationType.PART );
        assertEquals( pk1, relation.getSource() );
        assertEquals( pk2, relation.getTarget() );
        assertEquals( RelationType.PART, relation.getType() );

        final Set<Relation>  relList = reasoner.getRelationsWithSource( pk1 );
        assertEquals( 1, relList.size() );
        assertEquals( relList.iterator().next(), relation );
        assertEquals( relList.iterator().next().getSource(), pk1 );
        assertEquals( relList.iterator().next().getTarget(), pk2 );

        final Set<Relation>  relList2 = reasoner.getRelationsWithSource( pk2 );
        assertEquals( 1, relList2.size() );
        final Concept prop0700 = relList2.iterator().next().getTarget();
        assertTrue( prop0700 instanceof PriorKnowledge );

        final Set<Relation>  relList3 = reasoner.getRelationsWithTarget( prop0700 );
        assertEquals( 3, relList3.size() );


        /*assertTrue(pk.getName().equals("gp:Component_Evidence_75016"));
        assertEquals( pk.getOperator(), OperatorLogic.OR );
        final List<PriorKnowledge> variants = grools.getFragmentTheories( pk );
        assertNotNull( variants );*/    }

}
