package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.UnaryInstructionArm;

/**
 * Always branch
 * @author aitdriss
 * @date 23/01/2023
 */

public class ArmBal extends UnaryInstructionArm{
	public ArmBal(OperandArm op1) {
		super(op1);
	}


	@Override
	public String getName(){
		return "bal";
	}
}