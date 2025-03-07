package fr.ensimag.deca;


import java.io.File;
import org.apache.log4j.Logger;

import net.bytebuddy.asm.Advice.Exit;

import java.util.concurrent.*;


/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl24
 * @date 01/01/2023
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.print("***** team gl24 *****\n");
            System.exit(0);
        }
        if (options.getSourceFiles().isEmpty()) {
//            throw new UnsupportedOperationException("decac without argument not yet implemented");
            //TODO: Should it still display the options ?
            //options for the decac command
            System.out.print(". -b (banner) : affiche une bannière indiquant le nom de l'équipe\n" +
                    ". -p (parse) : arrête decac après l'étape de construction de" +
                    "l'arbre, et affiche la décompilation de ce dernier" +
                    "(i.e. s'il n'y a qu'un fichier source à" +
                    "compiler, la sortie doit être un programme" +
                    "deca syntaxiquement correct)\n" +
                    ". -v (verification) : arrête decac après l'étape de vérifications" +
                    "(ne produit aucune sortie en l'absence d'erreur)\n" +
                    ". -n (no check) : supprime les tests à l'exécution spécifiés dans" +
                    "les points 11.1 et 11.3 de la sémantique de Deca.\n" +
                    ". -r X (registers) : limite les registres banalisés disponibles à" +
                    "R0 ... R{X-1}, avec 4 <= X <= 16\n" +
                    ". -d (debug) : active les traces de debug. Répéter" +
                    "l'option plusieurs fois pour avoir plus de" +
                    "traces.\n" +
                    ". -P (parallel) : s'il y a plusieurs fichiers sources," +
                    "lance la compilation des fichiers en" +
                    "parallèle (pour accélérer la compilation)\n +" +
                    ".  -arm : compile vers un assembleur ARM 32bits\n");

        }
        if (options.getParallel()) {
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            for(File source : options.getSourceFiles()){
                Future<Boolean> f = executor.submit(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        Boolean err = false;
                        DecacCompiler compiler;
                        //Checking how many register to use
                        if (options.getCustomNumReg() >= 4 && options.getCustomNumReg() <= 16){
                            compiler = new DecacCompiler(options, source, options.getCustomNumReg());
                        }
                        else {
                            compiler = new DecacCompiler(options, source);
                        }
                        //Beginning of compilation
                        if (compiler.compile()) {
                            err = true;
                        }
                        return err;
                    }
                });
                try {
                    f.get();
                } catch (ExecutionException | CancellationException | InterruptedException ex){
                    System.exit(1);
                }
            }
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler;
                //Checking how many register to use
                if (options.getOptionr()){
                    compiler = new DecacCompiler(options, source, options.getCustomNumReg());
                }
                else {
                    compiler = new DecacCompiler(options, source);
                }
                //Beginning of compilation
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
