#!/usr/bin/env python

import socket
import rospy
from controller_server.msg import control_signal

HOST = '127.0.0.1'
PORT = 1989
SIGNAL = ['FORWARD', 'BACKWARD', 'TURNLEFT', 'TURNRIGHT']

if __name__ == '__main__':
    rospy.init_node('server')
    pub = rospy.Publisher('control_signal', control_signal, queue_size=1)
    soc = socket.socket()
    soc.bind((HOST, PORT))
    print('Server Online!')
    soc.listen(5)
    while (True):
        conn, addr = soc.accept()
        data = conn.recv(1024)
        if (data in SIGNAL):
            pub.publish(control_signal(data, False))
        else:
            pub.publish(control_signal(data, True))