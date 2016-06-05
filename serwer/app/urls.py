from django.conf.urls import url
from django.contrib import admin
from app.views import *

urlpatterns = [
    url(r'^save_timetable/', save_event_list),
    url(r'^get_windows/', get_windows),
    url(r'^register/', register),
    url(r'^login/', login),
    url(r'^get_windows/', get_windows),
    url(r'^get_blocks/', get_my_blocks),
    url(r'^check_zaczepkas/', check_zaczepkas),
    url(r'^list_zaczepkas/', list_zaczepkas),
    url(r'^send_hate_mail/', add_zaczepkas)
]
