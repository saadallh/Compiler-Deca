package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Syntax error for an expression that should be an lvalue (ie that can be
 * assigned), but is not.
 *
 * @author gl24
 * @date 01/01/2023
 */
public class InvalidMethod extends DecaRecognitionException {

    private static final long serialVersionUID = 4670163376041273741L;

    public InvalidMethod(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    @Override
    public String getMessage() {
        return "L'appel de cette construction n'est pas compatible dans le langage sans objet";
    }
}
