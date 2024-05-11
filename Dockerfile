FROM openjdk:17-oracle
RUN mkdir data-shared
COPY target/ms-workflow.jar ms-workflow.jar
EXPOSE 8017
ENTRYPOINT ["java","-jar","/ms-workflow.jar"]