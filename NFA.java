package Assignment1;

import java.util.*;

public class NFA 
{
    public static class State 
    {
        int id;
        Map<Character, List<State>> transitions = new HashMap<>();
        List<State> epsilontransitions = new ArrayList<>();

        public State(int id) 
        {
            this.id = id;
        }

        public void addtransition(char symbol, State next) 
        {
            transitions.computeIfAbsent(symbol, k -> new ArrayList<>()).add(next);
        }

        public void addepsilontransition(State next) 
        {
            epsilontransitions.add(next);
        }
    }

    public State start;
    public State accept;
    private int nextid; 

    private NFA(State start, State accept, int nextid) 
    {
        this.start = start;
        this.accept = accept;
        this.nextid = nextid;
    }

    public static NFA basic(char symbol, int nextid) 
    {
        State s0 = new State(nextid++);
        State s1 = new State(nextid++);
        s0.addtransition(symbol, s1);
        return new NFA(s0, s1, nextid);
    }

    public static NFA concatenate(NFA first, NFA second) 
    {
        first.accept.addepsilontransition(second.start);
        return new NFA(first.start, second.accept, second.nextid);
    }

    public static NFA union(NFA first, NFA second) 
    {
        int nextid = Math.max(first.nextid, second.nextid);
        
        State start = new State(nextid++);
        State accept = new State(nextid++);

        if (!start.epsilontransitions.contains(first.start)) {
            start.addepsilontransition(first.start);
        }
        
        if (!start.epsilontransitions.contains(second.start)) {
            start.addepsilontransition(second.start);
        }
        
        if (!first.accept.epsilontransitions.contains(accept)) {
            first.accept.addepsilontransition(accept);
        }
        
        if (!second.accept.epsilontransitions.contains(accept)) {
            second.accept.addepsilontransition(accept);
        }

        return new NFA(start, accept, nextid);
    }


    public static NFA kleenestar(NFA nfa) 
    {
        int nextid = nfa.nextid;
        State start = new State(nextid++);
        State accept = new State(nextid++);
        start.addepsilontransition(nfa.start);
        start.addepsilontransition(accept);
        nfa.accept.addepsilontransition(nfa.start);
        nfa.accept.addepsilontransition(accept);
        return new NFA(start, accept, nextid);
    }

    
    private static String insertconcatenation(String regex) 
    {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < regex.length(); i++) 
        {
            char c = regex.charAt(i);
            sb.append(c);
            
            if (i < regex.length() - 1) 
            {
                char next = regex.charAt(i + 1);
                
                if (c != '(' && c != '|' && next != ')' && next != '|' && next != '*') 
                {
                    sb.append('.');
                }
            }
        }
        
        return sb.toString();
    }

    private static int precedence(char c) 
    {
        switch (c) 
        {
            case '*': 
            	return 3;
            case '.': 
            	return 2;
            case '|': 
            	return 1;
            default: 
            	return 0;
        }
    }

    private static String infixtopostfixconversion(String regex) 
    {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        
        for (char c : regex.toCharArray()) 
        {
            if (c == '(') 
            {
                stack.push(c);
            } 
            else if (c == ')') 
            {
                while (!stack.isEmpty() && stack.peek() != '(')
                    output.append(stack.pop());
                
                if (!stack.isEmpty())
                    stack.pop();
                
            } 
            else if (c == '*' || c == '.' || c == '|') 
            {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c))
                    output.append(stack.pop());
                
                stack.push(c);
            } 
            else 
            {
                output.append(c);
            }
        }
        
        while (!stack.isEmpty())
            output.append(stack.pop());
        
        return output.toString();
    }

    private static String preprocessregularexpression(String regex) 
    {
        regex = regex.replaceAll("\\\\b", "");
        regex = regex.replaceAll("\\\\d", "[0-9]");
        regex = regex.replaceAll("\\[0-9\\]", "(0|1|2|3|4|5|6|7|8|9)");
        
        return regex;
    }

    public static NFA regularexpressiontonfa(String regex) 
    {
        regex = preprocessregularexpression(regex);
        regex = insertconcatenation(regex);
        String postfix = infixtopostfixconversion(regex);
        
        int nextid = 0;
        
        Stack<NFA> stack = new Stack<>();
        
        for (char c : postfix.toCharArray()) 
        {
            if (c == '*') 
            {
                NFA nfa = stack.pop();
                stack.push(kleenestar(nfa));
            } 
            else if (c == '.') 
            {
                NFA nfa2 = stack.pop();
                NFA nfa1 = stack.pop();
                stack.push(concatenate(nfa1, nfa2));
            } 
            else if (c == '|') 
            {
                NFA nfa2 = stack.pop();
                NFA nfa1 = stack.pop();
                stack.push(union(nfa1, nfa2));
            } 
            else 
            {
                stack.push(basic(c, nextid));
                nextid = stack.peek().nextid;
            }
        }
        
        return stack.pop();
    }

    public void displaytransitiontable() 
    {
        System.out.println("\nNFA Transition Table:");
        Set<State> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);
        
        while (!queue.isEmpty()) 
        {
            State s = queue.poll();
            
            for (Map.Entry<Character, List<State>> entry : s.transitions.entrySet()) 
            {
                char symbol = entry.getKey();
                for (State t : entry.getValue()) 
                {
                    System.out.println("State " + s.id + " --" + symbol + "--> State " + t.id);
                    
                    if (!visited.contains(t)) 
                    {
                        visited.add(t);
                        queue.add(t);
                    }
                }
            }
            
            for (State t : s.epsilontransitions) 
            {
                System.out.println("State " + s.id + " --ε--> State " + t.id);
                
                if (!visited.contains(t)) 
                {
                    visited.add(t);
                    queue.add(t);
                }
            }
        }
        
        System.out.println("Total NFA states: " + visited.size());
    }
}
