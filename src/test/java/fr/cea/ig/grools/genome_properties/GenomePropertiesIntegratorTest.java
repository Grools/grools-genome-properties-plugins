package fr.cea.ig.grools.genome_properties;


import fr.cea.ig.grools.Reasoner;
import fr.cea.ig.grools.model.OperatorLogic;
import fr.cea.ig.grools.model.Theory;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GenomePropertiesIntegratorTest {
    private Reasoner grools;
    private GenomePropertiesIntegrator integrator;

    @Before
    public void setUp() {
        grools = new Reasoner();
        assertNotNull(grools);
        integrator = new GenomePropertiesIntegrator(grools);
        assertNotNull(integrator);
        try {
            integrator.useDefault();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGenomePropertyIntegration() {
        final Theory theory = grools.getTheory( "4067" );
        assertNotNull(theory);
        assertTrue(theory.getId().equals("4067"));
        assertTrue(theory.getName().equals("gp:Genome_Property_4067"));
        assertEquals( theory.getOperator(), OperatorLogic.AND );
        final List<Theory> variants = grools.getFragmentTheories( theory );
        assertNotNull( variants );
    }
    @Test
    public void testComponentEvidenceOrNode() {
        final Theory theory = grools.getTheory( "GenProp0698" );
        assertNotNull(theory);
        assertTrue(theory.getId().equals("GenProp0698"));
        assertTrue(theory.getName().equals("gp:Component_Evidence_75016"));
        assertEquals( theory.getOperator(), OperatorLogic.OR );
        final List<Theory> variants = grools.getFragmentTheories( theory );
        assertNotNull( variants );
    }

}
