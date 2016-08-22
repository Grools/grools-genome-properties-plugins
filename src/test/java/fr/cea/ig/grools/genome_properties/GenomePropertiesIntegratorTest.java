/*
 * Copyright LABGeM 17/08/15
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
package fr.cea.ig.grools.genome_properties;

import fr.cea.ig.bio.model.genome_properties.Term;
import fr.cea.ig.grools.reasoner.Mode;
import fr.cea.ig.grools.reasoner.Reasoner;
import fr.cea.ig.grools.reasoner.ReasonerImpl;
import fr.cea.ig.grools.fact.Concept;
import fr.cea.ig.grools.fact.PriorKnowledge;
import fr.cea.ig.grools.fact.Relation;
import fr.cea.ig.grools.fact.RelationType;
import fr.cea.ig.grools.reasoner.Verbosity;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class GenomePropertiesIntegratorTest {

    Reasoner reasoner;
    GenomePropertiesIntegrator  integrator;

    @Before
    public void setUp(){
        reasoner = new ReasonerImpl( Mode.NORMAL, Verbosity.QUIET );
        assertNotNull(reasoner);
        try {
            integrator = new GenomePropertiesIntegrator(reasoner);
        } catch ( Exception e ) {
            System.err.println( "Error while reading rdf content!" );
            System.err.println( "    - " + e.getMessage() );
        }
        integrator.integration();
    }

    @Test
    public void testGenomePropertyIntegration() throws Exception {
        final Set<Term >           tigrs1 = integrator.getRdfReader( ).getTermsWithId( "TIGR01350" );
        final Set<PriorKnowledge> tigrs2 = integrator.getPriorKnowledgeRelatedToObservationNamed( "TIGRFAM", "TIGR01350" );
        assertNotNull(tigrs1);
        assertNotNull(tigrs2);
        assertEquals( 2, tigrs1.size() );
        assertEquals( 2, tigrs2.size() );
        for( final Term tigr : tigrs1 )
            assertEquals( "TIGR01350", tigr.getId() );
        boolean term1 = tigrs1.stream()
                                .filter( i -> i.getName()
                                               .equals( "gp:Component_Evidence_57170" )  )
                                .findFirst()
                                .isPresent();
        assertTrue( term1 );
        boolean term2 = tigrs1.stream()
                             .filter( i -> i.getName()
                                            .equals( "gp:Component_Evidence_87" )  )
                                .findFirst()
                                .isPresent();
        assertTrue( term2 );
        final PriorKnowledge pk1 = reasoner.getPriorKnowledge( "Evidence_57170" );
        final PriorKnowledge pk2 = reasoner.getPriorKnowledge( "Evidence_87" );
        assertNotNull(pk1);
        assertNotNull(pk2);
        assertTrue( tigrs2.contains( pk1 ) );
        assertTrue( tigrs2.contains( pk2 ) );
        /*final List<PriorKnowledge> variants = reasoner.PriorKnowledge( pk );
        assertNotNull( variants );*/
    }
    
    
    
    @Test
    public void testPropertyRelatedToEvidence() throws Exception {
        final PriorKnowledge pk1     = reasoner.getPriorKnowledge( "GenProp0698" ); // link to GenProp0698 by the accession field
        assertNotNull(pk1);
        assertEquals("GenProp0698", pk1.getName());

        final PriorKnowledge pk3     = reasoner.getPriorKnowledge( "Component_54100" ); //id GenProp0698
        assertNotNull(pk3);
        assertEquals("Component_54100", pk3.getName());

        final PriorKnowledge pk2 = (PriorKnowledge) reasoner.getRelationsWithTarget( pk3 )
                                                            .iterator()
                                                            .next()
                                                            .getSource();

        final Relation       relation= reasoner.getRelation( pk1, pk2, RelationType.PART );
        assertEquals( pk1, relation.getSource() );
        assertEquals( pk2, relation.getTarget() );
        assertEquals( RelationType.PART, relation.getType() );

        final Set<Relation>  relList = reasoner.getRelationsWithSource( pk1 );
        assertEquals( 1, relList.size() );
        assertEquals( relList.iterator().next(), relation );
        assertEquals( relList.iterator().next().getSource(), pk1 );
        assertEquals( relList.iterator().next().getTarget(), pk2 );

        final Set<Relation>  relList2 = reasoner.getRelationsWithSource( pk3 );
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
