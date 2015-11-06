package fr.cea.ig.grools;/*
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;


import org.kie.api.runtime.KieSession;


/**
 * fr.cea.ig.grools.ReasonerTest
 */
public final class ReasonerTest {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(ReasonerTest.class);
    private KieSession  kieSession;
    private Reasoner    grools;

    @Before
    public void setUp(){
        kieSession = DroolsBuilder.useKieBase("grools-reasonning");
        grools = new Reasoner(kieSession);
        assertNotNull(grools);
    }

    @Test
    public void copyReasoner(){
        final Reasoner bcopy = grools.copy();
        assertNotNull( bcopy );
        assertTrue(bcopy.getKieSession() != grools.getKieSession());
    }

    @Test
    public void mandatoryReasoningIsDisable(){
        grools.disableMandatoryReasoning();
        final Mode mode = grools.getMode();
        assertNotNull(mode);
        assertFalse( mode.getIsMandatoryRuleEnabled() );
    }

    @Test
    public void mandatoryReasoningIsEnable(){
        grools.enableMandatoryReasoning();
        final Mode mode    = grools.getMode();
        assertTrue( mode.getIsMandatoryRuleEnabled() );
    }

    @Test
    public void specificReasoningIsDisable(){
        grools.disableSpecificReasoning();
        final Mode mode = grools.getMode();
        assertNotNull(mode);
        assertFalse( mode.getIsSpecificRuleEnabled() );
    }

    @Test
    public void specificReasoningIsEnable(){
        grools.enableSpecificReasoning();
        final Mode mode      = grools.getMode();
        assertTrue( mode.getIsSpecificRuleEnabled() );
    }
}
