package fr.ensimag.arm.pseudocode;

import fr.ensimag.arm.pseudocode.LabelArm;

import java.io.PrintStream;

/**
 *Line of code in an Arm Program
 *
 * @author ouazzmoh
 * @date 10/01/2023
 */
public class LineArm extends AbstractLineArm {


    private InstructionArm instruction;
    private String comment;
    private LabelArm label;
    
    
    

	public LineArm(String s) {
		// TODO Auto-generated constructor stub
		this.comment = s;
	}

	public LineArm(LabelArm l) {
		// TODO Auto-generated constructor stub
		this.label = l;
	}

	public LineArm(InstructionArm i) {
		// TODO Auto-generated constructor stub
		this.instruction = i;
	}

	public LineArm(LabelArm l, InstructionArm i, String s) {
		// TODO Auto-generated constructor stub
		this.comment = s;
		this.instruction = i;
		this.label = l;
		
		
	
	}

	@Override
    void display(PrintStream s){
        //TODO: comments ..etc
        if (instruction != null){
            instruction.display(s);
            s.println();
        }
        if (label != null){
            label.display(s);
        }
    }

    public InstructionArm getInstruction() {
        return instruction;
    }

    public void setInstruction(InstructionArm instruction) {
        this.instruction = instruction;
    }
}
