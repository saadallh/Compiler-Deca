package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class Return extends AbstractInst {
	private AbstractExpr expression;
	
	
	
	public Return(AbstractExpr expression) {
		super();
		this.expression = expression;
	}

	@Override
	protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
			Type returnType) throws ContextualError {
		// TODO Auto-generated method stub
		if (returnType.isVoid()) {
			throw new ContextualError("impossible d'appler return lorsque le type est void", this.getLocation());
		}
		expression.verifyRValue(compiler, localEnv, currentClass, returnType);
	}

	@Override
	protected void codeGenInst(DecacCompiler compiler, Label endIf) {
		expression.codeGenReturn(compiler);
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		s.print("return ");
		this.expression.decompile(s);
		s.print(";");	
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		expression.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		//throw new DecacInternalError("not implemented yet");
		expression.iter(f);
	}

}

