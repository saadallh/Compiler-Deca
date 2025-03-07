package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

import fr.ensimag.arm.pseudocode.OperandArm;

public class ImmediateStringArm extends DValArm{
	    private String value;
	    public ImmediateStringArm(String value) {
	        super();
	        this.value = value;
	    }

	    @Override
	    public String toString() {
	        return
				"\t\t" + "asciz :" +	"\"" + value.replace("\"", "\"\"") + "\"";
	    }
	    
	    @Override
	    public String toStringWord() {
			// TODO Auto-generated method stub
			return null;
		}
	    @Override
	    void display(PrintStream s){
	    	s.println("		.ascii " + toString());
	        s.println();
	    }

	}
