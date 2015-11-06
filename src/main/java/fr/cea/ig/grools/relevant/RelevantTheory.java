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

import fr.cea.ig.grools.model.OperatorLogic;
import fr.cea.ig.grools.model.Theory;
import fr.cea.ig.grools.relevant.terms.ConclusionTerms;
import fr.cea.ig.grools.relevant.terms.ObservationTerms;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * RelevantTheory
 */
public final class RelevantTheory extends Theory<ObservationTerms,ConclusionTerms, RelevantTheory> {
    private static final long serialVersionUID = 8506605344605298850L;

    protected RelevantTheory(
                                    @NonNull final      String                  id          ,
                                    @NonNull final      String                  name        ,
                                    @NonNull final      String                  source      ,
                                    @NonNull final      LocalDate               date        ,
                                    @NonNull final      RelevantTheory[]        parents     ,
                                    @NonNull final      OperatorLogic           operator    ,
                                                        boolean                 isMandatory ,
                                             final      ObservationTerms        isPredicted ,
                                             final      ObservationTerms        isExpected  ,
                                    @NonNull final      ConclusionTerms         conclusion  ,
                                                        boolean                 isSpecific) {
        super(id, name, source, date, (RelevantTheory[])parents, operator, isMandatory, isPredicted, isExpected, conclusion, isSpecific);
    }

    public static RelevantTheoryBuilder builder() {
        return new RelevantTheoryBuilder();
    }

    public static class RelevantTheoryBuilder {
        private String                  id;
        private String                  name;
        private String                  source;
        private LocalDate               date;
        private List<RelevantTheory>     parents;
        private OperatorLogic           operator;
        private boolean                 isMandatory;
        private ObservationTerms        isPredicted;
        private ObservationTerms        isExpected;
        private ConclusionTerms         conclusion;
        private boolean                 isSpecific;

        RelevantTheoryBuilder() {
            parents = new ArrayList<RelevantTheory>();
        }

        public RelevantTheory.RelevantTheoryBuilder id(@NonNull final String id) {
            this.id = id;
            return this;
        }

        public RelevantTheory.RelevantTheoryBuilder name(@NonNull final String name) {
            this.name = name;
            return this;
        }

        public RelevantTheory.RelevantTheoryBuilder source(@NonNull final String source) {
            this.source = source;
            return this;
        }

        public RelevantTheory.RelevantTheoryBuilder date(@NonNull final LocalDate date) {
            this.date = date;
            return this;
        }

        public RelevantTheory.RelevantTheoryBuilder parents(@NonNull final List<RelevantTheory> parents) {
            this.parents = parents;
            return this;
        }

        public RelevantTheory.RelevantTheoryBuilder parent(@NonNull final RelevantTheory parent) {
            this.parents.add(parent);
            return this;
        }

        public RelevantTheory.RelevantTheoryBuilder operator(@NonNull final OperatorLogic operator) {
            this.operator = operator;
            return this;
        }

        public RelevantTheory.RelevantTheoryBuilder isMandatory( final boolean isMandatory) {
            this.isMandatory = isMandatory;
            return this;
        }

        public RelevantTheory.RelevantTheoryBuilder isPredicted(@NonNull final ObservationTerms isPredicted) {
            this.isPredicted = isPredicted;
            return this;
        }

        public RelevantTheory.RelevantTheoryBuilder isExpected(@NonNull final ObservationTerms isExpected) {
            this.isExpected = isExpected;
            return this;
        }

        public RelevantTheory.RelevantTheoryBuilder conclusion(@NonNull final ConclusionTerms conclusion) {
            this.conclusion = conclusion;
            return this;
        }

        public RelevantTheory build() {
            this.operator       = (operator         == null) ? OperatorLogic.AND            : operator;
            this.id             = (id               == null) ? name                         : id;
            this.source         = (source           == null) ? "UNKNONW"                    : source;
            this.date           = (date             == null) ? LocalDate.now()              : date;
//            this.isPredicted    = (isPredicted      == null) ? ObservationTerms.UNKNOWN     : isPredicted;
//            this.isExpected     = (isExpected       == null) ? ObservationTerms.UNKNOWN     : isExpected;
            this.conclusion     = (conclusion       == null) ? ConclusionTerms.UNEXPLAINED  : conclusion;
            final RelevantTheory[] p = new RelevantTheory[parents.size()];
            parents.toArray(p);
            if( p.length == 1)
                isSpecific = true;
            return new RelevantTheory(id, name, source, date, p, operator, isMandatory, isPredicted, isExpected, conclusion, isSpecific);
        }
    }
}
