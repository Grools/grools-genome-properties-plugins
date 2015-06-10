package fr.cea.ig.grools.biology;

import fr.cea.ig.grools.model.FiveState;
import fr.cea.ig.grools.model.Evidence;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

public final class BioPredictionBuilder {
    private String      id              = "";
    private String      name            = "";
    private String      knowledgeName   = "";
    private String      source          = "";
    private DateTime    date            = DateTime.now();
    private FiveState presence        = FiveState.TRUE;
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
    public BioPredictionBuilder setKnowledgeName(@NotNull final String knowledgeName) {
        this.knowledgeName = knowledgeName;
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
    public BioPredictionBuilder setPresence(@NotNull final FiveState presence) {
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
        return new BioPrediction(id, name, knowledgeName, source, date, presence, evidence);
    }
}
