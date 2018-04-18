# Introduction
A sample Java Spring Boot project to test how to replicate the Couchbase to Elastic Search and browse it in Kibana
# Required Steps
- [x] Create a SnapEngage trial account, install the chat code on a webpage
    - [x] Setup a SnapEngage trial account
    - [x] Write a test HTML with the chat code and test the chat  
- [x] Configure the POST API to send chat details to an endpoint you will create - http://developer.snapengage.com/post-api/post-api/
    - [x] Write a dummy REST POST Endpoint(webhook) and start the server
    - [x] Use ngrok and tunnel the running service
    - [x] Configure the tunnel URL in the SnapEngage POST API
    - [x] Perform a sample chat with few messages and terminate the chat
    - [x] Wait for SnapEngage to push the chat event
- [x] Endpoint should persist chat data and allowing it to be accessed again
    - [x] Chat Data Model
        - [x] Implement the data model from https://developer.snapengage.com/post-api/post-api-details-json/
        - [x] Configure JSON Serializer to convert the data from SNAKE_CASE
        - [x] Configure model to convert the date time to proper Java objects
    - [x] Create an endpoint `/chat-event/v1`
    - [x] Persist chat data
        - [x] Implement it using POST
        - [x] Determine and use the ID from the given chat data itself. Don't generate it. It should take care of updates(if any)
        - [ ] Validate and throw 400 if the bad data is provided
    - [x] Allowing it to be accessed again
        - [x] Entire data set - implement it using GET
            - [x] Return empty list if not found
        - [x] Specific Chat data by ID - implement it using GET with the ID path variable
            - [x] Throw 404 if it is not found
    - [x] Decide the persistence - Considered Couchbase (Because I would like to explore it)
        - [x] Use official couchbase docker and write scripts auto setup the single-node cluster and configure the bucket
        - [x] Manually setup RBAC for the bucket. The username should be as same as the bucket name
        - [x] Configure couchbase bean to be strongly consistent, create N1ql primary index and read the couchbase cluster host, bucket name and bucket password from the configuration file
- [x] Using the data you store, visualize or analyze it in some way you feel is interesting
    - [x] Explore ELK (Elastic Search, Logstash & Kibana) to analyze it
        - [ ] Explore Option 1: Using ELK, log each created chat data
            - The updated documents will be shown as duplicate
        - [x] Explore Option 2: Using Couchbase -> Elastic Search replication
            - The updated documents will be immediately reflected in the elastic search
            - [x] Install Elastic Search plugin: elasticsearch-transport-couchbase
            - [x] Manually setup the template with the required mappings (`curl -XPUT http://localhost:9200/_template/couchbase <TEMPLATE CONTENT>`)
            - [x] Manually create an index(`curl -XPUT http://localhost:9200/index-name`)
            - [x] Manually setup the Couchbase -> Elastic Search replication (https://developer.couchbase.com/documentation/server/5.1/connectors/elasticsearch/getting-started.html)
            - [x] Configure Kibana to use the index

# Non-functional
- [x] Unit Tests
- [ ] Cucumber BDDs(Integration Tests)
- [x] Readable
- [ ] Completable Future based  endpoints
- [ ] Input Validations
- [ ] ENUM for choices
- [ ] Whitelist only the create request
- [ ] Comments/Javadoc
- [ ] Swagger (springfox)
- [ ] Health Endpoint (Actuator)
- [ ] Pagination
- [ ] Setup Couchbase indexes for search usecases
    - [ ] Time-range search/fetch
- [ ] Proper connection and request time-outs and failsafe retries
- [ ] Proper connection pooling setup
- [ ] Release resources during the server shutdown
- [ ] Business Metrics: metrics-spring(Dropwizard Metrics library) can be added
- [ ] Centralised logging using logstash 

# Steps to run
## Perquisites
- Standard tools Java based application development
- Docker and Docker Compose
- ngrok for tunneling

## Couchbase
- Start the Couchbase instance: `docker-compose up -d --build --force-recreate --no-deps couchbase`
- Wait for it to auto-setup the cluster and bucket
- Login to the cluster(`http://localhost:8091`) with the credentials`Administrator/password`)
- Create a new user with the name `chat-event`(Password: `notSoSecret`) under the `Security` menu
    - The bucket name and the user name should be the same
    - Assign the `Bucket Full Access` role for the `chat-event` bucket

## Spring Boot REST Application
- Build the application: `mvn clean package`
- Start the REST endpoints: `docker-compose up -d --build --force-recreate --no-deps chat-event-store`
- Tunnel the local application: `ngrok http 80` 
- Configure the tunnel URL in the snapengage dashboard. Example: `https://5c6b92cf.ngrok.io/chat-event/v1`
- Perform a sample chat using the HTML page available under `resources/test-scripts/chat.html`
- Wait for the Snapengage to post the chat
- Check the Couchbase bucket and verify the documents

## Elastic Search
- Start the Elastic Search: `docker-compose up -d --build --force-recreate --no-deps elasticsearch`
- Wait for it start: `http://localhost:9200/`
- Create the couchbase template(payload available under `ops/elasticsearch/couchbase-template.json`)
```
    curl -XPUT 'http://localhost:9200/_template/couchbase' -d '{
      "template": "*",
      "order": 10,
      "mappings": {
        "couchbaseCheckpoint": {
          "_source": {
            "includes": [
              "doc.*"
            ]
          },
          "dynamic_templates": [
            {
              "store_no_index": {
                "match": "*",
                "mapping": {
                  "store": "no",
                  "index": "no",
                  "include_in_all": false
                }
              }
            }
          ]
        },
        "couchbaseDocument": {
          "properties": {
            "doc.createdAtDate": {
              "type": "date",
              "format": "epoch_millis"
            },
            "doc.ipAddress": {
              "type": "ip"
            }
    
          }
        }
      }
    }'
```
- Create a new index for couchbase replication: `curl -XPUT 'http://localhost:9200/chat-event'`
-  Manually setup the Couchbase -> Elastic Search replication (https://developer.couchbase.com/documentation/server/5.1/connectors/elasticsearch/getting-started.html)
    - Elastic Search hostname: `elasticsearch:9091`
    - Credentials: `Administrator/password`
    - Remote Bucket: `chat-event`
- Verify the index and replication: `http://localhost:9200/_cat/indices?v`

## Kibana
- Start the Kibana: `docker-compose up -d --build --force-recreate --no-deps kibana`
- Wait for it start(It will take several minutes to configure): `http://localhost:5601` 
- Kibana: Configure the index pattern `chat-event` under `Management` and create the required visualisation and analytics
    - Enable the auto-refresh and enjoy the created real-time analytics and visualisations 
