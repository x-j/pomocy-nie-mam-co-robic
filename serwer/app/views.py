from django.shortcuts import render
from django.contrib.auth.models import User
from .models import Block
from django.http import JsonResponse
from django.http import HttpResponse
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
                window = {"start":str(window_start), "end":str(t)}
                windows.append(window)
                window_start = None
        else:
            event_counter[person] -= 1
            if event_counter == [0, 0]:
                window_start = t

    return windows

def get_windows(request):
    user1 = request.GET.get("user1").strip()
    user2 = request.GET.get("user2").strip()

    #windows = find_windows(user1, user2)

    #return JsonResponse({"windows": windows})

    #user1 = request.POST["user1"]
    #user2 = request.POST["user2"]

    print(user1)
    print(user2)

    windows = find_windows(user1, user2)

    #print(json.dumps({"windows": windows}))

    return JsonResponse({"windows": windows})



def register(request):

    nickname = request.POST.get("nickname").strip()

    if User.objects.filter(username=nickname).exists():
        print("FUG :DDDD")
        return JsonResponse({"result": False})

    name = request.POST.get("name").strip()
    name, surname = name.split(" ", maxsplit=1)
    nickname = request.POST.get("nickname").strip()
    password = request.POST.get("password").strip()

    User.objects.create_user(
        username=nickname,
        password=password,
        first_name=name,
        last_name=surname
    )

    print("nice memes")
    return JsonResponse({"result": True})

def login(request):

    nickname = request.POST.get("nickname").strip()

    if not User.objects.filter(username=nickname).exists():
        print("NIE MA TAKIEGO XD")
        return JsonResponse({"result": False})

    password = request.POST.get("password").strip()


    if not User.objects.get(username=nickname).check_password(password):
        print("SWORDFISH")
        return JsonResponse({"result": False})

    return JsonResponse({"result": True})


def save_event_list(request):
    event_list = request.POST["event_list"]
    username = request.POST["nickname"]
    print(repr(username))
    event_list = json.loads(event_list)
    
    for block in event_list:
        c = Block.objects.create(person=User.objects.get(username=username), start=block["start_date"], end=block["end_date"], name=block["description"])
        c.save

    #return JsonResponse({"Result": 1})
    return HttpResponse("1")



