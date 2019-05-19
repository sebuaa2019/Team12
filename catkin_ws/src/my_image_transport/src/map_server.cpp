#include<ros/ros.h>
#include<nav_msgs/OccupancyGrid.h>

#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <image_transport/image_transport.h>
#include <cv_bridge/cv_bridge.h>

#include <sensor_msgs/image_encodings.h>
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

using namespace std;

#define PORT 2003
#define LENGTH_OF_LISTEN_QUEUE 20
#define BUFFER_SIZE 2000

struct sockaddr_in server_addr;
struct sockaddr_in client_addr;
int server_socket;
int new_server_socket;
int width=992;
int height=992;
int flag_connect=0;

void call_back(const nav_msgs::OccupancyGrid::ConstPtr &msg){

	printf("call back begin\n");	
	width = msg->info.width;
	height = msg->info.height;
	printf("data width :%d  height:%d\n",width,height);


	uchar* send_buffer = new uchar[width*height];
	int i,j;
	int num=0;
	for(i=height-1;i>=0;i--){
	//	if(msg->data[i]!=-1) printf("%d ",msg->data[i]);
		for(j=0;j<width;j++){
			int k=i*width;
			send_buffer[num++]=(uchar)(msg->data[k+j]);
		}
	}
		

	int toSend = width*height;
	int finished=0,receive=0;
	while(toSend>0){
		int size = toSend<BUFFER_SIZE?toSend:BUFFER_SIZE;
		if(receive = send(new_server_socket,send_buffer+finished,size,0))
		{
			if(receive == -1)
			{
				printf("receive error");
				flag_connect=0;
				new_server_socket=-1;
				return;
			}
			else{
				toSend -= receive; // 剩余发送数据
				finished += receive;	// 已发送数据
			}
		}
	}

}


int main(int argc,char **argv)
{
	ros::init(argc,argv,"map_server");
	ROS_INFO("--------------");

	ros::NodeHandle nh;
	ros::Subscriber sub = nh.subscribe("map",1,call_back);

	printf("%d \n",uchar(-1));
	bzero(&server_addr,sizeof(server_addr)); //把一段内存区的内容全部设置为
	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.s_addr = htons(INADDR_ANY);
	server_addr.sin_port = htons(PORT);

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
	printf("bind ok\n");
	listen(server_socket, LENGTH_OF_LISTEN_QUEUE);
	printf("listen ok\n");

	int flag=1;
	while(flag){
		socklen_t length = sizeof(client_addr);
		new_server_socket = accept(server_socket,(struct sockaddr*)&client_addr,&length);


		if(new_server_socket <0 ){
			perror("server accept failed\n");
		}
		else{
			printf("accept\n");
			flag_connect=1;
			ros::Rate loop_rate(1);
			while(ros::ok() && flag_connect==1){
				ros::spinOnce();
				loop_rate.sleep();
			}
		}
	
	}
	
	return 0;
}
