FROM docker.io/adoptopenjdk/openjdk15:jre-15.0.1_9-centos

MAINTAINER Laeni<m@laeni.cn>

WORKDIR /opt

#添加程序及运行脚本
ADD --chown=1500:1500 sconf-server/target/sconf-server-1.0.0-SNAPSHOT-boot.jar app.jar

RUN chmod u+x app.jar

#将运行脚本加入环境变量
#ENV PATH=$PATH:/opt

EXPOSE 8080

#启动镜像时执行脚本
ENTRYPOINT ["./app.jar"]
