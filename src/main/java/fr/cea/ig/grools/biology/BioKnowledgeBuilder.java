package fr.cea.ig.grools.biology;

import fr.cea.ig.grools.model.Conclusion;
import fr.cea.ig.grools.model.FourState;
import fr.cea.ig.grools.model.Knowledge;
import fr.cea.ig.grools.model.NodeType;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public final class BioKnowledgeBuilder {
    private List<Knowledge> isA         = new ArrayList<>();
    private List<Knowledge> partOf      = new ArrayList<>();
    private NodeType        nodeType    = NodeType.LEAF;
    private DateTime        date        = DateTime.now();
    private FourState       presence    = FourState.UNKNOWN;
    private Conclusion      conclusion  = Conclusion.UNKNOWN;
    private String          name        = "";
    private String          id          = "";
    private String          source      = "";

    @NotNull
    public BioKnowledgeBuilder setIsA( @NotNull final List<Knowledge> isA) {
        this.isA = isA;
        return this;
    }

    @NotNull
    public BioKnowledgeBuilder addIsA( @NotNull final Knowledge k) {
        this.isA.add(k);
        return this;
    }

    @NotNull
    public BioKnowledgeBuilder setPartOf( @NotNull final List<Knowledge> partOf) {
        this.partOf = partOf;
        return this;
    }

    @NotNull
    public BioKnowledgeBuilder addPartOf( @NotNull final Knowledge k) {
        this.partOf.add(k);
        return this;
    }

    @NotNull
    public BioKnowledgeBuilder setNodeType(@NotNull final NodeType nodeType) {
        this.nodeType = nodeType;
        return this;
    }

    @NotNull
    public BioKnowledgeBuilder setId(@NotNull final String id) {
        this.id = id;
        return this;
    }

    @NotNull
    public BioKnowledgeBuilder setName(@NotNull final String name) {
        this.name = name;
        return this;
    }

    @NotNull
    public BioKnowledgeBuilder setSource(@NotNull final String source) {
        this.source = source;
        return this;
    }

    @NotNull
    public BioKnowledgeBuilder setDate(@NotNull final DateTime date) {
        this.date = date;
        return this;
    }

    @NotNull
    public BioKnowledgeBuilder setPresence(@NotNull final FourState presence) {
        this.presence = presence;
        return this;
    }

    @NotNull
    public BioKnowledgeBuilder setConclusion(@NotNull final Conclusion conclusion) {
        this.conclusion = conclusion;
        return this;
    }

    @NotNull
    public BioKnowledge create() {
        if ( id.isEmpty() ){
            assert (!name.isEmpty());
            id = name;
        }
        Knowledge[] array = partOf.toArray( new Knowledge[partOf.size()] );
        return new BioKnowledge(isA, array, nodeType, id, name, source, date, presence, conclusion);
    }
}
