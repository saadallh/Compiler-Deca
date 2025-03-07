package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

public class DeclParam extends AbstractDeclParam {
	final private AbstractIdentifier type;
	final private AbstractIdentifier name;
	
	
	public DeclParam(AbstractIdentifier type, AbstractIdentifier name) {
		super();
		this.type = type;
		this.name = name;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		this.type.decompile(s);
		s.print(" ");
		this.name.decompile(s);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		type.prettyPrint(s, prefix, false);
		name.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Type verifyDeclParam(DecacCompiler compiler) throws ContextualError {
		// TODO Auto-generated method stub
		Type t = type.verifyType(compiler);
		if (t.isVoid()) {
			throw new ContextualError("parametre can't be void type", type.getLocation());
		}
		return t;
	}

	@Override
	protected EnvironmentExp verifyParam(DecacCompiler compiler) throws ContextualError {
		// TODO Auto-generated method stub
		Symbol name = this.name.getName();
		Type t = this.type.verifyType(compiler);
		ParamDefinition param = new ParamDefinition(t, this.name.getLocation());
		EnvironmentExp res = new EnvironmentExp(null);
		try {
			res.declare(name, param);
		} catch (DoubleDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.name.setDefinition(param);
		type.setType(t);
		return res;
	}


	@Override
	public void setParamOperand(int paramOffset){
		name.getExpDefinition().setOperand(new RegisterOffset(paramOffset, Register.LB));
	}
}
