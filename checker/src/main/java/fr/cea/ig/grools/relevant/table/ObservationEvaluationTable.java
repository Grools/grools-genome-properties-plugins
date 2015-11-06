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

package fr.cea.ig.grools.relevant.table;

import fr.cea.ig.grools.model.Category;
import fr.cea.ig.grools.model.EvaluationTable;
import fr.cea.ig.grools.model.OperatorLogic;
import fr.cea.ig.grools.relevant.terms.ObservationTerms;
import static fr.cea.ig.grools.relevant.terms.ObservationTerms.UNKNOWN;
import static fr.cea.ig.grools.relevant.terms.ObservationTerms.TRUE;
import static fr.cea.ig.grools.relevant.terms.ObservationTerms.FALSE;
import static fr.cea.ig.grools.relevant.terms.ObservationTerms.BOTH;
import lombok.NonNull;


/**
 * ObservationEvaluationTable
 */
public final class ObservationEvaluationTable extends EvaluationTable<ObservationTerms, ObservationTerms> {
    private ObservationEvaluationTable(
            @NonNull final ObservationTerms[] entry,
            @NonNull final ObservationTerms[][] values) {
        super( Category.OBSERVATION, entry, values );
    }

    private ObservationEvaluationTable(
            @NonNull final ObservationTerms[]   entry,
            @NonNull final ObservationTerms[][] values,
            @NonNull final OperatorLogic        operator) {
        super( operator, Category.OBSERVATION, entry, values );
    }


    public static final ObservationEvaluationTable AND = new ObservationEvaluationTable(
            new ObservationTerms[]{ UNKNOWN, TRUE, FALSE, BOTH },
            new ObservationTerms[][]{
                    // UNKNOWN  , TRUE  , FALSE , BOTH
                    { UNKNOWN   , TRUE  , FALSE , BOTH }, //UNKNOWN
                    { TRUE      , TRUE  , BOTH  , BOTH }, // TRUE
                    { FALSE     , BOTH  , FALSE , BOTH }, // FALSE
                    { BOTH      , BOTH  , BOTH  , BOTH }  // BOTH
            }
    );


    public static final ObservationEvaluationTable OR = new ObservationEvaluationTable(
            new ObservationTerms[]{ UNKNOWN, TRUE, FALSE, BOTH },
            new ObservationTerms[][]{
                    // UNKNOWN  , TRUE  , FALSE     , BOTH
                    { UNKNOWN   , TRUE  , UNKNOWN   , UNKNOWN }, //UNKNOWN
                    { TRUE      , TRUE  , TRUE      , TRUE }, // TRUE
                    { UNKNOWN   , TRUE  , FALSE     , BOTH }, // FALSE
                    { UNKNOWN   , TRUE  , BOTH      , BOTH }  // BOTH
            },
            OperatorLogic.OR
    );

    public ObservationTerms get( @NonNull final ObservationTerms entryA, @NonNull final ObservationTerms entryB){
        return super.get(entryA, entryB);
    }
}
