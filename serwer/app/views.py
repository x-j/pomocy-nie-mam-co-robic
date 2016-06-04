from django.shortcuts import render
from django.contrib.auth.models import User
from django.http import JsonResponse
import json

def register(request):

    nickname = request.POST.get("nickname")

    if User.objects.filter(username=nickname).exists():
        return JsonResponse({"result": False})

    name = request.POST.get("name")
    name, surname = name.split(" ", maxsplit=1)
    nickname = request.POST.get("nickname")
    password = request.POST.get("password")

    User.objects.create_user(
        username=nickname,
        password=password,
        first_name=name,
        last_name=surname
    )

    return JsonResponse({"result": True})

def save_event_list(request):
    event_list = request.POST["event_list"]
    #print(repr(event_list))
    event_list = json.loads(event_list)
    #print(event_list)
    return JsonResponse({"ASdf": 123})

def get_windows(request):
    return JsonResponse({"Windows 10": False})
