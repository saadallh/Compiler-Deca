package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

import fr.ensimag.arm.pseudocode.DValArm;

/**
 * Immediate operand containing a float value.
 * 
 * @author aitdriss
 * @date 20/01/2023
 */
public class ImmediateFloatArm extends DValArm {
    private float value;

    public ImmediateFloatArm(float value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "#" + Float.toHexString(value);
    }
    
    @Override
    void display(PrintStream s){
    	s.println("		.double " + value);
        s.println();
    }
    
    
    @Override
    public String toStringSWI() {
        return " " + value;
    	//return "" + value;
    }
    @Override
    public String toStringWord() {
		// TODO Auto-generated method stub
		return "" + value;
	}
}