from django.shortcuts import render
from django.contrib.auth.models import User
from django.http import JsonResponse
import json

def is_available(request):

    nickname = request.GET.get("nickname")

    result = False
    if User.objects.filter(username=nickname).exists():
        result = True
    return JsonResponse({"result": result})

def save_event_list(request):
    event_list = request.POST["event_list"]
    #print(repr(event_list))
    event_list = json.loads(event_list)
    #print(event_list)
    return JsonResponse({"ASdf": 123})

def get_windows(request):
    return JsonResponse({"Windows 10": False})
