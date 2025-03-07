package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;

/**
 * Operand of an ARM Instruction.
 *
 * @author aitdriss
 * @date 10/01/2023
 */

public abstract class DValArm extends OperandArm{
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t .asciz ";
	}
	

    void display(PrintStream s){
    	s.println("		.ascii " + toString());
        s.println();
    }


	public String toStringWord() {
		// TODO Auto-generated method stub
		return null;
	}


}
