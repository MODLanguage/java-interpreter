FROM gradle:alpine

# copy over our app code
COPY ./build.gradle /home/gradle/project/
COPY ./settings.gradle /home/gradle/project/
COPY ./src /home/gradle/project/src

USER root
RUN chown -R gradle /home/gradle/project
USER gradle