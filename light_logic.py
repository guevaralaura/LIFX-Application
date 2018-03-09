import sys
import requests
import json

def connect():
    token = 'cd0ed572badca8064cbdb078695975aa730217c5c8ec578e65e639df50d585c8'
    headers = {
        'Authorization': 'Bearer %s' % token,
    }
    return headers

def send_all(power_status, color, brightness):
    data_to_send={
        "power": power_status, # off or on
        "color": color, # hex color
        "brightness": brightness, #float from 0 to 1
    }
    return data_to_send

def send_power(power_status):
    data_to_send={
        "power": power_status, # off or on
    }
    return data_to_send

def send_color(color):
    data_to_send={
        "color": color, # hex color
    }
    return data_to_send

def send_brightness(brightness):
    data_to_send={
        "brightness": brightness, #float from 0 to 1
    }
    return data_to_send

def get_data(json_state):
    #output into file
    f = open('data.json', 'w')
    f.write(json_state)
    f.close()

#possible selector: all, label, id, group_id, groupname, locationid, locationname, sceneid

#########################
######    MAIN    #######
#########################

#get parameter from java
parameter = sys.argv[1]
data_to_send = {}
#connect to api
headers = connect()
item ='all'
arg_num = len(sys.argv)
if parameter == 'get':
    #if java sent get then output json string with all light info
    initial_state = requests.get('https://api.lifx.com/v1/lights/all', headers=headers)
    get_data(initial_state.text)
elif parameter == 'set_power':
    #power on or off
    power_str = sys.argv[2]
    data_to_send = send_power(power_str)
    if(arg_num == 4):
        item=sys.argv[3]
elif parameter == 'set_color':
    #color hex number
    color = sys.argv[2]
    data_to_send = send_color(color)
    if(arg_num == 4):
        item=sys.argv[3]
elif parameter == 'set_brightness':
    #brightness float b/w 0-1
    brightness = sys.argv[2]
    data_to_send = send_brightness(brightness)
    if(arg_num == 4):
        item=sys.argv[3]
elif parameter == 'set_all':
    power_str = sys.argv[2]
    color = sys.argv[3]
    brightness = sys.argv[4]
    data_to_send = send_all(power_str, color, brightness)
    if (arg_num == 6):
        item = sys.argv[5]

#send the data to api
if(item == 'all'):
    new_state = requests.put('https://api.lifx.com/v1/lights/all/state', data=data_to_send, headers=headers)
else:
    new_state = requests.put('https://api.lifx.com/v1/lights/label:%s/state'%item, data=data_to_send, headers=headers)

#get data with changes
final_state = requests.get('https://api.lifx.com/v1/lights/all', headers=headers)
get_data(final_state.text)

print('hello world')
