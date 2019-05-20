#!/usr/bin/env python

import rospy
import socket
from geometry_msgs.msg import PoseWithCovarianceStamped

def sendtoclient(self, msg):
    pos_x = ('%.3f' % msg.pose.pose.position.x)
    pos_y = ('%.3f' % msg.pose.pose.position.y)
    try:
        self.con.send(str(pos_x) + ',' + str(pos_y))
    except:
        self.con.close()

if __name__ == '__main__':
    HOST = rospy.get_param('HOST')
    Loc_Port = rospy.get_param('Loc_Port')
    soc = socket.socket()
    soc.bind((HOST, Loc_Port))
    rospy.init_node('loc_server')
    print('Loc-Server Online!')
    soc.listen(5)
    while (True):
        conn, addr = soc.accept()
        print('Loc-Server >> Connection From : ' + addr)
        rospy.Subscriber('amcl_pose', PoseWithCovarianceStamped, self.sendtoclient)
        rospy.spin()
