#!/usr/bin/env python

import rospy
import socket
from geometry_msgs.msg import Twist

def sendtoclient(self, msg):
    x = ('%.3f' % abs(msg.linear.x))
    z = ('%.3f' % abs(msg.angular.z))
    print(str(x) + " " + str(z))
    if ((x > 0 or z > 0)):
        try:
            self.con.send("("+str(x)+","+str(z)+")")
        except:
            self.con.close()

if __name__ == "__main__":
    HOST = rospy.get_param('HOST')
    Info_Port = rospy.get_param('Info_Port')
    soc = socket.socket()
    soc.bind((HOST, Info_Port))
    rospy.init_node('info_server')
    print('Info-Server Online!')
    soc.listen(5)
    while (True):
        conn, addr = soc.accept()
        print("Info-Server >> Connection From : " + addr)
        rospy.Subscriber('/cmd_vel', Twist, self.sendtoclient)
        rospy.spin()