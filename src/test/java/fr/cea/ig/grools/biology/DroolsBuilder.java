package fr.cea.ig.grools.biology;
/*
 * Copyright LABGeM 30/01/15
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


import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.MBeansOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public final class DroolsBuilder {

    public static KieSession useKieSession(final String name) {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kSession = kContainer.newKieSession(name);
        //kSession.addEventListener( new DebugAgendaEventListener() );
        kSession.addEventListener(new DebugDRL());
        //kSession.addEventListener( new DebugRuleRuntimeEventListener() );
        //kSession.addEventListener( new DefaultRuleRuntimeEventListener() );
        //ksession.execute( kieServices.getCommands().newInsertElements( Arrays.asList( new Object[] { application, applicant } ) );
        return kSession;
    }

    public static KieSession useKieBase(final String name) {
        final KieServices           ks          = KieServices.Factory.get();
        final KieContainer          kContainer  = ks.getKieClasspathContainer();
        final KieBaseConfiguration  kbaseConf   = ks.newKieBaseConfiguration();
        kbaseConf.setOption(MBeansOption.ENABLED);
        final KieBase               kbase       = kContainer.newKieBase(name, kbaseConf);
        final KieSession             kSession = kbase.newKieSession();
        //kSession.addEventListener( new DebugAgendaEventListener() );
        kSession.addEventListener(new DebugDRL());
        //kSession.addEventListener( new DebugRuleRuntimeEventListener() );
        //kSession.addEventListener( new DefaultRuleRuntimeEventListener() );
        //ksession.execute( kieServices.getCommands().newInsertElements( Arrays.asList( new Object[] { application, applicant } ) );
        return kSession;
    }

}
