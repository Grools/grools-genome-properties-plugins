package fr.cea.ig.grools.genome_properties;


import fr.cea.ig.grools.Grools;
import fr.cea.ig.grools.model.NodeType;
import fr.cea.ig.grools.model.PriorKnowledge;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GenomePropertiesIntegratorTest {
    private Grools grools;
    private GenomePropertiesIntegrator integrator;

    @Before
    public void setUp() {
        grools = new Grools();
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
        final PriorKnowledge knowledge = grools.hasKnowledgeId("4067");
        assertNotNull(knowledge);
        assertTrue(knowledge.getId().equals("4067"));
        assertTrue(knowledge.getName().equals("gp:Genome_Property_4067"));
        assertTrue( knowledge.getNodeType() == NodeType.AND );
        final List<PriorKnowledge> variants = grools.getSubKnowledge(knowledge);
        assertNotNull( variants );
    }
    @Test
    public void testComponentEvidenceOrNode() {
        final PriorKnowledge knowledge = grools.hasKnowledgeId("GenProp0698");
        assertNotNull(knowledge);
        assertTrue(knowledge.getId().equals("GenProp0698"));
        assertTrue(knowledge.getName().equals("gp:Component_Evidence_75016"));
        assertTrue( knowledge.getNodeType() == NodeType.OR );
        final List<PriorKnowledge> variants = grools.getSubKnowledge(knowledge);
        assertNotNull( variants );
    }

}
