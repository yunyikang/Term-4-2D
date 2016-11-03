
package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Bool;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     *
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {

        ImList<Clause> clauses = formula.getClauses();
        return solve(clauses, new Environment());
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     *
     * @param clauses
     *            formula in conjunctive normal form
     * @param e
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment e) {

        //empty clause found terminate
        if (clauses.isEmpty()) return e;

        Clause shortestClause = clauses.first();
        for (Clause currClause: clauses) {

            //empty clause found terminate
            if (currClause.isEmpty()) return null;

            //finding shortest clause
            if (currClause.size() < shortestClause.size())
                shortestClause = currClause;
        }

        Literal l = shortestClause.chooseLiteral();
        Variable var = l.getVariable();

        //unit propagation

        //if clause has only 1 literal
        if (shortestClause.isUnit()) {

            //if literal is +ve, set variable = true, else set to false
            if (PosLiteral.make(var).equals(l)) e = e.put(var, Bool.TRUE);

            else e = e.put(var, Bool.FALSE);

            //Unit propagate
            return solve(substitute(clauses, l), e);

        }

        //if clause has multiple literals
        else{

            if (PosLiteral.make(var).equals(l)) e = e.put(var, Bool.TRUE);

            else e = e.put(var, Bool.FALSE);

            //recurse
            Environment newEnv = solve(substitute(clauses, l), e);

            //if this makes formula unsatisfiable, set literal l to False instead (backtrack)
            if (newEnv == null) {

                if (PosLiteral.make(var).equals(l)) e = e.put(var, Bool.FALSE);
                else e = e.put(var, Bool.TRUE);
                return solve(substitute(clauses, l.getNegation()), e);

            } else return newEnv;

        }
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses, Literal l) {

        ImList<Clause> nClauses = new EmptyImList<Clause>();

        for (Clause currClause: clauses) {
            Clause newClause = currClause.reduce(l);

            if (newClause!= null) nClauses = nClauses.add(newClause);
        }
        return nClauses;
    }

}