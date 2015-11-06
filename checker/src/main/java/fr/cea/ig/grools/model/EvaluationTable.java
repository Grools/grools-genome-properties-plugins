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

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * EvaluationTable
 */
@Data
public abstract class EvaluationTable<T extends Term,U extends Term>  {
    protected final OperatorLogic       operator;
    protected final Map<T,Integer>      entries;
    protected final U[][]               values;
    protected final int                 numberOfCells;
    protected final Category            category;

    protected int computeNumberOfcells(){
        int result = 0;
        for( U[] row : values)
            for( U column : row )
                result++;
        return result;
    }

    protected T findEntry( int i ){
        T       result      = null;
        boolean isSearching = true;
        final Iterator<Map.Entry<T, Integer>> iterator = entries.entrySet().iterator();
        while ( isSearching ){
            if( ! iterator.hasNext() )
                isSearching = false;
            else{
                final Map.Entry<T, Integer> entry = iterator.next();
                final Integer value = entry.getValue();
                if( i == value ){
                    isSearching = false;
                    result      = entry.getKey();
                }
            }
        }
        return result;
    }

    public EvaluationTable(@NonNull final Category category, @NonNull final Map<T, Integer> entry, @NonNull final U[][] values ){
        this.operator       = OperatorLogic.AND;
        this.category       = category;
        this.entries        = entry;
        this.values         = values;
        this.numberOfCells  = computeNumberOfcells();
    }

    public EvaluationTable(@NonNull final Category category, @NonNull final T[] entry, @NonNull final U[][] values ){
        this.operator       = OperatorLogic.AND;
        this.category       = category;
        this.entries        = new HashMap<>();
        this.values         = values;
        this.numberOfCells  = computeNumberOfcells();
        IntStream.range(0, entry.length)
                 .forEach(i -> this.entries.put(entry[i], i));
    }

    public EvaluationTable(@NonNull final OperatorLogic operator, @NonNull final Category category, @NonNull final Map<T, Integer> entry, @NonNull final U[][] values ){
        this.operator       = operator;
        this.category       = category;
        this.entries        = entry;
        this.values         = values;
        this.numberOfCells  = computeNumberOfcells();
    }

    public EvaluationTable(@NonNull final OperatorLogic operator, @NonNull final Category category, @NonNull final T[] entry, @NonNull final U[][] values ){
        this.operator       = operator;
        this.category       = category;
        this.entries        = new HashMap<>();
        this.values         = values;
        this.numberOfCells  = computeNumberOfcells();
        IntStream.range(0, entry.length)
                 .forEach(i -> this.entries.put(entry[i], i));
    }

    public U get( T entryA, T entryB){
        int a = entries.get(entryA);
        int b = entries.get(entryB);
        return values[a][b];
    }

    @SuppressWarnings("unchecked")
    public <V extends EvaluationCell> V[] toCells(@NonNull final Class<V> cellType) {
        final Constructor constructor   = cellType.getConstructors()[0];
        final V[]         cells         = (V[]) Array.newInstance( cellType, numberOfCells );
        int   a = 0;
        int   b = 0;
        int   i = 0;
        try {
            for(U[] row : values) {
                for ( U column : row ) {
                    final T columnEntry   = findEntry( a );
                    final T rowEntry      = findEntry( b );
                    cells[ i ] = ( V ) constructor.newInstance( category, operator, rowEntry, columnEntry, column );
                    b++;
                    i++;
                }
                a++;
                b=0;
            }

        } catch ( Exception e ){
            System.err.println( "Unable to find the cell constructor!" );
            System.err.println("Terminating...");
            System.exit(3); // http://journal.thobe.org/2013/02/jvms-and-kill-signals.html
        }
        return  cells;
    }
}
