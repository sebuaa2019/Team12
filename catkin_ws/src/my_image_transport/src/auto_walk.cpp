



void subCallback(const sensor_msgs::LaserScan &LaserScan){
    new_speed = new_turn_rate =0.0;
    min_left_dist = get_min_distance_left(LaserScan);
    min_right_dist = get_min_distance_right(LaserScan);

    l = min_left_dist;
    r = min_right_dist;
    if(l>max_distance) l = max_distance;

    if(r>max_distance) r = max_distance;
    /*
    if( l<max_distance || r<max_distance ){
        if(abs(l-r) < force_turning_right_difference){   
            r=r-100;  //
        }
    }
    */
    if( ){
        new_speed = (r+l)/speed_ratio;
    }
    else{
        new_speed = 0.0;
    }
    new_speed = limit(new_speed,-max_speed,max_speed);

    cmd_vel.linear.x = new_speed;
    cmd_vel.linear.y = 0.0;
    cmd_vel.linear.z = 0.0;
    cmd_vel.angular.x = 0.0;
    cmd_vel.angular.y = 0.0;
    cmd_vel.angular.z = new_turn_rate;

    cmd_vel_pub.Publisher(cmd_vel);

}


int int main(int argc, char *argv[])
{
    /* code for main function */
    ros::init(argc, argv, "auto_walk");
    ros:NodeHandle nh;

    ros::Publisher pub = nh.advertise<geometry_msgs::Twist>("/cmd_vel", 1);
    
    ros::Subscriber sub = nh.subscribe("/scan", 10, subCallback);
    

    return 0;
}