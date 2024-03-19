# 基础镜像
FROM openjdk:8-jdk-alpine

# 指定工作目录
WORKDIR /app

# 将jar包添加到工作目录，如target/anti-fraud-backend-0.0.1-SNAPSHOT.jar
ADD /anti-fraud-backend-0.0.1-SNAPSHOT.jar .

# 暴露端口
EXPOSE 8101

# 配置容器启动后执行的命令
ENTRYPOINT ["java", "-jar", "/app/anti-fraud-backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
