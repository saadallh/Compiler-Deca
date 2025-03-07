package fr.ensimag.arm.pseudocode;

import fr.ensimag.arm.pseudocode.InstructionArm;
import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.UnaryInstructionArm;

import java.io.PrintStream;

/**
 * Bgt : goes to the bloc of instructions specified if the cmp
 * instruction before specifies that there is a inequality as r1>r2.
 * "bgt _next" exemple
 * @author aitdriss
 * @date 23/01/2023
 */

public class FunctionsInstructionArm extends InstructionArm {
	public FunctionsInstructionArm() {
		super();
	}

	@Override
	protected void displayOperands(PrintStream s){

	}

	@Override
	protected void display(PrintStream s){

	}
}