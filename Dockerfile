FROM tomcat:8.5.45-jdk8-openjdk
MAINTAINER Gautam.Kamat
COPY ./target/ixc-ecollection-profunds-api-v3.6-java-soap-webservice-*.war /usr/local/tomcat/webapps/ 
