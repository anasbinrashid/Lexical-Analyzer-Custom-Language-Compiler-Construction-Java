package Assignment1;

public class Symbol 
{
	String symbolname;
	String symboltype;
	String declarationspace;
	int location;
	
    public Symbol(String n, String t, String ds, int l) 
    {
        this.symbolname = n;
        this.symboltype = t;
        this.declarationspace = ds;
        this.location = l;
    }

    public String getsymbolname() {
		return symbolname;
	}

	public String getsymboltype() {
		return symboltype;
	}

	public String getdeclarationspace() {
		return declarationspace;
	}

	public int getlocation() {
		return location;
	}


	public String tostring() 
    {
    	String printstatement = String.format("Symbol Name: %s, Symbol Type: %s, Declaration Scope: %s, Location: %d", symbolname, symboltype, declarationspace, location);
        return printstatement;
    }

}
