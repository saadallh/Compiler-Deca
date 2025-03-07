package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

// A FAIRE: étendre cette classe pour traiter la partie "avec objet" de Déca
/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author gl24
 * @date 01/01/2023
 */
public class EnvironmentType {
    public EnvironmentType(DecacCompiler compiler) {
        
        envTypes = new HashMap<Symbol, TypeDefinition>();
        
        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));

        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        //envTypes.put(stringSymb, new TypeDefinition(STRING, Location.BUILTIN));
        
        Symbol nullType = compiler.createSymbol("null");
        NULL = new NullType(nullType);
        
        // not added to envTypes, it's not visible for the user.
        Symbol object = compiler.createSymbol("Object");
        OBJECT = new ClassType(object);
        OBJECT.definition = new ClassDefinition(OBJECT, Location.BUILTIN, null);
        OBJECT.definition.setNumberOfMethods(1);
        Symbol equals = compiler.createSymbol("equals");
        Signature s = new Signature();
        s.add(OBJECT);
        MethodDefinition eq = new MethodDefinition(this.BOOLEAN, Location.BUILTIN, s, 1);
        try {
        	OBJECT.definition.getMembers().declare(equals, eq);
		} catch (DoubleDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        envTypes.put(object, OBJECT.definition);
        
        
    }

    private final Map<Symbol, TypeDefinition> envTypes;

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }
    
    public void declareClass(Symbol s, ClassDefinition def) {
    	envTypes.put(s, def);
    }
    
    public boolean subType(Type t, Type t2) {
    	if(t.sameType(t2)) {
    		return true;
    	}
    	if (t.isNull() && t2.isClass()) {
    		return true;
    	}
    	Set<Symbol> s = envTypes.keySet();
    	Iterator<Symbol> i = s.iterator();
    	ClassDefinition def = null;
    	while(i.hasNext()) {
    		Symbol m = i.next();
    		if(defOfType(m).isClass()) {
        		def = (ClassDefinition) defOfType(m);
        		if(def.getType().sameType(t)) {
        	    	ClassDefinition superClass = def.getSuperClass();
        	    	while(superClass != null) {
        	    		if(superClass.getType().sameType(t2)) {
        	    			return true;
        	    		}
        	    		superClass = superClass.getSuperClass();
        	    	}
        		}
    		}
    	}
    	return false;
    }
    
    public void Empilement(EnvironmentType env){
    	Set<Symbol> s = envTypes.keySet();
    	Iterator<Symbol> i = s.iterator();
    	while(i.hasNext()) {
    		Symbol verif = i.next();
    		if(env.defOfType(verif) == null) {
    			env.declareClass(verif, (ClassDefinition) envTypes.get(verif));
    		}
    	}
    }
    
    

    public final NullType    NULL;
    public final VoidType    VOID;
    public final IntType     INT;
    public final FloatType   FLOAT;
    public final StringType  STRING;
    public final BooleanType BOOLEAN;
    public final ClassType OBJECT;
}

