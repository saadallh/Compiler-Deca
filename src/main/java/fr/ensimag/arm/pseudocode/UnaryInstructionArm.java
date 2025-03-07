package fr.ensimag.arm.pseudocode;

import fr.ensimag.arm.pseudocode.OperandArm;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Instruction with a single operand
 *
 * @author ouazzmoh
 * @date 10/01/2023
 */
public abstract class UnaryInstructionArm extends InstructionArm {

    private OperandArm operand;

    @Override
    void displayOperands(PrintStream s){

        s.print(" ");
        s.print(operand);
    }

    protected UnaryInstructionArm(OperandArm operand){
        Validate.notNull(operand);
        this.operand = operand;
    }

    public OperandArm getOperand(){
        return operand;
    }
    @Override
    protected void display(PrintStream s){
        s.print("		" + getName());
        displayOperands(s);
    }

}
