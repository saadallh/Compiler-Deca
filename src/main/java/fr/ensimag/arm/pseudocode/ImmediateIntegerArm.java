package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

/**
 * Immediate integer.
 * 
 * @author aitdriss
 * @date 10/01/2023
 */


public class ImmediateIntegerArm extends DValArm{
	private int value;

    public ImmediateIntegerArm(int value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "#" + value;
    	//return "" + value;
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
    
    @Override
    void display(PrintStream s){
    	s.println("		.word " + value);
        s.println();
    }
}
