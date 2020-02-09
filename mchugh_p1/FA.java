package mchugh_p1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

// class to hold information /methods about FA
public class FA {
	// NFA object
	public ArrayList<ArrayList<String>> NFA = new ArrayList<ArrayList<String>>();
	public ArrayList<Integer> accept_states = new ArrayList<Integer>();
	public boolean accept = true;
	// max states = 1001. create array of bools
	public boolean[] is_accept = new boolean[1001];
	// rejected strings
	public HashSet<String> rejected_strings = new HashSet<String>();
	// accepted strings
	public HashSet<String> accepted_strings = new HashSet<String>();
	public Map<String, ArrayList<String>> transitions = new HashMap<String, ArrayList<String>>();
	public String start_state = "";

	// NFA generator
	public void NFA_generator() {
		// if nothing, do not accept
		if(NFA.size() == 0) {
			accept = false;
		}
		// if nonempty
		if(NFA.size() != 0) {
			// get start and first state
			String first_state = NFA.get(0).get(1);
			// if NFA is not empty
			if(start_state.isEmpty() != true) {
				String state_status = first_state.substring(1);
				// get next state
				String curr_state = NFA.get(0).get(0) + NFA.get(0).get(1).charAt(0);
				// if transition set contains
				if(transitions.containsKey(curr_state)) {
					// create list of valid transitions
					ArrayList<String> NFA_states = new ArrayList<>(transitions.get(curr_state));

					for(int x = 0; x < NFA_states.size(); x++) {
						// String state
						String string_state = NFA_states.get(x);
						// Convert to int
						int int_state = Integer.parseInt(string_state);

						//if at end of string input
						if(state_status.isEmpty() == true) {
							// if string is at accept state
							if(is_accept[int_state]) {
								// if string is not already in list
								if(accepted_strings.contains(string_state) != true) {
									// add string
									accepted_strings.add(string_state);
									//System.out.println("String State: " + string_state);
								}
								// if not accept state and there are no accepted strings
								if(is_accept[int_state] != true && accepted_strings.size() == 0){
									// if rejected string is not already there, add it
									if(rejected_strings.contains(string_state)){
										rejected_strings.add(string_state);
									}
								}
							}
						}
						//nonempty
						else {
							ArrayList<String> updates = new ArrayList<String>(Arrays.asList(NFA_states.get(x), state_status));
							NFA.add(updates);
						}
					}
				}
				// else reject
				else accept = false;
			}
			else {
				String string_c_state = NFA.get(0).get(0);
				int int_c_state = Integer.parseInt(string_c_state);
				if(is_accept[int_c_state] == true) {
					accepted_strings.add(string_c_state);
				}
				else {
					// not an acceptable string
					rejected_strings.add(string_c_state);
				}
			}
			NFA.remove(0);
		}
	}

	public void set_start_state(String i, String start) {
		start_state = start;
		NFA.add(new ArrayList<String>(Arrays.asList(start, i)));
	}

	public void set_transition(String input_trans, String end) {
		if(transitions.containsKey(input_trans) == false) {
			transitions.put(input_trans, new ArrayList<String>());
			transitions.get(input_trans).add(end);
		}
	}

	public void set_accept_state(String s) {
		// convert to int
		int int_state = Integer.parseInt(s);
		is_accept[int_state] = true;
		// add to list
		accept_states.add(int_state);
	}

	public void pretty_print() {
		System.out.println("Accepted Strings: " + accepted_strings);
		if(accepted_strings.size() > 0) {
			System.out.print("Accept ");
			/*
			for(int i = 0; i < accepted_strings.size(); i++){
				System.out.print(accepted_strings + " ");
			 */
			for(String str : accepted_strings) System.out.print(str + " ");
		}
		else {
			/*
			for(int j = 0; j < rejected_strings.size(); j++) {
				System.out.print(j + " ");
			 */
			System.out.print("Reject: ");
			for(String rej : rejected_strings) System.out.print(rej + " ");
		}
	}
}
