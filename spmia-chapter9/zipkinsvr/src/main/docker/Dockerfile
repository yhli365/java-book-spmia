FROM johncarnell/spmia-jdk

RUN mkdir -p /usr/local/zipkinserver
ADD @project.build.finalName@.jar /usr/local/zipkinserver/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
