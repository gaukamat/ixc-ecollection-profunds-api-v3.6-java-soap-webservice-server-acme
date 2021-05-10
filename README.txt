# Running the war file on Tomcat 9.x server without having to manually run and deploy Tomcat
# or use ANT tasks to copy the final war file (from target) to the webapp directory of the 
# pre-installed (and setup) local Tomcat distribution.

mvn clean verify org.codehaus.cargo:cargo-maven2-plugin:run  -Dcargo.maven.containerId=tomcat9x  -Dcargo.maven.containerUrl=https://repo1.maven.org/maven2/org/apache/tomcat/tomcat/9.0.41/tomcat-9.0.41.zip


