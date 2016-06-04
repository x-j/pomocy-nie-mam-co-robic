from django.conf.urls import url
from django.contrib import admin
from app.views import *

urlpatterns = [
    url(r'^save_timetable/', save_event_list),
    url(r'^get_windows/', get_windows),
    url(r'^is_available/', is_available),
]
