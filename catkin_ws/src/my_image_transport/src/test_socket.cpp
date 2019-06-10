#include <ros/ros.h>
#include <image_transport/image_transport.h>
#include <cv_bridge/cv_bridge.h>

#include <sensor_msgs/image_encodings.h>

#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include<string>    
#include <sstream>
#include <std_msgs/UInt8.h> 
#include <std_msgs/String.h> 

#include <sys/types.h> 
#include <sys/socket.h> 
#include <stdio.h> 
#include <stdlib.h> 
#include <string.h> 
#include <sys/ioctl.h> 
#include <unistd.h> 
#include <netdb.h> 
#include <netinet/in.h>   
#include  <arpa/inet.h>  
#include  <string.h>

using namespace cv;
using namespace std;
#define PORT 1234
#define LENGTH_OF_LISTEN_QUEUE 20
#define BUFFER_SIZE 1024

int main(int argc,char **argv){
	printf("test start\n");

	ros::init(argc,argv,"Socket_Client");
	ROS_INFO("------------");
	
	struct sockaddr_in server_addr;

	bzero(&server_addr,sizeof(server_addr)); //把一段内存区的内容全部设置为
	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.s_addr = htons(INADDR_ANY);
	server_addr.sin_port = htons(PORT);
	
	int server_socket;

	if((server_socket=socket(AF_INET,SOCK_STREAM,0))<0)
	{
		ROS_ERROR("Creat Socket failed!\n");
		exit(1);
	}
	printf("server_socket is %d\n",server_socket);
	if( bind(server_socket,(struct sockaddr*)&server_addr,sizeof(server_addr)))
	{
		ROS_ERROR("Server Bind Port: %d Failed\n",PORT);
		exit(1);
	}
	printf("bind success\n");
	listen(server_socket, LENGTH_OF_LISTEN_QUEUE);
	printf("listen success\n");

	while(1){
		struct sockaddr_in client_addr;
		socklen_t length = sizeof(client_addr);
		int new_server_socket = accept(server_socket,(struct sockaddr*)&client_addr,&length);

		if(new_server_socket <0 ){
			perror("server accept failed\n");
		}
	
		char buffer[BUFFER_SIZE];
		bzero(buffer,BUFFER_SIZE);
	
		strcpy(buffer,"hello,world");
	
		send(new_server_socket,buffer,30,0);

		close(new_server_socket);

	}
	printf("send finshed!!");
	close(server_socket);

}
