package mchugh_p1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class mchugh_p1{

	class FA {
		// Create complete list for FA
		public LinkedList<LinkedList<String>> FA = new LinkedList<LinkedList<String>>();

		// Map of valid valid_transitions
		public TreeMap<String, ArrayList<String>> valid_transitions = new TreeMap<String, ArrayList<String>>();
		public String start;

		// List of Accept states
		public ArrayList<Integer> accept_state = new ArrayList<Integer>();

		// Array of potential accept states - 1000 valid_transitions
		public boolean[] accept_states = new boolean[1000];

		// Treesets to hold accepted_strings and rejected_strings strings
		public TreeSet<String> accepted_strings = new TreeSet<String>();
		public TreeSet<String> rejected_strings = new TreeSet<String>();

		// Bool - default mode = accept
		public boolean accept = true;

		// NFA generator
		public void NFA_generator() {
			// While FA is not empty
			while(FA.size() != 0) {
				// Get input state num
				String input_state = FA.get(0).get(1);

				if(input_state.isEmpty() == true) { // INPUT STATE IS EMPTY
					// get first state
					String first_state = FA.get(0).get(0);
					//System.out.println(first_state);
					int int_NS = Integer.parseInt(first_state);

					if(accept_states[int_NS] == false) {
						rejected_strings.add(first_state);
					}
					else {
						accepted_strings.add(first_state);
					}
				}
				// If input state is not empty
				else {
					// Create key
					// Get next transition
					String next = FA.get(0).get(0) + FA.get(0).get(1).charAt(0);
					String status = input_state.substring(1);
					//System.out.println(e);

					if(!valid_transitions.containsKey(next)) accept = false;

					else {
						//If valid transition
						// Get list of states beginning with next
						LinkedList<String> states = new LinkedList<>(valid_transitions.get(next));

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
								LinkedList<String> updated = new LinkedList<String>(Arrays.asList(states.get(i), status));
								FA.add(updated);
							}
						}
					}
				}
				// REMOVE
				FA.remove(0);
			}
		}

		// set accept state
		public void set_accept_state(String state) {
			// Change to int
			int int_state = Integer.parseInt(state);
			// Set bool list
			accept_states[int_state] = true;
			// Add to list of accept states
			accept_state.add(int_state);
		}

		// set start state
		public void set_start_state(String input, String n) {
			start = input;
			FA.add(new LinkedList<String>(Arrays.asList(start,n)));
		}

		// set transitions
		public void set_valid_transition(String input, String end) {
			if(valid_transitions.containsKey(input) == false) {
				valid_transitions.put(input, new ArrayList<String>());
			}
			valid_transitions.get(input).add(end);
		}

		// Formatting
		public void pretty_print() {
			if(accepted_strings.size() > 0) {
				System.out.print("accept ");
				for(String s : accepted_strings) {
					System.out.print(s + " ");
				}
			} else {
				System.out.print("reject ");
				for(String s : rejected_strings) {
					System.out.print(s + " ");
				}
			}
		}
	}

	public static void main(String[] args) {
		// Create NFA
		mchugh_p1 p1 = new mchugh_p1();
		FA nfa = p1.new FA();
		// Initialize file and input
		//File file;
		String input = args[1];
		//String path = "";
		//File file_input = new File(args[0]);
		//System.out.println(ii);

		// FIXME: Hard-coded paths
		//String sample1_path = "C:\\Users\\abbym\\eclipse-workspace\\mchugh_p1\\src\\mchugh_p1\\sample_1.txt";
		//String sample2_path = "C:\\Users\\abbym\\eclipse-workspace\\mchugh_p1\\src\\mchugh_p1\\sample_2.txt";

		//Check for correct # args
		if(args.length != 2) {
			System.out.println("Invalid Input: <FA_file_path> <input_string>");
		}

		try {
			// From prof.
			java.io.BufferedReader input_file = new java.io.BufferedReader(new java.io.FileReader(args[0]));
			String input_line;

			//Parse text file by line
			while((input_line = input_file.readLine()) != null) {
				// split input lines by whitespace
				String[] input_split = input_line.split("\\s+");
				// state
				String state = input_split[1];
				// type
				String type = input_split[0];

				// If type is transition
				if(type.equalsIgnoreCase("transition")) {
					// set transition
					String transition = input_split[1] + input_split[2];
					nfa.set_valid_transition(transition, input_split[3]);
				}
				// if state
				else if(type.equalsIgnoreCase("state")) {
					// 3 types
					// FIXME: THIS IS WHERE THE ERROR IS
					if(input_split.length != 3) { // Added additional if statement before declaring error
						if(input_split[2].equalsIgnoreCase("start") && input_split[3].equalsIgnoreCase("accept")) {
							// Not error
							nfa.set_start_state(state, input);
							nfa.set_accept_state(state);
						}
						else { //error
							System.out.println("Invalid state type");
							return;
						}
					}
					else {
						// next state
						String next_type = input_split[2];
						// if next type is either accept or start state
						if(next_type.equals("acceptstart")) {
							// set accept
							nfa.set_accept_state(state);
							// set start state
							nfa.set_start_state(state, input);
						}
						// if start
						else if(next_type.equalsIgnoreCase("start")) {
							nfa.set_start_state(state, input);
						}
						// if accept
						else if(next_type.equalsIgnoreCase("accept")) {
							nfa.set_accept_state(state);
						}
					}
				}
			}

			//Create NFA & Format Print
			nfa.NFA_generator();
			nfa.pretty_print();
			// Close BR
			input_file.close();

			// Error catching
		} catch(IOException e) {
			System.out.println("File not found");
		}
	}
}