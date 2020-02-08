package mchugh_p1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class mchugh_p1 {
	public static void main(String[] args){
		FA nfa = new FA();
		String test_file;
		String i;

		// error checking
		if(args.length != 2) {
			System.out.println("Invalid Input");
			return;
		}
		else {
			i = args[1];
			test_file = args[0];
		}

		try { // need to surrond buffered reader with try/catch
			// Get input
			BufferedReader input = new BufferedReader(new FileReader(test_file));
			String l;

			while((l = input.readLine()) != null){
				// Break up lines by whitespace characters
				String[] l_u = l.split("\\s+");
				// Get info on specific line
				String status = l_u[0];
				String state = l_u[1];

				// Check type
				if(status.equals("state")) {
					if(l_u.length == 3) {
						String next_type = l_u[2];
						if(next_type.equals("acceptstart")) {
							nfa.set_start_state(state, i);
							nfa.set_accept_state(state);
						}
						else if(next_type.equals("accept")) {
							nfa.set_accept_state(state);
						}
						else if(next_type.equals("start")) {
							nfa.set_start_state(state, i);
						}
						else { // impossible, invalid entry
							System.out.println("Invalid entry for state: " + next_type);
							return;
						}
					}
					else {
						if(l_u[3].equals("accept") && l_u[2].equals("start")) {
							nfa.set_start_state(state, i);
							nfa.set_accept_state(state);
						}
						else {
							System.out.println("Invalid entry type");
							return;
						}
					}
					nfa.NFA_generator();
					nfa.pretty_print();
					input.close();
				}
			}
		}
		catch(IOException e) {
			System.out.println("Error, File Not Found");
		}
	}
}