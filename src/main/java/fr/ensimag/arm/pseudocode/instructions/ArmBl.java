package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.UnaryInstructionArm;

/**
 * branch linked
 * @author ouazzmoh
 * @date 23/01/2023
 */

public class ArmBl extends UnaryInstructionArm{
	public ArmBl(OperandArm op1) {
		super(op1);
	}

	@Override
	public String getName(){
		return "bl";
	}
}