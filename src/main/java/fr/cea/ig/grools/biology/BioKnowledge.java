package fr.cea.ig.grools.biology;
/*
 * Copyright LABGeM 10/02/15
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


import fr.cea.ig.grools.model.FiveState;
import fr.cea.ig.grools.model.Conclusion;
import fr.cea.ig.grools.model.Knowledge;
import fr.cea.ig.grools.model.NodeType;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 *
 */
/*
 * @startuml
 * class BioKnowledge{
 * }
 * @enduml
 */
public final class BioKnowledge implements Knowledge {

    private List<Knowledge> isA;
    private final Knowledge[] partOf;
    private final NodeType    nodeType;
    private final String      id;
    private final String      name;
    private final String      source;
    private final DateTime    date;
    private FiveState presence;
    private       Conclusion  conclusion;

    public BioKnowledge( @NotNull final List<Knowledge> isA, @NotNull final Knowledge[] partOf, @NotNull final NodeType nodeType, @NotNull final String id, @NotNull final String name, @NotNull final String source, @NotNull final DateTime date, @NotNull final FiveState presence, @NotNull final Conclusion conclusion) {
        this.isA        = isA;
        this.partOf     = partOf.clone();
        this.nodeType   = nodeType;
        this.id         = id;
        this.name       = name;
        this.source     = source;
        this.date       = new DateTime(date);
        this.presence   = presence;
        this.conclusion = conclusion;
    }

    @Override @NotNull
    public List<Knowledge> getIsA() {
        return new ArrayList<>(isA);
    }

    @Override
    public void addIsA( @NotNull final Knowledge k) {
        this.isA.add(k);
    }

    @Override @NotNull
    public Knowledge[] getPartOf() {
        return partOf.clone();
    }

    @Override @NotNull
    public NodeType getNodeType() {
        return nodeType;
    }

    @Override @NotNull
    public Conclusion getConclusion() {
        return conclusion;
    }

    @Override
    public void setConclusion(final Conclusion conclusion) {
        this.conclusion = conclusion;
    }

    @Override
    public void setPresence(final FiveState presence) {
        this.presence = presence;
    }

    @Override @NotNull
    public String getId() {
        return id;
    }


    @Override @NotNull
    public String getName() {
        return name;
    }

    @Override @NotNull
    public String getSource() {
        return source;
    }

    @Override @NotNull
    public DateTime getDate() {
        return new DateTime( date );
    }

    @Override @NotNull
    public FiveState getPresence() {
        return presence;
    }

    @Override @NotNull
    public String toString(){
        Formatter formatter = new Formatter();
        StringBuilder tmp = new StringBuilder();
        if( partOf != null && partOf.length > 0) {
            for (Knowledge k : partOf){
                tmp.append(k.getName());
                tmp.append("-");
            }
            tmp.deleteCharAt(tmp.length()-1);
        }
        return formatter.format("%s: SOURCE(%s) NODE(%s) PRESENCE(%s) CONCLUSION(%s) PART_OF(%s)", name, source, nodeType, presence, conclusion, tmp.toString()).toString();
    }
}
