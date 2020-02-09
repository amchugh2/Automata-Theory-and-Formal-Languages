package mchugh_p1;
import java.util.*;

public class FA {
	// Create list for FA
    private ArrayList<ArrayList<String>> FA = new ArrayList<ArrayList<String>>();
    
    // HashSets to hold accepted_strings and rejected_strings strings
    private HashSet<String> accepted_strings = new HashSet<String>();
    private HashSet<String> rejected_strings = new HashSet<String>();
    
    // List of Accept states
    private ArrayList<Integer> accept_state = new ArrayList<Integer>();
    
    // Bool - default mode = accept
    private boolean reject = false;
    
    // Array of potential accept states - 1000 valid_transitions
    private boolean[] accept_states = new boolean[1000];
    
    // Map of valid valid_transitions
    private Map<String, ArrayList<String>> valid_transitions = new HashMap<String, ArrayList<String>>();
    private String start;

    public void set_start_state(String input, String n) {
        start = input;
        FA.add(new ArrayList<String>(Arrays.asList(start,n)));
    }

    public void set_accept_state(String state) {
    	// Change to int
        int int_state = Integer.parseInt(state);
        // Set bool list
        accept_states[int_state] = true;
        // Add to list of accept states
        accept_state.add(int_state);
    }

    public void set_valid_transition(String input, String end) {
        if(valid_transitions.containsKey(input) == false) {
        	valid_transitions.put(input, new ArrayList<String>());
        }
        valid_transitions.get(input).add(end);
    }
    
    // NFA generator
    public void NFA_generator() {
    	// While FA is not empty
        while(FA.size() != 0) {
        	// Get input state num
            String input_state = FA.get(0).get(1);

            // If input state is not empty
            if(input_state.isEmpty() == false) {
            	// Create key
                // Get next transition
                String next = FA.get(0).get(0) + FA.get(0).get(1).charAt(0);
                String status = input_state.substring(1);
                //System.out.println(e);

                //If valid transition
                if(valid_transitions.containsKey(next)) {
                	// Get list of states beginning with next
                    ArrayList<String> states = new ArrayList<>(valid_transitions.get(next));
                    
                    // Iterate thru list of new states
                    for(int i = 0; i < states.size(); i++) {
                    	// New state
                        String new_state = states.get(i);
                        // Integer to check for pool
                        int int_NS = Integer.parseInt(new_state);
                        
                        // if status is empty
                        if(status.isEmpty() == true) {
                        	// if in accept state
                            if(accept_states[int_NS]) {
                            	// if is not already included in accepted strings
                                if(accepted_strings.contains(new_state) == false) {
                                	// Add
                                	accepted_strings.add(new_state);
                                }
                            }
                            
                            // if NOT in accept states and empty
                            if(accept_states[int_NS] == false && accepted_strings.size() == 0) {
                            	// if NOT in rejected strings already, add it
                                if(rejected_strings.contains(new_state) == false) {
                                	rejected_strings.add(new_state);
                                }
                            }
                        } else {
                        	// update and add to FA
                            ArrayList<String> updated = new ArrayList<String>(Arrays.asList(states.get(i), status));
                            FA.add(updated);
                        }
                    }
                    // ELSE: REJECT THE STRING
                } else reject = true;
            } else { // INPUT STATE IS EMPTY
            	
                String first_state = FA.get(0).get(0);
                System.out.println(first_state);
                int int_NS = Integer.parseInt(first_state);

                if(accept_states[int_NS] == false) {
                	rejected_strings.add(first_state);
                }
                else{
                	accepted_strings.add(first_state);
                }
            }
            // REMOVE
            FA.remove(0);
        }
    }

    // Formatting
    public void pretty_print() {
        if(accepted_strings.size() > 0) {
            System.out.print("Accept ");
            for(String s : accepted_strings) System.out.print(s + " ");
        } else {
            System.out.print("Reject ");
            for(String s : rejected_strings) System.out.print(s + " ");
        }

        System.out.println();
    }
}
