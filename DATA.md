# Data Layer

There is going to be a Web UI for Firescape with the following properties:

 * Frontend NGINX for SSL termination, passes upstream to oauth2_proxy
 * oauth2_proxy service to perform authentication via OAUTH2
 * oauth2_proxy passes back to NGINX for Thin load balancing
 
This web application will be a nice front end. The database will be used by the game server as well as the Sidekiq ActiveJob framework for async processing.
