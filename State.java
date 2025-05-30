package Assignment1;

import java.util.*;

public class State 
{
    int id;
    Map<Character, State> transitions = new HashMap<>();
    boolean accepting;

    public State(int id, boolean accepting) 
    {
        this.id = id;
        this.accepting = accepting;
    }
}

