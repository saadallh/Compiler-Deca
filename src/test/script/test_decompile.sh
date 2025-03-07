cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:../global/bin/ima"$PATH"


test_decompile(){
    decac -p "$1.deca" > "res.deca" 
    cat "res.deca"
    decac -p "res.deca" >"res2.deca" 
    cat "res2.deca"
    if ! diff -q "res.deca" "res2.deca" ; then
        echo "Resultat non attendu decompile erronee"
        exit 1
    else
        echo "Success attendu"
    fi
}




direction_fichier_sans_deca="./src/test/deca/syntax/valid/Test"

test_decompile "$direction_fichier_sans_deca"
