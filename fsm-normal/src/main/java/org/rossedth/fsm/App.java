package org.rossedth.fsm;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import org.jeasy.states.api.FiniteStateMachineException;


/**
 * This tutorial is an implementation of the turnstile FSM described in <a href="http://en.wikipedia.org/wiki/Finite-state_machine">wikipedia</a>:
 * <p>
 * The turnstile has two states: Locked and Unlocked. There are two inputs that affect its state: putting a coin in the slot (coin) and pushing the arm (push).
 * In the locked state, pushing on the arm has no effect; no matter how many times the input push is given it stays in the locked state.
 * Putting a coin in, that is giving the machine a coin input, shifts the state from Locked to Unlocked.
 * In the unlocked state, putting additional coins in has no effect; that is, giving additional coin inputs does not change the state.
 * However, a customer pushing through the arms, giving a push input, shifts the state back to Locked.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class App {

	public static void main(String[] args) throws FiniteStateMachineException, IOException {


		/*
		 * Create a RecognizerFSM instance
		 */
		RecognizerFSM recognizer=new RecognizerFSM();
		

		/*
		 * Setup GraphViz
		 */
	
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.setup_graph();

		
		/*
		 * Fire some events and print FSM state
		 */

		recognizer.printCurrentState(gv);

		Scanner scanner = new Scanner(System.in);
		System.out.println("Insert an input or Press [q] to quit tutorial.");
		System.out.println("=================================================");

		while (true) {
			String input = scanner.nextLine();

			recognizer.processInput(input);
			recognizer.printCurrentState(gv);

			if (recognizer.atFinalState()) {
				System.out.println("Recognizer has reach final state ");   
				System.exit(0);
				scanner.close();                
			}

			if (input.trim().equalsIgnoreCase("q")) {
				System.out.println("input = " + input.trim());
				System.out.println("Bye!");
				System.exit(0);
				scanner.close();
			}         

		}

	} 

}