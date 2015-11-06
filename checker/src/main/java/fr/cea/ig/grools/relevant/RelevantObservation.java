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

package fr.cea.ig.grools.relevant;


import fr.cea.ig.grools.model.ObservationType;
import fr.cea.ig.grools.model.Observation;
import fr.cea.ig.grools.relevant.terms.ObservationTerms;
import lombok.NonNull;

import java.time.LocalDate;

/**
 * RelevantObservation
 */
public final class RelevantObservation extends Observation<ObservationTerms> {
    private static final long serialVersionUID = -6482329741907072433L;

    public RelevantObservation(
                                 @NonNull final String              id,
                                 @NonNull final String              name,
                                 @NonNull final String              source,
                                 @NonNull final LocalDate           date,
                                 @NonNull final String              theoryId,
                                 @NonNull final ObservationType observationType,
                                 @NonNull final ObservationTerms    state,
                                          final boolean             isExperimental){
        super(id, name, source, date, theoryId, observationType, state, isExperimental);
    }


    public static RelevantObservationBuilder builder() {
        return new RelevantObservationBuilder();
    }


    public static class RelevantObservationBuilder {
        private ObservationType observationType;
        private String              theoryId;
        private Boolean             isExperimental;
        private String              id;
        private String              name;
        private String              source;
        private LocalDate           date;
        private ObservationTerms    state;


        public RelevantObservation.RelevantObservationBuilder category( @NonNull final ObservationType observationType ) {
            this.observationType = observationType;
            return this;
        }

        public RelevantObservation.RelevantObservationBuilder theoryId( @NonNull final String theoryId) {
            this.theoryId = theoryId;
            return this;
        }

        public RelevantObservation.RelevantObservationBuilder isExperimental( @NonNull final Boolean isExperimental) {
            this.isExperimental = isExperimental;
            return this;
        }

        public RelevantObservation.RelevantObservationBuilder id( @NonNull final String id) {
            this.id = id;
            return this;
        }

        public RelevantObservation.RelevantObservationBuilder name( @NonNull final String name) {
            this.name = name;
            return this;
        }

        public RelevantObservation.RelevantObservationBuilder source( @NonNull final String source) {
            this.source = source;
            return this;
        }

        public RelevantObservation.RelevantObservationBuilder date( @NonNull final LocalDate date) {
            this.date = date;
            return this;
        }

        public RelevantObservation.RelevantObservationBuilder state(@NonNull final ObservationTerms state) {
            this.state = state;
            return this;
        }

        @SuppressWarnings("unchecked")
        public RelevantObservation.RelevantObservationBuilder state(@NonNull final String state) {
            this.state = ObservationTerms.valueOf(state);
            return this;
        }

        @SuppressWarnings("unchecked")
        public RelevantObservation build() {
            this.id             = (id       == null         ) ? name                            : id;
            this.source         = (source   == null         ) ? "UNKNONW"                       : source;
            this.date           = (date     == null         ) ? LocalDate.now()                 : date;
            this.state          = (state    == null         ) ? ObservationTerms.TRUE           : state;
            this.observationType = ( observationType == null         ) ? ObservationType.COMPUTATIONAL_ANALYSIS : observationType;
            this.isExperimental = ( isExperimental == null  ) ? false                           : isExperimental;
            return new RelevantObservation(id, name, source, date, theoryId, observationType, state, isExperimental);
        }
    }
}
