package Assignment1;

import java.io.*;
import java.util.*;

public class Demo {

	public static void main(String[] args) 
	{
        StringBuilder fileContent = new StringBuilder();
        
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\ANAS\\WORK\\SEMESTER 6\\Compiler Construction\\Assignment 1\\Assignment1\\src\\Assignment1\\input.customlanguage"))) 
        {	
            String line;
            
            while ((line = br.readLine()) != null) 
            {
                fileContent.append(line).append("\n");
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Error reading file: " + e.getMessage());
        
            return;
        }

        LexicalAnalyzer lexer = new LexicalAnalyzer(fileContent.toString());
                
        List<Token> tokens = lexer.tokenize();
        
        SymbolTable table = new SymbolTable();
        lexer.fillsymboltable(table);
                
        System.out.println("\nTokens:\n");
        
        for (Token token : tokens) 
        {
            System.out.println(token.tostring());
        }
        
        table.displaysymboltable();
        
        NFA combinednfa = null;
        int nextid = 0;

        for (Token token : tokens) 
        {
            if (token.lexeme.isEmpty()) 
            {
                continue; 
            }

            NFA tokennfa = NFA.basic(token.lexeme.charAt(0), nextid);
            nextid = tokennfa.accept.id + 1;

            if (combinednfa == null) 
            {
            	combinednfa = tokennfa;
            } 
            else 
            {
            	combinednfa = NFA.union(combinednfa, tokennfa);
            }
        }


        combinednfa.displaytransitiontable();

		DFA.nfatodfa(combinednfa);
        
        System.out.println("\nLexical Analysis Statistics:");
        System.out.println("\nTotal Tokens: " + tokens.size());
        

        Set<String> us = new HashSet<>();
        
        for (Token token : tokens) 
        {
            us.add(token.lexeme);
        }

        System.out.println("Total Unique States: " + us.size());

        long top = tokens.stream().filter(t -> t.type.contains("OPERATOR")).count();
        long tdt = tokens.stream().filter(t -> t.type.equals("KEYWORD")).count();

        System.out.println("Total Operators Used: " + top);
        System.out.println("Total Datatypes Used: " + tdt);

    }
}
