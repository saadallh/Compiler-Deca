package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.*;

/**
 * Beq : goes to the bloc of instructions specified if the cmp
 * instruction before specifies that there is a equality.
 * "beq _next" exemple
 * @author aitdriss
 * @date 23/01/2023
 */

public class ArmBeq extends UnaryInstructionArm{
	public ArmBeq(OperandArm op1) {
		super(op1);
	}

	@Override
	public String getName(){
		return "beq";
	}
}
