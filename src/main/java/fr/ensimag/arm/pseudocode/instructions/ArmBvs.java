package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.UnaryInstructionArm;

/**
 * BVS label, branch if overflow
 * @author ouazzmoh
 * @date 23/01/2023
 */

public class ArmBvs extends UnaryInstructionArm{
	public ArmBvs(OperandArm op1) {
		super(op1);
	}

	@Override
	public String getName(){
		return "bvs";
	}
}