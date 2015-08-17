#Installation instructions

Base folder is `cloudmanage/code`
1. User Interface (UI): 
    
        cd ui
        //Install yo, grunt-cli, bower, generator-angular and generator-karma:
        npm install -g grunt-cli bower yo generator-karma generator-angular
        //Install javascript dependencies
        bower install
        //Build project
        grunt build
2. Service (service)

        cd service
        mvn clean package -DskipTests=true
        java -jar target/cloudmanage-1.0.0-SNAPSHOT.jar
    By default java 1.6 is supported. Change the version in pom.xml to run java 1.7+
3. Apache
    
    The project is divided into two applications, UI and Service. To avoid CORS issues, it is recommended to run both the applications under apache. Sample conf is committed at `cloudmanage/code/config/apache`. 

# Frameworks
1. [Yoeman](https://github.com/yeoman/generator-angular): To generate boilerplate angularjs application. `ui` is generated using yoeman. It is served entirely by apache.
2. [Spring Boot](http://projects.spring.io/spring-boot/): Spring boot with embedded tomcat is used to server rest based services.