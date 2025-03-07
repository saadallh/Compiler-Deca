package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.UnaryInstructionArm;

/**
 * Beq : goes to the bloc of instructions specified if the cmp
 * instruction before specifies that there isn't a equality.
 * "beq _next" exemple
 * @author aitdriss
 * @date 23/01/2023
 */

public class ArmBne extends UnaryInstructionArm{
	public ArmBne(OperandArm op1) {
		super(op1);
	}

	@Override
	public String getName(){
		return "bne";
	}
}
