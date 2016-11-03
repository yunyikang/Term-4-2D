package sat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();


    public static void main(String[] args){
        Formula formula;

        String s8 = "s8Sat";
        String bigSat = "largeSat";
        String bigUnsat = "largeUnsat";

        try{
            //Parse CNF file
            System.out.println("Parsing CNF file ");
            formula = CNF.parser("C:\\Users\\kang\\Downloads\\Project-2D-starting\\sampleCNF\\"+ s8 + ".cnf");
            System.out.println("Done");

            //Solve formula
            System.out.println("NOW LET THE SOLVING COMMENCE!!!");
            long started = System.nanoTime();
            Environment result = SATSolver.solve(formula);
            long end = System.nanoTime();
            long timeTaken= end - started;
            System.out.println("Solver found an answer in record time of " + timeTaken/Math.pow(10,9) + " seconds!");
            System.out.println("SO SICK!");

            //Export results
            writeFile(result);
        }
        catch(ParseException | IOException e){
            System.out.println(e);
        }
    }

    public static void writeFile(Environment result) throws IOException {
        //if an answer is found
        if (result != null) {
            System.out.println("Input formula IS satisfiable");

            //output results to text file
            BufferedWriter writer = null;
            try {
                File txtFile = new File("Results.txt");

                writer = new BufferedWriter(new FileWriter(txtFile));
                for (int i = 1; i <= CNF.totalVars; i++) {
                    Variable key = new Variable(i + "");  //convert int i to String
                    writer.write(i + ":" + result.get(key));
                    writer.newLine();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                writer.close();
            }
            System.out.println("Refer to Results.txt for details");

        }
        //if there is no answer
        else {
            System.out.println("Input formula is NOT satisfiable");
        }
    }


    public void testSATSolver1(){
        // (a v b)
        Environment e = SATSolver.solve(makeFm(makeCl(a, b)));
/*    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())
    			|| Bool.TRUE == e.get(b.getVariable())	);*/

    }


    public void testSATSolver2(){
        // (~a)
        Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/
    }

    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }

    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }



}