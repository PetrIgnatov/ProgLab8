cp ../server.jar ./server/server.jar 
sudo docker build -t server_app_7 server/
sudo docker run -i server_app_7
