# Compiler from scratch
gl24, 01/01/2023.


En ce qui concerne l'extension, vous trouverez le script run_arm pour executer les assembleurs de l'ARM.
Pour cela il faut avoir une machine linux et utiliser l'emulateur qemu.
Avant de lancer l'execution du fichier:
    sudo apt install gcc-arm-linux-gnueabi
    sudo apt install qemu-user
Execution :
    ./run_arm fichier.ass

Pour ne pas causer de problème avec le compilateur ima, nous n'avons pas fait le merge avec notre avancement globale sur l'extension.
Pour l'instant le print ca marche que pour les chaine de caractères.
