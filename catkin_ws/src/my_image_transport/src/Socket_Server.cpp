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
#define PORT 1999
#define LENGTH_OF_LISTEN_QUEUE 20
#define BUFFER_SIZE 307200

struct sockaddr_in server_addr;
struct sockaddr_in client_addr;
int server_socket;
int new_server_socket;


void imageCallback(const sensor_msgs::ImageConstPtr& msg){

	Mat s_img1 = cv_bridge::toCvShare(msg,"bgr8")->image;
	Mat s_img;
	resize(s_img1,s_img,cv::Size(720,480));

//	cv::waitKey(30);
	if(s_img.empty()){
		ROS_ERROR("open error\n");
		exit(1);
	}

	vector<uchar> encode_img;
	int i,j;
	uchar* pxvec = s_img.ptr<uchar>(0);	

	printf("row is :%d ,col is :%d channels is :%d\n",s_img.rows,s_img.cols,s_img.channels());
	for( i=0;i<s_img.rows;i++)	//高度
	{
		pxvec = s_img.ptr<uchar>(i);	
		for(j=0;j<s_img.cols*s_img.channels();j=j+3)
		{	
			encode_img.push_back(pxvec[j+2]);
			encode_img.push_back(pxvec[j+1]);
			encode_img.push_back(pxvec[j]);
			encode_img.push_back(0xff);
		}
	}

	//get_send_buffer
	int encode_img_size = encode_img.size();
	int s_img_size = s_img.rows * s_img.cols*3;

	uchar* send_buffer = new uchar[encode_img.size()];
	copy(encode_img.begin(),encode_img.end(),send_buffer);


	//send image_length
	int toSend =encode_img_size,receive=0,finished=0;
	printf("img_size is %d\n",toSend);


	//send start
	while(toSend>0){
		int size = toSend<BUFFER_SIZE?toSend:BUFFER_SIZE;
		if((receive = send(new_server_socket,send_buffer+finished,size,0)))
		{
			if(receive == -1)
			{
				printf("receive error");
				break;
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

	ros::init(argc,argv,"Socket_Client");
	ROS_INFO("------------");	

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
			flag=0;
		}
	
	}

	printf("accept \n");
	ros::NodeHandle nh;
	image_transport::ImageTransport it(nh);
	image_transport::Subscriber sub = it.subscribe("camera/rgb/image_raw", 1, imageCallback);

	ros::Rate loop_rate(10);
	while(ros::ok()){
		ros::spinOnce();
		loop_rate.sleep();
	}
}









