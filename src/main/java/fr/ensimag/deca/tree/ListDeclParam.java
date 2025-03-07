package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclParam extends TreeList<AbstractDeclParam> {

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Not yet implemented");
		int c = 0;
        for (AbstractDeclParam i : getList()) {
            if( c == 0 ){
                if(i != null){
                    i.decompile(s);
                    c = c + 1;
                }
			}
            else{
                if(i != null){
                    s.print(",");
                    i.decompile(s);
                }             
            }         
        }
	}
	
	public Signature verifyListDeclParam(DecacCompiler compiler) throws ContextualError {
		Signature s = new Signature();
		for (AbstractDeclParam e: getList()) {
			Type t = e.verifyDeclParam(compiler);
			s.add(t);
		}
		return s;
	}

	public EnvironmentExp verifyParam(DecacCompiler compiler, EnvironmentExp env2) throws DecacFatalError, ContextualError {
		// TODO Auto-generated method stub
		EnvironmentExp envr = new EnvironmentExp(env2);
		EnvironmentExp env;
		for (AbstractDeclParam e: getList()) {
			env = e.verifyParam(compiler);
			env.UnionDisjoint(envr);
		}
		return envr;
		
	}
}

