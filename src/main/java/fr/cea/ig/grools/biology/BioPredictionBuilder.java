package fr.cea.ig.grools.biology;

import fr.cea.ig.grools.model.FourState;
import fr.cea.ig.grools.model.Evidence;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

public final class BioPredictionBuilder {
    private String      id              = "";
    private String      name            = "";
    private String      knowledgeId     = "";
    private String      source          = "";
    private DateTime    date            = DateTime.now();
    private FourState   presence        = FourState.TRUE;
    private Evidence    evidence        = Evidence.MEDIUM;

    @NotNull
    public BioPredictionBuilder setId(@NotNull final String id) {
        this.id = id;
        return this;
    }

    @NotNull
    public BioPredictionBuilder setName(@NotNull final String name) {
        this.name = name;
        return this;
    }

    @NotNull
    public BioPredictionBuilder setKnowledgeId(@NotNull final String knowledgeId) {
        this.knowledgeId = knowledgeId;
        return this;
    }

    @NotNull
    public BioPredictionBuilder setSource(@NotNull final String source) {
        this.source = source;
        return this;
    }

    @NotNull
    public BioPredictionBuilder setDate(@NotNull final DateTime date) {
        this.date = date;
        return this;
    }

    @NotNull
    public BioPredictionBuilder setPresence(@NotNull final FourState presence) {
        this.presence = presence;
        return this;
    }

    @NotNull
    public BioPredictionBuilder setEvidence(@NotNull final Evidence evidence) {
        this.evidence = evidence;
        return this;
    }

    public BioPrediction create() {
        if ( id.isEmpty() ){
            assert (!name.isEmpty());
            id = name;
        }
        return new BioPrediction(id, name, knowledgeId, source, date, presence, evidence);
    }
}
