#!/bin/bash
set -e

echo 'Executando testes e cobertura de código..'
mvn test verify
echo 'Sucesso!'

exec "$@"
