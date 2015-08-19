package fr.cea.ig.grools;
/*
 * Copyright LABGeM 26/03/15
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


import fr.cea.ig.grools.model.PriorKnowledge;
import fr.cea.ig.grools.model.KnowledgeStatistics;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.marshalling.Marshaller;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.api.runtime.rule.Variable;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.marshalling.MarshallerFactory;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 */
/*
 * @startuml
 * class Grools{
 *  -kSession : KieSession
 *  -kbase : KieBase
 *  -mode : Mode
 *  +Grools()
 *  +getKSession() : KieSession
 *  +getStatistics(): List<KnowledgeStatistics>
 *  +getStatistics( final String knowledgeId ): KnowledgeStatistics
 *  +getSubKnowledge( final Knowledge knowledge ) : List<Knowledge>
 *  +getUnknownKnowledges( ) : List<Knowledge>
 *  +getMissingKnowledgesConclusion( ) : List<Knowledge>
 *  +insert( final Object object) : void
 * }
 * @enduml
 */
public final class Grools {
    private static final String KNAME = "knowledge-reasonning";
    private final KieSession    kieSession;
    private final KieBase       kbase;


    private <T> T query( @NotNull final String queryName, @NotNull final String field, Class<T> type){
        final QueryResults              rows    = kieSession.getQueryResults( queryName );
        final Iterator<QueryResultsRow> iterator= rows.iterator();
        T result = null;
        if( iterator.hasNext()) {
            final Object object =  iterator.next().get( field );
            assert(type.cast( object ) != null);
            result = type.cast( object );
        }
        return result;
    }


    private <T> T query( @NotNull final String queryName, @NotNull final Object parameter, @NotNull final String field, Class<T> type){
        final QueryResults              rows    = kieSession.getQueryResults( queryName, parameter );
        final Iterator<QueryResultsRow> iterator= rows.iterator();
        T result = null;
        if( iterator.hasNext()) {
            final Object object =  iterator.next().get( field );
            assert(type.cast( object ) != null);
            result = type.cast( object );
        }
        return result;
    }


    private <T> void query( @NotNull final String queryName, @NotNull final String field, @NotNull final List<T> results){
        final QueryResults  rows    = kieSession.getQueryResults( queryName );

        for( final QueryResultsRow r : rows){
            final Object object = r.get( field );
            assert( object instanceof List<?> );
            results.addAll((List<T>) object);
        }
    }

    private <T> void query( @NotNull final String queryName, @NotNull final Object parameter, @NotNull final String field, @NotNull final List<T> results){
        final QueryResults  rows    = kieSession.getQueryResults( queryName, parameter );

        for( final QueryResultsRow r : rows){
            final Object object = r.get( field );
            assert( object instanceof List<?> );
            results.addAll((List<T>) object);
        }
    }


    public Grools(@NotNull final Mode mode){
        final KieServices                   ks          = KieServices.Factory.get();
        final KieContainer                  kContainer  = ks.getKieClasspathContainer();
        final KieBaseConfiguration          kbaseConf   = ks.newKieBaseConfiguration();
        kbaseConf.setProperty("drools.propertySpecific", "ALWAYS");
//        kbaseConf.setProperty("org.drools.ruleEngine", "reteoo");
        kbase       = kContainer.newKieBase(KNAME, kbaseConf);
        kieSession  = kbase.newKieSession();
        kieSession.insert(mode);
    }


    public Grools(){
        this(new Mode("STRONG"));
    }


    public Grools(@NotNull final KieSession kieSession, @NotNull final Mode mode){
        final KieSessionConfiguration kConfiguration   = kieSession.getSessionConfiguration();
        kConfiguration.setProperty("drools.propertySpecific", "ALWAYS");
//        kConfiguration.setProperty("org.drools.ruleEngine", "reteoo");
        this.kbase      = kieSession.getKieBase();
        this.kieSession = kieSession;
        kieSession.insert(mode);
    }


    public Grools(@NotNull final KieSession kieSession){
        this(kieSession, new Mode("STRONG"));
    }


    @NotNull
    public KieSession getKieSession(){
        return kieSession;
    }


    public void setStrongMode(){
        kieSession.insert("STRONG");
        fireAllRules();
    }


    public void setWeakMode(){
        kieSession.insert("WEAK");
        fireAllRules();
    }

    @NotNull
    public List<KnowledgeStatistics> getStatistics(){
        final List<KnowledgeStatistics> results = new ArrayList<>();
        query("allKnowledgeStatistics", Variable.v, "$knowledgeStatisticsList", results);
        return results;
    }


    public KnowledgeStatistics getStatistics( @NotNull final String knowledgeId){
        return query("specificKnowledgeStatistics", knowledgeId,"$knowledgeStatistics", KnowledgeStatistics.class );
    }


    public PriorKnowledge hasKnowledgeId( @NotNull final String id ){
        return query("hasKnowledgeId", id, "$knowledge",PriorKnowledge.class);
    }


    @NotNull
    public List<PriorKnowledge> getSubKnowledge( @NotNull final PriorKnowledge priorKnowledge){
        final List<PriorKnowledge> results = new ArrayList<>();
        query("allSubKnowledge", priorKnowledge,"$childs", results );
        return results;

    }


    @NotNull
    public List<PriorKnowledge> getUnknownKnowledgesPresence( ){
        List<PriorKnowledge> results = new ArrayList<>();
        query("knowledgePresenceIsUnknown", "$knowledges", results);
        return results;

    }


    @NotNull
    public List<PriorKnowledge> getMissingKnowledgesConclusion( ){
        List<PriorKnowledge> results = new ArrayList<>();
        query("knowledgeConclusionIsMissing","$knowledges", results);
        return results;

    }


    public Mode getCurrentMode(){
        return query("currentMode", "$mode", Mode.class );
    }

    public void insert(@NotNull final Object object){
        kieSession.insert(object);
    }

    public void fireAllRules(){
        kieSession.fireAllRules();
    }


    public Stream<PriorKnowledge> getKnowledges(){
        return (Stream<PriorKnowledge>) kieSession.getObjects().stream().filter(o -> o instanceof PriorKnowledge);
    }

    public Grools copy(){
        KieSession ksessionCopy;
        try {
            final Marshaller              marshaller= MarshallerFactory.newMarshaller(kbase);
            final ByteArrayOutputStream   o         = new ByteArrayOutputStream();
            marshaller.marshall(o,kieSession);
            ksessionCopy = marshaller.unmarshall(
                                                    new ByteArrayInputStream( o.toByteArray() ),
                                                    kieSession.getSessionConfiguration(),
                                                    KnowledgeBaseFactory.newEnvironment()
                                                );
        }
        catch (  Exception e) {
            throw new RuntimeException(e);
        }
        return new Grools(ksessionCopy);
    }

    public Collection<? extends Object> getObjects() {
        return kieSession.getObjects();
    }
}
