package org.rossedth.fsm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jeasy.states.api.AbstractEvent;
import org.jeasy.states.api.FiniteStateMachine;
import org.jeasy.states.api.FiniteStateMachineException;
import org.jeasy.states.api.State;
import org.jeasy.states.api.Transition;
import org.jeasy.states.core.FiniteStateMachineBuilder;
import org.jeasy.states.core.TransitionBuilder;

public class RecognizerFSM {
	private FiniteStateMachine fsm;
	private String path;

	public RecognizerFSM() {
		/*
		 * Define FSM states
		 */
		State s_State = new State("S");
		State q_State = new State("Q");
		State p_State = new State("P");
		State r_State = new State("R");

		Set<State> states = new HashSet<>();
		states.add(s_State);
		states.add(p_State);
		states.add(q_State);
		states.add(r_State);

		/*
		 * Define FSM transitions
		 */
		Transition s_q = new TransitionBuilder()
				.name("S to Q with b")
				.sourceState(s_State)
				.eventType(BEvent.class)
				.targetState(q_State)
				.build();

		Transition q_p = new TransitionBuilder()
				.name("Q to P with a")
				.sourceState(q_State)
				.eventType(AEvent.class)
				.targetState(p_State)
				.build();

		Transition p_p = new TransitionBuilder()
				.name("P to P with a")
				.sourceState(p_State)
				.eventType(AEvent.class)
				.targetState(p_State)
				.build();

		Transition p_q = new TransitionBuilder()
				.name("P to Q with b")
				.sourceState(p_State)
				.eventType(BEvent.class)
				.targetState(q_State)
				.build();

		Transition q_r_b = new TransitionBuilder()
				.name("Q to R with b")
				.sourceState(q_State)
				.eventType(BEvent.class)
				.eventHandler(new Recognized())                
				.targetState(r_State)
				.build();

		Transition q_r_c = new TransitionBuilder()
				.name("Q to R with c")
				.sourceState(q_State)
				.eventType(CEvent.class)
				.eventHandler(new Recognized())
				.targetState(r_State)
				.build();

		/*
		 * Build FSM instance
		 */
		fsm = new FiniteStateMachineBuilder(states, s_State)
				.registerTransition(s_q)
				.registerTransition(q_p)
				.registerTransition(p_p)
				.registerTransition(p_q)
				.registerTransition(q_r_b)
				.registerTransition(q_r_c)
				.registerFinalState(r_State)
				.build();		

		path="";
	}

	public void processInput(String input) throws FiniteStateMachineException {
		AbstractEvent event=null;
		switch(input.trim().toUpperCase()) {
		case "A":	event=new AEvent();
				  	break;
		case "B": 	event=new BEvent();
					break;
		case "C": 	event=new CEvent();
					break;
		default: System.out.println("Unsupported entry!");
		}
		
		if(event!=null) {
			if (transitionExists(fsm.getCurrentState(), event)) {           
				fsm.fire(event);
				path=buildPath(event);     
				System.out.println("Processed sequence: "+ path);  
			}
			else
			{
				System.out.println("Invalid input for current state " + fsm.getCurrentState().getName());
			}			
		}
	}

	public FiniteStateMachine getFSM() {
		return fsm;
	}

	public boolean atFinalState() {
		boolean atFinal=false;
		Set<State> finals=fsm.getFinalStates();
		atFinal=finals.contains(fsm.getCurrentState());
		return atFinal;
	}

	public boolean transitionExists(State s, AbstractEvent event) {
		boolean exists=false;
		Set<Transition> transitions = fsm.getTransitions();
		for (Transition transition : transitions) {
			if (
					fsm.getCurrentState().equals(transition.getSourceState()) && //fsm is in the right state as expected by transition definition
					transition.getEventType().equals(event.getClass())//fired event type is as expected by transition definition
					) {
				exists=true;
				break;
			} 
		}
		return exists;
	}


	/*
	 * Printing methods
	 */


	//Function to generate a graphical representation of the Automata using GraphViz
	public void genGraph(GraphViz gv){
		//  Adding states 
		gv.addln("  # NODES"+"\n");
		String dot="";

		gv.addln("  o [style=solid,color=black,fillcolor=black,shape=point];"+"\n");
		for(State s:fsm.getStates()) {
			if(fsm.getFinalStates().contains(s)){
				if(s==fsm.getCurrentState()){
					dot="  "+s.getName()+"[shape = doublecircle,fillcolor=\"#a9d18e\"];";			
				}
				else{
					dot="  "+s.getName()+"[shape = doublecircle];";
				}				
			}
			else if(s==fsm.getCurrentState()){
				dot="  "+s.getName()+"[fillcolor=\"#a9d18e\"];";
			}
			else {
				dot="  "+s.getName()+";";
			}
			gv.addln(dot+"\n");
		}

		//  Adding transitions
		gv.addln("  # TRANSITIONS"+"\n");
		String source="";
		String target="";
		String label="";
		source="o";
		target=fsm.getInitialState().getName();
		gv.addln("  "+ source+" -> "+target+";"+"\n");

		for(Transition t:fsm.getTransitions()) {
			source=t.getSourceState().getName();
			target=t.getTargetState().getName();
			label=t.getName().substring(t.getName().indexOf("with")+5);
			if(!fsm.getFinalStates().contains(t.getTargetState())){
				
				//gv.addln("  "+ source+" -> "+target+" [label = "+label +"];"+"\n");
				if(fsm.getLastTransition()==t) {
					gv.addln("  "+ source+" -> "+target+" [color=\"#a9d18e\",label = "+label +"];"+"\n");				
				}
				else {
					gv.addln("  "+ source+" -> "+target+" [label = "+label +"];"+"\n");
				}
			}
		}

		gv.addln("  {rank=same"+"\n");
		for(Transition t:fsm.getTransitions()) {
			if(fsm.getFinalStates().contains(t.getTargetState())){
				source=t.getSourceState().getName();
				target=t.getTargetState().getName();
				label=t.getName().substring(t.getName().indexOf("with")+5);
				//gv.addln("  "+ source+" -> "+target+" [label = "+label +"];"+"\n");
				if(fsm.getLastTransition()==t) {
					gv.addln("  "+ source+" -> "+target+" [color=\"#a9d18e\",label = "+label +"];"+"\n");				
				}
				else {
					gv.addln("  "+ source+" -> "+target+" [label = "+label +"];"+"\n");
				}
			}
		}
		gv.addln("  }"+"\n");
		
		//End graph construction
		gv.addln(gv.end_graph());

		//Print out Digraph
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("FSM.dot"))) {
			writer.write(gv.getDotSource());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gv.setNewGraph();

	}

	public void printCurrentState(GraphViz gv) {
		System.out.println("Recognizer at State : " + fsm.getCurrentState().getName());
		System.out.println("=================================================");
		genGraph(gv);		
	}

	public String buildPath(AbstractEvent e) {
		String separator;
		if (path.isEmpty()){
			separator= " ";
		} 
		else {
			separator="-";
		}
		return  path+separator+e.getName().charAt(0);
	}
}
