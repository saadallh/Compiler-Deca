package fr.ensimag.arm.pseudocode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;



/**
 * Abstract representation of an ARM program, i.e. set of Lines.
 * And representation of data as a dictionnary
 *
 * @author aitdriss
 * @date 10/01/2023
 */

public class ArmProgram  {
	private final LinkedList<AbstractLineArm> lines = new LinkedList<AbstractLineArm>();
	//public static HashMap<LabelArm, DValArm> data = new HashMap<LabelArm, DValArm>();
	/**public static  LabelArm getLabel(int i){
		int c = 0;
		for (LabelArm a: data.keySet()) {
			if (i==c) {
				return a;
			}
		}
		return null;
	}
	
	public static  LabelArm getLabel(){
		int c = 0;
		for (LabelArm a: data.keySet()) {
			if (data.keySet().size()>=0 &&c==data.keySet().size()-1) {
				return a;
			}
		}
		return null;
	}
	*/
    public void add(AbstractLineArm line) {
        lines.add(line);
    }
    
    public void addComment(String s) {
        lines.add(new LineArm(s));
    }

    public void addLabel(LabelArm l) {
        lines.add(new LineArm(l));
    }
    public void addInstruction(InstructionArm i) {
        lines.add(new LineArm(i));
    }
    public void addInstruction(InstructionArm i, String s) {
        lines.add(new LineArm(null, i, s));
    }
    
    /**
     * Append the content of program p to the current program. The new program
     * and p may or may not share content with this program, so p should not be
     * used anymore after calling this function.
     */
    public void append(ArmProgram p) {
        lines.addAll(p.lines);
    }
    
    /**
     * Add a line at the front of the program.
     */
    public void addFirst(LineArm l) {
        lines.addFirst(l);
    }

    /**
     * Display the program in a textual form readable by ARM to stream s.
     */
    public void display(PrintStream s) {
        for (AbstractLineArm l: lines) {
            l.display(s);
        }
//        s.println(".section .data");
//        for (LabelArm lab :DecacCompiler.data.keySet() ) {
//        	lab.display(s);
//        	if (DecacCompiler.data.get(lab)!= null) {
//        		DecacCompiler.data.get(lab).display(s);
//        	}
//        }
        }

    /**
     * Return the program in a textual form readable by ARM as a String.
     */
    public String display() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream s = new PrintStream(out);
        display(s);
        return out.toString();
    }

    public void addFirst(InstructionArm i) {
        addFirst(new LineArm(i));
    }
    
    public void addFirst(InstructionArm i, String comment) {
        addFirst(new LineArm(null, i, comment));
    }

    public LinkedList<AbstractLineArm> getLines() {
        return lines;
    }
}
