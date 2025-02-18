FROM openjdk:17

WORKDIR /usrapp/bin

ENV PORT 35000

COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency

# Copiar los archivos estáticos (HTML, CSS, JS, etc.)
COPY src/main/webapp /usrapp/bin/webapp

CMD ["java","-cp","./classes:./dependency/*","org.example.MicroSpringBoot"]