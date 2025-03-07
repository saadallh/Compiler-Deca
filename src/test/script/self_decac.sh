#! /bin/sh

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"
target_dir=$(pwd)/src/test/deca/codegen/
target_dir2=$(pwd)
test_decac_valid () {
    decac "$1.deca"
    ima "$(basename "$1").ass"  > "$1.res" 2>&1 
    if ! diff -q "$1.res" "src/test/deca/codegen/valid/ResAttendu/$(basename "$1").txt" ; then
        echo "Resultat non attendu"
    else
        echo "Success attendu"
    fi
}

test_decac_invalid(){
    decac "$1.deca"
    if ima "$(basename "$1").ass" | grep -q -e "$1:[0-9][0-9]*:" ; then
        echo "Echec attendu"
    else
        echo "Success inattendu"
    fi
}


for cas_de_test in src/test/deca/codegen/valid/*.deca
do

    direction_fichier_sans_deca="${cas_de_test%.*}"
    test_decac_valid "$direction_fichier_sans_deca" 
done

for cas_de_test in src/test/deca/codegen/invalid/*.deca
do
    direction_fichier_sans_deca="${cas_de_test%.*}"
    test_decac_invalid "$direction_fichier_sans_deca" 
done

find $target_dir -name "*.res" -delete
find $target_dir2 -name "*.ass" -delete
