from django.shortcuts import render

from django.http import HttpResponse
import json


def save_event_list(request):
	event_list = request.POST["event_list"]
	#print(repr(event_list))
	event_list = json.loads(event_list)
	#print(event_list)
	return HttpResponse("ASdf")

def get_windows(request):
	return HttpResponse("Windows 10")