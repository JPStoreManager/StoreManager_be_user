# TODO [멀티 모듈 프로젝트] 멀티 모듈 프로젝트로 전환 시 수정 필요
FROM bellsoft/liberica-openjdk-alpine:21

CMD ["./gradlew", "clean", "build"]

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} store-manager-user.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/store-manager-user.jar"]