package fr.ensimag.deca.context;

import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import org.apache.commons.lang.Validate;

/**
 * Type defined by a method.
 *
 * @author gl24
 * @date 01/01/2023
 */
public class MethodType extends Type {
    /*
    protected MethodDefinition definition;
    
    public MethodDefinition getDefinition() {
        return this.definition;
    }
            
    @Override
    public MethodType asMethodType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isMethod() {
        return true;
    }

    @Override
    public boolean isMethodOrNull() {
        return true;
    }
*/

    /**
     * Standard creation of a type method.
     */
/*
    public MethodType(Symbol methodName, Location location, MethodDefinition superClass) {
        super(methodName);
        this.definition = new MethodDefinition(this, location, superClass);
    }
*/
    /**
     * Creates a type representing a method methodName.
     * (To be used by subclasses only)
     */
    protected MethodType(Symbol methodName) {
        super(methodName);
    }
    

    @Override
    public boolean sameType(Type otherType) {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    /**
     * Return true if potentialSuperClass is a superclass of this method.
     */
/*
    public boolean isSubMethodOf(MethodType potentialSuperClass) {
        throw new UnsupportedOperationException("not yet implemented"); 
    }
*/
}
