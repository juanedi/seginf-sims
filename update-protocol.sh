#! /bin/bash

#
# SCRIPT PARA COPIAR ÚLTIMA VERSIÓN DEL JAR DEL PROTOCOLO
# DE MENSAJES COMO DEPENDENCIA DE LAS APLICACIONES.
#
# UTILIZAR DESPUÉS DE COMPILAR LA BIBLIOTECA.
#

echo 'sims/lib/' 'demo-db/lib/' 'demo-ldap/lib/' | xargs -n1 cp sims-msg-protocol/target/sims-msg-converters*.jar
