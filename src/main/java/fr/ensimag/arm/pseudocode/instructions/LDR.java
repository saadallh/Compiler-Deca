package fr.ensimag.arm.pseudocode.instructions;
import java.io.PrintStream;

import fr.ensimag.arm.pseudocode.*;

/**
 * LDR loads a 32-bit constant (LDRH (halfword): 16 bit, LDRB (byte): 8 bit) 
 * from memory into the specified target register.
 *
 * @author aitdriss
 * @date 10/01/2023
 */

public class LDR extends BinaryInstructionArm{

	public LDR(GPRegisterArm op1, LabelArm op2) {
		super(op1, op2);
		// TODO Auto-generated constructor stub
	}

	
    protected void displayOperands(PrintStream s) {
        s.print(" ");
        s.print(this.getOperand1());
        s.print(", =");
        s.print(this.getOperand2());
    }


}
