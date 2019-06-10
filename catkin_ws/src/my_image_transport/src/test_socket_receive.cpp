
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

int HELLO_WORLD_SERVER_PORT=1234;
int BUFFER_SIZE=1024;

int test(char* addr){
	struct sockaddr_in client_addr;
    	bzero(&client_addr,sizeof(client_addr)); 
    	client_addr.sin_family = AF_INET;   
    	client_addr.sin_addr.s_addr = htons(INADDR_ANY);
    	client_addr.sin_port = htons(0);

	int client_socket = socket(AF_INET,SOCK_STREAM,0);
	if(client_socket<0){
		return -1;
	}
	
	if( bind(client_socket,(struct sockaddr*)&client_addr,sizeof(client_addr))){
		return -1;
	}
	struct sockaddr_in server_addr;
	bzero(&server_addr,sizeof(server_addr));
	server_addr.sin_family = AF_INET;
	if(inet_aton(addr,&server_addr.sin_addr) == 0)
	{
		close(client_socket);
	    	return 0;
	}
	server_addr.sin_port = htons(HELLO_WORLD_SERVER_PORT);
	socklen_t server_addr_length = sizeof(server_addr);


	if(connect(client_socket,(struct sockaddr*)&server_addr, server_addr_length) < 0)
        {
		close(client_socket);
	    	return  0;
	}

	char buffer[BUFFER_SIZE];
    	bzero(buffer,BUFFER_SIZE);

   	int length = recv(client_socket,buffer,BUFFER_SIZE,0);
	if(length<0){
		close(client_socket);
		return -1;
	}
	if(strstr("hello,world",buffer)){
		close(client_socket);
		return 1;
	}
	close(client_socket);
	return 3;
}

int main(){
	char addr1[10]="127.0.0.0";
	char addr2[10]="127.1.1.1";
	char addr3[10]="127.1.1.1";
	char addr4[10]="127.0.0.1";


	HELLO_WORLD_SERVER_PORT=1111;
	if(test(addr1)==0){
		printf("test1 ok\n");
	}
	else{
		printf("test1 fail\n");
	}

	HELLO_WORLD_SERVER_PORT=1234;
	if(test(addr2)==1){
		printf("test2 ok\n");
	}
	else{
		printf("test2 fail\n");
	}

	HELLO_WORLD_SERVER_PORT=1111;
	if(test(addr3)==0){
		printf("test3 ok\n");
	}
	else{
		printf("test3 fail\n");
	}

	HELLO_WORLD_SERVER_PORT=1234;
	if(test(addr4)==1){
		printf("test4 ok\n");
	}
	else{
		printf("test4 fail\n");
	}

	return 0;
}


