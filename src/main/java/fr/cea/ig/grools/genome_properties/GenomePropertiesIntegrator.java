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
import fr.cea.ig.genome_properties.model.PropertyComponent;
import fr.cea.ig.genome_properties.model.Term;
import fr.cea.ig.grools.biology.BioKnowledgeBuilder;
import fr.cea.ig.grools.model.NodeType;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import fr.cea.ig.grools.Grools;

final public class GenomePropertiesIntegrator {
    private static  final Logger LOG = (Logger) LoggerFactory.getLogger( GenomePropertiesIntegrator.class );
    private         final Grools grools;
    private         final Map<String,BioKnowledgeBuilder> knowledges; // Key: name, Value: object

    @NotNull
    private InputStream getFile(@NotNull final String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();

        return classLoader.getResourceAsStream(fileName);

    }

    @NotNull
    private BioKnowledgeBuilder toBioKnowledge( @NotNull final Term term, @NotNull final GenomePropertiesParser rdfParser ){
        final BioKnowledgeBuilder knowledge = new BioKnowledgeBuilder();
        knowledge.setName(term.getName());
        if( term instanceof PropertyComponent ) {
            final String                parentStr       = ((PropertyComponent) term).getRequiredBy();
            final Term                  parent          = rdfParser.getTerm(parentStr);
            final BioKnowledgeBuilder   parentKnowledge = knowledges.get(parent.getName());
            parentKnowledge.setNodeType(NodeType.AND);
            knowledge.addPartOf( parentKnowledge );
        }
    }

    public GenomePropertiesIntegrator(final Grools grools) {
        this.grools     = grools;
        this.knowledges = new HashMap<>();
    }

    public void useDefault() throws Exception {
        final InputStream rdf = getFile("GenProp_3.2_release.RDF");
        use( rdf );
    }



    private void use( @NotNull final InputStream rdf ) throws Exception {
        final GenomePropertiesParser rdfParser = new GenomePropertiesParser( rdf );
        rdfParser.forEach( (k,v) -> knowledges.put(
                                                    k,
                                                    new BioKnowledgeBuilder().setName(v.getName())
                                                                             .setId( v.getId() )
                                                   ));

    }

}
