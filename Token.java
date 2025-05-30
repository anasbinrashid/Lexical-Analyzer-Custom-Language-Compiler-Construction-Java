package Assignment1;

public class Token 
{
    public String type;
    public String lexeme;
    public int line;

    public Token(String t, String lex, int l) 
    {
        this.type = t;
        this.lexeme = lex;
        this.line = l;
    }
    
    public String tostring()
    {
        String str = String.format("Token {type='%s', lexeme='%s', line='%d'}", this.type, this.lexeme, this.line);
        
        return str;
    }
}
