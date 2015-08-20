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
import fr.cea.ig.grools.biology.BioKnowledgeBuilder;
import fr.cea.ig.grools.model.NodeType;
import fr.cea.ig.grools.model.PriorKnowledge;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import fr.cea.ig.grools.Grools;

final public class GenomePropertiesIntegrator {
    private static  final Logger LOG = (Logger) LoggerFactory.getLogger( GenomePropertiesIntegrator.class );
    private         final Grools grools;

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
    private static PriorKnowledge toPriorKnowledge(@NotNull final Term term, @NotNull final GenomePropertiesParser rdfParser, @NotNull final Map<String, Set<String>> propertyToEvidence, @NotNull final Map<String, PriorKnowledge> knowledges){
        final BioKnowledgeBuilder   knowledgeBuilder    = new BioKnowledgeBuilder();
        Term                        parentTerm          = null;
        PriorKnowledge parentKnowledge;
        if( term instanceof PropertyComponent ){
            final PropertyComponent component = (PropertyComponent)term;
            if( component.getRequiredBy() != null ) {
                parentTerm = component.getRequiredBy();
                knowledgeBuilder.setIsMandatory(true);
            }
            else{
                parentTerm = component.getPartOf();
                knowledgeBuilder.setIsMandatory(false);
            }
            knowledgeBuilder.setNodeType( NodeType.OR );
        }
        else if( term instanceof GenomeProperty ){
            final Set<String> accessionSet =  propertyToEvidence.get( term.getName());
            knowledgeBuilder.setNodeType(NodeType.AND);

            if( accessionSet != null ){
                for( final String accession: accessionSet) {
                    final ComponentEvidence     evidence = (ComponentEvidence) rdfParser.getTerm(accession);
                    final PriorKnowledge        component = toPriorKnowledge(evidence.getSufficientFor(), rdfParser, propertyToEvidence, knowledges);
                    final BioKnowledgeBuilder   evidenceKnowledgeBuilder = new BioKnowledgeBuilder().setName(evidence.getName())
                                                                                                    .setId(evidence.getId())
                                                                                                    .setNodeType(NodeType.AND)
                                                                                                    .addPartOf(component);
                    final PriorKnowledge evidenceKnowledge = evidenceKnowledgeBuilder.create();
                    knowledgeBuilder.addPartOf(evidenceKnowledge);
                }
            }
        }
        knowledgeBuilder.setName( term.getName() )
                        .setId(term.getId());
        if( parentTerm != null ){
            parentKnowledge = knowledges.get(parentTerm.getName());
            if(parentKnowledge == null )
                parentKnowledge = toPriorKnowledge( parentTerm , rdfParser, propertyToEvidence, knowledges);
            knowledgeBuilder.addPartOf(parentKnowledge);
        }
        final PriorKnowledge knowledge = knowledgeBuilder.create();
        knowledges.put(term.getName(), knowledge );
        return knowledge;
    }

    @NotNull
    private static Map<String,Set<String>> getPropertyLinkedToEvidence(@NotNull final Set<Map.Entry<String, Term>> entries, @NotNull final GenomePropertiesParser rdfParser ){
        Map<String,Set<String>> result = new HashMap<>();
        for(final Map.Entry<String,Term> entry: entries){
            final String value = entry.getValue().getName();
            if( isGenPropEvidence(entry) ){
                final GenomeProperty property = rdfParser.getTermFromAccession(entry.getValue().getId() );
                if( property != null ) {
                    Set<String> values = result.get(property.getName());
                    if (values == null) {
                        values = new HashSet<>();
                        values.add(value);
                    }
                    result.put(property.getName(), values);
                }
            }
        }
        return result;
    }

    public GenomePropertiesIntegrator(final Grools grools) {
        this.grools     = grools;
    }

    public void useDefault() throws Exception {
        final InputStream rdf = getFile("GenProp_3.2_release.RDF");
        use( rdf );
    }

    public void use( @NotNull final InputStream rdf ) throws Exception {
        final Map<String,PriorKnowledge>    knowledges          = new HashMap<>();
        final GenomePropertiesParser        rdfParser           = new GenomePropertiesParser( rdf );
        final Map<String,Set<String>>            propertyToEvidence  = getPropertyLinkedToEvidence( rdfParser.entrySet(), rdfParser );
        final Set<ComponentEvidence> evidencesHMM               =  rdfParser.entrySet()
                                                                           .stream()
                                                                           .filter(entry -> isHMMEvidence(entry))
                                                                           .map(e ->(ComponentEvidence) e.getValue())
                                                                           .collect(Collectors.toSet());
        for( final ComponentEvidence evidence : evidencesHMM){
            final PriorKnowledge        component           = toPriorKnowledge(evidence.getSufficientFor(), rdfParser, propertyToEvidence, knowledges);
            final BioKnowledgeBuilder   evidenceBuilder    = new BioKnowledgeBuilder();

            evidenceBuilder.setName( evidence.getName() )
                            .setId(evidence.getId())
                            .setNodeType(NodeType.AND)
                            .addPartOf(component);
            knowledges.put(evidence.getName(), evidenceBuilder.create());
        }
        // Now create leaf from evidencesHMM
        //leaf =
        final Map<String, Set<ComponentEvidence>>leafSet = new HashMap<>();
        for( final ComponentEvidence evidence : evidencesHMM){
            Set<ComponentEvidence> values = leafSet.get(evidence.getId() );
            if( values == null )
                values = new HashSet<>();
            values.add(evidence);
            leafSet.put(evidence.getId(), values);
        }
        for( final Map.Entry<String, Set<ComponentEvidence>> entry : leafSet.entrySet() ){
            List<PriorKnowledge> parents = entry.getValue().stream().map(evidence -> knowledges.get(evidence.getName())).collect(Collectors.toList());
            final BioKnowledgeBuilder   leafBuilder    = new BioKnowledgeBuilder();
            leafBuilder.setName( entry.getKey() )
                            .setId(entry.getKey())
                            .setNodeType(NodeType.LEAF)
                            .setPartOf(parents);
            knowledges.put(entry.getKey(), leafBuilder.create());
        }
        knowledges.entrySet().parallelStream().forEach( (entry) -> grools.insert(entry.getValue()) );
    }

}
