#!/usr/bin/env python

import rospy
import socket
from geometry_msgs.msg import Twist

class SpeedInfoServer:
    def __init__(self, host, port):
        self.soc = socket.socket()
        self.soc.bind((host, port))
        self.con = None

    def sendtoclient(self, msg):
        x = ('%.3f' % abs(msg.linear.x))
        z = ('%.3f' % abs(msg.angular.z))
        if ((x > 0 or z > 0)):
            try:
                self.con.send("("+str(x)+","+str(z)+")")
            except:
                self.con.close()

    def start(self, threadname):
        print('Info-Server Online!')
        self.soc.listen(5)
        self.con, addr = self.soc.accept()
        rospy.Subscriber('/cmd_vel', Twist, self.sendtoclient)
        rospy.spin()

    def stop(self):
        self.con.close()
        self.soc.close()
