package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.BinaryInstructionArm;
import fr.ensimag.arm.pseudocode.GPRegisterArm;
import fr.ensimag.arm.pseudocode.OperandArm;

/**
 * LDR loads a 32-bit constant (LDRH (halfword): 16 bit, LDRB (byte): 8 bit)
 * from memory into the specified target register.
 *
 * @author aitdriss
 * @date 17/01/2023
 */

public class LDRreg extends BinaryInstructionArm{
	public LDRreg(GPRegisterArm op1, OperandArm adr) {
		super(op1, adr);
	}

	@Override
	public String getName(){
		return "ldr";
	}

//	protected void displayOperands(PrintStream s) {
//		s.print(" ");
//		s.print(this.getOperand1());
//		s.print(", [");
//		s.print(this.getOperand2());
//		s.print("]");
//	}
}