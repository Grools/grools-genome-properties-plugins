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

package fr.cea.ig.grools.model;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * EvaluationCell
 */
@Data
public abstract  class EvaluationCell<T extends Term,U extends Term> implements Serializable {
    private static final long serialVersionUID = -7067576402293397707L;
    protected final Category        category;
    protected final OperatorLogic   operator;
    protected final T               rowEntry;
    protected final T               columnEntry;
    protected final U               value;

    protected EvaluationCell(
                                    @NonNull final Category         category,
                                    @NonNull final OperatorLogic    operator,
                                    @NonNull final T                rowEntry,
                                    @NonNull final T                columnEntry,
                                    @NonNull final U                value){
        this.category       = category;
        this.operator       = operator;
        this.rowEntry       = rowEntry;
        this.columnEntry    = columnEntry;
        this.value          = value;
    }
}
