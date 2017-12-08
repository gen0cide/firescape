PWD := $(shell pwd)
SERVER_JAR=jars/server/server-1.0.jar
CLIENT_JAR=jars/client/client-1.0.jar
OPEN_LINK=xdg-open

.PHONY: all test build coverage clean

all: build

test:
	mvn test

build: $(CLIENT_JAR) $(SERVER_JAR)

coverage:
	mvn cobertura:cobertura

$(CLIENT_JAR):
	mvn -P client compile package

run-client: $(CLIENT_JAR)
	java -jar $(CLIENT_JAR) --server localhost --port 27337

.PHONY: run-client

$(SERVER_JAR):
	mvn -P server compile package

run-server: $(SERVER_JAR)
	java -jar $(SERVER_JAR)

.PHONY: run-server

view-coverage:
	$(OPEN_LINK) file://$(PWD)/server/target/site/cobertura/index.html

.PHONY: view-coverage

clean:
	rm $(CLIENT_JAR)
	rm $(SERVER_JAR)

docker-devubuntu:
	docker build -t firescape-dev:latest --file docker/Dockerfile.devubuntu .

.PHONY: docker-ubuntu

docker-server:
	$(MAKE) build
	docker build -t firescape-server:latest --file docker/Dockerfile.server .

.PHONY: docker-server

# TODO: currently breaks because it can't connect to Redis, need to implement
# container parameterization and maybe docker-compose.
run-server-docker:
	$(MAKE) docker-server
	docker run firescape-server:latest
