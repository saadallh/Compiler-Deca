package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;

public class CastExpr extends AbstractExpr {
	
	private AbstractIdentifier type;
	private AbstractExpr expr;

	public CastExpr(AbstractExpr operand, AbstractIdentifier type) {
		this.type = type;
		this.expr = operand;
	}
	
	public boolean isCompatible(DecacCompiler compiler, Type t1, Type t2) {
		if (t1.isFloat() && t2.isInt()) {
			return true;
		}
		else if (t1.isInt() && t2.isFloat()) {
			return true;
		}
		else if (compiler.environmentType.subType(t1, t2)) {
			return true;
		}
		else if (compiler.environmentType.subType(t2, t1)) {
			return true;
		}
		else {
			return false;
		}
		}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		Type t2 = type.verifyType(compiler);
		Type t1 = expr.verifyExpr(compiler, localEnv, currentClass);
		if (!t2.isVoid()) {
			if (isCompatible(compiler, t1, t2)) {
				this.setType(t2);
				return t2;
			}
			throw new ContextualError("cast types are not valids", this.getLocation());
		}
		throw new ContextualError("cast with void type is not valid", this.getLocation());
		
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("(");
		type.decompile(s);
		s.print(") (");
		this.expr.decompile(s);
		s.print(")");
	}
    @Override
    String prettyPrintNode() {
        return "Cast";
    }

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		type.prettyPrint(s, prefix, false);
		expr.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// leaf node => nothing to do
        type.iter(f);
        expr.iter(f);
	}

}
