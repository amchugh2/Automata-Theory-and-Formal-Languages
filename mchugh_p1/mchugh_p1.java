package mchugh_p1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class mchugh_p1 {
	public static void main(String[] args) {
		// Create NFA
		FA nfa = new FA();
		// Initialize file and input
		String file = "";
		String input = "";
		// FIXME: Hard-coded paths
		//String sample1_path = "C:\\Users\\abbym\\eclipse-workspace\\mchugh_p1\\src\\sample_1.txt";
		//String sample2_path = "C:\\Users\\abbym\\eclipse-workspace\\mchugh_p1\\src\\sample_2.txt";

		//Check for correct # args
		if(args.length == 2) {
			file = args[0];
			input = args[1];
		} else {
			System.out.println("Invalid Input: include only input file and input string");
		}

		try {
			// OFFICE HOURS: How to read multiple input files at once and get path?

			BufferedReader input_file = new BufferedReader(new FileReader("C:\\Users\\abbym\\eclipse-workspace\\mchugh_p1\\src\\sample_1.txt"));
			String input_line;

			//Parse text file by line
			while((input_line = input_file.readLine()) != null) {
				String[] input_split = input_line.split("\\s+");
				String state = input_split[1];
				String type = input_split[0];

				// if state
				if(type.equals("state")) {
					// 3 types
					if(input_split.length == 3) {
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
						if(next_type.equals("start")) {
							nfa.set_start_state(state, input);
						}
						// if accept
						else if(next_type.equals("accept")) {
							nfa.set_accept_state(state);
						}
						else {
							// then, not valid entry
							System.out.println("Invalid state entry");
							return;
						}

					} else {
						// End
						if(input_split[2].equals("start") && input_split[3].equals("accept")) {
							nfa.set_accept_state(state);
							nfa.set_start_state(state, input);
						} else {
							// Invalid
							System.out.println("Invalid state type");
							return;
						}
					}
					// If type equals tranisition
				} else if(type.equals("transition")) {
					String transition = input_split[1] + input_split[2];
					nfa.set_valid_transition(transition, input_split[3]);
				} else {
					System.out.println("Invalid input type");
					return;
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
		//finally 
	}
}
