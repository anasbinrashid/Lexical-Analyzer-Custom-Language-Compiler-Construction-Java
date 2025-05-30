package Assignment1;

import java.util.*;

public class SymbolTable {
	
    private Map<String, Symbol> table;

    public SymbolTable() 
    {
        table = new HashMap<>();
    }

    public void addsymbol(String symbolname, String symboltype, String declarationspace, int location) 
    {
        Symbol symbol = new Symbol(symbolname, symboltype, declarationspace, location);
        table.put(symbolname, symbol);
    }

    public void displaysymboltable()
    {
        System.out.println("\n\nSymbol Table:\n");
        System.out.printf("%-20s | %-20s | %-10s | %-10s\n", "Name", "Type", "Scope", "Memory Location");
        System.out.println("---------------------------------------------------------------------------");
        
        for (Symbol symbol : table.values()) 
        {
            System.out.printf("%-20s | %-20s | %-10s | %-10s\n", symbol.getsymbolname(), symbol.getsymboltype(), symbol.getdeclarationspace(), symbol.getlocation());
        }
    }
    
}
