package sat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;

/**
 * Created by SUTD on 4/11/2015.
 */
public class CNF {

    static int totalVars;
    static int totalClause;

    public static Formula parser(String fileLocation) throws IOException, ParseException {
        InputStream fileInput = new FileInputStream(fileLocation);
        Scanner scanner = new Scanner(fileInput);

        try{
            String word = scanner.next();

            //skipping comments
            while(word.equals("c")){
                scanner.nextLine();
                word = scanner.next();
            }

            //finding problem line
            if (word.equals("p") == false){
                throw new ParseException("Letter p expected, " + word + " found instead", 1);
            }
        }
        catch (NoSuchElementException e){
            throw new ParseException("Can't find p",1);
        }

        try{
            String word = scanner.next();
            if (word.equals("cnf") == false) {
                throw new ParseException("cnf expected, " + word + " found instead", 1);
            }
            totalVars = scanner.nextInt();
            totalClause = scanner.nextInt();
        }
        catch (NoSuchElementException e){
            throw new ParseException("Wrong formatting",1);
        }

        //begin synthesising formula
        Formula formula = new Formula();
        Clause clause = new Clause();
        try{
            while (totalClause > 0){
                String literal = scanner.next();

                //make new clause
                if (literal.equals("0")){
                    totalClause--;
                    formula = formula.addClause(clause);
                    clause = new Clause();
                }

                //add negative literal
                else if ((literal.substring(0,1)).equals("-")){
                    String posnum = literal.substring(1);
                    clause = clause.add(NegLiteral.make(posnum));
                }

                //all positive literal
                else{
                    clause = clause.add(PosLiteral.make(literal));
                }
            }
        }
        catch(NoSuchElementException e){
            throw new ParseException(
                    "Missing clauses",1);

        }

        return formula;
    }
}