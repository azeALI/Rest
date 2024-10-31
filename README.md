You have to configure your database credentials first. Go to src/main/resources , edit application.properties file to your needs. Then come to the root folder of project and run this to build the project: $ mvn install
target folder will appear on root directory, go into it and there will be a .jar file, you can run it with : $ java -jar somejarfile.jar
