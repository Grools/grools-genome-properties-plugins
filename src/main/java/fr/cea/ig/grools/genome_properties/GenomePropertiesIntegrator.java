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
import fr.cea.ig.bio.model.genome_properties.ComponentEvidence;
import fr.cea.ig.bio.model.genome_properties.GenomeProperty;
import fr.cea.ig.bio.model.genome_properties.PropertyComponent;
import fr.cea.ig.bio.model.genome_properties.Term;
import fr.cea.ig.bio.scribe.GenomePropertiesReader;
import fr.cea.ig.grools.reasoner.Integrator;
import fr.cea.ig.grools.reasoner.Reasoner;
import fr.cea.ig.grools.fact.PriorKnowledge;
import fr.cea.ig.grools.fact.PriorKnowledgeImpl;
import fr.cea.ig.grools.fact.RelationImpl;
import fr.cea.ig.grools.fact.RelationType;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


final public class GenomePropertiesIntegrator implements Integrator{
    private static final String SOURCE = "Genome Properties";
    private static final Logger LOG = ( Logger ) LoggerFactory.getLogger( GenomePropertiesIntegrator.class );

    @NonNull
    private final InputStream rdf;

    @NonNull
    @Getter
    private final GenomePropertiesReader rdfReader;

    @NonNull
    private final Reasoner grools;

    @NonNull
    private static InputStream getFile( @NonNull final String fileName ) {
        ClassLoader classLoader = GenomePropertiesIntegrator.class.getClassLoader();
        return classLoader.getResourceAsStream( fileName );
    }


    /**
     * isEvidence
     * Return true if term is a ComponentEvidence instance and his category is equal to the parameter category
     *
     * @param term
     * @param category can be "GENPROP", "HMM"
     * @return boolean
     */
    @NonNull
    private static boolean isEvidence( @NonNull final Term term, @NonNull final String category ) {
        return ( term instanceof ComponentEvidence && ( ( ComponentEvidence ) term ).getCategory( ).equals( category ) );
    }


    @NonNull
    private static String descriptionFromTerm( @NonNull final Term term){
        String result;
        if(term instanceof GenomeProperty ){
            final GenomeProperty gp = (GenomeProperty)term;
            result = gp.getTitle() + "~" + gp.getDefinition();
        }
        else if(term instanceof PropertyComponent ){
            final PropertyComponent pc = (PropertyComponent)term;
            result = pc.getTitle();
        }
        else if(term instanceof ComponentEvidence ){
            final ComponentEvidence ce = (ComponentEvidence)term;
            result = ce.getId() + " " + ce.getCategory();
        }
        else
            result = "";
        return result;
    }

    /**
     * toPriorKnowledge
     *
     * @param term
     * @return
     */
    @NonNull
    private static PriorKnowledge toPriorKnowledge( @NonNull final Term term, boolean isDispensable, @NonNull Map<String, PriorKnowledge> knowledges ) {
        final String id = (term instanceof GenomeProperty ) ?  ((GenomeProperty)term).getAccession() : simplifyName( term.getName() );
        final PriorKnowledge pk = PriorKnowledgeImpl.builder()
                                                    .name( id )
                                                    .label( term.getName() )
                                                    .description( descriptionFromTerm(term) )
                                                    .source( "Genome Properties v3.2" )
                                                    .isDispensable( isDispensable )
                                                    .build();
        knowledges.put( pk.getName(), pk );
        return pk;
    }

    private static String simplifyName( final String name ){
        int index = name.indexOf( "_" );
        return name.substring( index+1 );
    }

    public GenomePropertiesIntegrator( @NonNull final Reasoner reasoner ) throws Exception {
        rdf         = getFile( "GenProp_3.2_release.RDF" );
        rdfReader =  new GenomePropertiesReader( rdf );
        grools      = reasoner;
    }

    public GenomePropertiesIntegrator( @NonNull final Reasoner reasoner,  @NonNull final File rdfFile ) throws Exception {
        rdf         = new FileInputStream( rdfFile );
        rdfReader = new GenomePropertiesReader( rdf );
        grools      = reasoner;
    }


    public void integration(  ) {

        final Map<String, PriorKnowledge> knowledges = new HashMap<>();

        for ( final Map.Entry<String, Term> entry : rdfReader.entrySet( ) ) {
            //final String key = entry.getKey();
            final Term term = entry.getValue();
            if ( term instanceof GenomeProperty ) {
                final GenomeProperty    gp      = ( GenomeProperty ) term;
                final PriorKnowledge    child   = ( knowledges.containsKey( gp.getAccession() ) ) ? knowledges.get( gp.getAccession() ) : toPriorKnowledge( gp, false, knowledges );
                Set<Term> terms = rdfReader.getTermsWithId( gp.getAccession( ) );
                for( final Term parentTerm : terms ) {
                    if ( parentTerm instanceof ComponentEvidence ) {
                        final ComponentEvidence ce = ( ComponentEvidence ) parentTerm;
                        //final PropertyComponent pc = ce.getSufficientFor();
                        //final PriorKnowledge parent = ( knowledges.containsKey( pc.getName() ) ) ? knowledges.get( pc.getName() ) : toPriorKnowledge( pc, false, knowledges );
                        final PriorKnowledge parent = ( knowledges.containsKey( simplifyName( ce.getName() ) ) ) ? knowledges.get( simplifyName( ce.getName() ) ) : toPriorKnowledge( ce, false, knowledges );
                        grools.insert( new RelationImpl( child, parent, RelationType.PART ) );
                    }
                }
            } else if ( term instanceof PropertyComponent ) {
                final PropertyComponent pc = ( PropertyComponent ) term;
                if ( pc.getPartOf() != null ) {
                    final PriorKnowledge child  = ( knowledges.containsKey( simplifyName( pc.getName() ) ) ) ? knowledges.get( simplifyName( pc.getName() ) ) : toPriorKnowledge( pc, true, knowledges );
                    if( ! child.getIsDispensable() )
                        child.setIsDispensable(true);
                    final GenomeProperty gp     =  pc.getPartOf();
                    final PriorKnowledge parent = ( knowledges.containsKey( gp.getAccession() ) ) ? knowledges.get( gp.getAccession() ) : toPriorKnowledge( gp, false, knowledges );
                    grools.insert( new RelationImpl( child, parent, RelationType.PART ) );
                } else if ( pc.getRequiredBy() != null ) {
                    final PriorKnowledge child = ( knowledges.containsKey( simplifyName( pc.getName() ) ) ) ? knowledges.get( simplifyName( pc.getName() ) ) : toPriorKnowledge( pc, false, knowledges );
                    final GenomeProperty gp     = pc.getRequiredBy();
                    final PriorKnowledge parent = ( knowledges.containsKey( gp.getAccession() ) ) ? knowledges.get( gp.getAccession() ) : toPriorKnowledge( gp, false, knowledges );
                    grools.insert( new RelationImpl( child, parent, RelationType.PART ) );
                } else
                    LOG.warn( "Component " + pc.getName() + " with any parents!" );
            } else if ( term instanceof ComponentEvidence ) {
                final ComponentEvidence ce      = ( ComponentEvidence ) term;
                //if( ce.getCategory().equals( "HMM" ) ) {
                    final PriorKnowledge child = ( knowledges.containsKey( simplifyName( ce.getName() ) ) ) ? knowledges.get( simplifyName( ce.getName() ) ) : toPriorKnowledge( ce, false, knowledges );
                    final PropertyComponent pc = ce.getSufficientFor();
                    final PriorKnowledge parent = ( knowledges.containsKey( simplifyName( pc.getName() ) ) ) ? knowledges.get( simplifyName( pc.getName() ) ) : toPriorKnowledge( pc, false, knowledges );
                    grools.insert( new RelationImpl( child, parent, RelationType.SUBTYPE ) );
                //}
            } else
                LOG.warn( "Term " + term.getName() + " has an unexpected type!" );
        }
        grools.insert( knowledges.values() );
    }

    public Set<PriorKnowledge> getPriorKnowledgeRelatedToObservationNamed( @NonNull final String source, @NonNull final String id ){
        Set<PriorKnowledge> result = null;
        if( id.startsWith( "GenProp" ) ){
            PriorKnowledge pk = grools.getPriorKnowledge( id );
            if( pk != null ) {
                result = new HashSet<>( 1 );
                result.add( pk );
            }
        }
        else {
            if( ! source.equals("TIGRFAM") && ! source.equals("PFAM") )
                LOG.warn("Only observation from PFAM or TIGRFAM is supported! Source provided: "+source);
            else {
                result = rdfReader.getTermsWithId( id )
                                  .stream()
                                  .map(i -> simplifyName(i.getName()))
                                  .map(grools::getPriorKnowledge)
                                  .collect(Collectors.toSet());
            }
        }
        return result;
    }
}

    
    
