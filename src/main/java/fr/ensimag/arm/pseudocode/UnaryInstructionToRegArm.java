package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

public class UnaryInstructionToRegArm extends UnaryInstructionArm{

	protected UnaryInstructionToRegArm(OperandArm operand) {
		super(operand);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void display(PrintStream s) {
		// TODO Auto-generated method stub
		 s.print("		" + getName());
         displayOperands(s);
	}
}
