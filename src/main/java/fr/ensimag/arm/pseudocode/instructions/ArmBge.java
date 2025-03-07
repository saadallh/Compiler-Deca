package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.UnaryInstructionArm;

/**
 * Bgt : goes to the bloc of instructions specified if the cmp
 * instruction before specifies that there is a inequality as r1>=r2.
 * "bgt _next" exemple
 * @author aitdriss
 * @date 23/01/2023
 */

public class ArmBge extends UnaryInstructionArm{
	public ArmBge(OperandArm op1) {
		super(op1);
	}

	@Override
	public String getName(){
		return "bge";
	}
}