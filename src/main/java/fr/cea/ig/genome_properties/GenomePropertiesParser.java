/*
 * Copyright LABGeM 12/08/15
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
package fr.cea.ig.genome_properties;

import fr.cea.ig.genome_properties.model.*;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenomePropertiesParser  implements Iterable{
    private static final int PAGE_SIZE              = 4_096;
    private static final int DEFAULT_NUMBER_PAGE    = 10;
    private Map<String,Term> terms;

    /**
     * @param input stream from obo file to read
     * @param numberPage custom number page to used, default 10 page size
     * @throws IOException if an error occur while trying to read the file
     */
    public GenomePropertiesParser( @NotNull final InputStream input, final int numberPage) throws Exception {
        final InputStreamReader             isr                         = new InputStreamReader(input, Charset.forName("US-ASCII"));
        final BufferedReader                br                          = new BufferedReader(isr, PAGE_SIZE * numberPage);
        final Map<String, Set<PropertyComponentBuilder>> requiredBy     = new HashMap<>();
        final Map<String, Set<PropertyComponentBuilder>> partOf         = new HashMap<>();
        final Map<String, Set<ComponentEvidenceBuilder>> sufficientFor  = new HashMap<>();
        final Map<String, TermBuilder>                   termsBuilder   = new HashMap<>();
        terms                                                           = new HashMap<>();
        String line = br.readLine();
        while (line != null) {
            if( line.startsWith("gp:") ){
                TermBuilder builder = null;
                if ( line.startsWith("gp:Genome_Property") )
                    builder = new GenomePropertyBuilder();
                else if(line.startsWith("gp:Property_Component") )
                    builder = new PropertyComponentBuilder();
                else if(line.startsWith("gp:Component_Evidence") )
                    builder = new ComponentEvidenceBuilder();
                else
                    throw new IOException("Unknown type of terms: "+line);
                final String name   = line;
                line                = br.readLine();
                builder.setName( name );
                if( line == null )
                    throw new IOException("Malformated File");
                while( ! line.equals( "." )  ){
                    line = line.trim();
                    if( line.startsWith(":"))
                        line = line.substring(1);
                    if(line.startsWith("accession"))
                        ((GenomePropertyBuilder) builder).setAccession(getValue(line));
                    else if(line.startsWith("id"))
                        builder.setId( getValue(line) );
                    else if(line.startsWith("a")) {
                        //do nothing
                    }
                    else {
                        final String value = getValue( line );
                        if (line.startsWith("category"))
                            ((BuildCategory) builder).setCategory(value);
                        else if (line.startsWith("threshold"))
                            ((GenomePropertyBuilder) builder).setThreshold(Integer.parseInt(value));
                        else if (line.startsWith("title"))
                            ((BuildTitle) builder).setTitle( value );
                        else if (line.startsWith("definition"))
                            ((GenomePropertyBuilder) builder).setDefinition( value );
                        else if (line.startsWith("required_by")) {
                            final PropertyComponentBuilder componentBuilder = (PropertyComponentBuilder) builder;
                            componentBuilder.setRelationType(RelationType.REQUIRED_BY);
                            Set<PropertyComponentBuilder> childs = requiredBy.get(value);
                            if( childs == null )
                                childs = new HashSet<>();
                            childs.add(componentBuilder);
                            requiredBy.put(value, childs);
                        }
                        else if (line.startsWith("sufficient_for")) {
                            final ComponentEvidenceBuilder evidenceBuilder = (ComponentEvidenceBuilder) builder;
                            Set<ComponentEvidenceBuilder> childs = sufficientFor.get(value);
                            if( childs == null )
                                childs = new HashSet<>();
                            childs.add(evidenceBuilder);
                            sufficientFor.put(value, childs);
                        }
                        else if (line.startsWith("part_of")) {
                            final PropertyComponentBuilder componentBuilder = (PropertyComponentBuilder) builder;
                            componentBuilder.setRelationType(RelationType.PART_OF);
                            Set<PropertyComponentBuilder> childs = partOf.get(value);
                            if( childs == null )
                                childs = new HashSet<>();
                            childs.add(componentBuilder);
                            partOf.put(value, childs);
                        }
                        else
                            throw new IOException("Unknown type of property: " + line);
                    }
                    line = br.readLine();
                    if( line == null )
                        throw new IOException("Malformated File");
                }
                termsBuilder.put(name, builder);
            }
            line = br.readLine();
        }
        isr.close();
        br.close();

        for( final Map.Entry<String, Set<PropertyComponentBuilder>> entry : requiredBy.entrySet() ){
            final String                        propertyName    = entry.getKey();
            final Set<PropertyComponentBuilder> childs          = entry.getValue();
            final GenomePropertyBuilder         propertyBuilder = (GenomePropertyBuilder) termsBuilder.get(propertyName);
            final GenomeProperty                property        = propertyBuilder.create();
            if( ! terms.containsKey(propertyName))
                terms.put(propertyName, property);
            for( final PropertyComponentBuilder componentBuilder : childs ){
                componentBuilder.setRequiredBy(property);
                final PropertyComponent component =  componentBuilder.create();
                terms.put(component.getName(), component);
            }
        }

        for( final Map.Entry<String, Set<PropertyComponentBuilder>> entry : partOf.entrySet() ){
            final String                        propertyName    = entry.getKey();
            final Set<PropertyComponentBuilder> childs          = entry.getValue();
            final GenomePropertyBuilder         propertyBuilder = (GenomePropertyBuilder) termsBuilder.get(propertyName);
            final GenomeProperty                property        = propertyBuilder.create();
            if( ! terms.containsKey(propertyName))
                terms.put(propertyName, property);
            for( final PropertyComponentBuilder componentBuilder : childs ) {
                componentBuilder.setPartOf(property);
                final PropertyComponent component = componentBuilder.create();
                terms.put(component.getName(), component);
            }
        }

        for( final Map.Entry<String, Set<ComponentEvidenceBuilder>> entry : sufficientFor.entrySet() ){
            final String                        componentName   = entry.getKey();
            final Set<ComponentEvidenceBuilder> childs          = entry.getValue();
            final PropertyComponentBuilder      componentBuilder= (PropertyComponentBuilder) termsBuilder.get(componentName);
            final PropertyComponent             component       = componentBuilder.create();
            if( ! terms.containsKey(componentName))
                terms.put(componentName, component);
            for( final ComponentEvidenceBuilder evidenceBuilder : childs ) {
                evidenceBuilder.setSufficientFor(component);
                final ComponentEvidence evidence = evidenceBuilder.create();
                terms.put(evidence.getName(), evidence);
            }
        }


    }

    private static String getValue(final String line) throws Exception {
        final Pattern pattern = Pattern.compile("\\s*:?\\w+\\s+:?\"?([^\"]*)\"?;?");
        final Matcher matcher = pattern.matcher(line);
        final boolean isFound = matcher.find();
        String result = "";
        if( isFound )
            result = matcher.group(1);
        else
            throw new IOException("Unfound value from: "+line);
        return result;
    }


    /**
     * @param path path location to the obo file to read
     * @throws IOException if an error occur while trying to read the file
     */
    public GenomePropertiesParser( @NotNull final String path) throws Exception {
        this( new FileInputStream(path), DEFAULT_NUMBER_PAGE );
    }


    /**
     * @param input stream from the obo file to read
     * @throws IOException if an error occur while trying to read the file
     */
    public GenomePropertiesParser( @NotNull final InputStream input) throws Exception {
        this( input, DEFAULT_NUMBER_PAGE );
    }

    public Term getTerm(@NotNull final String s) {
        return terms.get( s );
    }

    @Override
    public Iterator iterator() {
        return terms.entrySet().iterator();
    }

    public void forEach(BiConsumer<String,? super Term> action){
        Objects.requireNonNull(action);
        for (Map.Entry<String, Term> entry : terms.entrySet()) {
            action.accept( entry.getKey(), entry.getValue() );
        }
    }
}
