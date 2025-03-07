package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractDeclMethod extends Tree {


    

	protected abstract EnvironmentExp verifyDeclMethod(DecacCompiler compiler, Symbol className,
			Symbol superClass, int j) throws ContextualError;
	
	protected abstract void verifyMethod(DecacCompiler compiler, Symbol className,
			EnvironmentExp env) throws ContextualError;

	protected abstract void codeGenDeclMethod(DecacCompiler compiler, String className);
}
