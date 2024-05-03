cp ../server.jar ./server/server.jar 
sudo docker build -t server_app_8 server/
sudo docker run -i server_app_8
