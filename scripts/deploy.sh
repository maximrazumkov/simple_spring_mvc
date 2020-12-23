#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/portal-1.0-SNAPSHOT.jar \
    caramba@192.168.0.240:/home/caramba/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa caramba@192.168.0.240 << EOF
pgrep java | xargs kill -9
nohup java -jar portal-1.0-SNAPSHOT.jar > log.txt &
EOF

echo 'Bye'