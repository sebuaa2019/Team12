#!/usr/bin/env python

import socket
import rospy
import thread
from SpeedInfoServer import SpeedInfoServer
from controller_server.msg import control_signal

HOST = '127.0.0.1'
CONTRO_PORT = 1989
INFO_PORT = 2000
SIGNAL = ['FORWARD', 'BACKWARD', 'TURNLEFT', 'TURNRIGHT']

if __name__ == '__main__':
    rospy.init_node('server')
    pub = rospy.Publisher('control_signal', control_signal, queue_size=1)
    soc = socket.socket()
    soc.bind((HOST, CONTRO_PORT))
    thread.start_new_thread(SpeedInfoServer(HOST, INFO_PORT).start, ("INFO-Server-Thread", ))
    print('Server Online!')
    soc.listen(5)
    while (True):
        conn, addr = soc.accept()
        data = conn.recv(1024)
        print(data)
        if (data in SIGNAL):
            pub.publish(control_signal(data, False))
        else:
            pub.publish(control_signal(data, True))