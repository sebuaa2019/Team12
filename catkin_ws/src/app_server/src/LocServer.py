#!/usr/bin/env python

import rospy
import socket
from geometry_msgs.msg import PoseWithCovarianceStamped

global conn
global soc

def sendtoclient(msg):
    global conn, soc
    pos_x = ('%.3f' % msg.pose.pose.position.x)
    pos_y = ('%.3f' % msg.pose.pose.position.y)
    try:
        conn.send(str(pos_x) + ',' + str(pos_y))
    except Exception:
        conn, addr = soc.accept()

if __name__ == '__main__':
    global conn, soc
    #HOST = rospy.get_param('HOST')
    #Loc_Port = rospy.get_param('Loc_Port')
    soc = socket.socket()
    #soc.bind((HOST, Loc_Port))
    soc.bind(('127.0.0.1', 2001))
    rospy.init_node('loc_server')
    print('Loc-Server Online!')
    soc.listen(5)
    conn, addr = soc.accept()
    print('Loc-Server >> Connection From : ' + addr[0])
    rospy.Subscriber('amcl_pose', PoseWithCovarianceStamped, sendtoclient)
    rospy.spin()
