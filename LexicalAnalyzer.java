package Assignment1;

import java.util.*;

public class LexicalAnalyzer
{
    private String input;
    private int position;
    private int line;
    private List<Token> tokens;
    private boolean errorinline;
    private boolean inmain;
    private boolean inif;
    private int ifcount;
    private Map<String, Integer> globalvariables;
    
    private static final Set<String> KEYWORDS = Set.of("int", "bool", "char", "decimal", "if", "else", "return", "string");
    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "%", "^");
    private static final Set<String> RELATIONALOPERATORS = Set.of("==", "!=", "<=", "<", ">", ">=", "&&", "||", "!");
    private static final Set<Character> DELIMITERS = Set.of('(', ')', ';', '{', '}', ',');
    private static final Set<String> IO = Set.of("read", "write");
    private static final Set<String> BOOLEANLITERAL = Set.of("true", "false");

    private int memloc = 1000;

    private ErrorHandler errorhandler = new ErrorHandler();

    public LexicalAnalyzer(String input) 
    {
        this.input = preprocessinput(input);
        this.position = 0;
        this.line = 1;
        this.tokens = new ArrayList<>();
        this.errorinline = false;
        this.inmain = false;
        this.inif = false;
        this.ifcount = 0;
        this.globalvariables = new HashMap<>();
    }

    public String preprocessinput(String input) 
    {
        return input.replaceAll("[\\t\\r]+", "");
    }
    
    public List<Token> tokenize()
    {
    	while(position < input.length())
    	{
    		char current = input.charAt(position);
    		errorinline = false;
    		
    		if(current == '\n')
    		{
    			line++;
    			position++;
    			continue;
    		}
    		
    		if(Character.isWhitespace(current))
    		{
    			position++;
    			continue;
    		}
    		
    		if(current == '/')
    		{
    			if(position+1 < input.length())
    			{
    				if((input.charAt(position+1)=='/') || (input.charAt(position+1)=='*'))
    				{
    	    			tokens.add(commentfound());
    					continue;
    				}
    			}
    		}
    		
    		if(current == '{')
    		{
    			if(!inmain)
    			{
        			inmain = true;
    			}
    			else
    			{
    				inif = true;
    				ifcount++;
    			}
    		}
    		
    		if(current == '}')
    		{
    			if(!inif)
    			{
    				inmain = false;
    			}
    			else
    			{
    				ifcount--;
    				
    				if(ifcount==0)
    				{
        				inif = false;
    				}
    			}
    		}
    		
    		if(Character.isLowerCase(current))
    		{
    			Token t = extractidentifierorkeyword();
    			
    			if(t!=null)
    			{
                    tokens.add(t);
    			}
    			
    		}
    		else if(Character.isUpperCase(current))
            {
    			String str = "";
    			
    			while(position < input.length() && !Character.isWhitespace(current) && current != '=' && current != '\n')
    			{
    				
    				str+= current;
    				position++;
    				
        	        if (position < input.length()) 
        	        {
        	            current = input.charAt(position);
        	        }
        	        else 
        	        {
        	            break; 
        	        }
    			}

	            errorhandler.reporterror(line, "Invalid variable name: '" + str + "' (variable names cannot contain capital letters)");            	
            }
    		else if (Character.isDigit(current))
    		{
    			Token t = extractnumber();
    			
    			if(t!=null)
    			{
                    tokens.add(t);
    			}

    		}
    		else if (current == '"' || current == '\'') 
    		{                
    			Token t = extractstringorchar();
    			
    			if(t!=null)
    			{
                    tokens.add(t);
    			}

            }
    		else if(checkmultipleoperators())
    		{    			
    			Token t = extractrelationaloperator();
    			
    			if(t!=null)
    			{
                    tokens.add(t);
    			}

    		}
    		else if (OPERATORS.contains(Character.toString(current))) 
    		{
    			
    			tokens.add(new Token("OPERATOR", String.valueOf(current), line));
                position++;
            } 
    		else if (DELIMITERS.contains(current)) 
    		{
                tokens.add(new Token("DELIMITER", String.valueOf(current), line));
                position++;
            } 
    		else if (current == '=') 
    		{
                tokens.add(new Token("ASSIGNMENT", String.valueOf(current), line));
                position++;
            } 
    		else 
    		{
                errorhandler.reporterror(line, "Unknown character: " + current);
                errorinline = true;
            }

            if (errorinline) 
            {
                skipline();
                errorinline = false;
            }
    	}
    	
        tokens.add(new Token("EOF", "", line));
        
        
        errorhandler.printallerrors();

        System.out.println("\nGlobal variables:\n");
        
		for (Map.Entry<String, Integer> entry : globalvariables.entrySet()) 
		{
		    String key = entry.getKey();
		    int value = entry.getValue();
		    
		    System.out.println("Name: " + key + ", Line Number: "+ value);
		}

		
        return tokens;
    }
    
    private Token extractnumber() 
    {
        StringBuilder sb = new StringBuilder();
        boolean isdecimal = false;

        while (position < input.length() && (Character.isDigit(input.charAt(position)) || input.charAt(position) == '.')) 
        {
            if (input.charAt(position) == '.') 
            {
                if (isdecimal) 
                {
                    errorhandler.reporterror(line, "Invalid number format: multiple decimal points");
                    break;
                }
                
                isdecimal = true;
            }
            
            sb.append(input.charAt(position++));
        }

        if (position < input.length() && (Character.isLowerCase(input.charAt(position)) || input.charAt(position) == '_')) 
        {
            String str = "";
            char current = input.charAt(position);
            
            while (position < input.length() && !Character.isWhitespace(current) && current != '=' && current != '\n') 
            {            	
                str += current;
                position++;
                
                if(input.charAt(position) == '\n')
                {
                	line++;
                }
                
                if (position < input.length()) 
                {
                    current = input.charAt(position);
                }
                else 
                {
                    break; 
                }
            }
            
            String id = String.valueOf(sb);
            id += str;

            errorhandler.reporterror(line, "Invalid format: '" + id + "' (numbers cannot mix with alphabets)");
                    	
            return null;
        }

        if (isdecimal) 
        {
            double num = Double.parseDouble(sb.toString());
            
            Token t = new Token("DECIMAL", String.format("%.5f", num), line);
        	
            return t;            
        }

        Token t = new Token("INTEGER", String.valueOf(sb), line);
    	
        return t;
    }

    private Token extractrelationaloperator() 
    {
        for (String op : RELATIONALOPERATORS) 
        {
            if (input.startsWith(op, position)) 
            {
                position += op.length();
                
                if(op.equalsIgnoreCase("&&") || op.equalsIgnoreCase("!") || op.equalsIgnoreCase("||"))
                {
                    Token t = new Token("LOGICAL OPERATOR", op, line);
                	
                    return t;
                }
                
                Token t = new Token("RELATIONAL OPERATOR", op, line);
            	
                return t;
            }
        }
            	
        return null;
    }

    
    public Token commentfound()
    {
    	String comm = "";
    	
    	if(input.charAt(position+1) == '/')
    	{
    		position+=2;
    		
        	while(position < input.length() && input.charAt(position) != '\n') 
        	{
        		comm += input.charAt(position);
        		
                position++;
            }
        	
            line++;
            
            if (position < input.length()) 
            {
                position++;
            }
            
        	Token t = new Token("SINGLELINECOMMENT", comm, line);
        	
            return t;

    	}
    	else if (input.charAt(position+1) == '*')
    	{
    		position++;
    		position++;
    		
    		while(position < input.length()-1)
    		{
    			if(input.charAt(position) == '\n')
                {
                	line++;
                }
    			
    			if(input.charAt(position) == '*' && input.charAt(position+1) == '/') 
    			{
    	    		position++;
    	    		position++;
    	    		
    	    		Token t = new Token("MULTILINECOMMENT", comm, line);
    	        	
    	            return t;
                }
    			
				comm += input.charAt(position);

                position++;
    		}
    		
            errorhandler.reporterror(line, "Unterminated multiline comment");            
    	}
    	
    	
        return null;
    }
    
    private Token extractstringorchar() 
    {
        char delimiter = input.charAt(position++);

        StringBuilder sb = new StringBuilder();
        
        while (position < input.length() && input.charAt(position) != delimiter) 
        {
            sb.append(input.charAt(position++));
        }
        
        if (position < input.length() && input.charAt(position) == delimiter)
        {
            position++;
        } 
        else 
        {
            errorhandler.reporterror(line, "Unterminated string or character literal");
            errorinline = true;
        }

        if (sb.length() == 1)
        {
            Token t = new Token("CHARACTERLITERAL", String.valueOf(sb), line);
        	
            return t;
        }

        Token t = new Token("STRINGLITERAL", String.valueOf(sb), line);
    	
        return t;
    }

    
    public boolean checkmultipleoperators()
    {
        for (String op : RELATIONALOPERATORS) 
        {
            if (input.startsWith(op, position)) 
            {
                return true;
            }
        }
        
        return false;
    }
    
    private Token extractidentifierorkeyword() 
    {
        StringBuilder sb = new StringBuilder();
        boolean hasdigit = false;
        boolean hasunderscore = false;

        while (position < input.length() && (Character.isLowerCase(input.charAt(position)) || Character.isDigit(input.charAt(position)) || input.charAt(position) == '_')) 
        {
            if (Character.isDigit(input.charAt(position))) 
            {
                hasdigit = true;
            }
            
            if (input.charAt(position) == '_') 
            {
                hasunderscore = true;
            }
            
            sb.append(input.charAt(position++));
        }

        String identifier = sb.toString();

        if (KEYWORDS.contains(identifier)) 
        {
        	Token t = new Token("KEYWORD", identifier, line);
        	
            return t;
        }
        
        if (IO.contains(identifier)) 
        {
        	Token t = new Token("IO", identifier, line);
        	
            return t;
        }
        
        if (BOOLEANLITERAL.contains(identifier)) 
        {
        	Token t = new Token("BOOLEANLITERAL", identifier, line);
        	
            return t;
        }
        
        if (position < input.length() && Character.isUpperCase(input.charAt(position))) 
        {
            String str = "";
            char current = input.charAt(position);
            
            while (position < input.length() && !Character.isWhitespace(current) && current != '=' && current != '\n') 
            {            	
                str += current;
                position++;
                
                if(input.charAt(position) == '\n')
                {
                	line++;
                }
                
                if (position < input.length()) 
                {
                    current = input.charAt(position);
                }
                else 
                {
                    break; 
                }
            }
            
            identifier += str;
            
            errorhandler.reporterror(line, "Invalid variable name: '" + identifier + "' (variable names cannot contain capital letters)");
            
            return null;
        }

                
        if (hasdigit)
        {
            errorhandler.reporterror(line, "Invalid variable name: '" + identifier + "' (variable names cannot contain digits)");
                    	
            return null;
        }

        if (hasunderscore)
        {
            errorhandler.reporterror(line, "Invalid variable name: '" + identifier + "' (variable names cannot contain underscore)");
                    	
            return null;
        }

        
        if (!inmain && !identifier.equalsIgnoreCase("main")) 
        {
            globalvariables.put(identifier, line);            
        }

    	Token t = new Token("IDENTIFIER", identifier, line);
    	
        return t;
    }

    
    public void skipline()
    {
    	while(position < input.length() && input.charAt(position) != '\n') 
    	{
            position++;
        }
    	
        line++;
        
        if (position < input.length()) 
        {
            position++;
        }
    }
    
    public void fillsymboltable(SymbolTable table) 
    {
    	for (Token t : tokens) 
        {
    		if(!t.type.equalsIgnoreCase("INTEGER") && !t.type.equalsIgnoreCase("DECIMAL") && !t.type.equalsIgnoreCase("STRINGLITERAL") && !t.type.equalsIgnoreCase("CHARACTERLITERAL") && !t.type.equalsIgnoreCase("SINGLELINECOMMENT") && !t.type.equalsIgnoreCase("MULTILINECOMMENT"))
    		{
                boolean isglobal = globalvariables.containsKey(t.lexeme); 
                
                if (isglobal) 
                {
                    table.addsymbol(t.lexeme, t.type, "Global", memloc); 
                } 
                else 
                {
                    table.addsymbol(t.lexeme, t.type, "Local", memloc);
                }
                
                memloc++;

    		}    		
        }
    }    
}