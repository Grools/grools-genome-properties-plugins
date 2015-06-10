package fr.cea.ig.grools.model;
/*
 * Copyright LABGeM 26/01/15
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


/**
 *
 */
/*
 * @startuml
 * enum Conclusion{
    CONFIRMED_PRESENCE
    CONFIRMED_ABSENCE
    UNCONFIRMED_PRESENCE
    UNCONFIRMED_ABSENCE
    UNCONFIRMED_CONTRADICTORY
    MISSING
    UNEVALUATED
    UNKNOWN
    AMBIGUOUS,
    AMBIGUOUS_PRESENCE
    AMBIGUOUS_ABSENCE
    AMBIGUOUS_CONTRADICTORY
    UNEXPECTED
    UNEXPECTED_PRESENCE
    UNEXPECTED_ABSENCE
    AMBIGUOUS_CONTRADICTORY
    CONTRADICTORY_PRESENCE
    CONTRADICTORY_ABSENCE
 * }
 * hide Conclusion methods
 * @enduml
 */
public enum Conclusion {
    CONFIRMED_PRESENCE          ("confirmed presence"),
    CONFIRMED_ABSENCE           ("confirmed absence"),
    UNCONFIRMED                 ("unconfirmed"),
    UNCONFIRMED_PRESENCE        ("unconfirmed presence"),
    UNCONFIRMED_ABSENCE         ("unconfirmed absence"),
    UNCONFIRMED_CONTRADICTORY   ("unconfirmed contradictory"),
    MISSING                     ("missing"),
    UNEVALUATED                 ("unevaluated"),
    UNKNOWN                     ("unknown"),
    AMBIGUOUS                   ("ambiguous"),
    AMBIGUOUS_PRESENCE          ("ambiguous "),
    AMBIGUOUS_ABSENCE           ("ambiguous absence"),
    AMBIGUOUS_CONTRADICTORY     ("ambiguous contradictory"),
    UNEXPECTED                  ("unexpected"),
    UNEXPECTED_PRESENCE         ("unexpected presence"),
    UNEXPECTED_ABSENCE          ("unexpected absence"),
    CONTRADICTORY               ("contradictory"),
    CONTRADICTORY_PRESENCE      ("contradictory presence"),
    CONTRADICTORY_ABSENCE       ("contradictory absence");

    private final String text;

    private Conclusion(final String s) { text = s; }

    public String toString() { return text; }

}
