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


import ch.qos.logback.classic.Logger;
import fr.cea.ig.genome_properties.GenomePropertiesParser;
import fr.cea.ig.genome_properties.model.ComponentEvidence;
import fr.cea.ig.genome_properties.model.GenomeProperty;
import fr.cea.ig.genome_properties.model.PropertyComponent;
import fr.cea.ig.genome_properties.model.Term;
import fr.cea.ig.grools.Reasoner;
import fr.cea.ig.grools.model.OperatorLogic;
import fr.cea.ig.grools.relevant.RelevantTheory;
import fr.cea.ig.grools.relevant.RelevantTheory.RelevantTheoryBuilder;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


final public class GenomePropertiesIntegrator {
    private static  final String SOURCE = "Genome Properties";
    private static  final Logger LOG = (Logger) LoggerFactory.getLogger( GenomePropertiesIntegrator.class );
    private         final Reasoner grools;

    @NotNull
    private InputStream getFile(@NotNull final String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();

        return classLoader.getResourceAsStream(fileName);

    }


    @NotNull
    private static boolean isGenPropEvidence( @NotNull final Map.Entry<String,Term> entry ){
        return (entry.getValue() instanceof ComponentEvidence && ((ComponentEvidence) entry.getValue()).getCategory().equals("GENPROP"));
    }

    @NotNull
    private static boolean isHMMEvidence( @NotNull final Map.Entry<String,Term> entry ){
        return (entry.getValue() instanceof ComponentEvidence && ((ComponentEvidence) entry.getValue()).getCategory().equals("HMM"));
    }

    @NotNull
    private static RelevantTheory toTheory(@NotNull final Term term, @NotNull final GenomePropertiesParser rdfParser, @NotNull final Map<String, Set<String>> propertyToEvidence, @NotNull final Map<String, RelevantTheory> knowledges){
        final RelevantTheoryBuilder fragmentTheory    = RelevantTheory.builder();
        Term                        parentTerm          = null;
        RelevantTheory theory;
        if( term instanceof PropertyComponent ){
            final PropertyComponent component = (PropertyComponent)term;
            if( component.getRequiredBy() != null )
                parentTerm = component.getRequiredBy();
            else{
                parentTerm = component.getPartOf();
                fragmentTheory.isMandatory( false );
            }
            fragmentTheory.operator( OperatorLogic.OR );
        }
        else if( term instanceof GenomeProperty ){
            final Set<String> accessionSet =  propertyToEvidence.get( term.getName());
            fragmentTheory.operator( OperatorLogic.AND );

            if( accessionSet != null ){
                for( final String accession: accessionSet) {
                    final ComponentEvidence     evidence  = (ComponentEvidence) rdfParser.getTerm(accession);
                    final RelevantTheory        component = toTheory( evidence.getSufficientFor(), rdfParser, propertyToEvidence, knowledges );
                    final RelevantTheoryBuilder evidenceKnowledgeBuilder = RelevantTheory.builder().name( evidence.getName() )
                                                                                                    .id( evidence.getId() )
                                                                                                    .operator( OperatorLogic.OR )
                                                                                                    .source( SOURCE )
                                                                                                    .parent( component );
                    final RelevantTheory evidenceKnowledge = evidenceKnowledgeBuilder.build();
                    knowledges.put(evidenceKnowledge.getName(), evidenceKnowledge );
                    fragmentTheory.parent( evidenceKnowledge );
                }
            }
        }
        fragmentTheory.name( term.getName() )
                        .source( SOURCE )
                        .id( term.getId() );
        if( parentTerm != null ){
            theory = knowledges.get(parentTerm.getName());
            if(theory == null )
                theory = toTheory( parentTerm, rdfParser, propertyToEvidence, knowledges );
            fragmentTheory.parent( theory );
        }
        final RelevantTheory knowledge = fragmentTheory.build();
        knowledges.put(term.getName(), knowledge );
        return knowledge;
    }

    @NotNull
    private static Map<String,Set<String>> getPropertyLinkedToEvidence(@NotNull final Set<Map.Entry<String, Term>> entries, @NotNull final GenomePropertiesParser rdfParser ){
        Map<String,Set<String>> result = new HashMap<>();
        // value = evidence | key = property
        for(final Map.Entry<String,Term> entry: entries){
            final String value = entry.getValue().getName();
            if( isGenPropEvidence( entry ) ){
                final GenomeProperty property = rdfParser.getTermFromAccession( entry.getValue().getId() );
                // Some genprop evidence are not linked to a property ... Nothing to do
                if( property != null ) {
                    final String propertyName   = property.getName();
                    Set<String>  values         = result.get( propertyName );
                    if (values == null) {
                        values = new HashSet<>();
                        values.add(value);
                    }
                    result.put( propertyName, values);
                }
            }
        }
        return result;
    }

    public GenomePropertiesIntegrator(final Reasoner grools) {
        this.grools     = grools;
    }

    public void useDefault() throws Exception {
        final InputStream rdf = getFile("GenProp_3.2_release.RDF");
        use( rdf );
    }

    public void use( @NotNull final InputStream rdf ) throws Exception {
        final Map<String,RelevantTheory>    knowledges          = new HashMap<>();
        final GenomePropertiesParser        rdfParser           = new GenomePropertiesParser( rdf );
        final Map<String,Set<String>>       propertyToEvidence  = getPropertyLinkedToEvidence( rdfParser.entrySet(), rdfParser );
        final Set<ComponentEvidence>        evidencesHMM        = rdfParser.entrySet()
                                                                           .stream()
                                                                           .filter(entry -> isHMMEvidence(entry))
                                                                           .map(e -> (ComponentEvidence) e.getValue())
                                                                           .collect(Collectors.toSet());
        for( final ComponentEvidence evidence : evidencesHMM ){
            final RelevantTheory        component          = toTheory( evidence.getSufficientFor(), rdfParser, propertyToEvidence, knowledges );
            final RelevantTheoryBuilder   evidenceBuilder    = RelevantTheory.builder();

            evidenceBuilder.name( evidence.getName() )
                           .id( evidence.getId() )
                           .operator( OperatorLogic.AND )
                           .parent( component );
            knowledges.put(evidence.getName(), evidenceBuilder.build());
        }
        // Now build leaf from evidencesHMM
        // Many evidencesHMM identify a same tigrfam
        // for this reason they are linked to a common RelevantTheory
        final Map<String, Set<ComponentEvidence>>leafSet = new HashMap<>();
        for( final ComponentEvidence evidence : evidencesHMM ){
            Set<ComponentEvidence> values = leafSet.get(evidence.getId() );
            if( values == null )
                values = new HashSet<>();
            values.add(evidence);
            leafSet.put(evidence.getId(), values);
        }
        for( final Map.Entry<String, Set<ComponentEvidence>> entry : leafSet.entrySet() ){
            final List<RelevantTheory>  parents     = entry.getValue()
                                                           .stream()
                                                           .map(evidence -> knowledges.get(evidence.getName()))
                                                           .collect(Collectors.toList());
            final RelevantTheoryBuilder   leafBuilder = RelevantTheory.builder();
            leafBuilder.name( entry.getKey() )
                       .id( entry.getKey() )
                       .source( SOURCE )
                       .parents( parents );
            knowledges.put(entry.getKey(), leafBuilder.build());
        }
        knowledges.entrySet()
                  .parallelStream()
                  .forEach((entry) -> grools.insert(entry.getValue()) );
    }

}
