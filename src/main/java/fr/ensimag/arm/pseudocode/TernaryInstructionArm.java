package fr.ensimag.arm.pseudocode;

import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Base class for instructions with 2 operands.
 *
 * @author aitdriss
 * @date 10/01/2023
 */

public class TernaryInstructionArm extends InstructionArm{

    private OperandArm operand1, operand2, operand3;

    public OperandArm getOperand1() {
        return operand1;
    }

    public OperandArm getOperand2() {
        return operand2;
    }

    public OperandArm getOperand3() {
        return operand3;
    }

    @Override
	protected
    void displayOperands(PrintStream s) {
        s.print(" ");
        s.print(operand1);
        s.print(", ");
        s.print(operand2);
        s.print(", ");
        s.print(operand3);
    }

    protected TernaryInstructionArm(OperandArm op1, OperandArm op2, OperandArm op3) {
        Validate.notNull(op1);
        Validate.notNull(op2);
        Validate.notNull(op3);
        this.operand1 = op1;
        this.operand2 = op2;
        this.operand3 = op3;
    }

	@Override
	protected void display(PrintStream s) {
		// TODO Auto-generated method stub
		 s.print("		" + getName());
         displayOperands(s);
	}
}

