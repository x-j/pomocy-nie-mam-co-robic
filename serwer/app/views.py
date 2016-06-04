from django.shortcuts import render
from django.contrib.auth.models import User
from django.http import JsonResponse
import json


def find_windows(nick1, nick2):
    user1 = User.objects.get(username=nick1)
    user2 = User.objects.get(username=nick2)

    blocks1 = user1.block_set.order_by("start").all()
    blocks2 = user2.block_set.order_by("start").all()

    flat_timeline = []

    for block in blocks1:
        flat_timeline.append((block.start, "start", 0))
        flat_timeline.append((block.end, "end", 0))
    for block in blocks2:
        flat_timeline.append((block.start, "start", 1))
        flat_timeline.append((block.end, "end", 1))

    def get_first_el(tup):
        return tup[0]

    flat_timeline.sort(key=get_first_el)

    windows = []
    window_start = None
    event_counter = [0, 0]

    for t, flag, person in flat_timeline:
        if flag == "start":
            event_counter[person] += 1
            if window_start and window_start.day == t.day:
                window = {"start":window_start, "end":t}
                windows.append(window)
                window_start = None
        else:
            event_counter[person] -= 1
            if event_counter == [0, 0]:
                window_start = t

    return windows

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

def login(request):

    nickname = request.POST.get("nickname")

    if not User.objects.filter(username=nickname).exists():
        return JsonResponse({"result": False})

    password = request.POST.get("password")


    if not User.objects.get(username=nickname).check_password(password):
        return JsonResponse({"result": False})

    return JsonResponse({"result": True})




def save_event_list(request):
    event_list = request.POST["event_list"]
    #print(repr(event_list))
    event_list = json.loads(event_list)
    #print(event_list)
    return JsonResponse({"ASdf": 123})

def get_windows(request):
    return JsonResponse({"Windows 10": False})

