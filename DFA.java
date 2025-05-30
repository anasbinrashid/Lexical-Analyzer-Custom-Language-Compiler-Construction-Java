package Assignment1;

import java.util.*;

public class DFA 
{
    public State start;
    public Map<Set<Integer>, State> statemapping = new HashMap<>();

    private static Set<NFA.State> epsilonclosure(Set<NFA.State> states) 
    {
        Set<NFA.State> closure = new HashSet<>(states);
        Stack<NFA.State> stack = new Stack<>();
        stack.addAll(states);

        while (!stack.isEmpty()) 
        {
            NFA.State s = stack.pop();
            
            for (NFA.State t : s.epsilontransitions) 
            {
                if (!closure.contains(t)) 
                {
                    closure.add(t);
                    stack.push(t);
                }
            }
        }
        
        return closure;  
    }

    private static Set<NFA.State> move(Set<NFA.State> states, char symbol)
    {
        Set<NFA.State> result = new HashSet<>();
        
        for (NFA.State s : states) 
        {
            List<NFA.State> targets = s.transitions.get(symbol);
            
            if (targets != null) 
            {
                result.addAll(targets);
            }
        }
        
        return result;
    }

	public static DFA nfatodfa(NFA nfa) 
	{
	    DFA dfa = new DFA();
	    Map<Set<Integer>, State> dfastates = new HashMap<>();
	    Queue<Set<NFA.State>> queue = new LinkedList<>();
	
	    Set<NFA.State> closurebegin = epsilonclosure(Set.of(nfa.start));
	    Set<Integer> startids = new HashSet<>();
	
	    for (NFA.State s : closurebegin)
	    {
	        startids.add(s.id);
	    }
	
	    boolean accepting = closurebegin.contains(nfa.accept);
	    State dfabegin = new State(dfastates.size(), accepting);
	    dfa.start = dfabegin;
	    dfastates.put(startids, dfabegin);
	    dfa.statemapping.put(startids, dfabegin);
	    queue.add(closurebegin);
	
	    while (!queue.isEmpty()) 
	    {
	        Set<NFA.State> current = queue.poll();
	        Set<Integer> currentids = new HashSet<>();
	
	        for (NFA.State s : current)
	            currentids.add(s.id);
	
	        State currentdfa = dfastates.get(currentids);
	
	        Set<Character> symbols = new HashSet<>();
	
	        for (NFA.State s : current)
	            symbols.addAll(s.transitions.keySet());
	
	        for (char symbol : symbols) 
	        {
	            Set<NFA.State> move = move(current, symbol);
	            Set<NFA.State> closureend = epsilonclosure(move);
	
	            if (closureend.isEmpty())
	                continue;
	
	            Set<Integer> targetids = new HashSet<>();
	
	            for (NFA.State s : closureend)
	                targetids.add(s.id);
	
	            State goaldfa = dfastates.get(targetids);
	
	            if (goaldfa == null) 
	            {
	                boolean goalaccepting = closureend.contains(nfa.accept);
	                goaldfa = new State(dfastates.size(), goalaccepting);
	                dfastates.put(targetids, goaldfa);
	                dfa.statemapping.put(targetids, goaldfa);
	                queue.add(closureend);
	            }
	
	            currentdfa.transitions.put(symbol, goaldfa);
	        }
	    }
	
	    System.out.println("\nDFA State Mappings:\n");
	    
	    for (Map.Entry<Set<Integer>, State> entry : dfastates.entrySet()) 
	    {
	        System.out.println("DFA State: " + entry.getValue().id + " corresponds to NFA states: " + entry.getKey());
	    }
	
	    
	    return dfa;
	}
}
