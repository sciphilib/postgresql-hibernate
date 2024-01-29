path=$(mvn clean package | grep 'Building jar' | awk '{printf $4}')
java -jar $path
