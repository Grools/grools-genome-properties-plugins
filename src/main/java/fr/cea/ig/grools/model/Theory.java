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
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.LocalDate;

/**
 * Theory
 */
/*
 * @startuml
 * skinparam shadowing false
 * skinparam defaultFontName courier
 * interface Theory<T extends Term, U extends Term> extends Fact {
 *  + getParents()          : Theory<T,U>[]
 *  + getOperator()         : OperatorLogic
 *  + getIsMandatory()      : boolean
 *  + getIsPredicted()      : T
 *  + getIsExpected()       : T
 *  + getConclusion()       : U
 *  + getIsSpecific()       : boolean
 *  + setIsSpecific(boolean): void
 * }
 * @enduml
 */
@Data
@EqualsAndHashCode(callSuper=false)
public abstract class Theory<T extends Term, U extends Term, V extends Theory> extends Fact {
    protected final V[]                 parents;
    protected final OperatorLogic       operator;
    protected final boolean             isMandatory;
    protected       T                   isPredicted;
    protected       T                   isExpected;
    protected       U                   conclusion;
    protected final boolean             isSpecific;
    protected       boolean             needToReEvaluateParent;


    protected Theory(
            @NonNull final String id,
            @NonNull final String name,
            @NonNull final String source,
            @NonNull final LocalDate date,
            @NonNull final V[] parents,
            @NonNull final OperatorLogic operator,
                     final boolean isMandatory,
                     final T isPredicted,
                     final T isExpected,
            @NonNull final U conclusion,
                     final boolean isSpecific){
        super( id, name, source, date );
        this.parents            = parents;
        this.operator           = operator;
        this.isMandatory        = isMandatory;
        this.isPredicted        = isPredicted;
        this.isExpected         = isExpected;
        this.conclusion         = conclusion;
        this.isSpecific         = isSpecific;
        this.needToReEvaluateParent = false;
    }

    public T getIsPredicted(){
        return isPredicted;
    }

    public void setIsPredicted( final T value ){
        isPredicted = value;
    }

    public T getIsExpected(){
        return isExpected;
    }

    public void setIsExpected( @NonNull final T value ){
        isExpected = value;
    }

    public U getConclusion(){
        return conclusion;
    }

    public void setConclusion( @NonNull final U value ){
        conclusion = value;
    }

    public boolean getIsSpecific(){
        return isSpecific;
    }

    public boolean getIsMandatory(){
        return isMandatory;
    }

    @Override
    public String toString() {
        String parentStr = "[]";
        if( parents.length > 0) {
            StringBuilder stringBuilder = new StringBuilder( "[" );
            for ( final V theory : parents )
                stringBuilder.append( theory.getId() + ',' );

            stringBuilder.replace( stringBuilder.length() - 1, stringBuilder.length() - 1, "]" );
            parentStr = stringBuilder.toString();
        }
        return "Theory( \n" +
               "         parents                = " + parentStr             + '\n'  +
               "         operator               = " + this.operator         + ",\n" +
               "         id                     = " + this.id               + ",\n" +
               "         name                   = " + this.name             + ",\n" +
               "         source                 = " + this.source           + ",\n" +
               "         date                   = " + this.date             + ",\n" +
               "         isSpecific             = " + this.isSpecific       + ",\n" +
               "         isMandatory            = " + this.isMandatory      + ",\n" +
               "         isPredicted            = " + this.isPredicted      + ",\n" +
               "         isExpected             = " + this.isExpected       + ",\n" +
               "         conclusion             = " + this.conclusion       + ",\n" +
               "         needToReEvaluateParent = " + this.needToReEvaluateParent + " )";
    }
}
