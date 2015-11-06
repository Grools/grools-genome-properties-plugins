/*
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

package fr.cea.ig.grools;

import fr.cea.ig.grools.relevant.RelevantTheory;
import fr.cea.ig.grools.relevant.table.*;
import lombok.Data;
import lombok.NonNull;
import org.kie.api.marshalling.Marshaller;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.marshalling.MarshallerFactory;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KnowledgeBaseFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Reasoner
 */
@Data
public class Reasoner {
    private static  final String        KB_NAME = "grools-reasonning";
    private         final KieSession    kieSession;
    private         final KieBase       kBase;
    private         final Mode          mode;


    private static KieBase getKbase(@NonNull final KieServices ks){
        final KieContainer          kContainer  = ks.getKieClasspathContainer();
        final KieBaseConfiguration  kbaseConf   = ks.newKieBaseConfiguration();
        kbaseConf.setProperty("drools.propertySpecific", "ALWAYS");
//        kbaseConf.setProperty("org.drools.ruleEngine", "reteoo");
        return kContainer.newKieBase( KB_NAME, kbaseConf);
    }


    private void init(){
        kieSession.insert( mode );
        for(ConclusionEvaluationCell cell: ConclusionEvaluationTable.EVALUATION_TABLE.toCells( ConclusionEvaluationCell.class ) )
            kieSession.insert( cell );
        for( ObservationEvaluationCell cell: ObservationEvaluationTable.AND.toCells( ObservationEvaluationCell.class ) )
            kieSession.insert( cell );
        for( ObservationEvaluationCell cell: ObservationEvaluationTable.OR.toCells( ObservationEvaluationCell.class ) )
            kieSession.insert( cell );
        for( RelevantTheoryEvaluationCell cell: RelevantTheoryEvaluationTable.AND.toCells( RelevantTheoryEvaluationCell.class ) )
            kieSession.insert( cell );
        for( RelevantTheoryEvaluationCell cell: RelevantTheoryEvaluationTable.OR.toCells( RelevantTheoryEvaluationCell.class ) )
            kieSession.insert( cell );
    }

    public Reasoner(){
        this( Mode.MODE );
    }


    public Reasoner(@NonNull final Mode mode){
        this.mode       = mode;
        this.kBase      = getKbase( KieServices.Factory.get() );
        this.kieSession = kBase.newKieSession();
        init();
    }


    public Reasoner(@NonNull final KieSession kieSession, @NonNull final Mode mode){
        this.mode       = mode;
        this.kBase      = getKbase( KieServices.Factory.get() );
        this.kieSession = kieSession;
        init();
    }


    public Reasoner(@NonNull final KieSession kieSession){
        this( kieSession, Mode.MODE );
    }


    public Reasoner copy(){
        KieSession ksessionCopy;
        try {
            final Marshaller marshaller= MarshallerFactory.newMarshaller( kBase );
            final ByteArrayOutputStream o         = new ByteArrayOutputStream();
            marshaller.marshall(o,kieSession);
            ksessionCopy = marshaller.unmarshall(
                                                        new ByteArrayInputStream( o.toByteArray() ),
                                                        kieSession.getSessionConfiguration(),
                                                        KnowledgeBaseFactory.newEnvironment()
            );
        }
        catch ( Exception e ) {
            throw new RuntimeException(e);
        }
        return new Reasoner(ksessionCopy);
    }


    public void insert(@NonNull final Object object){
        kieSession.insert(object);
    }


    public void fireAllRules(){
        kieSession.fireAllRules();
    }


    public void dispose(){
        kieSession.dispose();
    }


    public Collection<? extends Object> getObjects() {
        return kieSession.getObjects();
    }


    public void enableSpecificReasoning(){
        insert( Message.ENABLE_SPECIFIC );
        fireAllRules();
    }


    public void disableSpecificReasoning(){
        insert( Message.DISABLE_SPECIFIC );
        fireAllRules();
    }


    public void enableMandatoryReasoning(){
        insert( Message.ENABLE_MANDATORY );
        fireAllRules();
    }


    public void disableMandatoryReasoning(){
        insert( Message.DISABLE_MANDATORY );
        fireAllRules();
    }

    private <T> T query( @NonNull final String queryName, @NonNull final String field, Class<T> type){
        final QueryResults rows    = kieSession.getQueryResults( queryName );
        final Iterator<QueryResultsRow> iterator= rows.iterator();
        T result = null;
        if( iterator.hasNext()) {
            final Object object =  iterator.next().get( field );
            if( type.isInstance( object ) )
                result = type.cast( object );
        }
        return result;
    }


    private <T> T query( @NonNull final String queryName, @NonNull final Object parameter, @NonNull final String field, Class<T> type){
        final QueryResults              rows    = kieSession.getQueryResults( queryName, parameter );
        final Iterator<QueryResultsRow> iterator= rows.iterator();
        T result = null;
        if( iterator.hasNext()) {
            final Object object =  iterator.next().get( field );
            if( type.isInstance( object ) )
                result = type.cast( object );
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    private <T> void query( @NonNull final String queryName, @NonNull final String field, @NonNull final List<T> results){
        final QueryResults  rows    = kieSession.getQueryResults( queryName );

        for( final QueryResultsRow r : rows){
            final Object object = r.get( field );
            assert( object instanceof List<?> );
            results.addAll((List<T>) object);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void query( @NonNull final String queryName, @NonNull final Object parameter, @NonNull final String field, @NonNull final List<T> results){
        final QueryResults  rows    = kieSession.getQueryResults( queryName, parameter );

        for( final QueryResultsRow r : rows){
            final Object object = r.get( field );
            assert( object instanceof List<?> );
            results.addAll((List<T>) object);
        }
    }

    public List<RelevantTheory> getTheories() {
        List<RelevantTheory> result = new ArrayList<>(  );
        query("getTheories", "$theories", result);
        return result;
    }
}
