package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.UnaryInstructionArm;

/**
 * BVC label, branch if overflow
 * @author ouazzmoh
 * @date 23/01/2023
 */

public class ArmBvc extends UnaryInstructionArm{
	public ArmBvc(OperandArm op1) {
		super(op1);
	}

	@Override
	public String getName(){
		return "bvc";
	}
}