# ###########
# echo "Maven setup if not already done."
# ###########
export MAVEN_PATH="/usr/share/maven/bin"
export PATH=$MAVEN_PATH:$PATH
# ###########
echo "Build Package and Docker Container Deploy" 
echo "Build package (.war)"
# ###########
mvn clean package

echo "We need to be in the Maven project that we are building. Paths will be resolved relatively."
export PACKAGE_NAME="ixc-ecollection-profunds-api-v3.6-swaggerhub-jaxrs-resteasy-server-acme-2.0.0-configrity.war"
export PACKAGE_SOURCE="./target"
# ###########
# echo "Local Tomcat Deployment"
# ###########
# export PACKAGE_TARGET="/opt/tomcat/apache-tomcat-9.0.35/webapps"
# copy $PACKAGE_SOURCE/$PACKAGE_NAME $PACKAGE_TARGET                                                                            
# ###########
echo "Local Docker Deployment"
# ###########
export DEPLOYMENT_IMAGE_NAME="gaukamat/ixc-ecollection-profunds-api-v3.6-swaggerhub-jaxrs-resteasy-server-acme"
export DEPLOYMENT_IMAGE_TAG="2.0.0-configrity"
export DEPLOYMENT_CONTAINER_NAME="ecol-configrity-ninja"
# #Check what containers are running.
docker container ls    
echo "Remove the docker container and the image. Housekeeping !"
echo "If the tag name or version changes, the older versions of image and contain will need to be #oved manually."
docker container rm -f $DEPLOYMENT_CONTAINER_NAME
docker image rm -f $DEPLOYMENT_IMAGE_NAME:$DEPLOYMENT_IMAGE_TAG
echo "Build a new image and container from the new package."
docker build --rm  --tag $DEPLOYMENT_IMAGE_NAME:$DEPLOYMENT_IMAGE_TAG .
docker create --name ecol-configrity-ninja --publish 9836:8080 $DEPLOYMENT_IMAGE_NAME:$DEPLOYMENT_IMAGE_TAG
# #Start the container.
docker container start ecol-configrity-ninja
# #Check if the container is running.
docker container ls
docker logs --follow  --timestamps $DEPLOYMENT_CONTAINER_NAME  
# #Tail container log
# #Test using a tool (curl, JMeter) of your choice.
# #[EOF]